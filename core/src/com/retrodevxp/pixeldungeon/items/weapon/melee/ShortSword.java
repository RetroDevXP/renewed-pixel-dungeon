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

import java.util.ArrayList;

import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.Boomerang;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.windows.WndBag;

public class ShortSword extends MeleeWeapon {
	
	public static final String AC_REFORGE	= "REFORGE";
	
	private static final String TXT_SELECT_WEAPON	= "Select a weapon to upgrade";
	
	private static final String TXT_REFORGED = 
		"you reforged the short sword to upgrade your %s";
	private static final String TXT_NOT_BOOMERANG = 
		"you can't upgrade a boomerang this way";
	
	private static final float TIME_TO_REFORGE	= 2f;
	
	private boolean  equipped;
	
	{
		name = "short sword";
		image = ItemSpriteSheet.SHORT_SWORD;
	}
	
	public ShortSword() {
		super( 1, 1f, 1f, 0, 0  );
		
		STR = 11;
	}

	@Override
	protected int rangemin(){
		return 2;
	}
	
	@Override
	protected int rangemax(){
		return 7;
	}

	@Override
	protected int rangestr(){
		return 11;
	}
	
	@Override
	protected int max0() {
		return 12;
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (level() > 0) {
			actions.add( AC_REFORGE );
		}
		return actions;
	}
	
	@Override
	public void execute( Hero hero, String action ) {
		if (action == AC_REFORGE) {
			
			if (hero.belongings.weapon == this) {
				equipped = true;
				hero.belongings.weapon = null;
			} else {
				equipped = false;
				detach( hero.belongings.backpack );
			}
			
			curUser = hero;
			
			GameScene.selectItem( itemSelector, WndBag.Mode.WEAPON, TXT_SELECT_WEAPON );
			
		} else {
			
			super.execute( hero, action );
			
		}
	}
	
	@Override
	public String desc() {
		return 
			"It is indeed quite short, just a few inches longer than a dagger.";
	}
	
	private final WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null && !(item instanceof Boomerang)) {
				
				Sample.INSTANCE.play( Assets.SND_EVOKE );
				ScrollOfUpgrade.upgrade( curUser );
				evoke( curUser );
				
				GLog.w( TXT_REFORGED, item.name() );
				
				((MeleeWeapon)item).safeUpgrade();
				curUser.spendAndNext( TIME_TO_REFORGE );
				
				Badges.validateItemLevelAquired( item );
				
			} else {
				
				if (item instanceof Boomerang) {
					GLog.w( TXT_NOT_BOOMERANG );
				}
				
				if (equipped) {
					curUser.belongings.weapon = ShortSword.this;
				} else {
					collect( curUser.belongings.backpack );
				}
			}
		}
	};
}
