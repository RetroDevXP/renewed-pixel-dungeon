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
package com.retrodevxp.pixeldungeon.actors.hero;

import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.items.ArmorKit;
import com.retrodevxp.pixeldungeon.items.FireBomb;
import com.retrodevxp.pixeldungeon.items.SmokeBomb;
import com.retrodevxp.pixeldungeon.items.TomeOfMastery;
import com.retrodevxp.pixeldungeon.items.armor.ClothArmor;
import com.retrodevxp.pixeldungeon.items.armor.PlateArmor;
import com.retrodevxp.pixeldungeon.items.bags.Keyring;
import com.retrodevxp.pixeldungeon.items.food.Food;
import com.retrodevxp.pixeldungeon.items.food.Pasty;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfHealing;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfMindVision;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfParalyticGas;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfStrength;
import com.retrodevxp.pixeldungeon.items.rings.RingOfShadows;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfEnchantment;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.retrodevxp.pixeldungeon.items.wands.WandOfAmok;
import com.retrodevxp.pixeldungeon.items.wands.WandOfAvalanche;
import com.retrodevxp.pixeldungeon.items.wands.WandOfBlindness;
import com.retrodevxp.pixeldungeon.items.wands.WandOfBlink;
import com.retrodevxp.pixeldungeon.items.wands.WandOfDisintegration;
import com.retrodevxp.pixeldungeon.items.wands.WandOfFirebolt;
import com.retrodevxp.pixeldungeon.items.wands.WandOfMagicMissile;
import com.retrodevxp.pixeldungeon.items.wands.WandOfPoison;
import com.retrodevxp.pixeldungeon.items.wands.WandOfReach;
import com.retrodevxp.pixeldungeon.items.wands.WandOfRegrowth;
import com.retrodevxp.pixeldungeon.items.wands.WandOfSpirits;
import com.retrodevxp.pixeldungeon.items.wands.WandOfTeleportation;
import com.retrodevxp.pixeldungeon.items.weapon.melee.*;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.Boomerang;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.Dart;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.SerratedSpike;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.WoodenDart;
import com.retrodevxp.pixeldungeon.plants.*;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.ui.QuickSlot;
import com.retrodevxp.pixeldungeon.windows.WndOptions;
import com.retrodevxp.utils.Bundle;

public enum HeroClass {

	WARRIOR( "warrior" ), MAGE( "mage" ), ROGUE( "rogue" ), HUNTRESS( "huntress" );
	
	private String title;
	
	private HeroClass( String title ) {
		this.title = title;
	}
	
	public static final String[] WAR_PERKS = {
		"Warriors start with 11 points of Strength.",
		"Warriors start with a unique short sword. This sword can be later \"reforged\" to upgrade another melee weapon.",
		"Warriors are less proficient with missile weapons. Even less with thrown melee weapons.",
		"Any piece of food restores some health when eaten.",
		"Potions of Strength are identified from the beginning.",
	};
	
	public static final String[] MAG_PERKS = {
		"Mages start with a unique Wand of Magic Missile. This wand can be later \"disenchanted\" to upgrade another wand.",
		"Mages recharge their wands faster.",
		"Mages are less likely to have enchantments removed when upgrading an enchanted item.",
		"Any piece of food restores 1 charge for all wands in the inventory when eaten",
		"Mages can use wands as a melee weapon.",
		"Scrolls of Identify are identified from the beginning."
	};
	
	public static final String[] ROG_PERKS = {
		"Rogues start with 15 points of Health.",
		"Rogues start with a Ring of Shadows+1.",
		"Rogues identify a type of a ring on equipping it.",
		"Rogues are proficient with light armor, dodging better while wearing one.",
		"Rogues are proficient in detecting hidden doors and traps.",
		"Rogues can go without food longer.",
		"Scrolls of Magic Mapping are identified from the beginning."
	};
	
	public static final String[] HUN_PERKS = {
		"Huntresses start with 15 points of Health.",
		"Huntresses start with a unique upgradeable boomerang.",
		"Huntresses are proficient with missile weapons and get a damage bonus for excessive strength when using them.",
		"Huntresses gain more health from dewdrops.",
		"Huntresses sense neighbouring monsters even if they are hidden behind obstacles.",
		"Potions of Paralysis are identified from the beginning."
	};
	
	public void initHero( Hero hero ) {
		
		hero.heroClass = this;
		
		initCommon( hero );
		
		switch (this) {
		case WARRIOR:
			initWarrior( hero );
			break;
			
		case MAGE:
			initMage( hero );
			break;
			
		case ROGUE:
			initRogue( hero );
			break;
			
		case HUNTRESS:
			initHuntress( hero );
			break;
		}

		
		if (Badges.isUnlocked( masteryBadge() )) {
			new TomeOfMastery().collect();
		}
		
		hero.updateAwareness();
	}

