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
package com.retrodevxp.pixeldungeon.items.armor;

import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;


public class PlateArmor extends Armor {

	{
		name = "plate armor";
		image = ItemSpriteSheet.ARMOR_PLATE;
	}
	
	public PlateArmor() {
		super( 5, 0, 0 );
	}
	
	@Override
	public String desc() {
		return 
			"Enormous plates of metal are joined together into a suit that provides " +
			"unmatched protection to any adventurer strong enough to bear its staggering weight.";
	}
}
