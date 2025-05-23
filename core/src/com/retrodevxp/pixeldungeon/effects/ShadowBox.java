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

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.retrodevxp.gltextures.SmartTexture;
import com.retrodevxp.noosa.NinePatch;
import com.retrodevxp.pixeldungeon.Assets;

public class ShadowBox extends NinePatch {

	public static final float SIZE	= 16;
	
	public ShadowBox() {
		super( Assets.SHADOW, 1 );
		
		texture.filter( TextureFilter.Linear, TextureFilter.Linear );
		
		scale.set( SIZE, SIZE );
	}
	
	@Override
	public void size(float width, float height) {
		super.size( width / SIZE, height / SIZE );
	}

	public void boxRect( float x, float y, float width, float height ) {
		this.x = x - SIZE;
		this.y = y - SIZE;
		size( width + SIZE * 2, height + SIZE * 2 );
	}
}
