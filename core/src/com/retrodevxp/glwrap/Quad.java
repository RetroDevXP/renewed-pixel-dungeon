/*
 * Copyright (C) 2012-2015 Oleg Dolya
 *
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

package com.retrodevxp.glwrap;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.retrodevxp.glwrap.BoundBuffer;

public class Quad {

	// 0---1
	// | \ |
	// 3---2
	public static final short[] VALUES = {0, 1, 2, 0, 2, 3};
	
	public static final int SIZE = VALUES.length;

	private static int indexSize = 0;
	private static ShortBuffer indices;
	public static BoundBuffer indexBuffer = getIndices(50 * 50);
	
	public static FloatBuffer create() {
		return ByteBuffer.
			allocateDirect( 16 * Float.SIZE / 8 ).
			order( ByteOrder.nativeOrder() ).
			asFloatBuffer();
	}
	
	public static FloatBuffer createSet( int size ) {
		return ByteBuffer.
			allocateDirect( size * 16 * Float.SIZE / 8 ).
			order( ByteOrder.nativeOrder() ).
			asFloatBuffer();
	}
	
	public static BoundBuffer getIndices( int size ) {

		if (size > indexSize) {
			
			indexSize = size;
			
			indices =  ByteBuffer.
				allocateDirect( size * SIZE * Short.SIZE / 8 ).
				order( ByteOrder.nativeOrder() ).
				asShortBuffer();

				
			short[] values = new short[size * 6];
			int pos = 0;
			int limit = size * 4;
			for (int ofs=0; ofs < limit; ofs += 4) {
				values[pos++] = (short)(ofs + 0);
				values[pos++] = (short)(ofs + 1);
				values[pos++] = (short)(ofs + 2);
				values[pos++] = (short)(ofs + 0);
				values[pos++] = (short)(ofs + 2);
				values[pos++] = (short)(ofs + 3);
			}
			
			indices.put( values );
			indices.position( 0 );
			
			if (indexBuffer == null) {
				indexBuffer = new BoundBuffer(indices, Short.BYTES, BoundBuffer.ELEMENT_ARRAY);
			} else {
				indexBuffer.update(indices);
			}
		}

		return indexBuffer;
	}
	
	public static void fill( float[] v, 
		float x1, float x2, float y1, float y2, 
		float u1, float u2, float v1, float v2 ) {
		
		v[0] = x1;
		v[1] = y1;
		v[2] = u1;
		v[3] = v1;
		
		v[4] = x2;
		v[5] = y1;
		v[6] = u2;
		v[7] = v1;
		
		v[8] = x2;
		v[9] = y2;
		v[10]= u2;
		v[11]= v2;
		
		v[12]= x1;
		v[13]= y2;
		v[14]= u1;
		v[15]= v2;
	}
	
	public static void fillXY( float[] v, float x1, float x2, float y1, float y2 ) {
		
		v[0] = x1;
		v[1] = y1;
		
		v[4] = x2;
		v[5] = y1;
		
		v[8] = x2;
		v[9] = y2;
		
		v[12]= x1;
		v[13]= y2;
	}
	
	public static void fillUV( float[] v, float u1, float u2, float v1, float v2 ) {
		
		v[2] = u1;
		v[3] = v1;
		
		v[6] = u2;
		v[7] = v1;
		
		v[10]= u2;
		v[11]= v2;
		
		v[14]= u1;
		v[15]= v2;
	}
}
