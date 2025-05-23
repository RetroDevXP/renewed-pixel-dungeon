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
package com.retrodevxp.pixeldungeon.levels;

import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.Scene;
import com.retrodevxp.noosa.particles.Emitter;
import com.retrodevxp.noosa.particles.PixelParticle;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.DungeonTilemap;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.Ghost;
import com.retrodevxp.pixeldungeon.items.DewVial;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.utils.ColorMath;
import com.retrodevxp.utils.PointF;
import com.retrodevxp.utils.Random;

public class SewerLevel extends RegularLevel {

	{
		color1 = 0x48763c;
		color2 = 0x59994a;
	}
	
	@Override
	public String tilesTex() {
		return Assets.TILES_SEWERS;
	}
	
	@Override
	public String waterTex() {
		return Assets.WATER_SEWERS;
	}
	
	protected boolean[] water() {
		return Patch.generate( feeling == Feeling.WATER ? 0.60f : 0.45f, 5 );
	}
	
	protected boolean[] grass() {
		return Patch.generate( feeling == Feeling.GRASS ? 0.60f : 0.40f, 4 );
	}
	
	@Override
	protected void decorate() {
		
		for (int i=0; i < WIDTH; i++) {
			if (map[i] == Terrain.WALL &&  
				map[i + WIDTH] == Terrain.WATER &&
				Random.Int( 4 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		for (int i=WIDTH; i < LENGTH - WIDTH; i++) {
			if (map[i] == Terrain.WALL && 
				map[i - WIDTH] == Terrain.WALL && 
				map[i + WIDTH] == Terrain.WATER &&
				Random.Int( 2 ) == 0) {
				
				map[i] = Terrain.WALL_DECO;
			}
		}
		
		for (int i=WIDTH + 1; i < LENGTH - WIDTH - 1; i++) {
			if (map[i] == Terrain.EMPTY) { 
				
				int count = 
					(map[i + 1] == Terrain.WALL ? 1 : 0) + 
					(map[i - 1] == Terrain.WALL ? 1 : 0) + 
					(map[i + WIDTH] == Terrain.WALL ? 1 : 0) +
					(map[i - WIDTH] == Terrain.WALL ? 1 : 0);
				
				if (Random.Int( 16 ) < count * count) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
		
		while (true) {
			int pos = roomEntrance.random();
			if (pos != entrance) {
				map[pos] = Terrain.SIGN;
				break;
			}
		}
	}
	
	@Override
	protected void createMobs() {
		super.createMobs();

		Ghost.Quest.spawn( this );
	}
	
	@Override
	protected void createItems() {
		if (Dungeon.dewVial && Random.Int( 4 - Dungeon.depth ) == 0) {
			addItemToSpawn( new DewVial() );
			Dungeon.dewVial = false;
		}
		
		super.createItems();
	}
	
	@Override
	public void addVisuals( Scene scene ) {
		super.addVisuals( scene );
		addVisuals( this, scene );
	}
	
	public static void addVisuals( Level level, Scene scene ) {
		for (int i=0; i < LENGTH; i++) {
			if (level.map[i] == Terrain.WALL_DECO) {
				scene.add( new Sink( i ) );
			}
		}
	}
	
	@Override
	public String tileName( int tile ) {
		switch (tile) {
		case Terrain.WATER:
			return "Murky water";
		default:
			return super.tileName( tile );
		}
	}
	
	@Override
	public String tileDesc(int tile) {
		switch (tile) {
		case Terrain.EMPTY_DECO:
			return "Wet yellowish moss covers the floor.";
		case Terrain.BOOKSHELF:
			return "The bookshelf is packed with cheap useless books. Might it burn?";
		default:
			return super.tileDesc( tile );
		}
	}
	
	public static class Sink extends Emitter {
		
		private int pos;
		private float rippleDelay = 0;
		
		private static final Emitter.Factory factory = new Factory() {
			
			@Override
			public void emit( Emitter emitter, int index, float x, float y ) {
				WaterParticle p = (WaterParticle)emitter.recycle( WaterParticle.class );
				p.reset( x, y );
			}
		};
		
		public Sink( int pos ) {
			super();
			
			this.pos = pos;
			
			PointF p = DungeonTilemap.tileCenterToWorld( pos );
			pos( p.x - 2, p.y + 1, 4, 0 );
			
			pour( factory, 0.05f );
		}
		
		@Override
		public void update() {
			if (visible = Dungeon.visible[pos]) {
				
				super.update();
				
				if ((rippleDelay -= Game.elapsed) <= 0) {
					GameScene.ripple( pos + WIDTH ).y -= DungeonTilemap.SIZE / 2;
					rippleDelay = Random.Float( 0.2f, 0.3f );
				}
			}
		}
	}

	public static final class WaterParticle extends PixelParticle {
		
		public WaterParticle() {
			super();
			
			acc.y = 50;
			am = 0.5f;
			
			color( ColorMath.random( 0xb6ccc2, 0x3b6653 ) );
			size( 2 );
		}
		
		public void reset( float x, float y ) {
			revive();
			
			this.x = x;
			this.y = y;
			
			speed.set( Random.Float( -2, +2 ), 0 );
			
			left = lifespan = 0.5f;
		}
	}
}
