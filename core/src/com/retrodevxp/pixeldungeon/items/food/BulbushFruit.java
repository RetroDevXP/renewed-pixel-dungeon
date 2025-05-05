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
package com.retrodevxp.pixeldungeon.items.food;

import com.retrodevxp.pixeldungeon.actors.buffs.Hunger;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;

public class BulbushFruit extends Food {


	{
		name = "bulbush fruit";
		image = ItemSpriteSheet.BULBUSHFRUIT;
		energy = Hunger.HUNGRY - 100;
		message = "That fruit tasted horrible!";
	}
	
	// @Override
	// public ArrayList<String> actions( Hero hero ) {
	// 	ArrayList<String> actions = super.actions( hero );
	// 	actions.add( AC_EAT );
	// 	return actions;
	// }
	

	
	@Override
	public String info() {
		return 
			"The large fruit of the Bulbush. " +
			"Despite its unpleasantness, it's nutritious.";
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 3 * quantity;
	}
}
