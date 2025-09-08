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
package com.retrodevxp.pixeldungeon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.retrodevxp.noosa.Game;
import com.retrodevxp.pixeldungeon.actors.mobs.Acidic;
import com.retrodevxp.pixeldungeon.actors.mobs.Albino;
import com.retrodevxp.pixeldungeon.actors.mobs.Bandit;
import com.retrodevxp.pixeldungeon.actors.mobs.Mob;
import com.retrodevxp.pixeldungeon.actors.mobs.Senior;
import com.retrodevxp.pixeldungeon.actors.mobs.Shielded;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.bags.ScrollHolder;
import com.retrodevxp.pixeldungeon.items.bags.SeedPouch;
import com.retrodevxp.pixeldungeon.items.bags.WandHolster;
import com.retrodevxp.pixeldungeon.items.potions.Potion;
import com.retrodevxp.pixeldungeon.items.rings.Ring;
import com.retrodevxp.pixeldungeon.items.rings.RingOfHaggler;
import com.retrodevxp.pixeldungeon.items.rings.RingOfThorns;
import com.retrodevxp.pixeldungeon.items.scrolls.Scroll;
import com.retrodevxp.pixeldungeon.items.wands.Wand;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.Callback;

public class Badges {
	
	public static enum Badge {
		MONSTERS_SLAIN_1( "10 enemies slain", 0, "Hunter" ),
		MONSTERS_SLAIN_2( "50 enemies slain", 1, "Hunter" ),
		MONSTERS_SLAIN_3( "150 enemies slain", 2, "Hunter" ),
		MONSTERS_SLAIN_4( "250 enemies slain", 3, "Hunter" ),
		GOLD_COLLECTED_1( "100 gold collected", 4, "Gold Collector" ),
		GOLD_COLLECTED_2( "500 gold collected", 5, "Gold Collector" ),
		GOLD_COLLECTED_3( "2500 gold collected", 6, "Gold Collector" ),
		GOLD_COLLECTED_4( "7500 gold collected", 7, "Gold Collector" ),
		LEVEL_REACHED_1( "Level 6 reached", 8, "Experienced" ),
		LEVEL_REACHED_2( "Level 12 reached", 9, "Experienced" ),
		LEVEL_REACHED_3( "Level 18 reached", 10, "Experienced" ),
		LEVEL_REACHED_4( "Level 24 reached", 11, "Experienced" ),
		ALL_POTIONS_IDENTIFIED( "All potions identified", 16, "Potion Identifier" ),
		ALL_SCROLLS_IDENTIFIED( "All scrolls identified", 17, "Scroll Identifier" ),
		ALL_RINGS_IDENTIFIED( "All rings identified", 18, "Ring Identifier" ),
		ALL_WANDS_IDENTIFIED( "All wands identified", 19, "Wand Identifier" ),
		ALL_ITEMS_IDENTIFIED( "All potions, scrolls, rings & wands identified", 35, "All-Knowing", true ),
		BAG_BOUGHT_SEED_POUCH,
		BAG_BOUGHT_SCROLL_HOLDER,
		BAG_BOUGHT_WAND_HOLSTER,
		ALL_BAGS_BOUGHT( "All bags bought", 23, "Backpacker" ),
		DEATH_FROM_FIRE( "Death from fire", 24, "In Flames" ),
		DEATH_FROM_POISON( "Death from poison", 25, "Poisonous Demise" ),
		DEATH_FROM_GAS( "Death from toxic gas", 26, "Too Toxic" ),
		DEATH_FROM_HUNGER( "Death from hunger", 27, "Starved" ),
		DEATH_FROM_GLYPH( "Death from an enchantment", 57, "Double Edged" ),
		DEATH_FROM_FALLING( "Death from falling down", 59, "Fatal Fall" ),
		SURVIVE_FALLING( "Take fall damage and survive", 75, "Survived The Fall" ),
		YASD( "Death from fire, poison, toxic gas & hunger", 34, "Yet Another Stupid Death", true ),
		BOSS_SLAIN_1_WARRIOR,
		BOSS_SLAIN_1_MAGE,
		BOSS_SLAIN_1_ROGUE,
		BOSS_SLAIN_1_HUNTRESS,
		BOSS_SLAIN_1( "1st boss slain", 12, "Glurp...Glurp..." ),
		BOSS_SLAIN_2( "2nd boss slain", 13, "No More Espionage" ),
		BOSS_SLAIN_3( "3rd boss slain", 14, "Shut Down" ),
		BOSS_SLAIN_4( "4th boss slain", 15, "To The Grave" ),
		BOSS_SLAIN_1_ALL_CLASSES( "1st boss slain by Warrior, Mage, Rogue & Huntress", 32, "Goo is Gone", true ),
		BOSS_SLAIN_3_GLADIATOR,
		BOSS_SLAIN_3_BERSERKER,
		BOSS_SLAIN_3_KNIGHT,
		BOSS_SLAIN_3_WARLOCK,
		BOSS_SLAIN_3_BATTLEMAGE,
		BOSS_SLAIN_3_SCRIBE,
		BOSS_SLAIN_3_FREERUNNER,
		BOSS_SLAIN_3_SPY,
		BOSS_SLAIN_3_CRAWLER,
		BOSS_SLAIN_3_DEADEYE,
		BOSS_SLAIN_3_WARDEN,
		BOSS_SLAIN_3_CHASER,
		BOSS_SLAIN_3_ALL_SUBCLASSES( 
			"3rd boss slain by all subclasses", 33, "Subclass Specialist", true ),
		RING_OF_HAGGLER( "Ring of Haggler obtained", 20, "Haggler's Path" ),
		RING_OF_THORNS( "Ring of Thorns obtained", 21, "Mutual Damage" ),
		STRENGTH_ATTAINED_1( "13 points of Strength attained", 40, "Growing Strong" ),
		STRENGTH_ATTAINED_2( "15 points of Strength attained", 41, "Growing Strong" ),
		STRENGTH_ATTAINED_3( "17 points of Strength attained", 42, "Growing Strong" ),
		STRENGTH_ATTAINED_4( "19 points of Strength attained", 43, "Growing Strong" ),
		FOOD_EATEN_1( "10 pieces of food eaten", 44, "Hungry Hero" ),
		FOOD_EATEN_2( "20 pieces of food eaten", 45, "Hungry Hero" ),
		FOOD_EATEN_3( "30 pieces of food eaten", 46, "Hungry Hero" ),
		FOOD_EATEN_4( "40 pieces of food eaten", 47, "Hungry Hero" ),
		MASTERY_WARRIOR,
		MASTERY_MAGE,
		MASTERY_ROGUE,
		MASTERY_HUNTRESS,
		ITEM_LEVEL_1( "Item of level 3 acquired", 48, "Artifact" ),
		ITEM_LEVEL_2( "Item of level 6 acquired", 49, "Artifact" ),
		ITEM_LEVEL_3( "Item of level 9 acquired", 50, "Artifact" ),
		ITEM_LEVEL_4( "Item of level 12 acquired", 51, "Artifact" ),
		RARE_ALBINO,
		RARE_BANDIT,
		RARE_SHIELDED,
		RARE_SENIOR,
		RARE_ACIDIC,
		RARE( "All rare monsters slain", 37, "", true ),
		VICTORY_WARRIOR,
		VICTORY_MAGE,
		VICTORY_ROGUE,
		VICTORY_HUNTRESS,
		VICTORY( "Amulet of Yendor obtained", 22, "The Ultimate Goal" ),
		VICTORY_ALL_CLASSES( "Amulet of Yendor obtained by Warrior, Mage, Rogue & Huntress", 36, "", true ),
		MASTERY_COMBO( "7-hit combo with Gladiator", 56, "Combo Master" ),
		WARLOCK_SOUL( "Get down to 1 HP with Warlock's ability", 74, "Tempting Fate" ),
		POTIONS_COOKED_1( "3 potions cooked", 52, "Alchemist" ),
		POTIONS_COOKED_2( "6 potions cooked", 53, "Alchemist" ),
		POTIONS_COOKED_3( "9 potions cooked", 54, "Alchemist" ),
		POTIONS_COOKED_4( "12 potions cooked", 55, "Alchemist" ),
		THROW_MELEE_1( "Enemy defeated by throwing a melee weapon.", 68, "Improvised Throw" ),
		THROW_MELEE_2( "5 enemies defeated by throwing a melee weapon in one dungeon run.", 69, "Improvised Throw" ),
		THROW_MELEE_3( "10 enemies defeated by throwing a melee weapon in one dungeon run.", 70, "Improvised Throw" ),
		THROW_MELEE_4( "25 enemies defeated by throwing a melee weapon in one dungeon run.", 71, "Improvised Throw" ),
		SURPRISE_1( "Land 10 surprise attacks in one dungeon run.", 64, "Assassin" ),
		SURPRISE_2( "Land 20 surprise attacks in one dungeon run.", 65, "Assassin" ),
		SURPRISE_3( "Land 35 surprise attacks in one dungeon run.", 66, "Assassin" ),
		SURPRISE_4( "Land 50 surprise attacks in one dungeon run.", 67, "Assassin" ),
		YOG_DZEWA_ENCOUNTER( "Reach the deepest depth and encounter Yog-Dezwa", 72, "Hope Is An Illusion" ),
		RESURRECT( "Return from death", 73, "Second Chance" ),
		NO_MONSTERS_SLAIN( "Level completed without killing any monsters", 28, "Pacifist" ),
		GRIM_WEAPON( "Monster killed by a Grim weapon", 29, "Grim Reaper" ),
		PIRANHAS( "6 piranhas killed", 30, "Dangerous Fisher" ),
		NIGHT_HUNTER( "15 monsters killed at nighttime", 58, "Dark Hunter" ),
		GAMES_PLAYED_1( "10 games played in total", 60, "Dedicated Dungeoneer", true ),
		GAMES_PLAYED_2( "100 games played in total", 61, "Dedicated Dungeoneer", true ),
		GAMES_PLAYED_3( "500 games played in total", 62, "Dedicated Dungeoneer", true ),
		GAMES_PLAYED_4( "2000 games played in total", 63, "Dedicated Dungeoneer", true ),
		HAPPY_END( "Achieved the Happy Ending", 38, "Happy Ending" ),
		CHAMPION( "Won with a Challenge activated", 39, "Champion", true ),
		SUPPORTER( "Thank you for checking the donate page!", 31, "Donator", true );
		
