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

package com.retrodevxp.noosa;

import com.retrodevxp.gltextures.TextureCache;
import com.retrodevxp.noosa.Image;

public class PseudoPixel extends Image {
	
	public PseudoPixel() {
		super( TextureCache.createSolid( 0xFFFFFFFF ) );
	}
	
	public PseudoPixel( float x, float y, int color ) {

		this();
		
		this.x = x;
		this.y = y;
		color( color );
	}
	
	public void size( float w, float h ) {
		scale.set( w, h );
	}
	
	public void size( float value ) {
		scale.set( value );
	}
}
