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
package com.retrodevxp.pixeldungeon.actors.blobs;

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.Journal;
import com.retrodevxp.pixeldungeon.effects.BlobEmitter;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.Heap;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.utils.Bundle;

public class Alchemy extends Blob {

	protected int pos;
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		
		for (int i=0; i < LENGTH; i++) {
			if (cur[i] > 0) {
				pos = i;
				break;
			}
		}
	}
	
	@Override
	protected void evolve() {
		volume = off[pos] = cur[pos];
		
		if (Dungeon.visible[pos]) {
			Journal.add( Journal.Feature.ALCHEMY );
		}
	}
	
	@Override
	public void seed( int cell, int amount ) {
		cur[pos] = 0;
		pos = cell;
		volume = cur[pos] = amount;
	}
	
	public static void transmute( int cell ) {
		Heap heap = Dungeon.level.heaps.get( cell );
		if (heap != null) {
			
			Item result = heap.transmute();
			if (result != null) {
				Dungeon.level.drop( result, cell ).sprite.drop( cell );
			}
		}
	}
	
	@Override
	public void use( BlobEmitter emitter ) {
		super.use( emitter );	
		emitter.start( Speck.factory( Speck.BUBBLE ), 0.4f, 0 );
	}
}
