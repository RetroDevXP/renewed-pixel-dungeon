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
package com.retrodevxp.pixeldungeon.items.wands;

import java.util.ArrayList;

import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.Bee;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.SpiritWolf;
import com.retrodevxp.pixeldungeon.effects.MagicMissile;
import com.retrodevxp.pixeldungeon.effects.Pushing;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.effects.Splash;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.mechanics.Ballistica;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.noosa.tweeners.AlphaTweener;
import com.retrodevxp.utils.Callback;
import com.retrodevxp.utils.Random;

public class WandOfSpirits extends Wand {

	{
		name = "Wand of Spirits";
	}
	
	@Override
	protected void onZap( int cell ) {

		int level = power();
		
		if (Ballistica.distance > level + 5) {
			cell = Ballistica.trace[level + 3];
		} else if (Actor.findChar( cell ) != null && Ballistica.distance > 1) {
			cell = Ballistica.trace[Ballistica.distance - 2];
		}
		
		// curUser.sprite.visible = true;
		if (Dungeon.visible[cell]) {
			Splash.at( cell, 0xffd500, 5 );
		}
		
		int newPos = cell;
		if (Actor.findChar( cell ) != null) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			boolean[] passable = Level.passable;
			
			for (int n : Level.NEIGHBOURS4) {
				int c = cell + n;
				if (passable[c] && Actor.findChar( c ) == null) {
					candidates.add( c );
				}
			}
	
			newPos = candidates.size() > 0 ? Random.element( candidates ) : -1;
		}
		
		if (newPos != -1) {
			SpiritWolf spiritwolf = new SpiritWolf();
			spiritwolf.spawn( (int)(Dungeon.depth / 5) + level );
			spiritwolf.HP = spiritwolf.HT;
			spiritwolf.pos = newPos;
			
			GameScene.add( spiritwolf );
			Actor.addDelayed( new Pushing( spiritwolf, cell, newPos ), -1 );
			
			spiritwolf.sprite.alpha( 0 );
			spiritwolf.sprite.parent.add( new AlphaTweener( spiritwolf.sprite, 1, 0.15f ) );
			
			Sample.INSTANCE.play( Assets.SND_GHOST );
		}
		Dungeon.observe();
	}
	
	@Override
	protected void fx( int cell, Callback callback ) {
		MagicMissile.whiteLight( curUser.sprite.parent, curUser.pos, cell, callback );
		Sample.INSTANCE.play( Assets.SND_ZAP );
		// curUser.sprite.visible = false;
	}
	
	public static void appear( Char ch, int pos ) {
		
		ch.sprite.interruptMotion();
		
		ch.move( pos );
		ch.sprite.place( pos );
		
		if (ch.invisible == 0) {
			ch.sprite.alpha( 0 );
			ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
		}
		
		ch.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.2f, 3 );
		Sample.INSTANCE.play( Assets.SND_TELEPORT );
	}
	
	@Override
	public String desc() {
		return
			"This wand summons a small yet loyal spirit wolf. " +
			"The spirit wolf lasts for a limited duration, dealing damage to any enemies.";
	}
}
