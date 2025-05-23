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
package com.retrodevxp.pixeldungeon;

import java.util.Arrays;

import com.retrodevxp.noosa.Image;
import com.retrodevxp.noosa.TextureFilm;
import com.retrodevxp.noosa.Tilemap;
import com.retrodevxp.noosa.tweeners.AlphaTweener;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.utils.Point;
import com.retrodevxp.utils.PointF;

public class DungeonTilemap extends Tilemap {

	public static final int SIZE = 16;
	
	private static DungeonTilemap instance;
	
	public DungeonTilemap() {
		super( 
			Dungeon.level.tilesTex(), 
			new TextureFilm( Dungeon.level.tilesTex(), SIZE, SIZE ) );
		map( Dungeon.level.map, Level.WIDTH );
		
		instance = this;
	}
	
	public int screenToTile( int x, int y ) {
		Point p = camera().screenToCamera( x, y ).
			offset( this.point().negate() ).
			invScale( SIZE ).
			floor();
		return p.x >= 0 && p.x < Level.WIDTH && p.y >= 0 && p.y < Level.HEIGHT ? p.x + p.y * Level.WIDTH : -1;
	}
	
	@Override
	public boolean overlapsPoint( float x, float y ) {
		return true;
	}
	
	public void discover( int pos, int oldValue ) {
		
		final Image tile = tile( oldValue );
		tile.point( tileToWorld( pos ) );
		
		// For bright mode
		tile.rm = tile.gm = tile.bm = rm;
		tile.ra = tile.ga = tile.ba = ra;
		parent.add( tile );
		
		parent.add( new AlphaTweener( tile, 0, 0.6f ) {
			protected void onComplete() {
				tile.killAndErase();
				killAndErase();
			};
		} );
	}
	
	public static PointF tileToWorld( int pos ) {
		return new PointF( pos % Level.WIDTH, pos / Level.WIDTH  ).scale( SIZE );
	}
	
	public static PointF tileCenterToWorld( int pos ) {
		return new PointF( 
			(pos % Level.WIDTH + 0.5f) * SIZE, 
			(pos / Level.WIDTH + 0.5f) * SIZE );
	}

	public static Point tileToPoint( int pos ) {
		return new Point(pos % Level.WIDTH, pos / Level.WIDTH);
	}

	public static int pointToTile( Point point ) {
		return point.y * Level.WIDTH + point.x;
	}
	
	public static Image tile( int index ) {
		Image img = new Image( instance.texture );
		img.frame( instance.tileset.get( index ) );
		return img;
	}
	
	@Override
	public boolean overlapsScreenPoint( int x, int y ) {
		return true;
	}
}
