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
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.items.armor.Armor;
import com.retrodevxp.pixeldungeon.items.armor.Armor.Glyph;
import com.retrodevxp.pixeldungeon.items.wands.WandOfBlink;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite.Glowing;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Random;

public class Displacement extends Glyph {

	private static final String TXT_DISPLACEMENT	= "%s of displacement";
	
	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing( 0x66AAFF );
	
	@Override
	public int proc( Armor armor, Char attacker, Char defender, int damage ) {

		if (Dungeon.bossLevel()) {
			return damage;
		}
		
		int level = armor.effectiveLevel();
		int nTries = (level < 0 ? 1 : level + 1) * 5;
		for (int i=0; i < nTries; i++) {
			int pos = Random.Int( Level.LENGTH );
			if (Dungeon.visible[pos] && Level.passable[pos] && Actor.findChar( pos ) == null) {
				
				WandOfBlink.appear( defender, pos );
				Dungeon.level.press( pos, defender );
				Dungeon.observe();

				break;
			}
		}
		
		return damage;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_DISPLACEMENT, weaponName );
	}

	@Override
	public Glowing glowing() {
		return BLUE;
	}

	@Override
	public String description(){
		return "Rigged with potentially life-saving magic, this armor has a chance to teleport its wearer to another location when damaged.";
	}
}
