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

import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.pixeldungeon.windows.WndBag;

public class BrandistockRetract extends MeleeWeapon {

	public static final String AC_RETRACT	= "Extend";
	{
		name = "brandistock";
		image = ItemSpriteSheet.RETRACTED;
		defaultAction = AC_RETRACT;
	}
	
	public BrandistockRetract() {
		super( 2, 1.1f, 1.1f, -1, -1  );
	}
	
	@Override
	public ArrayList<String> actions( Hero hero ) {
		ArrayList<String> actions = super.actions( hero );
		if (level() > -1) {
			actions.add( AC_RETRACT );
		}
		return actions;
	}
	
	@Override
	protected int rangemin(){
		return 3;
	}
	
	@Override
	protected int rangemax(){
		return 7;
	}

	@Override
	protected int rangestr(){
		return 12;
	}
	
	@Override
	public String desc() {
		return "A slender wooden rod with sharp, retractable metallic spikes.";
	}

	@Override
	public void execute( Hero hero, String action ) {
		if (action.equals( AC_RETRACT )) {
			int weaponlevel = level();
			Brandistock extracted = new Brandistock();
			if (this.enchantment != null){
				extracted.enchant(this.enchantment);
			}
			extracted.identify();
			if (weaponlevel > 0){
				for (int i = 0; i < weaponlevel; i++){
					extracted.upgrade();
				}
			}
			if (hero.belongings.weapon == this) {
				hero.belongings.weapon = extracted;
				updateQuickslot();
			} else {
				detach( hero.belongings.backpack );
				extracted.collect();
			}
			
			
			curUser = hero;
			
		} else {
		
			super.execute( hero, action );
			
		}
	}
}
