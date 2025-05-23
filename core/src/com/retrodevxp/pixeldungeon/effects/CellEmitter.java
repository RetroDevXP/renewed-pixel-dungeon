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
package com.retrodevxp.pixeldungeon.effects;

import com.retrodevxp.noosa.particles.Emitter;
import com.retrodevxp.pixeldungeon.DungeonTilemap;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.utils.PointF;

public class CellEmitter {

	public static Emitter get( int cell ) {
		
		PointF p = DungeonTilemap.tileToWorld( cell );
		
		Emitter emitter = GameScene.emitter();
		emitter.pos( p.x, p.y, DungeonTilemap.SIZE, DungeonTilemap.SIZE );
		
		return emitter;
	}
	
	public static Emitter center( int cell ) {
		
		PointF p = DungeonTilemap.tileToWorld( cell );
		
		Emitter emitter = GameScene.emitter();
		emitter.pos( p.x + DungeonTilemap.SIZE / 2, p.y + DungeonTilemap.SIZE / 2 );
		
		return emitter;
	}
	
	public static Emitter bottom( int cell ) {
		
		PointF p = DungeonTilemap.tileToWorld( cell );
		
		Emitter emitter = GameScene.emitter();
		emitter.pos( p.x, p.y + DungeonTilemap.SIZE, DungeonTilemap.SIZE, 0 );
		
		return emitter;
	}
}