		public boolean meta;
		
		public String description;
		public int image;
		public String title;

		
		private Badge( String description, int image , String title) {
			this( description, image, title, false );
		}
		
		private Badge( String description, int image, String title, boolean meta ) {
			this.description = description;
			this.image = image;
			this.meta = meta;
			this.title = title;
		}
		
		private Badge() {
			this( "", -1, "" );
		}
	}
	
	private static HashSet<Badge> global;
	private static HashSet<Badge> local = new HashSet<Badges.Badge>();
	
	private static boolean saveNeeded = false;
	
	public static Callback loadingListener = null;
	
	public static void reset() {
		local.clear();
		loadGlobal();
	}
	
	private static final String BADGES_FILE	= "badges.dat";
	private static final String BADGES		= "badges";
	
	private static HashSet<Badge> restore( Bundle bundle ) {
		HashSet<Badge> badges = new HashSet<Badge>();
		
		String[] names = bundle.getStringArray( BADGES );
		if (names == null) {
			return badges;
		}
		for (int i=0; i < names.length; i++) {
			try {
				badges.add( Badge.valueOf( names[i] ) );
			} catch (Exception e) {
			}
		}
	
		return badges;
	}
	
	private static void store( Bundle bundle, HashSet<Badge> badges ) {
		int count = 0;
		String names[] = new String[badges.size()];
		
		for (Badge badge:badges) {
			names[count++] = badge.toString();
		}
		bundle.put( BADGES, names );
	}
	
