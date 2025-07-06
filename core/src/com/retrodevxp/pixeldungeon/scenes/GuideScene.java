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
package com.retrodevxp.pixeldungeon.scenes;

import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.BitmapTextMultiline;
import com.retrodevxp.noosa.Camera;
import com.retrodevxp.noosa.Image;
import com.retrodevxp.noosa.TouchArea;
import com.retrodevxp.pixeldungeon.PixelDungeon;
import com.retrodevxp.pixeldungeon.effects.Flare;
import com.retrodevxp.pixeldungeon.ui.Archs;
import com.retrodevxp.pixeldungeon.ui.ExitButton;
import com.retrodevxp.pixeldungeon.ui.Icons;
import com.retrodevxp.pixeldungeon.ui.Window;

public class GuideScene extends PixelScene {

	private static int pagetotal = 7;
	private static final String TXT_BASICS = 
		"Renewed Pixel Dungeon" + " Page 1 of " + (pagetotal + 1) + "\n\n" +
		"Basics\n\n" +
		"-A turn-based roguelike game. The goal of this game is to obtain the Amulet of Yendor on the deepest floor of the dungeon.\n" +
		"-As with other roguelikes, you start from the beginning when you lose. However, some benefits, such as the Tome of Mastery, are permanently unlocked.\n" +
		"-Each class has their own perks, as well as extra items they get to choose from.\n" +
		"-Once you unlock the Tome of Mastery, each class can be upgraded into subclasses, each with their own benefits.\n" +
		"-Each class has their own perks, as well as extra items they get to choose from.\n" +
		"-Defeat enemies to obtain EXP in order to level up, increasing your max HP and some other stats.\n";


		private static final String TXT_DUNGEON = 
		"Renewed Pixel Dungeon" + " Page 2 of " + (pagetotal + 1)  + "\n\n" +
		"The Dungeon\n\n" + 
		"-Each floor of the Dungeon might contain hidden traps or hidden doors. Some rooms are locked, requiring a key.\n" +
		"-There are 25 floors total. As you travel deeper, prepare to encounter stronger enemies.\n" +
		"-Every 5th floor, you will encounter a boss. They typically have some immunities.\n" +
		"-Each room has a way to be solved within the floor. For example, if you come across a chasm room, there is a potion of leviatation on that floor.\n" +
		"-There are extra tips on each level's entrance.\n";

		private static final String TXT_ITEMS = 
		"Renewed Pixel Dungeon" + " Page 3 of " + (pagetotal + 1)  + "\n\n" +
		"Items\n\n" + 
		"-Items are obtained in many different ways. There are items dropped on each floor. On top of that, some enemies might drop items.\n" +
		"-Some items aren't identified from the beginning.\n" +
		"-Some items require a certain amount of strength to be used properly. Try to only use items you can handle.\n" +
		"-Some items can be equipped. However, they might be cursed. In which case they can't be removed!\n" +
		"-Try to utilize the avaliable items to survive the situation. Take as long to consider as you need. It's turn-based.\n";

		private static final String TXT_WEAPONS = 
		"Renewed Pixel Dungeon" + " Page 4 of " + (pagetotal + 1)  + "\n\n" +
		"Weapons\n\n" + 
		"-You deal more damage with a weapon equipped.\n" +
		"-Each weapon have different required strengths. If you use weapons unsuited for your strength, its effectiveness worsens.\n" +
		"-Each weapon have different damage, speed, and accuracy.\n" +
		"-Enchanted weapons have extra effects. Some enchantments activates by chance. Some by conditions. Some always.\n" +
		"-When a weapon is upgraded, its required strength to equip also decreases. This does not affect the strength needed to throw it.\n";

		private static final String TXT_ARMOR = 
		"Renewed Pixel Dungeon" + " Page 5 of " + (pagetotal + 1)  + "\n\n" +
		"Armor\n\n" + 
		"-You receive less damage with an armor equipped.\n" +
		"-Each armor have different required strengths. If you use armor unsuited for your strength, its effectiveness worsens.\n" +
		"-Class armor are available deep into the dungeon. They grant an extra ability, at a cost upon usage.\n" +
		"-Enchanted armor have extra effects. Some enchantments activates by chance. Some by conditions. Some always.\n" +
		"-When an armor is upgraded, its required strength also decreases.\n";

		private static final String TXT_SPD = 
		"Renewed Pixel Dungeon" + " Page 6 of " + (pagetotal + 1)  + "\n\n" +
		"Speed\n\n" + 
		"-Not all enemies have the same move speed.\n" +
		"-Some enemies, such as Sewer Crabs, move much quicker than you. You can't outrun them!\n" +
		"-Some enemies have different attack speeds. Some might land multiple attacks for each one of yours!\n" +
		"-Some weapons also have different attack speeds when equipped.\n" +
		"-There are many different ways to manipulate the speed of you or your enemies.\n";

