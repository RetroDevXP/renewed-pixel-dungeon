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

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.effects.particles.ShadowParticle;
import com.retrodevxp.pixeldungeon.items.weapon.Weapon;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite.Glowing;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Random;

public class Initiliazing extends Weapon.Enchantment {

	private static final String TXT_PIONEERING	= "initializing %s";
	
	private static ItemSprite.Glowing TURQUOISE = new ItemSprite.Glowing( 0x6666CC );
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		// weapon.polish();
		if (defender.HP == defender.HT){
			int level = Math.max( 0, weapon.effectiveLevel() );
			defender.damage( Random.Int( (level * 2) + 1 + (int)(Dungeon.depth / 2) ) , this );
			defender.sprite.emitter().burst( ShadowParticle.UP, 5 );
			return true;
		}
		else{
			return false;
		}
	}
	
	@Override
	public Glowing glowing() {
		return TURQUOISE;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_PIONEERING, weaponName );
	}

	@Override
	public String description(){
		return "The magic engulfing this weapon grants a powerful opening strike, dealing increased damage to healthy enemies.";
	}

}
