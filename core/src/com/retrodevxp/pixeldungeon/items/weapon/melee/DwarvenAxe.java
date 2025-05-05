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

public class DwarvenAxe extends MeleeWeapon {

	{
		name = "dwarven axe";
		image = ItemSpriteSheet.DWARVEN_AXE;
	}
	
	public DwarvenAxe() {
		super( 4, 1.2f, 1f, -1, -1 );
	}

	@Override
	protected int rangemin(){
		return 5;
	}
	
	@Override
	protected int rangemax(){
		return 11;
	}

	@Override
	protected int rangestr(){
		return 17;
	}
	
	@Override
	public String desc() {
		return "A weapon forged by the dwarves. Even though the steel head isn't as large as a typical axe head," +
		"it is made from a material heavier than most other metal. It is attached to a thick, sturdy pole.";
	}
}
