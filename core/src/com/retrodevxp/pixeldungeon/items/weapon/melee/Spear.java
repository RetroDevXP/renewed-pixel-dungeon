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

import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;

public class Spear extends MeleeWeapon {

	{
		name = "spear";
		image = ItemSpriteSheet.SPEAR;
	}
	
	public Spear() {
		super( 2, 1f, 1.5f, 0, 0  );
	}

	@Override
	protected int rangemin(){
		return 5;
	}
	
	@Override
	protected int rangemax(){
		return 9;
	}

	@Override
	protected int rangestr(){
		return 11;
	}
	
	@Override
	public String desc() {
		return "A slender wooden rod tipped with sharpened iron.";
	}
}
