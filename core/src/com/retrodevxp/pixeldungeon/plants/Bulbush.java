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
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.mobs.Mob;
import com.retrodevxp.pixeldungeon.effects.CellEmitter;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.food.*;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfMindVision;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;

public class Bulbush extends Plant {

	private static final String TXT_DESC = 
		"The fruit of the Bulbush is nutritious, " +
		"although it isn't exactly pleasant.";
	
	{
		image = 8;
		plantName = "Bulbush";
	}
	
	@Override
	public void activate( Char ch ) {
		super.activate( ch );
		
		Dungeon.level.drop( new BulbushFruit(), pos).sprite.drop( pos );
	}
	
	@Override
	public String desc() {
		return TXT_DESC;
	}
	
	public static class Seed extends Plant.Seed {
		{
			plantName = "Bulbush";
			
			name = "seed of " + plantName;
			image = ItemSpriteSheet.SEED_BULBUSH;
			
			plantClass = Bulbush.class;
			alchemyClass = PotionOfMindVision.class;
		}
		
		@Override
		public String desc() {
			return TXT_DESC;
		}
	}
}
