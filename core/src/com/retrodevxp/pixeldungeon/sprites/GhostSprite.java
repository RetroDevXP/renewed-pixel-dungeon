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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.retrodevxp.noosa.TextureFilm;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.effects.particles.ShaftParticle;

public class GhostSprite extends MobSprite {
	
	public GhostSprite() {
		super();
		
		texture( Assets.GHOST );
		
		TextureFilm frames = new TextureFilm( texture, 14, 15 );
		
		idle = new Animation( 5, true );
		idle.frames( frames, 0, 1 );
		
		run = new Animation( 10, true );
		run.frames( frames, 0, 1 );
		
		die = new Animation( 20, false );
		die.frames( frames, 0 );
		
		play( idle );
	}
	
	@Override
	public void draw() {
		Gdx.gl.glBlendFunc( GL20.GL_SRC_ALPHA, GL20.GL_ONE );
		super.draw();
		Gdx.gl.glBlendFunc( GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA );
	}
	
	@Override
	public void die() {
		super.die();
		emitter().start( ShaftParticle.FACTORY, 0.3f, 4 );
		emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
	}
	
	@Override
	public int blood() {
		return 0xFFFFFF;
	}
}
