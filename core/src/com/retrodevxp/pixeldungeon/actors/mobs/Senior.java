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
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Paralysis;
import com.retrodevxp.pixeldungeon.sprites.SeniorSprite;
import com.retrodevxp.utils.Random;

public class Senior extends Monk {

	{
		name = "dwarven brawler";
		spriteClass = SeniorSprite.class;

		EXP = 15;
		maxLvl = 21;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 12, 20 );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (Random.Int( 10 ) == 0) {
			Buff.prolong( enemy, Paralysis.class, 1.1f );
		}
		return super.attackProc( enemy, damage );
	}
	
	@Override
	public void die( Object cause ) {
		super.die( cause );
		Badges.validateRare( this );
	}
}
