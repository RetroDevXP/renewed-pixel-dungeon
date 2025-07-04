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
package com.retrodevxp.pixeldungeon.items.wands;

import java.util.ArrayList;

import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Cripple;
import com.retrodevxp.pixeldungeon.actors.buffs.Invisibility;
import com.retrodevxp.pixeldungeon.actors.buffs.Weakness;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.hero.HeroClass;
import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.effects.MagicMissile;
import com.retrodevxp.pixeldungeon.effects.particles.ShadowParticle;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.ItemStatusHandler;
import com.retrodevxp.pixeldungeon.items.KindOfWeapon;
import com.retrodevxp.pixeldungeon.items.bags.Bag;
import com.retrodevxp.pixeldungeon.items.rings.RingOfPower.Power;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.retrodevxp.pixeldungeon.items.wands.Wand.Charger;
import com.retrodevxp.pixeldungeon.mechanics.Ballistica;
import com.retrodevxp.pixeldungeon.scenes.CellSelector;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.pixeldungeon.ui.QuickSlot;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.pixeldungeon.windows.WndBag;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.Callback;
import com.retrodevxp.utils.Random;

public abstract class Wand extends KindOfWeapon {

	private static final int USAGES_TO_KNOW	= 40;
	
	public static final String AC_ZAP	= "ZAP";
	
	public static final String AC_MERGE = "MERGE";

    private static final float TIME_TO_MERGE = 2.0f;
	private static final String TXT_MERGED = "you merged two your wands to create one %s";
    private static final String TXT_SELECT_WAND = "Select a wand to merge";

	private static final String TXT_WOOD	= "This thin %s wand is warm to the touch. Who knows what it will do when used?";
	private static final String TXT_DAMAGE	= "When this wand is used as a melee weapon, its average damage is %d points per hit.";
	private static final String TXT_WEAPON	= "You can use this wand as a melee weapon.";
			
	private static final String TXT_FIZZLES		= "your wand fizzles; it must be out of charges for now";
	private static final String TXT_SELF_TARGET	= "You can't target yourself";
	
	private static final String TXT_IDENTIFY	= "You are now familiar enough with your %s.";
	
	private static final float TIME_TO_ZAP	= 1f;
	
	public int maxCharges = initialCharges();
	public int curCharges = maxCharges;
	
	protected Charger charger;
	
	private boolean curChargeKnown = false;
	
	private int usagesToKnow = USAGES_TO_KNOW;
	
	protected boolean hitChars = true;

	protected boolean disenchantEquipped;

	private final WndBag.Listener mergeItemSelector;
	
	private static final Class<?>[] wands = {
		WandOfTeleportation.class, 
		WandOfSlowness.class, 
		WandOfFirebolt.class, 
		WandOfPoison.class, 
		WandOfRegrowth.class,
		WandOfBlink.class,
		WandOfLightning.class,
		WandOfAmok.class,
		WandOfReach.class,
		WandOfFlock.class,
		WandOfDisintegration.class,
		WandOfAvalanche.class,
		WandOfSpirits.class,
		WandOfBlindness.class
	};
	private static final String[] woods = 
		{"holly", "yew", "ebony", "cherry", "teak", "rowan", "willow", "mahogany", "bamboo", "purpleheart", "oak", "birch", "pine", "alder"};
	private static final Integer[] images = {
		ItemSpriteSheet.WAND_HOLLY, 
		ItemSpriteSheet.WAND_YEW, 
		ItemSpriteSheet.WAND_EBONY, 
		ItemSpriteSheet.WAND_CHERRY, 
		ItemSpriteSheet.WAND_TEAK, 
		ItemSpriteSheet.WAND_ROWAN, 
		ItemSpriteSheet.WAND_WILLOW, 
		ItemSpriteSheet.WAND_MAHOGANY, 
		ItemSpriteSheet.WAND_BAMBOO, 
		ItemSpriteSheet.WAND_PURPLEHEART, 
		ItemSpriteSheet.WAND_OAK, 
		ItemSpriteSheet.WAND_BIRCH,
		ItemSpriteSheet.WAND_PINE,
		ItemSpriteSheet.WAND_ALDER};
	
