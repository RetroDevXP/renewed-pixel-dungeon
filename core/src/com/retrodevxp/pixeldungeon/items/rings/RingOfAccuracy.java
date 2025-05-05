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
package com.retrodevxp.pixeldungeon.items.rings;

public class RingOfAccuracy extends Ring {

	{
		name = "Ring of Accuracy";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Accuracy();
	}
	
	@Override
	public String desc() {
		return isKnown() ?
			"This ring enhances your accuracy, increasing your chance to hit the enemy. Degraded rings of accuracy instead make you less precise." :
			super.desc();
	}
	
	public class Accuracy extends RingBuff {
	}
}
