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
package com.retrodevxp.pixeldungeon.items.weapon.melee;

import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Blindness;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.hero.HeroClass;
import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.weapon.Weapon;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.MissileWeapon;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.sprites.CharSprite;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Random;

public class MeleeWeapon extends Weapon {
	
	private int tier;
	private int bonus_atk;
	private int bonus_str;
	private int rangemin;
	private int rangemax;
	protected static final String TXT_DEFEAT	= "%s defeated %s";
	private static final String TXT_YOU_MISSED	= "%s %s your attack";
	
	public MeleeWeapon( int tier, float acu, float dly, int bonus_atk , int bonus_str ) {
		super();
		
		this.tier = tier;
		this.bonus_atk = bonus_atk;
		this.bonus_str = bonus_str;
		
		ACU = acu;
		DLY = dly;
		
		STR = typicalSTR();
		
		defaultAction = AC_THROW;
	}
	
	protected int min0() {
		return Math.max(Math.max(tier, tier + bonus_atk), max0() / 7);
	}
	
	protected int max0() {
		return (int)(((tier * tier - tier + 10) / ACU * DLY) + bonus_atk);
	}
	
	@Override
	public int min() {
		return isBroken() ? min0() : min0() + level(); 
	}
	
	@Override
	public int max() {
		return isBroken() ? max0() : max0() + level() * tier;
	}
	
	@Override
	final public Item upgrade() {
		return upgrade( false );
	}
	
	public Item upgrade( boolean enchant ) {
		STR--;		
		return super.upgrade( enchant );
	}
	
	public Item safeUpgrade() {
		return upgrade( enchantment != null );
	}
	
	@Override
	public Item degrade() {		
		STR++;
		return super.degrade();
	}
	
	public int typicalSTR() {
		return 8 + tier * 2 + bonus_str;
	}
	
	@Override
	public String info() {
		
		final String p = "\n\n";
		
		StringBuilder info = new StringBuilder( desc() );
		
		int lvl = visiblyUpgraded();
		String quality = lvl != 0 ? 
			(lvl > 0 ? 
				(isBroken() ? "broken" : "upgraded") : 
				"degraded") : 
			"";
		info.append( p );
		info.append( "This " + name + " is " + Utils.indefinite( quality ) );
		info.append( " tier-" + tier + " melee weapon. " );
		
		if (levelKnown) {
			int min = min();
			int max = max();
			info.append( "Its average damage is " + (min + (max - min) / 2) + " points per hit. " );
		} else {
			int min = min0();
			int max = max0();
			info.append( 
				"Its typical average damage is " + (min + (max - min) / 2) + " points per hit " +
				"and usually it requires " + typicalSTR() + " points of strength. " );
			if (typicalSTR() > Dungeon.hero.STR()) {
				info.append( "Probably this weapon is too heavy for you. " );
			}
		}
		
		if (DLY != 1f) {
			info.append( "This is a rather " + (DLY < 1f ? "fast" : "slow") );
			if (ACU != 1f) {
				if ((ACU > 1f) == (DLY < 1f)) {
					info.append( " and ");
				} else {
					info.append( " but ");
				}
				info.append( ACU > 1f ? "accurate" : "inaccurate" );
			}
			info.append( " weapon. ");
		} else if (ACU != 1f) {
			info.append( "This is a rather " + (ACU > 1f ? "accurate" : "inaccurate") + " weapon. " );
		}
		switch (imbue) {
		case SPEED:
			info.append( "It was balanced to make it faster. " );
			break;
		case ACCURACY:
			info.append( "It was balanced to make it more accurate. " );
			break;
		case NONE:
		}
		
		if (enchantment != null) {
			info.append( "It is enchanted. \n\n" );
			info.append( enchantment.description() );
		}

		if (levelKnown) {
			info.append( p );
			int rangemin = rangemin();
			int rangemax = rangemax();
			info.append( "When thrown, its average damage is " + (rangemin + (rangemax - rangemin) / 2) + " points per hit " +
			"and requires " + rangestr() + " points of strength. " );
		} else {
			info.append( p );
			int rangemin = rangemin();
			int rangemax = rangemax();
			info.append( 
				"When thrown, its typical average damage is " + (rangemin + (rangemax - rangemin) / 2) + " points per hit " +
				"and requires " + rangestr() + " points of strength. " );
			info.append( 
				"Because of your unfamiliarity with this weapon, it might be less accurate when thrown ");
		}
		
		if (levelKnown && Dungeon.hero.belongings.backpack.items.contains( this )) {
			if (STR > Dungeon.hero.STR()) {
				info.append( p );
				info.append( 
					"Because of your inadequate strength the accuracy and speed " +
					"of your attack with this " + name + " is decreased." );
			}
			if (STR < Dungeon.hero.STR()) {
				info.append( p );
				info.append( 
					"Because of your excess strength the damage " +
					"of your attack with this " + name + " is increased." );
			}
		}
		
		if (isEquipped( Dungeon.hero )) {
			info.append( p );
			info.append( "You hold the " + name + " at the ready" + 
				(cursed ? ", and because it is cursed, you are powerless to let go." : ".") ); 
		} else {
			if (cursedKnown && cursed) {
				info.append( p );
				info.append( "You can feel a malevolent magic lurking within " + name +"." );
			}
		}
		
		return info.toString();
	}
	
