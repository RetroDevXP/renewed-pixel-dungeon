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

import com.retrodevxp.pixeldungeon.items.Generator;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.Heap.Type;
import com.retrodevxp.pixeldungeon.items.keys.IronKey;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Room;
import com.retrodevxp.pixeldungeon.levels.Terrain;
import com.retrodevxp.utils.Point;

public class CryptPainter extends Painter {

	@Override
	public void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY );

		Point c = room.center();
		int cx = c.x;
		int cy = c.y;
		
		Room.Door entrance = room.entrance();
		
		entrance.set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
		
		if (entrance.x == room.left) {
			set( level, new Point( room.right-1, room.top+1 ), Terrain.STATUE );
			set( level, new Point( room.right-1, room.bottom-1 ), Terrain.STATUE );
			cx = room.right - 2;
		} else if (entrance.x == room.right) {
			set( level, new Point( room.left+1, room.top+1 ), Terrain.STATUE );
			set( level, new Point( room.left+1, room.bottom-1 ), Terrain.STATUE );
			cx = room.left + 2;
		} else if (entrance.y == room.top) {
			set( level, new Point( room.left+1, room.bottom-1 ), Terrain.STATUE );
			set( level, new Point( room.right-1, room.bottom-1 ), Terrain.STATUE );
			cy = room.bottom - 2;
		} else if (entrance.y == room.bottom) {
			set( level, new Point( room.left+1, room.top+1 ), Terrain.STATUE );
			set( level, new Point( room.right-1, room.top+1 ), Terrain.STATUE );
			cy = room.top + 2;
		}
		
		level.drop( prize( level ), cx + cy * Level.WIDTH ).type = Type.TOMB;
	}
	
	private static Item prize( Level level ) {
		
		Item prize = Generator.random( Generator.Category.ARMOR );

		for (int i=0; i < 3; i++) {
			Item another = Generator.random( Generator.Category.ARMOR );
			if (another.level() > prize.level()) {
				prize = another;
			}
		}
		
		return prize;
	}
}
