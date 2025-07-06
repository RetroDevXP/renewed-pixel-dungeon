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
package com.retrodevxp.pixeldungeon.items.armor.glyphs;

import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Invisibility;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.armor.Armor;
import com.retrodevxp.pixeldungeon.items.armor.Armor.Glyph;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite.Glowing;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.utils.GameMath;
import com.retrodevxp.utils.Random;

public class Stealth extends Glyph {

	private static final String TXT_STEALTH	= "%s of stealth";
	
	private static ItemSprite.Glowing DARK = new ItemSprite.Glowing( 0x000033 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = (int)GameMath.gate( 0, armor.effectiveLevel(), 6 );
		
		if (Level.adjacent( attacker.pos, defender.pos ) && Random.Int( level / 2 + 5 ) >= 3) {
			
			int duration = Random.IntRange( 3, 7 );
			
			Buff.affect( defender, Invisibility.class, duration );
			defender.sprite.centerEmitter().start( Speck.factory( Speck.BLINDING ), 0.2f, 5 );
			Sample.INSTANCE.play( Assets.SND_MELD );
		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_STEALTH, weaponName );
	}

	@Override
	public Glowing glowing() {
		return DARK;
	}

	@Override
	public String description(){
		return "When the wearer is damaged, this armor sometimes generates a cloud of smoke that makes the wearer invisible for a short duration.";
	}
}
