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
package com.retrodevxp.pixeldungeon.actors.mobs;

import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.sprites.AcidicSprite;
import com.retrodevxp.utils.Random;

public class Acidic extends Scorpio {

	{
		name = "acidic scorpio";
		spriteClass = AcidicSprite.class;
		
		EXP = 17;
		maxLvl = 25;
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {
		
		int dmg = Random.IntRange( 0, damage );
		if (dmg > 0) {
			enemy.damage( dmg, this );
		}
		
		return super.defenseProc( enemy, damage );
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Badges.validateRare( this );
	}

	@Override
	public String description() {
		return
			"A rare kind of Scorpio. It often sprays powerful corrosive acid when attacked, " +
			"damaging its enemy in return.";
	}
}
