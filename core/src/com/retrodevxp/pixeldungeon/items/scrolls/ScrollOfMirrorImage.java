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
package com.retrodevxp.pixeldungeon.items.scrolls;

import java.util.ArrayList;

import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Invisibility;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.MirrorImage;
import com.retrodevxp.pixeldungeon.effects.Pushing;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.wands.WandOfBlink;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.utils.Random;

public class ScrollOfMirrorImage extends Scroll {

	private static final int NIMAGES	= 3;
	
	{
		name = "Scroll of Mirror Image";
	}
	
	@Override
	protected void doRead() {
		
		ArrayList<Integer> respawnPoints = new ArrayList<Integer>();
		
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = curUser.pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				respawnPoints.add( p );
			}
		}
		
		int nImages = NIMAGES;
		while (nImages > 0 && respawnPoints.size() > 0) {
			int index = Random.index( respawnPoints );
			
			MirrorImage mob = new MirrorImage();
			mob.duplicate( curUser );
			GameScene.add( mob );
			// WandOfBlink.appear( mob, respawnPoints.get( index ) );
			int pos = respawnPoints.get(index).intValue();
            mob.sprite.place(pos);
            mob.move(pos);
            mob.sprite.flipHorizontal = curUser.sprite.flipHorizontal;
            Actor.addDelayed(new Mirroring(mob, curUser.pos, pos), -1.0f);
			
			respawnPoints.remove( index );
			nImages--;
		}
		
		if (nImages < NIMAGES) {
			setKnown();
		}
		
		Sample.INSTANCE.play( Assets.SND_READ );
		Invisibility.dispel();
		
		readAnimation();
	}
	
	@Override
	public String desc() {
		return 
			"The incantation on this scroll will summon several magical illusions of the reader. Each illusion, filled with condensed magical energy, will seek out nearby enemies and unleash that energy upon them.";
	}

	public static class Mirroring extends Pushing {
        public Mirroring(Char ch, int from, int to) {
            super(ch, from, to);
            ch.sprite.am = 0;
        }

        @Override
        protected Pushing.Effect effect() {
            sprite.emitter().start(Speck.factory(2), 0.2f, 3);
            Sample.INSTANCE.play(Assets.SND_TELEPORT);
            return new Effect();
        }

        public class Effect extends Pushing.Effect {
            protected Effect() {
                super();
            }

            @Override
            public void update() {
                super.update();
                sprite.am = delay / 0.15f;
            }
        }
    }
}
