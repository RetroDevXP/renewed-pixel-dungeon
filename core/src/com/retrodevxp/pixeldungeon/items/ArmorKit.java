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
package com.retrodevxp.pixeldungeon.items;

import java.util.ArrayList;

import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.armor.Armor;
import com.retrodevxp.pixeldungeon.items.armor.ClassArmor;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.HeroSprite;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.windows.WndBag;

public class ArmorKit extends Item {
	
	private static final String TXT_SELECT_ARMOR	= "Select an armor to upgrade";
	private static final String TXT_UPGRADED		= "you applied the armor kit to upgrade your %s";
	
	private static final float TIME_TO_UPGRADE = 2;
	
	private static final String AC_APPLY = "APPLY";
	
	{
		name = "armor kit";
		image = ItemSpriteSheet.KIT;
		
		unique = true;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		actions.add( AC_APPLY );
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action == AC_APPLY) {

			curUser = hero;
			GameScene.selectItem( itemSelector, WndBag.Mode.ARMOR, TXT_SELECT_ARMOR );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	private void upgrade( Armor armor ) {
		
		detach( curUser.belongings.backpack );
		
		curUser.sprite.centerEmitter().start( Speck.factory( Speck.KIT ), 0.05f, 10 );
		curUser.spend( TIME_TO_UPGRADE );
		curUser.busy();
		
		GLog.w( TXT_UPGRADED, armor.name() );
		
		ClassArmor classArmor = ClassArmor.upgrade( curUser, armor );
		if (curUser.belongings.armor == armor) {
			
			curUser.belongings.armor = classArmor;
			((HeroSprite)curUser.sprite).updateArmor();
			
		} else {
			
			armor.detach( curUser.belongings.backpack );
			classArmor.collect( curUser.belongings.backpack );
			
		}
		
		curUser.sprite.operate( curUser.pos );
		Sample.INSTANCE.play( Assets.SND_EVOKE );
	}
	
	@Override
	public String info() {
		return
			"Using this kit of small tools and materials anybody can transform any armor into an \"epic armor\", " +
			"which will keep all properties of the original armor, but will also provide its wearer a special ability " +
			"depending on his class. No skills in tailoring, leatherworking or blacksmithing are required.";
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				ArmorKit.this.upgrade( (Armor)item );
			}
		}
	};
}