	//Cheat code to test some items during development.
	private static void initExtra(){
		new TomeOfMastery().collect();
		new WandOfAmok().identify().collect();
		// new WandOfDisintegration().identify().collect();
		// new WandOfFirebolt().identify().collect();
		// new ScrollOfPsionicBlast().identify().collect();
		new WandOfSpirits().identify().collect();
		// new WandOfReach().identify().collect();
		// new WandOfReach().identify().collect();
		// new WandOfReach().identify().collect();
		new WandOfTeleportation().identify().collect();
		// new WandOfBlink().identify().collect();
		new WandOfRegrowth().identify().collect();
		// new WandOfPoison().identify().collect();
		// new WandOfAvalanche().identify().collect();
		new PlateArmor().identify().collect();
		new ScrollOfMirrorImage().identify().collect();
		new Cutlass().identify().collect();
		new Sai().identify().collect();
		new Falx().identify().collect();
		// new Whip().identify().collect();
		// new Flail().identify().collect();
		// new Brandistock().identify().collect();

		for (int i = 1; i < 100; i++){
			new PotionOfHealing().identify().collect();
			new PotionOfMindVision().identify().collect();
			new PotionOfStrength().identify().collect();
			new ScrollOfEnchantment().identify().collect();
			new ScrollOfUpgrade().identify().collect();
			new Pasty().collect();
		}
		// QuickSlot.primaryValue = ArmorKit.class;
		// for (int i = 1; i < 100; i++){
		// 	new ScrollOfEnchantment().identify().collect();
		// }
	}
	
	private static void initCommon( Hero hero ) {
		(hero.belongings.armor = new ClothArmor()).identify();
		new Food().identify().collect();
		new Keyring().collect();

		//Enable this to start with a bunch of items. Used to test stuff during development.
		initExtra();
		
	}
	
	public Badges.Badge masteryBadge() {
		switch (this) {
		case WARRIOR:
			return Badges.Badge.MASTERY_WARRIOR;
		case MAGE:
			return Badges.Badge.MASTERY_MAGE;
		case ROGUE:
			return Badges.Badge.MASTERY_ROGUE;
		case HUNTRESS:
			return Badges.Badge.MASTERY_HUNTRESS;
		}
		return null;
	}
	
	private static void initWarrior( Hero hero ) {
		hero.STR = hero.STR + 1;
		
		(hero.belongings.weapon = new ShortSword()).identify();
		
		QuickSlot.secondaryValue = WoodenDart.class;
		QuickSlot.primaryValue = PotionOfStrength.class;
		
		new PotionOfStrength().setKnown();
	}
	
	private static void initMage( Hero hero ) {	
		(hero.belongings.weapon = new Knuckles()).identify();
		
		WandOfMagicMissile wand = new WandOfMagicMissile();
		wand.identify().collect();
		
		QuickSlot.secondaryValue = wand;
		QuickSlot.primaryValue = ScrollOfIdentify.class;

		
		new ScrollOfIdentify().setKnown();
	}
	
	private static void initRogue( Hero hero ) {
		(hero.belongings.weapon = new Dagger()).identify();
		(hero.belongings.ring1 = new RingOfShadows()).upgrade().identify();
		// new Dart( 3 ).identify().collect();
		hero.HP = (hero.HT -= 5);
		
		hero.belongings.ring1.activate( hero );


		
		QuickSlot.secondaryValue = Dart.class;
		QuickSlot.primaryValue = ScrollOfMagicMapping.class;

		
		new ScrollOfMagicMapping().setKnown();
	}
	
	private static void initHuntress( Hero hero ) {
		
		hero.HP = (hero.HT -= 5);
		
		(hero.belongings.weapon = new Dagger()).identify();
		Boomerang boomerang = new Boomerang();
		boomerang.identify().collect();
		
		// new Dart( 5 ).identify().collect();
		QuickSlot.secondaryValue = boomerang;
		QuickSlot.primaryValue = PotionOfParalyticGas.class;


		new PotionOfParalyticGas().setKnown();
	}
	
	public String title() {
		return title;
	}

	
	
	public String spritesheet() {
		
		switch (this) {
		case WARRIOR:
			return Assets.WARRIOR;
		case MAGE:
			return Assets.MAGE;
		case ROGUE:
			return Assets.ROGUE;
		case HUNTRESS:
			return Assets.HUNTRESS;
		}
		
		return null;
	}
	
	public String[] perks() {
		
		switch (this) {
		case WARRIOR:
			return WAR_PERKS;
		case MAGE:
			return MAG_PERKS;
		case ROGUE:
			return ROG_PERKS;
		case HUNTRESS:
			return HUN_PERKS;
		}
		
		return null;
	}

	private static final String CLASS	= "class";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( CLASS, toString() );
	}
	
	public static HeroClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( CLASS );
		return value.length() > 0 ? valueOf( value ) : ROGUE;
	}
}
