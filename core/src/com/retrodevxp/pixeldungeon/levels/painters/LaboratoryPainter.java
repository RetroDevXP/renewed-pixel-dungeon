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
package com.retrodevxp.pixeldungeon.levels.painters;

import com.retrodevxp.pixeldungeon.actors.blobs.Alchemy;
import com.retrodevxp.pixeldungeon.items.Generator;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.keys.IronKey;
import com.retrodevxp.pixeldungeon.items.potions.Potion;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Room;
import com.retrodevxp.pixeldungeon.levels.Terrain;
import com.retrodevxp.utils.Point;
import com.retrodevxp.utils.Random;

public class LaboratoryPainter extends Painter {

	@Override
	public void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY_SP );
		
		Room.Door entrance = room.entrance();
		
		Point pot = null;
		if (entrance.x == room.left) {
			pot = new Point( room.right-1, Random.Int( 2 ) == 0 ? room.top + 1 : room.bottom - 1 );
		} else if (entrance.x == room.right) {
			pot = new Point( room.left+1, Random.Int( 2 ) == 0 ? room.top + 1 : room.bottom - 1 );
		} else if (entrance.y == room.top) {
			pot = new Point( Random.Int( 2 ) == 0 ? room.left + 1 : room.right - 1, room.bottom-1 );
		} else if (entrance.y == room.bottom) {
			pot = new Point( Random.Int( 2 ) == 0 ? room.left + 1 : room.right - 1, room.top+1 );
		}
		set( level, pot, Terrain.ALCHEMY );
		
		Alchemy alchemy = new Alchemy();
		alchemy.seed( pot.x + Level.WIDTH * pot.y, 1 );
		level.blobs.put( Alchemy.class, alchemy );
		
		int n = Random.IntRange( 2, 3 );
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = room.random();
			} while (
				level.map[pos] != Terrain.EMPTY_SP || 
				level.heaps.get( pos ) != null);
			level.drop( prize( level ), pos );
		}
		
		entrance.set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
	}
	
	private static Item prize( Level level ) {
		
		Item prize = level.itemToSpanAsPrize();
		if (prize instanceof Potion) {
			return prize;
		} else if (prize != null) {
			level.addItemToSpawn( prize );
		}
		
		return Generator.random( Generator.Category.POTION );
	}
}