		private static final String TXT_HUNGER = 
		"Renewed Pixel Dungeon" + " Page 7 of " + (pagetotal + 1)  + "\n\n" +
		"Hunger\n\n" + 
		"-As you act, you get more hungry.\n" +
		"-When starving, your heath regeneration stops, you lose health periodically, and you deal reduced damage.\n" +
		"-When starving, some abilities such as Freerunner's speed boost also stop working. Others such as Chaser's speed boost worsens.\n" +
		"-There are food on each floor of the dungeon. Some enemies also drops food.\n" +		
		"-Some foods have extra effects, which are not always positive. Some foods are cookable with heat or cold.\n";

		private static final String TXT_CHALLENGES = 
		"Renewed Pixel Dungeon" + " Page 8 of " + (pagetotal + 1)  + "\n\n" +
		"Challenges\n\n" + 
		"-After winning at least once, you can activate challenges to make the game more difficult.\n" +
		"-Unnutritious: Food restores much less hunger.\n" +
		"-Faith is my armor: You won't obtain any armor from the dungeon.\n" +
		"-Pharmacophobia: Potions of healing won't drop.\n" +
		"-Barren land: You won't obtain seeds or dew from grass.\n" +
		"-Swarm intelligence: Once noticed by an enemy, others will swarm your location.\n" +
		"-Into darkness: A darker dungeon limits visibility.\n" +
		"-Forbidden runes: You won't obtain any scrolls, apart from Scrolls of Upgrade.\n" +
		"-Dire dungeon: Enemies deals slightly more damage.\n" +
		"-Undurable items: Upgraded items may break with lots of usage.\n" +
		"-To have an experience more similar to the vanilla version, try using Dire dungeon and Undurable items.\n";


	private BitmapTextMultiline text;
	
	private static final String NEXT = "Next ->";
	private static final String LAST = "<- Last";
	private int page = 0;
	
	
	@Override
	public void create() {
		super.create();
		page = 0;
		
		text = createMultiline( TXT_BASICS, 6 );
		text.maxWidth = Math.min( Camera.main.width, 150 );
		text.measure();
		add( text );
		
		text.x = align( (Camera.main.width - text.width()) / 2 );
		text.y = align( (Camera.main.height - text.height()) / 2 );
		
		BitmapTextMultiline next = createMultiline( NEXT, 8 );
		next.maxWidth = Math.min( Camera.main.width, 120 );
		next.measure();
		next.hardlight( Window.TITLE_COLOR );
		add( next );
		
		next.x = align(Camera.main.width / 2) + 5;
		next.y = text.y - next.height();
		
		TouchArea hotAreaNext = new TouchArea( next ) {
			@Override
			protected void onClick( NoosaInputProcessor.Touch touch ) {
				page++;
				if (page >= pagetotal){
					page = pagetotal;
				}
				switchPage(page);
			}
		};
		add( hotAreaNext );

		BitmapTextMultiline last = createMultiline( LAST, 8 );
		last.maxWidth = Math.min( Camera.main.width, 120 );
		last.measure();
		last.hardlight( Window.TITLE_COLOR );
		add( last );
		
		last.x = align(Camera.main.width / 2) - last.width() - 5;
		last.y = text.y - last.height();
		
		TouchArea hotAreaLast = new TouchArea( last ) {
			@Override
			protected void onClick( NoosaInputProcessor.Touch touch ) {
				page--;
				if (page <= 0){
					page = 0;
				}
				switchPage(page);
			}
		};
		add( hotAreaLast );
		
		Image wata = Icons.WATA.get();
		wata.x = align( (Camera.main.width - wata.width) / 2 );
		wata.y = text.y - wata.height - 8;
		add( wata );
		
		new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;
		
		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}

	protected void switchPage(int page){
		switch(page){
			case 0:
				text.text(TXT_BASICS);
				break;
			case 1:
				text.text(TXT_DUNGEON);
				break;
			case 2:
				text.text(TXT_ITEMS);
				break;
			case 3:
				text.text(TXT_WEAPONS);
				break;
			case 4:
				text.text(TXT_ARMOR);
				break;
			case 5:
				text.text(TXT_SPD);
				break;
			case 6:
				text.text(TXT_HUNGER);
				break;
			case 7:
				text.text(TXT_CHALLENGES);
				break;
			default:
				text.text(TXT_BASICS);
				break;

		}
		text.measure();
		remove(text);
		add(text);
	}
	
	@Override
	protected void onBackPressed() {
		PixelDungeon.switchNoFade( TitleScene.class );
	}
}
