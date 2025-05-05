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
package com.retrodevxp.pixeldungeon.plants;

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Bleeding;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Cripple;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.mobs.Mob;
import com.retrodevxp.pixeldungeon.effects.CellEmitter;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfMindVision;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;

public class Hardthorn extends Plant {

	private static final String TXT_DESC = 
		"The Hardthorn has sharp, crippling points all across. " +
		"A creature which touches its thorns would definitely bleed.";
	
	{
		image = 9;
		plantName = "Hardthorn";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		Buff.prolong( ch, Cripple.class, 5 + (int)(Math.floor(Dungeon.depth / 10)) );
		Buff.affect( ch, Bleeding.class ).set( (Dungeon.depth * 3) + 2 );
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Hardthorn";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_HARDTHORN;
			
			plantClass = Hardthorn.class;
			alchemyClass = PotionOfMindVision.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
