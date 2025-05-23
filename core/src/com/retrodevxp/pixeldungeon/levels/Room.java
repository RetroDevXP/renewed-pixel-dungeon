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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.retrodevxp.pixeldungeon.PixelDungeon;
import com.retrodevxp.pixeldungeon.levels.painters.*;
import com.retrodevxp.utils.Bundlable;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.Graph;
import com.retrodevxp.utils.Point;
import com.retrodevxp.utils.Random;
import com.retrodevxp.utils.Rect;

public class Room extends Rect implements Graph.Node, Bundlable {
	
	public HashSet<Room> neigbours = new HashSet<Room>();
	public HashMap<Room, Door> connected = new HashMap<Room, Door>();
	
	public int distance;
	public int price = 1;
	
	public static enum Type {
		NULL( null ),
		STANDARD	( new StandardPainter() ),
		ENTRANCE	( new EntrancePainter() ),
		EXIT		( new ExitPainter() ),
		BOSS_EXIT	( new BossExitPainter() ),
		TUNNEL		( new TunnelPainter() ),
		PASSAGE		( new PassagePainter() ),
		SHOP		( new ShopPainter() ),
		BLACKSMITH	( new BlacksmithPainter() ),
		TREASURY	( new TreasuryPainter() ),
		ARMORY		( new ArmoryPainter() ),
		LIBRARY		( new LibraryPainter() ),
		LABORATORY	( new LaboratoryPainter() ),
		VAULT		( new VaultPainter() ),
		TRAPS		( new TrapsPainter() ),
		STORAGE		( new StoragePainter() ),
		SEEDVAULT		( new SeedVaultPainter() ),
		MAGIC_WELL	( new MagicWellPainter() ),
		GARDEN		( new GardenPainter() ),
		CRYPT		( new CryptPainter() ),
		STATUE		( new StatuePainter() ),
		POOL		( new PoolPainter() ),
		RAT_KING	( new RatKingPainter() ),
		WEAK_FLOOR	( new WeakFloorPainter() ),
		PIT			( new PitPainter() ),
		ALTAR		( new AltarPainter() );
		
		private Painter painter;
		
		private Type( Painter painter ) {
			this.painter = painter;
		}
		
		public void paint( Level level, Room room ) {
			try {
				painter.paint(level, room);
			} catch (Exception e) {
				PixelDungeon.reportException( e );
			}
		}
	};
	
	public static final ArrayList<Type> SPECIALS = new ArrayList<Type>( Arrays.asList(
		Type.MAGIC_WELL, Type.WEAK_FLOOR, Type.ARMORY, Type.CRYPT, Type.POOL, Type.GARDEN, Type.LIBRARY,
		Type.TREASURY, Type.TRAPS, Type.STORAGE, Type.SEEDVAULT, Type.STATUE, Type.LABORATORY, Type.VAULT, Type.ALTAR
	) );
	
	public Type type = Type.NULL;
	
	public int random() {
		return random( 0 );
	}
	
	public int random( int m ) {
		int x = Random.Int( left + 1 + m, right - m );
		int y = Random.Int( top + 1 + m, bottom - m );
		return x + y * Level.WIDTH;
	}
	
	public void addNeigbour( Room other ) {
		
		Rect i = intersect( other );
		if ((i.width() == 0 && i.height() >= 3) || 
			(i.height() == 0 && i.width() >= 3)) {
			neigbours.add( other );
			other.neigbours.add( this );
		}
		
	}
	
	public void connect( Room room ) {
		if (!connected.containsKey( room )) {	
			connected.put( room, null );
			room.connected.put( this, null );			
		}
	}
	
	public Door entrance() {
		return connected.values().iterator().next();
	}
	
	public boolean inside( int p ) {
		int x = p % Level.WIDTH;
		int y = p / Level.WIDTH;
		return x > left && y > top && x < right && y < bottom;
	}
	
	public Point center() {
		return new Point( 
			(left + right) / 2 + (((right - left) & 1) == 1 ? Random.Int( 2 ) : 0),
			(top + bottom) / 2 + (((bottom - top) & 1) == 1 ? Random.Int( 2 ) : 0) );
	}
	
	// **** Graph.Node interface ****

	@Override
	public int distance() {
		return distance;
	}

	@Override
	public void distance( int value ) {
		distance = value;
	}
	
	@Override
	public int price() {
		return price;
	}

	@Override
	public void price( int value ) {
		price = value;
	}

	@Override
	public Collection<Room> edges() {
		return neigbours;
	} 
	
	// FIXME: use proper string constants
	
	@Override
	public void storeInBundle( Bundle bundle ) {	
		bundle.put( "left", left );
		bundle.put( "top", top );
		bundle.put( "right", right );
		bundle.put( "bottom", bottom );
		bundle.put( "type", type.toString() );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		left = bundle.getInt( "left" );
		top = bundle.getInt( "top" );
		right = bundle.getInt( "right" );
		bottom = bundle.getInt( "bottom" );		
		type = Type.valueOf( bundle.getString( "type" ) );
	}
	
	public static void shuffleTypes() {
		int size = SPECIALS.size();
		for (int i=0; i < size - 1; i++) {
			int j = Random.Int( i, size );
			if (j != i) {
				Type t = SPECIALS.get( i );
				SPECIALS.set( i, SPECIALS.get( j ) );
				SPECIALS.set( j, t );
			}
		}
	}
	
	public static void useType( Type type ) {
		if (SPECIALS.remove( type )) {
			SPECIALS.add( type );
		}
	}
	
	private static final String ROOMS	= "rooms";
	
	public static void restoreRoomsFromBundle( Bundle bundle ) {
		if (bundle.contains( ROOMS )) {
			SPECIALS.clear();
			for (String type : bundle.getStringArray( ROOMS )) {
				SPECIALS.add( Type.valueOf( type ));
			}
		} else {
			shuffleTypes();
		}
	}
	
	public static void storeRoomsInBundle( Bundle bundle ) {
		String[] array = new String[SPECIALS.size()];
		for (int i=0; i < array.length; i++) {
			array[i] = SPECIALS.get( i ).toString();
		}
		bundle.put( ROOMS, array );
	}
	
	public static class Door extends Point {
		
		public static enum Type {
			EMPTY, TUNNEL, REGULAR, UNLOCKED, HIDDEN, BARRICADE, LOCKED
		}
		public Type type = Type.EMPTY;
		
		public Door( int x, int y ) {
			super( x, y );
		}
		
		public void set( Type type ) {
			if (type.compareTo( this.type ) > 0) {
				this.type = type;
			}
		}
	}
}
