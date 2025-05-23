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

import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Attribute {

	private int location;
	
	public Attribute( int location ) {
		this.location = location;
	}
	
	public int location() {
		return location;
	}
	
	public void enable() {
		Gdx.gl.glEnableVertexAttribArray( location );
	}
	
	public void disable() {
		Gdx.gl.glDisableVertexAttribArray( location );
	}
	
	public void vertexPointer( int size, int stride, int offset ) {
		Gdx.gl.glVertexAttribPointer( location, size, GL20.GL_FLOAT, false, stride * Float.BYTES, offset * Float.BYTES );
	}
}
