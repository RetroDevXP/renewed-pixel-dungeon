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
package com.retrodevxp.pixeldungeon.sprites;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.retrodevxp.gdx.GdxTexture;
import com.retrodevxp.gltextures.TextureCache;
import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.MovieClip;
import com.retrodevxp.noosa.TextureFilm;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.DungeonTilemap;
import com.retrodevxp.pixeldungeon.effects.CellEmitter;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.Gold;
import com.retrodevxp.pixeldungeon.items.Heap;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Terrain;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.utils.PointF;
import com.retrodevxp.utils.Random;

public class ItemSprite extends MovieClip {

	public static final int SIZE	= 16;
	
	private static final float DROP_INTERVAL = 0.4f;
	
	protected static TextureFilm film;
	
	public Heap heap;
	
	private Glowing glowing;
	private float phase;
	private boolean glowUp;
	
	private float dropInterval;
	
	public ItemSprite() {
		this( ItemSpriteSheet.SMTH, null );
	}
	
	public ItemSprite( Item item ) {
		this( item.image(), item.glowing() );
	}
	
	public ItemSprite( int image, Glowing glowing ) {
		super( Assets.ITEMS );
		
		if (film == null) {
			film = new TextureFilm( texture, SIZE, SIZE );
		}
		
		view( image, glowing );
	}
	
	public void originToCenter() {
		origin.set(SIZE / 2 );
	}
	
	public void link() {
		link( heap );
	}
	
	public void link( Heap heap ) {
		this.heap = heap;
		view( heap.image(), heap.glowing() );
		place( heap.pos );
	}
	
	@Override
	public void revive() {
		super.revive();
		
		speed.set( 0 );
		acc.set( 0 );
		dropInterval = 0;
		
		heap = null;
	}
	
	public PointF worldToCamera( int cell ) {
		final int csize = DungeonTilemap.SIZE;
		
		return new PointF(
			cell % Level.WIDTH * csize + (csize - SIZE) * 0.5f,
			cell / Level.WIDTH * csize + (csize - SIZE) * 0.5f
		);
	}
	
	public void place( int p ) {
		point( worldToCamera( p ) );
	}
	
	public void drop() {

		if (heap.isEmpty()) {
			return;
		}
			
		dropInterval = DROP_INTERVAL;
		
		speed.set( 0, -100 );
		acc.set( 0, -speed.y / DROP_INTERVAL * 2 );
		
		if (visible && heap != null && heap.peek() instanceof Gold) {
			CellEmitter.center( heap.pos ).burst( Speck.factory( Speck.COIN ), 5 );
			Sample.INSTANCE.play( Assets.SND_GOLD, 1, 1, Random.Float( 0.9f, 1.1f ) );
		}
	}
	
	public void drop( int from ) {

		if (heap.pos == from) {
			drop();
		} else {
			
			float px = x;
			float py = y;		
			drop();
			
			place( from );
	
			speed.offset( (px-x) / DROP_INTERVAL, (py-y) / DROP_INTERVAL );
		}
	}
	
	public ItemSprite view( int image, Glowing glowing ) {
		frame( film.get( image ) );
		if ((this.glowing = glowing) == null) {
			resetColor();
		}
		return this;
	}
	
	@Override
	public void update() {
		super.update();

		visible = (heap == null || Dungeon.visible[heap.pos]);
		
		if (dropInterval > 0 && (dropInterval -= Game.elapsed) <= 0) {
			
			speed.set( 0 );
			acc.set( 0 );
			place( heap.pos );
			
			if (visible) {
				boolean water = Level.water[heap.pos];
				
				if (water) {
					GameScene.ripple( heap.pos );
				} else {
					int cell = Dungeon.level.map[heap.pos];
					water = (cell == Terrain.WELL || cell == Terrain.ALCHEMY);
				}
				
				if (!(heap.peek() instanceof Gold)) {
					Sample.INSTANCE.play( water ? Assets.SND_WATER : Assets.SND_STEP, 0.8f, 0.8f, 1.2f );
				}
			}
		}
		
		if (visible && glowing != null) {
			if (glowUp && (phase += Game.elapsed) > glowing.period) {
				
				glowUp = false;
				phase = glowing.period;
				
			} else if (!glowUp && (phase -= Game.elapsed) < 0) {
				
				glowUp = true;
				phase = 0;
				
			}
			
			float value = phase / glowing.period * 0.6f;
			
			rm = gm = bm = 1 - value;
			ra = glowing.red * value;
			ga = glowing.green * value;
			ba = glowing.blue * value;
		}
	}
	
	public static int pick( int index, int x, int y ) {
		GdxTexture bmp = TextureCache.get( Assets.ITEMS ).bitmap;
		int rows = bmp.getWidth() / SIZE;
		int row = index / rows;
		int col = index % rows;
		// FIXME: I'm assuming this is super slow?
		final TextureData td = bmp.getTextureData();
		if (!td.isPrepared()) {
			td.prepare();
		}
		final Pixmap pixmap = td.consumePixmap();
		int pixel = pixmap.getPixel(col * SIZE + x, row * SIZE + y);
		pixmap.dispose();
		return pixel;
	}
	
	public static class Glowing {
		
		public static final Glowing WHITE = new Glowing( 0xFFFFFF, 0.6f );
		
		public int color;
		public float red;
		public float green;
		public float blue;
		public float period;
		
		public Glowing( int color ) {
			this( color, 1f );
		}
		
		public Glowing( int color, float period ) {
			
			this.color = color;
			
			red = (color >> 16) / 255f;
			green = ((color >> 8) & 0xFF) / 255f;
			blue = (color & 0xFF) / 255f;
			
			this.period = period;
		}
	}
}
