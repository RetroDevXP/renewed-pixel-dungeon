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
import com.retrodevxp.pixeldungeon.items.keys.GoldenKey;
import com.retrodevxp.pixeldungeon.items.keys.IronKey;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Room;
import com.retrodevxp.pixeldungeon.levels.Terrain;
import com.retrodevxp.utils.Random;

public class VaultPainter extends Painter {

	@Override
	public void paint( Level level, Room room ) {

		fill( level, room, Terrain.WALL );
		fill( level, room, 1, Terrain.EMPTY_SP );
		fill( level, room, 2, Terrain.EMPTY );
		
		int cx = (room.left + room.right) / 2;
		int cy = (room.top + room.bottom) / 2;
		int c = cx + cy * Level.WIDTH;
		
		switch (Random.Int( 3 )) {
		
		case 0:
			level.drop( prize( level ), c ).type = Type.LOCKED_CHEST;
			level.addItemToSpawn( new GoldenKey() );
			break;
			
		case 1:
			Item i1, i2;
			do {
				i1 = prize( level );
				i2 = prize( level );
			} while (i1.getClass() == i2.getClass());
			level.drop( i1, c ).type = Type.CRYSTAL_CHEST;
			level.drop( i2, c + Level.NEIGHBOURS8[Random.Int( 8 )]).type = Type.CRYSTAL_CHEST;
			level.addItemToSpawn( new GoldenKey() );
			break;
			
		case 2:
			level.drop( prize( level ), c );
			set( level, c, Terrain.PEDESTAL );
			break;
		}
		
		room.entrance().set( Room.Door.Type.LOCKED );
		level.addItemToSpawn( new IronKey() );
	}
	
	private static Item prize( Level level ) {
		return Generator.random( Random.oneOf(  
			Generator.Category.WAND, 
			Generator.Category.RING 
		) );
	}
}
