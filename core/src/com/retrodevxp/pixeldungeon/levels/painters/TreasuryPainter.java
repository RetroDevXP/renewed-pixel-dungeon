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

import com.retrodevxp.pixeldungeon.items.Gold;
import com.retrodevxp.pixeldungeon.items.Heap;
import com.retrodevxp.pixeldungeon.items.keys.IronKey;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Room;
import com.retrodevxp.pixeldungeon.levels.Terrain;
import com.retrodevxp.utils.Random;

public class TreasuryPainter extends Painter {

	@Override
	public void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );
		
		set( level, room.center(), Terrain.STATUE );
		
		Heap.Type heapType = Random.Int( 2 ) == 0 ? Heap.Type.CHEST : Heap.Type.HEAP;
		
		int n = Random.IntRange( 2, 3 );
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = room.random();
			} while (level.map[pos] != Terrain.EMPTY || level.heaps.get( pos ) != null);
			level.drop( new Gold().random(), pos ).type = (i == 0 && heapType == Heap.Type.CHEST ? Heap.Type.MIMIC : heapType);
		}
		
		if (heapType == Heap.Type.HEAP) {
			for (int i=0; i < 6; i++) {
				int pos;
				do {
					pos = room.random();
				} while (level.map[pos] != Terrain.EMPTY);
				level.drop( new Gold( Random.IntRange( 1, 3 ) ), pos );
			}
		}
		
		room.entrance().set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
	}
}
