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
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.DungeonTilemap;
import com.retrodevxp.pixeldungeon.actors.blobs.Blob;
import com.retrodevxp.utils.Random;

public class BlobEmitter extends Emitter {

	private static final int WIDTH	= Blob.WIDTH;
	private static final int LENGTH	= Blob.LENGTH;
	
	private Blob blob;
	
	public BlobEmitter( Blob blob ) {
		
		super();
		
		this.blob = blob;
		blob.use( this );
	}
	
	@Override
	protected void emit( int index ) {
		
		if (blob.volume <= 0) {
			return;
		}
		
		int[] map = blob.cur;
		float size = DungeonTilemap.SIZE;
		
		for (int i=0; i < LENGTH; i++) {
			if (map[i] > 0 && Dungeon.visible[i]) {
				float x = ((i % WIDTH) + Random.Float()) * size;
				float y = ((i / WIDTH) + Random.Float()) * size;
				factory.emit( this, index, x, y );
			}
		}
	}
}