	private static ItemStatusHandler<Wand> handler;
	
	private String wood;
	
	{
		defaultAction = AC_ZAP;
	}
	
	@SuppressWarnings("unchecked")
	public static void initWoods() {
		handler = new ItemStatusHandler<Wand>( (Class<? extends Wand>[])wands, woods, images );
	}
	
	public static void save( Bundle bundle ) {
		handler.save( bundle );
	}
	
	@SuppressWarnings("unchecked")
	public static void restore( Bundle bundle ) {
		handler = new ItemStatusHandler<Wand>( (Class<? extends Wand>[])wands, woods, images, bundle );
	}
	
	public Wand() {
		super();

		this.mergeItemSelector = new WndBag.Listener() {
            @Override
            public void onSelect(Item item) {
                if (item != null) {
                    Sample.INSTANCE.play(Assets.SND_EVOKE);
                    ScrollOfUpgrade.upgrade(Wand.curUser);
                    Wand.evoke(Wand.curUser);
                    Wand wand = (Wand) item;
                    wand.level(Math.max(wand.level(), level()));

                    wand.levelKnown = wand.levelKnown && levelKnown;
                    wand.curChargeKnown = wand.curChargeKnown && curChargeKnown;
					
                    wand.upgrade();
                    GLog.w(Wand.TXT_MERGED, wand.toString());
                    Wand.curUser.spendAndNext(2.0f);
                    Badges.validateItemLevelAquired(item);
                } else if (disenchantEquipped) {
                    Wand.curUser.belongings.weapon = Wand.this;
                    updateQuickslot();
                } else {
                    collect(Wand.curUser.belongings.backpack);
                }
            }
        };

		try {
			image = handler.image( this );
			wood = handler.label( this );
		} catch (Exception e) {
			// Wand of Magic Missile
		}
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (curCharges > 0 || !curChargeKnown) {
			actions.add( AC_ZAP );
		}
		if (hero.heroClass != HeroClass.MAGE) {
			actions.remove( AC_EQUIP );
			actions.remove( AC_UNEQUIP );
		}
		if (hero.belongings.getItems(getClass()).size() > 1) {
            actions.add(AC_MERGE);
        }
		return actions;
	}
	
	@Override
	public boolean doUnequip( Hero hero, boolean collect, boolean single ) {
		onDetach();
		return super.doUnequip( hero, collect, single );
	}
	
	@Override
	public void activate( Hero hero ) {
		charge( hero );
	}
	
