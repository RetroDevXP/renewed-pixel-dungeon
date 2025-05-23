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
package com.retrodevxp.pixeldungeon.items;

import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.ResultDescriptions;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Paralysis;
import com.retrodevxp.pixeldungeon.effects.CellEmitter;
import com.retrodevxp.pixeldungeon.effects.particles.BlastParticle;
import com.retrodevxp.pixeldungeon.effects.particles.SmokeParticle;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Random;

public class Bomb extends Item {
	
	{
		name = "bomb";
		image = ItemSpriteSheet.BOMB;
		defaultAction = AC_THROW;
		stackable = true;
	}
	
	@Override
	protected void onThrow( int cell ) {
		if (Level.pit[cell]) {
			super.onThrow( cell );
		} else {
			Sample.INSTANCE.play( Assets.SND_BLAST, 2 );
			
			if (Dungeon.visible[cell]) {
				CellEmitter.center( cell ).burst( BlastParticle.FACTORY, 30 );
			}
			
			boolean terrainAffected = false;
			for (int n : Level.NEIGHBOURS9) {
				int c = cell + n;
				if (c >= 0 && c < Level.LENGTH) {
					if (Dungeon.visible[c]) {
						CellEmitter.get( c ).burst( SmokeParticle.FACTORY, 4 );
					}
					
					if (Level.flamable[c]) {
						Dungeon.level.destroy( c );
						GameScene.updateMap( c );
						terrainAffected = true;	
					}
					
					Char ch = Actor.findChar( c );
					if (ch != null) {
						int dmg = Random.Int( 1 + Dungeon.depth, 10 + Dungeon.depth * 2 ) - Random.Int( ch.dr() );
						if (dmg > 0) {
							ch.damage( dmg, this );
							if (ch.isAlive()) {
								Buff.prolong( ch, Paralysis.class, 2 );
							} else if (ch == Dungeon.hero) {
								Dungeon.fail( Utils.format( ResultDescriptions.BOMB, Dungeon.depth ) );
								GLog.n( "You killed yourself with a bomb..." );
							}
						}
					}
				}
			}
			
			if (terrainAffected) {
				Dungeon.observe();
			}
		}
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public Item random() {
		quantity = Random.IntRange( 1, 3 );
		return this;
	}	
	
	@Override
	public int price() {
		return 10 * quantity;
	}
	
	@Override
	public String info() {
		return
			"This is a relatively small bomb, filled with black powder. Conveniently, its fuse is lit automatically when the bomb is thrown.";
	}
}
