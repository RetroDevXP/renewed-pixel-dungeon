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

public class Slow extends Weapon.Enchantment {

	private static final String TXT_CHILLING = "chilling %s";
	
	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x0044FF );
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		int level = Math.max( 0, weapon.effectiveLevel() );
		
		if (Random.Int( level + 4 ) >= 3) {
			
			Buff.prolong( defender, com.retrodevxp.pixeldungeon.actors.buffs.Slow.class, 
				Random.Float( 1, 1.5f + level ) );
			
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Glowing glowing() {
		return BLUE;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_CHILLING, weaponName );
	}

	@Override
	public String description(){
		return "Cold to the touch, this weapon limits the speed of its targets.";
	}

}
