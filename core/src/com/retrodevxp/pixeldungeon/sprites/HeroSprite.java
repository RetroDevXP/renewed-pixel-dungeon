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

import com.retrodevxp.gltextures.SmartTexture;
import com.retrodevxp.gltextures.TextureCache;
import com.retrodevxp.noosa.Camera;
import com.retrodevxp.noosa.Image;
import com.retrodevxp.noosa.TextureFilm;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.buffs.Disguise;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.hero.HeroClass;
import com.retrodevxp.utils.Callback;
import com.retrodevxp.utils.PointF;
import com.retrodevxp.utils.RectF;

public class HeroSprite extends CharSprite {
	
	private static final int FRAME_WIDTH	= 12;
	private static final int FRAME_HEIGHT	= 15;
	
	private static final int RUN_FRAMERATE	= 20;
	
	private static TextureFilm tiers;
	
	private Animation fly;
	private Animation read;
	
	public HeroSprite() {
		super();
		
		link( Dungeon.hero );
		
		updateTexture();
		
		idle();
	}
	
	public void updateArmor() {

		TextureFilm film = new TextureFilm( tiers(), ((Hero)ch).tier(), FRAME_WIDTH, FRAME_HEIGHT );
		
		idle = new Animation( 1, true );
		idle.frames( film, 0, 0, 0, 1, 0, 0, 1, 1 );
		
		run = new Animation( RUN_FRAMERATE, true );
		run.frames( film, 2, 3, 4, 5, 6, 7 );
		
		die = new Animation( 20, false );
		die.frames( film, 8, 9, 10, 11, 12, 11 );
		
		attack = new Animation( 15, false );
		attack.frames( film, 13, 14, 15, 0 );
		
		zap = attack.clone();
		
		operate = new Animation( 8, false );
		operate.frames( film, 16, 17, 16, 17 );
		
		fly = new Animation( 1, true );
		fly.frames( film, 18 );
		
		read = new Animation( 20, false );
		read.frames( film, 19, 20, 20, 20, 20, 20, 20, 20, 20, 19 );
	}
	
	public void updateTexture() {
        Disguise disguise = Dungeon.hero.buff(Disguise.class);
        texture((disguise != null ? disguise.costume : Dungeon.hero.heroClass).spritesheet());
        updateArmor();
    }

	@Override
	public void place( int p ) {
		super.place( p );
		Camera.main.target = this;
	}

	@Override
	public void move( int from, int to ) {		
		super.move( from, to );
		if (ch.flying) {
			play( fly );
		}
		Camera.main.target = this;
	}
	
	@Override
	public void jump( int from, int to, Callback callback ) {	
		super.jump( from, to, callback );
		play( fly );
	}
	
	public void read() {
		animCallback = new Callback() {
			@Override
			public void call() {
				idle();
				ch.onOperateComplete();
			}
		};
		play( read );
	}
	
	@Override
	public void update() {
		sleeping = ((Hero)ch).restoreHealth;
		
		super.update();
	}
	
	public boolean sprint( boolean on ) {
		run.delay = on ? 0.625f / RUN_FRAMERATE : 1f / RUN_FRAMERATE;
		return on;
	}
	
	public static TextureFilm tiers() {
		if (tiers == null) {
			SmartTexture texture = TextureCache.get( Assets.ROGUE );
			tiers = new TextureFilm( texture, texture.width, FRAME_HEIGHT );
		}
		
		return tiers;
	}
	
	public static Image avatar( HeroClass cl, int armorTier ) {
		
		RectF patch = tiers().get( armorTier );
		Image avatar = new Image( cl.spritesheet() );
		RectF frame = avatar.texture.uvRect( 1, 0, FRAME_WIDTH, FRAME_HEIGHT );
		frame = frame.offset( patch.left, patch.top );
		avatar.frame( frame );
		
		return avatar;
	}
}
