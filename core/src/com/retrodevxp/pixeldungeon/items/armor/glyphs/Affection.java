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

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Charm;
import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.armor.Armor;
import com.retrodevxp.pixeldungeon.items.armor.Armor.Glyph;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite.Glowing;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.GameMath;
import com.retrodevxp.utils.Random;

public class Affection extends Glyph {

	private static final String TXT_AFFECTION	= "%s of affection";
	
	private static ItemSprite.Glowing PINK = new ItemSprite.Glowing( 0xFF4488 );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage) {

		int level = (int)GameMath.gate( 0, armor.effectiveLevel(), 6 );
		
		if (Level.adjacent( attacker.pos, defender.pos ) && Random.Int( level / 2 + 5 ) >= 4) {
			
			int duration = Random.IntRange( 3, 7 );
			
			Buff.affect( attacker, Charm.class, Charm.durationFactor( attacker ) * duration ).object = defender.id();
			attacker.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
			
			duration *= Random.Float( 0.5f, 1 );
			if (Dungeon.hero.subClass == HeroSubClass.SCRIBE){
				duration *= Random.Float( 0.5f, 1 );
			}
			
			Buff.affect( defender, Charm.class, Charm.durationFactor( defender ) * duration ).object = attacker.id();
			defender.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_AFFECTION, weaponName );
	}

	@Override
	public Glowing glowing() {
		return PINK;
	}
	
	@Override
	public String description(){
		return "The magical powers of this enchantment has a chance to charm both the wearer and the target.";
	}
}
