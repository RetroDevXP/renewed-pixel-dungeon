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

import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.Visual;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.sprites.CharSprite;
import com.retrodevxp.utils.PointF;

public class Pushing extends Actor {

	protected CharSprite sprite;
	private int from;
	private int to;
	
	private Effect effect;
	
	public Pushing( Char ch, int from, int to ) {
		sprite = ch.sprite;
		this.from = from;
		this.to = to;
	}
	
	@Override
	protected boolean act() {
		if (sprite != null && sprite.parent != null) {
			
			if (effect == null) {
				effect = effect();
			}
			return false;
			
		} else {
			
			Actor.remove( Pushing.this );
			return true;
		}
	}

	protected Effect effect() {
        return new Effect();
    }

	public class Effect extends Visual {

		private static final float DELAY = 0.15f;
		
		private PointF end;
		
		protected float delay;
		
		public Effect() {
			super( 0, 0, 0, 0 );
			
			point( sprite.worldToCamera( from ) );
			end = sprite.worldToCamera( to );
			
			speed.set( 2 * (end.x - x) / DELAY, 2 * (end.y - y) / DELAY );
			acc.set( -speed.x / DELAY, -speed.y / DELAY );
			
			delay = 0;
			
			sprite.parent.add( this );
		}
		
		@Override
		public void update() {
			super.update();
			
			if ((delay += Game.elapsed) < DELAY) {
				
				sprite.x = x;
				sprite.y = y;
				
			} else {
				
				sprite.point( end );
				
				killAndErase();
				Actor.remove( Pushing.this );
				
				next();
			}
		}
	}

}
