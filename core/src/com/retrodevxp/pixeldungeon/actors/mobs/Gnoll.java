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

import com.retrodevxp.pixeldungeon.Challenges;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Rage;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.Ghost;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.items.Gold;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.sprites.GnollSprite;
import com.retrodevxp.pixeldungeon.utils.GLog;
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
				if (Dungeon.isChallenged( Challenges.STRONGER_MOBS)){
					return Random.NormalIntRange( 4, 9 );
				}
				return Random.NormalIntRange( 3, 7 );
			}
		}
		catch(Exception e){
			
		}
		if (Dungeon.isChallenged( Challenges.STRONGER_MOBS)){
			return Random.NormalIntRange( 3, 6 );
		}
		return Random.NormalIntRange( 2, 5 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 11;
	}

	//Gnoll Scouts alerts other Gnolls to the hero's position when damaged at full HP.
	//If the damage is from other sources, it won't alert other Gnolls. The damage must be from the hero.
	@Override
	public int defenseProc( Char enemy, int damage ){
		if (enemy == Dungeon.hero && HP == HT && damage < HT){
			int countGnolls = 0;
			for (Mob mob : Dungeon.level.mobs.toArray( new Mob[0] )) {
			if (mob instanceof Gnoll || mob instanceof Brute || mob instanceof Shaman){
				if (mob != this){
					mob.beckon( Dungeon.hero.pos );
					countGnolls += 1;
				}
			if (Dungeon.visible[mob.pos]) {
				}
			}
			// System.out.println("Gnoll");
			
		}
		if (countGnolls >= 1){
			GLog.w("Gnoll Scout alerted other Gnolls! You hear other Gnolls answering in the distance.");
		}
		else{
			GLog.w("Gnoll Scout alerted other Gnolls!");
		}
		this.sprite.centerEmitter().start( Speck.factory( Speck.SCREAM ), 0.3f, 3 );	
		}
		return super.defenseProc( enemy, damage );
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
			"Gnoll scouts are more commonly encountered in the upper parts of the dungeon where they are more likely to survive without protection from their pack. " + 
			"While not as strong as brutes and not as intelligent as shamans, these scouts alerts their pack to the presence of intruders.";
	}
}
