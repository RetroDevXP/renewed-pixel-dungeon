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
package com.retrodevxp.pixeldungeon.actors.mobs;

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.Ghost;
import com.retrodevxp.pixeldungeon.items.Gold;
import com.retrodevxp.pixeldungeon.sprites.GnollSprite;
import com.retrodevxp.utils.Random;

public class Gnoll extends Mob {
	
	{
		name = "gnoll scout";
		spriteClass = GnollSprite.class;
		
		HP = HT = 12;
		defenseSkill = 4;
		
		EXP = 2;
		maxLvl = 8;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	//Gnolls also spawn deeper in the dungeon. This code makes it slightly stronger deeper.
	//For the lore, the stronger gnolls survive deeper as well, which is why they deal more damage.
	@Override
	public int damageRoll() {
		try{
			if (Dungeon.depth > 5){
				return Random.NormalIntRange( 3, 7 );
			}
		}
		catch(Exception e){
			
		}
		return Random.NormalIntRange( 2, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}
	
	@Override
	public int dr() {
		return 2;
	}
	
	@Override
	public void die( Object cause ) {
		Ghost.Quest.processSewersKill( pos );
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"Gnolls are hyena-like humanoids. They dwell in sewers and dungeons, venturing up to raid the surface from time to time. " +
			"Gnoll scouts are more commonly encountered in the upper parts of the dungeon where they are more likely to survive without protection from their pack." + 
			"Being regular members of their pack, they are not as strong as brutes and not as intelligent as shamans.";
	}
}
