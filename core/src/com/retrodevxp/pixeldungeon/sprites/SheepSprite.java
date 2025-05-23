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

import com.retrodevxp.noosa.TextureFilm;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.effects.CellEmitter;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.utils.Random;

public class SheepSprite extends MobSprite {
	
	public SheepSprite() {
		super();
		
		texture( Assets.SHEEP );
		
		TextureFilm frames = new TextureFilm( texture, 16, 15 );
		
		idle = new Animation( 8, true );
		idle.frames( frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 0 );
		
		run = idle.clone();	
		attack = idle.clone();
		
		die = new Animation( 20, false );
		die.frames( frames, 4 );
		
		play( idle );
		curFrame = Random.Int( curAnim.frames.length );
	}

	@Override
    public void die() {
        if (visible) {
            CellEmitter.get(ch.pos).burst(Speck.factory(7), 4);
        }
        super.die();
    }

    @Override
    public void update() {
        if (ch != null) {
            turnTo(ch.pos, Dungeon.hero.pos);
        }
        super.update();
    }
}