	@Override
	public int price() {
		int price = 20 * (1 << (tier - 1));
		if (enchantment != null) {
			price *= 1.5;
		}
		return considerState( price );
	}
	
	@Override
	public Item random() {
		super.random();
		
		if (Random.Int( 10 + level() ) == 0) {
			enchant();
		}
		
		return this;
	}

	protected int rangemin(){
		return 1;
	}

	protected int rangemax(){
		return 5;
	}

	protected int rangestr(){
		return 11;
	}

	@Override
	protected void onThrow( int cell ) {
		rangemin = rangemin();
		rangemax = rangemax();
		// if (Dungeon.hero.belongings.weapon != this){
		// 	Dungeon.hero.spend(1);
		// 	//Currently doesn't function. Might make it a subclass perk.
		// }
		int encumrance = rangestr() - Dungeon.hero.STR();
		Char enemy = Actor.findChar( cell );
		if (enemy == null || enemy == curUser) {
			super.onThrow( cell );
		} else {
			// if (curUser.throwmelee( enemy, this )) {
				
			int dr =  Dungeon.hero.subClass == HeroSubClass.DEADEYE ? 0 :
				Random.IntRange( enemy.dr() / 5, enemy.dr() );
			
			int dmg = Random.Int(rangemin, rangemax);
			int effectiveDamage = Math.max( dmg - dr - encumrance, 0 );
			
			effectiveDamage = enemy.attackProc( enemy, effectiveDamage );
			effectiveDamage = enemy.defenseProc( Dungeon.hero, effectiveDamage );
				
			if (throwaccuracy(Dungeon.hero, enemy, cursed, encumrance, isIdentified())){
				enemy.damage(effectiveDamage, Dungeon.hero);
				Sample.INSTANCE.play(Assets.SND_HIT, 1, 1, Random.Float(0.7f,1.1f));
				if (!enemy.isAlive()){
					GLog.i( TXT_DEFEAT, Dungeon.hero.name, enemy.name );
				}
			}
			else{
				String defense = enemy.defenseVerb();
				enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );
					GLog.i( TXT_YOU_MISSED, enemy.name, defense );

				
				Sample.INSTANCE.play( Assets.SND_MISS );
			}


			// }
			super.onThrow( cell );
		}
		Dungeon.hero.spend(Math.max( encumrance, 0 ));
	}
	
	protected void miss( int cell ) {
		super.onThrow( cell );
	}

	//Thrown melee weapons have their own accuracy checks.
	//Warrior is less accurate with thrown melee weapons as well.
	//If adjacent, has more accuracy, though that kind of defeats the point of throwing a melee weapon.
	//Cursed weapons have less accuracy when thrown.
	//Unidentified weapons have slightly less accuracy when thrown.
	//Throwing a weapon above your strength limit has worse accuracy.
	public static boolean throwaccuracy( Char attacker, Char defender, boolean cursed, int encumrance, boolean identified ) {
		float acuRoll = Random.Float( attacker.attackSkill( defender ) );
		float defRoll = Random.Float( defender.defenseSkill( attacker ) );
		acuRoll = (Dungeon.hero.heroClass == HeroClass.WARRIOR ? acuRoll * 0.75f : acuRoll);
		acuRoll = (Dungeon.hero.subClass == HeroSubClass.DEADEYE ? acuRoll * 1.05f : acuRoll);
		acuRoll = (Level.adjacent( attacker.pos, defender.pos ) ? acuRoll : acuRoll * 0.9f);
		acuRoll = (attacker.buff(Blindness.class) == null) ? acuRoll : acuRoll / 2;
		defRoll *= (identified ? 1 : 1.2);
		return (cursed ? acuRoll / 2f : acuRoll) >= defRoll + (Random.Float( Math.max(encumrance, 0 ) ));
		// return (cursed ? acuRoll / 2f : acuRoll) >= defRoll + (identified ? 0 : Random.Float( 5 )) + (Random.Float( Math.max(encumrance, 0 ) ));
	}

}
