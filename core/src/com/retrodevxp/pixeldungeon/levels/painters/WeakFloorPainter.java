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

import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Room;
import com.retrodevxp.pixeldungeon.levels.Terrain;
import com.retrodevxp.utils.Point;
import com.retrodevxp.utils.Random;

public class WeakFloorPainter extends Painter {

	@Override
	public void paint( Level level, Room room ) {
		
		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.CHASM );
		
		Room.Door door = room.entrance(); 
		door.set( Room.Door.Type.REGULAR );
		
		if (door.x == room.left) {
			for (int i=room.top + 1; i < room.bottom; i++) {
				drawInside( level, room, new Point( room.left, i ), Random.IntRange( 1, room.width() - 2 ), Terrain.EMPTY_SP );
			}
		} else if (door.x == room.right) {
			for (int i=room.top + 1; i < room.bottom; i++) {
				drawInside( level, room, new Point( room.right, i ), Random.IntRange( 1, room.width() - 2 ), Terrain.EMPTY_SP );
			}
		} else if (door.y == room.top) {
			for (int i=room.left + 1; i < room.right; i++) {
				drawInside( level, room, new Point( i, room.top ), Random.IntRange( 1, room.height() - 2 ), Terrain.EMPTY_SP );
			}
		} else if (door.y == room.bottom) {
			for (int i=room.left + 1; i < room.right; i++) {
				drawInside( level, room, new Point( i, room.bottom ), Random.IntRange( 1, room.height() - 2 ), Terrain.EMPTY_SP );
			}
		}
	}
}