	protected void doMerge(Hero hero) {
        if (hero.belongings.weapon == this) {
            disenchantEquipped = true;
            hero.belongings.weapon = null;
            updateQuickslot();
        } else {
            disenchantEquipped = false;
            detach(hero.belongings.backpack);
        }
        curUser = hero;
        WndBag.wandClass = getClass();
        GameScene.selectItem(this.mergeItemSelector, WndBag.Mode.WAND, TXT_SELECT_WAND);
    }

	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_ZAP )) {
			
			curUser = hero;
			curItem = this;
			GameScene.selectCell( zapper );
			
		} else if (action.equals(AC_MERGE)) {
            doMerge(hero);
        } else {
			
			super.execute( hero, action );
			
		}
	}
	
	protected abstract void onZap( int cell );
	
	@Override
	public boolean collect( Bag container ) {
		if (super.collect( container )) {
			if (container.owner != null) {
				charge( container.owner );
			}
			return true;
		} else {
			return false;
		}
	};
	
	public void charge( Char owner ) {
		if (charger == null) {
			(charger = new Charger()).attachTo( owner );
		}
	}
	
	@Override
	public void onDetach( ) {
		stopCharging();
	}
	
	public void stopCharging() {
		if (charger != null) {
			charger.detach();
			charger = null;
		}
	}
	
	public int power() {
		int eLevel = effectiveLevel();
		if (charger != null) {
			Power power = charger.target.buff( Power.class );
			return power == null ? eLevel : Math.max( eLevel + power.level, 0 ); 
		} else {
			return eLevel;
		}
	}
	
	protected boolean isKnown() {
		return handler.isKnown( this );
	}
	
	public void setKnown() {
		if (!isKnown()) {
			handler.know( this );
		}
		
		Badges.validateAllWandsIdentified();
	}
	
	@Override
	public Item identify() {
		
		setKnown();
		curChargeKnown = true;
		super.identify();
		
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder( super.toString() );
		
		String status = status();
		if (status != null) {
			sb.append( " (" + status +  ")" );
		}
		
		if (isBroken()) {
			sb.insert( 0, "broken " );
		}
		
		return sb.toString();
	}
	
	@Override
	public String name() {
		return isKnown() ? name : wood + " wand";
	}
	
	@Override
	public String info() {
		StringBuilder info = new StringBuilder( isKnown() ? desc() : Utils.format( TXT_WOOD, wood ) );
		if (Dungeon.hero.heroClass == HeroClass.MAGE) {
			info.append( "\n\n" );
			if (levelKnown) {
				int min = min();
				info.append( Utils.format( TXT_DAMAGE, min + (max() - min) / 2 ) );
			} else {
				info.append(  Utils.format( TXT_WEAPON ) );
			}
		}
		return info.toString();
	}
	
	@Override
	public boolean isIdentified() {
		return super.isIdentified() && isKnown() && curChargeKnown;
	}
	
	@Override
	public String status() {
		if (levelKnown) {
			return (curChargeKnown ? curCharges : "?") + "/" + maxCharges;
		} else {
			return null;
		}
	}
	
	@Override
	public Item upgrade() {

		super.upgrade();
		
		updateLevel();
		curCharges = Math.min( curCharges + 1, maxCharges );
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public Item degrade() {
		super.degrade();
		
		updateLevel();
		updateQuickslot();
		
		return this;
	}
	
	@Override
	public int maxDurability( int lvl ) {
		// return 6 * (lvl < 16 ? 16 - lvl : 1);
		return Integer.MAX_VALUE;
	}
	
	protected void updateLevel() {
		maxCharges = Math.min( initialCharges() + level(), 9 );
		curCharges = Math.min( curCharges, maxCharges );
	}
	
	protected int initialCharges() {
		return 2;
	}
	
	@Override
	public int min() {
		int tier = 1 + effectiveLevel() / 3;
		return tier;
	}
	
	@Override
	public int max() {
		int level = effectiveLevel();
		int tier = 1 + level / 3;
		return (tier * tier - tier + 10) / 2 + level;
	}
	
	protected void fx( int cell, Callback callback ) {
		MagicMissile.blueLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
	}

	protected void wandUsed() {
		
		curCharges--;
		if (!isIdentified() && --usagesToKnow <= 0) {
			identify();
			GLog.w( TXT_IDENTIFY, name() );
		} else {
			updateQuickslot();
		}
		
		use();
		
		curUser.spendAndNext( TIME_TO_ZAP );
	}
	
	//If the Warlock does not have enough HP for a wand charge, he charges it anyways, but becomes crippled at 1 HP.
	//However, if he is already at 1 HP when he tries to charge it, it fails. He needs to spend at least some HP.
	protected boolean wandUseWarlock() {
			if (Dungeon.hero.HP <= 1){
				return false;
			}
			curCharges++;
			int cost = (int)(Dungeon.hero.HT / 10f + Random.Float( Dungeon.hero.HT / 15f, 1 + Dungeon.hero.HT / 10f ));
			if (Dungeon.hero.HP > cost){
				Dungeon.hero.HP -= cost;
				Dungeon.hero.sprite.emitter().burst( ShadowParticle.UP, 10 );
				//Failsafe.
				if (Dungeon.hero.HP < 1){
					Dungeon.hero.HP = 1;
				}
			}
			else{
				Dungeon.hero.HP = 1;
				Dungeon.hero.sprite.emitter().burst( ShadowParticle.UP, 7 );
				Buff.prolong( Dungeon.hero, Weakness.class, Random.Float( 3.5f, 9.5f ) );
				Buff.prolong( Dungeon.hero, Cripple.class, Random.Float( 1.5f, 5.5f ) );
			}

			updateQuickslot();
			return true;
	}
	
	@Override
	public Item random() {
		if (Random.Float() < 0.5f) {
			upgrade();
			if (Random.Float() < 0.15f) {
				upgrade();
			}
		}
		
		return this;
	}
	
	public static boolean allKnown() {
		return handler.known().size() == wands.length;
	}
	
	@Override
	public int price() {
		return considerState( 50 );
	}
	
	private static final String UNFAMILIRIARITY		= "unfamiliarity";
	private static final String MAX_CHARGES			= "maxCharges";
	private static final String CUR_CHARGES			= "curCharges";
	private static final String CUR_CHARGE_KNOWN	= "curChargeKnown";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( UNFAMILIRIARITY, usagesToKnow );
		bundle.put( MAX_CHARGES, maxCharges );
		bundle.put( CUR_CHARGES, curCharges );
		bundle.put( CUR_CHARGE_KNOWN, curChargeKnown );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		if ((usagesToKnow = bundle.getInt( UNFAMILIRIARITY )) == 0) {
			usagesToKnow = USAGES_TO_KNOW;
		}
		maxCharges = bundle.getInt( MAX_CHARGES );
		curCharges = bundle.getInt( CUR_CHARGES );
		curChargeKnown = bundle.getBoolean( CUR_CHARGE_KNOWN );
	}
	
	protected static CellSelector.Listener zapper = new  CellSelector.Listener() {
		
		@Override
		public void onSelect( Integer target ) {
			
			if (target != null) {
				
				if (target == curUser.pos) {
					GLog.i( TXT_SELF_TARGET );
					return;
				}
				
				final Wand curWand = (Wand)Wand.curItem;
				
				curWand.setKnown();
				
				final int cell = Ballistica.cast( curUser.pos, target, true, curWand.hitChars );
				curUser.sprite.zap( cell );
				
				QuickSlot.target( curItem, Actor.findChar( cell ) );
				
				if (curWand.curCharges > 0) {
					
					curUser.busy();
					
					curWand.fx( cell, new Callback() {
						@Override
						public void call() {
							curWand.onZap( cell );
							curWand.wandUsed();
						}
					} );
					
					Invisibility.dispel();
					
				}else if (Dungeon.hero.subClass == HeroSubClass.WARLOCK) {
					//Warlocks are able to use out-of-charge wands at a cost of their HP.
					if(curWand.wandUseWarlock() ){
					curUser.busy();
					
					curWand.fx( cell, new Callback() {
						@Override
						public void call() {
							curWand.onZap( cell );
							curWand.wandUsed();
						}
					} );
					
					Invisibility.dispel();
				}
				else{
					curUser.spendAndNext( TIME_TO_ZAP );
					GLog.w( "You don't have enough energy in your soul to extract for a wand charge." );
					curWand.levelKnown = true;
					
					curWand.updateQuickslot();
				}
					
				} else {
					
					curUser.spendAndNext( TIME_TO_ZAP );
					GLog.w( TXT_FIZZLES );
					curWand.levelKnown = true;
					
					curWand.updateQuickslot();
				}
				
			}
		}
		
		@Override
		public String prompt() {
			return "Choose direction to zap";
		}
	};
	
	public class Charger extends Buff {
		
		private static final float TIME_TO_CHARGE = 40f;
		
		@Override
		public boolean attachTo( Char target ) {
			super.attachTo( target );
			delay();
			
			return true;
		}
		
		@Override
		public boolean act() {
			
			if (curCharges < maxCharges) {
				curCharges++;
				updateQuickslot();
			}
			
			delay();
			
			return true;
		}
		
		protected void delay() {
			float time2charge = ((Hero)target).heroClass == HeroClass.MAGE ? 
				TIME_TO_CHARGE / (float)Math.sqrt( 1 + effectiveLevel() ) : 
				TIME_TO_CHARGE;
				try{
					if (((Hero)target).isStarving()){
						time2charge *= 1.25f;
					}
				}
				catch(Exception e){
					
				}
			spend( time2charge );
		}
	}
}
