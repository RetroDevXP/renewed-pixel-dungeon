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
package com.retrodevxp.pixeldungeon.items.quest;

import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;

public class DwarfToken extends Item {
	
	{
		name = "dwarf token";
		image = ItemSpriteSheet.TOKEN;
		
		stackable = true;
		unique = true;
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
	public String info() {
		return
			"Many dwarves and some of their larger creations carry these small pieces of metal of unknown purpose. " +
			"Maybe they are jewelry or maybe some kind of ID. Dwarves are strange folk.";
	}
	
	@Override
	public int price() {
		return quantity * 100;
	}
}
