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

public class RingOfHaste extends Ring {

	{
		name = "Ring of Haste";
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Haste();
	}
	
	@Override
	public String desc() {
		return isKnown() ?
			"This ring accelerates the wearer's flow of time, allowing one to perform all actions a little faster. A degraded ring of haste instead slows the wearer down." :
			super.desc();
	}
	
	public class Haste extends RingBuff {
	}
}