	public static void loadLocal( Bundle bundle ) {
		local = restore( bundle );
	}
	
	public static void saveLocal( Bundle bundle ) {
		store( bundle, local );
	}
	
	public static void loadGlobal() {
		if (global == null) {
			try {
				Bundle bundle = Bundle.read( Game.instance.readFile( BADGES_FILE ) );
				
				global = restore( bundle );
				
			} catch (IOException e) {
				global = new HashSet<Badge>();
			}
		}
	}
	
	public static void saveGlobal() {
		
		Bundle bundle = null;
		
		if (saveNeeded) {
			
			bundle = new Bundle();
			store( bundle, global );
			
			Game.instance.writeFile( BADGES_FILE, Bundle.write(bundle) );
			saveNeeded = false;
		}
	}

	public static void validateMonstersSlain() {
		Badge badge = null;
		
		if (!local.contains( Badge.MONSTERS_SLAIN_1 ) && Statistics.enemiesSlain >= 10) {
			badge = Badge.MONSTERS_SLAIN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_2 ) && Statistics.enemiesSlain >= 50) {
			badge = Badge.MONSTERS_SLAIN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_3 ) && Statistics.enemiesSlain >= 150) {
			badge = Badge.MONSTERS_SLAIN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.MONSTERS_SLAIN_4 ) && Statistics.enemiesSlain >= 250) {
			badge = Badge.MONSTERS_SLAIN_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateGoldCollected() {
		Badge badge = null;
		
		if (!local.contains( Badge.GOLD_COLLECTED_1 ) && Statistics.goldCollected >= 100) {
			badge = Badge.GOLD_COLLECTED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_2 ) && Statistics.goldCollected >= 500) {
			badge = Badge.GOLD_COLLECTED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_3 ) && Statistics.goldCollected >= 2500) {
			badge = Badge.GOLD_COLLECTED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.GOLD_COLLECTED_4 ) && Statistics.goldCollected >= 7500) {
			badge = Badge.GOLD_COLLECTED_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateLevelReached() {
		Badge badge = null;
		
		if (!local.contains( Badge.LEVEL_REACHED_1 ) && Dungeon.hero.lvl >= 6) {
			badge = Badge.LEVEL_REACHED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_2 ) && Dungeon.hero.lvl >= 12) {
			badge = Badge.LEVEL_REACHED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_3 ) && Dungeon.hero.lvl >= 18) {
			badge = Badge.LEVEL_REACHED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.LEVEL_REACHED_4 ) && Dungeon.hero.lvl >= 24) {
			badge = Badge.LEVEL_REACHED_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateStrengthAttained() {
		Badge badge = null;
		
		if (!local.contains( Badge.STRENGTH_ATTAINED_1 ) && Dungeon.hero.STR >= 13) {
			badge = Badge.STRENGTH_ATTAINED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_2 ) && Dungeon.hero.STR >= 15) {
			badge = Badge.STRENGTH_ATTAINED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_3 ) && Dungeon.hero.STR >= 17) {
			badge = Badge.STRENGTH_ATTAINED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.STRENGTH_ATTAINED_4 ) && Dungeon.hero.STR >= 19) {
			badge = Badge.STRENGTH_ATTAINED_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateFoodEaten() {
		Badge badge = null;
		
		if (!local.contains( Badge.FOOD_EATEN_1 ) && Statistics.foodEaten >= 10) {
			badge = Badge.FOOD_EATEN_1;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_2 ) && Statistics.foodEaten >= 20) {
			badge = Badge.FOOD_EATEN_2;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_3 ) && Statistics.foodEaten >= 30) {
			badge = Badge.FOOD_EATEN_3;
			local.add( badge );
		}
		if (!local.contains( Badge.FOOD_EATEN_4 ) && Statistics.foodEaten >= 40) {
			badge = Badge.FOOD_EATEN_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validatePotionsCooked() {
		Badge badge = null;
		
		if (!local.contains( Badge.POTIONS_COOKED_1 ) && Statistics.potionsCooked >= 3) {
			badge = Badge.POTIONS_COOKED_1;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_2 ) && Statistics.potionsCooked >= 6) {
			badge = Badge.POTIONS_COOKED_2;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_3 ) && Statistics.potionsCooked >= 9) {
			badge = Badge.POTIONS_COOKED_3;
			local.add( badge );
		}
		if (!local.contains( Badge.POTIONS_COOKED_4 ) && Statistics.potionsCooked >= 12) {
			badge = Badge.POTIONS_COOKED_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validatePiranhasKilled() {
		Badge badge = null;
		
		if (!local.contains( Badge.PIRANHAS ) && Statistics.piranhasKilled >= 6) {
			badge = Badge.PIRANHAS;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateItemLevelAquired( Item item ) {
		
		// This method should be called:
		// 1) When an item gets obtained (Item.collect)
		// 2) When an item gets upgraded (ScrollOfUpgrade, ScrollOfWeaponUpgrade, ShortSword, WandOfMagicMissile)
		// 3) When an item gets identified
		if (!item.levelKnown) {
			return;
		}
		
		Badge badge = null;
		
		if (!local.contains( Badge.ITEM_LEVEL_1 ) && item.level() >= 3) {
			badge = Badge.ITEM_LEVEL_1;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_2 ) && item.level() >= 6) {
			badge = Badge.ITEM_LEVEL_2;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_3 ) && item.level() >= 9) {
			badge = Badge.ITEM_LEVEL_3;
			local.add( badge );
		}
		if (!local.contains( Badge.ITEM_LEVEL_4 ) && item.level() >= 12) {
			badge = Badge.ITEM_LEVEL_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateAllPotionsIdentified() {
		if (Dungeon.hero != null && Dungeon.hero.isAlive() && 
			!local.contains( Badge.ALL_POTIONS_IDENTIFIED ) && Potion.allKnown()) {
			
			Badge badge = Badge.ALL_POTIONS_IDENTIFIED;
			local.add( badge );
			displayBadge( badge );
			
			validateAllItemsIdentified();
		}
	}
	
	public static void validateAllScrollsIdentified() {
		if (Dungeon.hero != null && Dungeon.hero.isAlive() && 
			!local.contains( Badge.ALL_SCROLLS_IDENTIFIED ) && Scroll.allKnown()) {
			
			Badge badge = Badge.ALL_SCROLLS_IDENTIFIED;
			local.add( badge );
			displayBadge( badge );
			
			validateAllItemsIdentified();
		}
	}
	
	public static void validateAllRingsIdentified() {
		if (Dungeon.hero != null && Dungeon.hero.isAlive() && 
			!local.contains( Badge.ALL_RINGS_IDENTIFIED ) && Ring.allKnown()) {
			
			Badge badge = Badge.ALL_RINGS_IDENTIFIED;
			local.add( badge );
			displayBadge( badge );
			
			validateAllItemsIdentified();
		}
	}
	
	public static void validateAllWandsIdentified() {
		if (Dungeon.hero != null && Dungeon.hero.isAlive() && 
			!local.contains( Badge.ALL_WANDS_IDENTIFIED ) && Wand.allKnown()) {
			
			Badge badge = Badge.ALL_WANDS_IDENTIFIED;
			local.add( badge );
			displayBadge( badge );
			
			validateAllItemsIdentified();
		}
	}
	
	public static void validateAllBagsBought( Item bag ) {
		
		Badge badge = null;
		if (bag instanceof SeedPouch) {
			badge = Badge.BAG_BOUGHT_SEED_POUCH;
		} else if (bag instanceof ScrollHolder) {
			badge = Badge.BAG_BOUGHT_SCROLL_HOLDER;
		} else if (bag instanceof WandHolster) {
			badge = Badge.BAG_BOUGHT_WAND_HOLSTER;
		}
		
		if (badge != null) {
			
			local.add( badge );
			
			if (!local.contains( Badge.ALL_BAGS_BOUGHT ) &&
				local.contains( Badge.BAG_BOUGHT_SCROLL_HOLDER ) &&
				local.contains( Badge.BAG_BOUGHT_SEED_POUCH ) &&
				local.contains( Badge.BAG_BOUGHT_WAND_HOLSTER )) {
						
					badge = Badge.ALL_BAGS_BOUGHT;
					local.add( badge );
					displayBadge( badge );
			}
		}
	}
	
	public static void validateAllItemsIdentified() {
		if (!global.contains( Badge.ALL_ITEMS_IDENTIFIED ) &&
			global.contains( Badge.ALL_POTIONS_IDENTIFIED ) &&
			global.contains( Badge.ALL_SCROLLS_IDENTIFIED ) &&
			global.contains( Badge.ALL_RINGS_IDENTIFIED ) &&
			global.contains( Badge.ALL_WANDS_IDENTIFIED )) {
			
			Badge badge = Badge.ALL_ITEMS_IDENTIFIED;
			displayBadge( badge );
		}
	}
	
	public static void validateDeathFromFire() {
		Badge badge = Badge.DEATH_FROM_FIRE;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}

	
	
	public static void validateDeathFromPoison() {
		Badge badge = Badge.DEATH_FROM_POISON;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromGas() {
		Badge badge = Badge.DEATH_FROM_GAS;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromHunger() {
		Badge badge = Badge.DEATH_FROM_HUNGER;
		local.add( badge );
		displayBadge( badge );
		
		validateYASD();
	}
	
	public static void validateDeathFromGlyph() {
		Badge badge = Badge.DEATH_FROM_GLYPH;
		local.add( badge );
		displayBadge( badge );
	}
	
	public static void validateDeathFromFalling() {
		Badge badge = Badge.DEATH_FROM_FALLING;
		local.add( badge );
		displayBadge( badge );
	}
	
	private static void validateYASD() {
		if (global.contains( Badge.DEATH_FROM_FIRE ) &&
			global.contains( Badge.DEATH_FROM_POISON ) &&
			global.contains( Badge.DEATH_FROM_GAS ) &&
			global.contains( Badge.DEATH_FROM_HUNGER)) {
			
			Badge badge = Badge.YASD;
			local.add( badge );
			displayBadge( badge );
		}
	}

	public static void validateSurviveFalling() {
		Badge badge = Badge.SURVIVE_FALLING;
		local.add( badge );
		displayBadge( badge );
		
	}
	public static void validateResurrect() {
		Badge badge = Badge.RESURRECT;
		local.add( badge );
		displayBadge( badge );
		
	}
	public static void validateYogEncounter() {
		Badge badge = Badge.YOG_DZEWA_ENCOUNTER;
		local.add( badge );
		displayBadge( badge );
		
	}
	public static void validate1HPWarlock() {
		Badge badge = Badge.WARLOCK_SOUL;
		local.add( badge );
		displayBadge( badge );
		
	}

	public static void validateSurpriseAttack() {
		Badge badge = null;
		
		if (!local.contains( Badge.SURPRISE_1 ) && Statistics.surpriseAttacks >= 10) {
			badge = Badge.SURPRISE_1;
			local.add( badge );
		}
		if (!local.contains( Badge.SURPRISE_2 ) && Statistics.surpriseAttacks >= 20) {
			badge = Badge.SURPRISE_2;
			local.add( badge );
		}
		if (!local.contains( Badge.SURPRISE_3 ) && Statistics.surpriseAttacks >= 35) {
			badge = Badge.SURPRISE_3;
			local.add( badge );
		}
		if (!local.contains( Badge.SURPRISE_4 ) && Statistics.surpriseAttacks >= 50) {
			badge = Badge.SURPRISE_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}

	public static void validateThrownMelee() {
		Badge badge = null;
		
		if (!local.contains( Badge.THROW_MELEE_1 ) && Statistics.thrownMeleeKills >= 1) {
			badge = Badge.THROW_MELEE_1;
			local.add( badge );
		}
		if (!local.contains( Badge.THROW_MELEE_2 ) && Statistics.thrownMeleeKills >= 5) {
			badge = Badge.THROW_MELEE_2;
			local.add( badge );
		}
		if (!local.contains( Badge.THROW_MELEE_3 ) && Statistics.thrownMeleeKills >= 10) {
			badge = Badge.THROW_MELEE_3;
			local.add( badge );
		}
		if (!local.contains( Badge.THROW_MELEE_4 ) && Statistics.thrownMeleeKills >= 25) {
			badge = Badge.THROW_MELEE_4;
			local.add( badge );
		}
		
		displayBadge( badge );
	}
	
	public static void validateBossSlain() {
		Badge badge = null;
		switch (Dungeon.depth) {
		case 5:
			badge = Badge.BOSS_SLAIN_1;
			break;
		case 10:
			badge = Badge.BOSS_SLAIN_2;
			break;
		case 15:
			badge = Badge.BOSS_SLAIN_3;
			break;
		case 20:
			badge = Badge.BOSS_SLAIN_4;
			break;
		}
		
		if (badge != null) {
			local.add( badge );
			displayBadge( badge );
			
			if (badge == Badge.BOSS_SLAIN_1) {
				switch (Dungeon.hero.heroClass) {
				case WARRIOR:
					badge = Badge.BOSS_SLAIN_1_WARRIOR;
					break;
				case MAGE:
					badge = Badge.BOSS_SLAIN_1_MAGE;
					break;
				case ROGUE:
					badge = Badge.BOSS_SLAIN_1_ROGUE;
					break;
				case HUNTRESS:
					badge = Badge.BOSS_SLAIN_1_HUNTRESS;
					break;
				}
				local.add( badge );
				if (!global.contains( badge )) {
					global.add( badge );
					saveNeeded = true;
				}
				
				if (global.contains( Badge.BOSS_SLAIN_1_WARRIOR ) &&
					global.contains( Badge.BOSS_SLAIN_1_MAGE ) &&
					global.contains( Badge.BOSS_SLAIN_1_ROGUE ) &&
					global.contains( Badge.BOSS_SLAIN_1_HUNTRESS)) {
					
					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!global.contains( badge )) {
						displayBadge( badge );
						global.add( badge );
						saveNeeded = true;
					}
				}
			} else
			if (badge == Badge.BOSS_SLAIN_3) {
				switch (Dungeon.hero.subClass) {
				case GLADIATOR:
					badge = Badge.BOSS_SLAIN_3_GLADIATOR;
					break;
				case BERSERKER:
					badge = Badge.BOSS_SLAIN_3_BERSERKER;
					break;
				case KNIGHT:
					badge = Badge.BOSS_SLAIN_3_KNIGHT;
					break;
				case WARLOCK:
					badge = Badge.BOSS_SLAIN_3_WARLOCK;
					break;
				case BATTLEMAGE:
					badge = Badge.BOSS_SLAIN_3_BATTLEMAGE;
					break;
					case SCRIBE:
					badge = Badge.BOSS_SLAIN_3_SCRIBE;
					break;
				case FREERUNNER:
					badge = Badge.BOSS_SLAIN_3_FREERUNNER;
					break;
				case SPY:
					badge = Badge.BOSS_SLAIN_3_SPY;
					break;
					case CRAWLER:
					badge = Badge.BOSS_SLAIN_3_CRAWLER;
					break;
				case DEADEYE:
					badge = Badge.BOSS_SLAIN_3_DEADEYE;
					break;
				case WARDEN:
					badge = Badge.BOSS_SLAIN_3_WARDEN;
					break;
					case CHASER:
					badge = Badge.BOSS_SLAIN_3_CHASER;
					break;
				default:
					return;
				}
				local.add( badge );
				if (!global.contains( badge )) {
					global.add( badge );
					saveNeeded = true;
				}
				
				if (global.contains( Badge.BOSS_SLAIN_3_GLADIATOR ) &&
					global.contains( Badge.BOSS_SLAIN_3_BERSERKER ) &&
					global.contains( Badge.BOSS_SLAIN_3_KNIGHT ) &&
					global.contains( Badge.BOSS_SLAIN_3_WARLOCK ) &&
					global.contains( Badge.BOSS_SLAIN_3_BATTLEMAGE ) &&
					global.contains( Badge.BOSS_SLAIN_3_SCRIBE ) &&
					global.contains( Badge.BOSS_SLAIN_3_FREERUNNER ) &&
					global.contains( Badge.BOSS_SLAIN_3_SPY ) &&
					global.contains( Badge.BOSS_SLAIN_3_CRAWLER ) &&
					global.contains( Badge.BOSS_SLAIN_3_DEADEYE ) &&
					global.contains( Badge.BOSS_SLAIN_3_WARDEN ) &&
					global.contains( Badge.BOSS_SLAIN_3_CHASER )) {
					
					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!global.contains( badge )) {
						displayBadge( badge );
						global.add( badge );
						saveNeeded = true;
					}
				}
			}
		}
	}
	
	public static void validateMastery() {
		
		Badge badge = null;
		switch (Dungeon.hero.heroClass) {
		case WARRIOR:
			badge = Badge.MASTERY_WARRIOR;
			break;
		case MAGE:
			badge = Badge.MASTERY_MAGE;
			break;
		case ROGUE:
			badge = Badge.MASTERY_ROGUE;
			break;
		case HUNTRESS:
			badge = Badge.MASTERY_HUNTRESS;
			break;
		}
		
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}
	}
	
	public static void validateMasteryCombo( int n ) {
		if (!local.contains( Badge.MASTERY_COMBO ) && n == 7) {
			Badge badge = Badge.MASTERY_COMBO;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateRingOfHaggler() {
		if (!local.contains( Badge.RING_OF_HAGGLER ) && new RingOfHaggler().isKnown()) {
			Badge badge = Badge.RING_OF_HAGGLER;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateRingOfThorns() {
		if (!local.contains( Badge.RING_OF_THORNS ) && new RingOfThorns().isKnown()) {
			Badge badge = Badge.RING_OF_THORNS;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateRare( Mob mob ) {
		
		Badge badge = null;
		if (mob instanceof Albino) {
			badge = Badge.RARE_ALBINO;
		} else if (mob instanceof Bandit) {
			badge = Badge.RARE_BANDIT;
		} else if (mob instanceof Shielded) {
			badge = Badge.RARE_SHIELDED;
		} else if (mob instanceof Senior) {
			badge = Badge.RARE_SENIOR;
		} else if (mob instanceof Acidic) {
			badge = Badge.RARE_ACIDIC;
		}
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}
		
		if (global.contains( Badge.RARE_ALBINO ) &&
			global.contains( Badge.RARE_BANDIT ) &&
			global.contains( Badge.RARE_SHIELDED ) &&
			global.contains( Badge.RARE_SENIOR ) &&
			global.contains( Badge.RARE_ACIDIC )) {
			
			badge = Badge.RARE;
			displayBadge( badge );
		}
	}
	
	public static void validateVictory() {

		Badge badge = Badge.VICTORY;
		displayBadge( badge );

		switch (Dungeon.hero.heroClass) {
		case WARRIOR:
			badge = Badge.VICTORY_WARRIOR;
			break;
		case MAGE:
			badge = Badge.VICTORY_MAGE;
			break;
		case ROGUE:
			badge = Badge.VICTORY_ROGUE;
			break;
		case HUNTRESS:
			badge = Badge.VICTORY_HUNTRESS;
			break;
		}
		local.add( badge );
		if (!global.contains( badge )) {
			global.add( badge );
			saveNeeded = true;
		}
		
		if (global.contains( Badge.VICTORY_WARRIOR ) &&
			global.contains( Badge.VICTORY_MAGE ) &&
			global.contains( Badge.VICTORY_ROGUE ) &&
			global.contains( Badge.VICTORY_HUNTRESS )) {
			
			badge = Badge.VICTORY_ALL_CLASSES;
			displayBadge( badge );
		}
	}
	
	public static void validateNoKilling() {
		if (!local.contains( Badge.NO_MONSTERS_SLAIN ) && Statistics.completedWithNoKilling) {
			Badge badge = Badge.NO_MONSTERS_SLAIN;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateGrimWeapon() {
		if (!local.contains( Badge.GRIM_WEAPON )) {
			Badge badge = Badge.GRIM_WEAPON;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateNightHunter() {
		if (!local.contains( Badge.NIGHT_HUNTER ) && Statistics.nightHunt >= 15) {
			Badge badge = Badge.NIGHT_HUNTER;
			local.add( badge );
			displayBadge( badge );
		}
	}
	
	public static void validateSupporter() {

		global.add( Badge.SUPPORTER );
		saveNeeded = true;
		
		PixelScene.showBadge( Badge.SUPPORTER );
	}
	
	public static void validateGamesPlayed() {
		Badge badge = null;
		if (Rankings.INSTANCE.totalNumber >= 10) {
			badge = Badge.GAMES_PLAYED_1;
		}
		if (Rankings.INSTANCE.totalNumber >= 100) {
			badge = Badge.GAMES_PLAYED_2;
		}
		if (Rankings.INSTANCE.totalNumber >= 500) {
			badge = Badge.GAMES_PLAYED_3;
		}
		if (Rankings.INSTANCE.totalNumber >= 2000) {
			badge = Badge.GAMES_PLAYED_4;
		}
		
		displayBadge( badge );
	}
	
	public static void validateHappyEnd() {
		displayBadge( Badge.HAPPY_END );
	}
	
	public static void validateChampion() {
		displayBadge( Badge.CHAMPION );
	}
	
	private static void displayBadge( Badge badge ) {
		
		if (badge == null) {
			return;
		}
		
		if (global.contains( badge )) {
			
			if (!badge.meta) {
				GLog.h( "Badge endorsed: %s", badge.description );
			}
			
		} else {
			
			global.add( badge );
			saveNeeded = true;
			
			if (badge.meta) {
				GLog.h( "New super badge: %s", badge.description );
			} else {
				GLog.h( "New badge: %s", badge.description );
			}	
			PixelScene.showBadge( badge );
		}
	}
	
	public static boolean isUnlocked( Badge badge ) {
		return global.contains( badge );
	}
	
	public static void disown( Badge badge ) {
		loadGlobal();
		global.remove( badge );
		saveNeeded = true;
	}
	
	public static List<Badge> filtered( boolean global ) {
		
		HashSet<Badge> filtered = new HashSet<Badge>( global ? Badges.global : Badges.local );
		
		{
			Iterator<Badge> iterator = filtered.iterator();
			while (iterator.hasNext()) {
				Badge badge = iterator.next();
				if ((!global && badge.meta) || badge.image == -1) {
					iterator.remove();
				}
			}
		}
		
		leaveBest( filtered, Badge.MONSTERS_SLAIN_1, Badge.MONSTERS_SLAIN_2, Badge.MONSTERS_SLAIN_3, Badge.MONSTERS_SLAIN_4 );
		leaveBest( filtered, Badge.GOLD_COLLECTED_1, Badge.GOLD_COLLECTED_2, Badge.GOLD_COLLECTED_3, Badge.GOLD_COLLECTED_4 );
		leaveBest( filtered, Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2, Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4 );
		leaveBest( filtered, Badge.LEVEL_REACHED_1, Badge.LEVEL_REACHED_2, Badge.LEVEL_REACHED_3, Badge.LEVEL_REACHED_4 );
		leaveBest( filtered, Badge.STRENGTH_ATTAINED_1, Badge.STRENGTH_ATTAINED_2, Badge.STRENGTH_ATTAINED_3, Badge.STRENGTH_ATTAINED_4 );
		leaveBest( filtered, Badge.FOOD_EATEN_1, Badge.FOOD_EATEN_2, Badge.FOOD_EATEN_3, Badge.FOOD_EATEN_4 );
		leaveBest( filtered, Badge.ITEM_LEVEL_1, Badge.ITEM_LEVEL_2, Badge.ITEM_LEVEL_3, Badge.ITEM_LEVEL_4 );
		leaveBest( filtered, Badge.POTIONS_COOKED_1, Badge.POTIONS_COOKED_2, Badge.POTIONS_COOKED_3, Badge.POTIONS_COOKED_4 );
		leaveBest( filtered, Badge.BOSS_SLAIN_1_ALL_CLASSES, Badge.BOSS_SLAIN_3_ALL_SUBCLASSES );
		leaveBest( filtered, Badge.DEATH_FROM_FIRE, Badge.YASD );
		leaveBest( filtered, Badge.DEATH_FROM_GAS, Badge.YASD );
		leaveBest( filtered, Badge.DEATH_FROM_HUNGER, Badge.YASD );
		leaveBest( filtered, Badge.DEATH_FROM_POISON, Badge.YASD );
		leaveBest( filtered, Badge.ALL_POTIONS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED );
		leaveBest( filtered, Badge.ALL_SCROLLS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED );
		leaveBest( filtered, Badge.ALL_RINGS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED );
		leaveBest( filtered, Badge.ALL_WANDS_IDENTIFIED, Badge.ALL_ITEMS_IDENTIFIED );
		leaveBest( filtered, Badge.VICTORY, Badge.VICTORY_ALL_CLASSES );
		leaveBest( filtered, Badge.VICTORY, Badge.HAPPY_END );
 		leaveBest( filtered, Badge.VICTORY, Badge.CHAMPION );
		leaveBest( filtered, Badge.GAMES_PLAYED_1, Badge.GAMES_PLAYED_2, Badge.GAMES_PLAYED_3, Badge.GAMES_PLAYED_4 );
		
		ArrayList<Badge> list = new ArrayList<Badge>( filtered );
		Collections.sort( list );
		
		return list;
	}
	
	private static void leaveBest( HashSet<Badge> list, Badge...badges ) {
		for (int i=badges.length-1; i > 0; i--) {
			if (list.contains( badges[i])) {
				for (int j=0; j < i; j++) {
					list.remove( badges[j] );
				}
				break;
			}
		}
	}
}
