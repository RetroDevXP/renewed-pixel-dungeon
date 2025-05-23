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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.retrodevxp.pixeldungeon.sprites.CharSprite;
import com.retrodevxp.noosa.Game;

public class TorchHalo extends Halo {

	private CharSprite target;
	
	private float phase = 0;
	
	public TorchHalo( CharSprite sprite ) {
		super( 24, 0xFFDDCC, 0.15f );
		target = sprite;
		am = 0;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (phase < 0) {
			if ((phase += Game.elapsed) >= 0) {
				killAndErase();
			} else {
				scale.set( (2 + phase) * radius / RADIUS );
				am = -phase * brightness;
			}
		} else if (phase < 1) {
			if ((phase += Game.elapsed) >= 1) {
				phase = 1;
			}
			scale.set( phase * radius / RADIUS );
			am = phase * brightness;
		}
		
		point( target.x + target.width / 2, target.y + target.height / 2 );
	}
	
	@Override
	public void draw() {
		Gdx.gl.glBlendFunc( GL20.GL_SRC_ALPHA, GL20.GL_ONE );
		super.draw();
		Gdx.gl.glBlendFunc( GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA );
	}
	
	public void putOut() {
		phase = -1;
	}
}
