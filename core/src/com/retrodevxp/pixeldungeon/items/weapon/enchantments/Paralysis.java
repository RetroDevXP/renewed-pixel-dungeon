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
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.items.weapon.Weapon;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite.Glowing;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Random;

public class Paralysis extends Weapon.Enchantment {

	private static final String TXT_STUNNING = "stunning %s";
	
	private static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing( 0xCCAA44 );
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// lvl 0 - 13%
		// lvl 1 - 22%
		// lvl 2 - 30%
		int level = Math.max( 0, weapon.effectiveLevel() );
		
		if (Random.Int( level + 8 ) >= 7) {
			
			Buff.prolong( defender, com.retrodevxp.pixeldungeon.actors.buffs.Paralysis.class, 
				Random.Float( 1, 1.5f + level ) );
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Glowing glowing() {
		return YELLOW;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_STUNNING, weaponName );
	}

	@Override
	public String description(){
		return "Electrical powers stored within this weapon sometimes causes paralysis to its target.";
	}

}
