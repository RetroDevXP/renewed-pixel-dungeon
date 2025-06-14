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
package com.retrodevxp.pixeldungeon.items.weapon.enchantments;

import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.items.weapon.Weapon;
import com.retrodevxp.pixeldungeon.utils.Utils;

public class Instability extends Weapon.Enchantment {

	private static final String TXT_UNSTABLE	= "unstable %s";
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		return random().proc( weapon, attacker, defender, damage );
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_UNSTABLE, weaponName );
	}

	@Override
	public String description(){
		return "The unstable magic bound to this weapon is mostly unknown.";
	}

}
