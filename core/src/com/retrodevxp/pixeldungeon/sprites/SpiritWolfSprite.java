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

import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.noosa.TextureFilm;

public class SpiritWolfSprite extends MobSprite {
	
	public SpiritWolfSprite() {
		super();
		
		texture( Assets.SPIRITWOLF );
		
		TextureFilm frames = new TextureFilm( texture, 16, 16 );
		
		idle = new Animation( 5, true );
		idle.frames( frames, 4, 3, 7, 1, 4, 3, 7, 1 );
		
		run = new Animation( 5, true );
		run.frames( frames, 0, 5, 8, 0, 5, 8, 0, 5, 8 );
		
		attack = new Animation( 5, false );
		attack.frames( frames, 1, 2, 6, 1 );
		
		die = new Animation( 5, false );
		die.frames( frames, 1, 9, 10 );
		
		play( idle );
	}
	
	@Override
	public int blood() {
		return 0x99FFFF;
	}
}
