/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Renewed Pixel Dungeon
 * Copyright (C) 2025 RetroDevXP
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.retrodevxp.pixeldungeon.items.weapon;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.hero.HeroClass;
import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.KindOfWeapon;
import com.retrodevxp.pixeldungeon.items.weapon.Weapon.Enchantment;
import com.retrodevxp.pixeldungeon.items.weapon.Weapon.Imbue;
import com.retrodevxp.pixeldungeon.items.weapon.enchantments.*;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Bundlable;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.Random;

abstract public class Weapon extends KindOfWeapon {

	private static final int HITS_TO_KNOW	= 20;
	
	private static final String TXT_IDENTIFY		= 
		"You are now familiar enough with your %s to identify it. It is %s.";
	private static final String TXT_INCOMPATIBLE	= 
		"Interaction of different types of magic has negated the enchantment on this weapon!";
	
	private static final String TXT_TO_STRING	= "%s :%d";
	private static final String TXT_BROKEN		= "broken %s :%d";
	
	public int		STR	= 10;
	public float	ACU	= 1;
	public float	DLY	= 1f;
	
	public enum Imbue {
		NONE, SPEED, ACCURACY
	}
	public Imbue imbue = Imbue.NONE;
	
	private int hitsToKnow = HITS_TO_KNOW;
	
	protected Enchantment enchantment;
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		
		if (enchantment != null) {
			enchantment.proc( this, attacker, defender, damage );
		}
		
		if (!levelKnown) {
			if (--hitsToKnow <= 0) {
				levelKnown = true;
				GLog.i( TXT_IDENTIFY, name(), toString() );
				Badges.validateItemLevelAquired( this );
			}
		}
		
		use();
	}
	
	private static final String UNFAMILIRIARITY	= "unfamiliarity";
	private static final String ENCHANTMENT		= "enchantment";
	private static final String IMBUE			= "imbue";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, hitsToKnow );
		bundle.put( ENCHANTMENT, enchantment );
		bundle.put( IMBUE, imbue );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((hitsToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			hitsToKnow = HITS_TO_KNOW;
		}
		enchantment = (Enchantment)bundle.get( ENCHANTMENT );
		imbue = bundle.getEnum( IMBUE, Imbue.class );
	}
	
	@Override
	public float acuracyFactor( Hero hero ) {
		
		int encumbrance = STR - hero.STR();
		
		if (this instanceof MissileWeapon) {
			switch (hero.heroClass) {
			case WARRIOR:
				encumbrance += 3;
				break;
			case HUNTRESS:
				encumbrance -= 2;
				break;
			default:
			}
		}
		
		return 
			(encumbrance > 0 ? (float)(ACU / Math.pow( 1.5, encumbrance )) : ACU) *
			(imbue == Imbue.ACCURACY ? 1.5f : 1.0f);
	}
	
	@Override
	public float speedFactor( Hero hero ) {

		int encumrance = STR - hero.STR();
		if (this instanceof MissileWeapon && hero.heroClass == HeroClass.HUNTRESS) {
			encumrance -= 2;
		}
		
		return 
			(encumrance > 0 ? (float)(DLY * Math.pow( 1.2, encumrance )) : DLY) * 
			(imbue == Imbue.SPEED ? 0.6f : 1.0f);
	}
	
	@Override
	public int damageRoll( Hero hero ) {
		
		int damage = super.damageRoll( hero );
		
		if ((hero.rangedWeapon != null) == (hero.heroClass == HeroClass.HUNTRESS)) {
			int exStr = hero.STR() - STR;
			if (exStr > 0) {
				damage += Random.IntRange( 0, exStr );
			}
		}
		
		return damage;
	}
	
	public Item upgrade( boolean enchant ) {		
		if (enchantment != null) {
			if (!enchant && Random.Int( level() ) > 0) {
				if (Dungeon.hero.heroClass.toString() == "MAGE"){
					if (Random.Int( 1 ) > 0){
						if (Dungeon.hero.subClass == HeroSubClass.SCRIBE){
							GLog.w("You applied the upgrade proficiently despite the interactions of different types of magic.");
						}
						else{
						GLog.w( TXT_INCOMPATIBLE );
						enchant( null );
						}
					}
				}
				else{
					GLog.w( TXT_INCOMPATIBLE );
					enchant( null );
				}
			}
		} else {
			if (enchant) {
				enchant();
			}
		}
		
		return super.upgrade();
	}
	
	@Override
	public int maxDurability( int lvl ) {
		// return 5 * (lvl < 16 ? 16 - lvl : 1);
		return Integer.MAX_VALUE;
	}
	
	@Override
	public String toString() {
		return levelKnown ? Utils.format( isBroken() ? TXT_BROKEN : TXT_TO_STRING, super.toString(), STR ) : super.toString();
	}
	
	@Override
	public String name() {
		return enchantment == null ? super.name() : enchantment.name( super.name() );
	}
	
	@Override
	public Item random() {
		if (Random.Float() < 0.4) {
			int n = 1;
			if (Random.Int( 3 ) == 0) {
				n++;
				if (Random.Int( 3 ) == 0) {
					n++;
				}
			}
			if (Random.Int( 2 ) == 0) {
				upgrade( n );
			} else {
				degrade( n );
				cursed = true;
			}
		}
		return this;
	}
	
	public Weapon enchant( Enchantment ench ) {
		enchantment = ench;
		return this;
	}
	
	public Weapon enchant() {
		
		Class<? extends Enchantment> oldEnchantment = enchantment != null ? enchantment.getClass() : null;
		Enchantment ench = Enchantment.random();
		while (ench.getClass() == oldEnchantment) {
			ench = Enchantment.random();
		}
		
		return enchant( ench );
	}
	
	public boolean isEnchanted() {
		return enchantment != null;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return enchantment != null ? enchantment.glowing() : null;
	}
	
	public static abstract class Enchantment implements Bundlable {
		
		private static final Class<?>[] enchants = new Class<?>[]{ 
			Fire.class, Poison.class, Death.class, Paralysis.class, Leech.class, 
			Slowness.class, Shock.class, Instability.class, Horror.class, Luck.class,
			Tempering.class, Culling.class, Initiliazing.class, Binding.class};
		private static final float[] chances= new float[]{ 10, 10, 1, 2, 1, 2, 6, 3, 2, 2, 3, 3, 5, 3 };
			
		public abstract boolean proc( Weapon weapon, Char attacker, Char defender, int damage );
		
		public String name( String weaponName ) {
			return weaponName;
		}

		public String description(){
			return "Awesome description for weapon enchantment.";
		}
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {	
		}

		@Override
		public void storeInBundle( Bundle bundle ) {	
		}
		
		public ItemSprite.Glowing glowing() {
			return ItemSprite.Glowing.WHITE;
		}
		
		@SuppressWarnings("unchecked")
		public static Enchantment random() {
			try {
				return ClassReflection.newInstance((Class<Enchantment>) enchants[Random.chances(chances)]);
			} catch (Exception e) {
				return null;
			}
		}
		
	}
}
