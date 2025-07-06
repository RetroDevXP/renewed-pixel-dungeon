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

import java.util.HashSet;

import com.retrodevxp.pixeldungeon.Challenges;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Terror;
import com.retrodevxp.pixeldungeon.items.Gold;
import com.retrodevxp.pixeldungeon.sprites.BruteSprite;
import com.retrodevxp.pixeldungeon.sprites.CharSprite;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.Random;

public class Brute extends Mob {

	private static final String TXT_BERSERK = "%s becomes berserk!";
	
	{
		name = "gnoll brute";
		spriteClass = BruteSprite.class;
		
		HP = HT = 40;
		defenseSkill = 15;
		
		EXP = 8;
		maxLvl = 15;
		
		loot = Gold.class;
		lootChance = 0.5f;
	}
	
	private boolean berserk = false;
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		berserk = HP < HT / 4;
	}
	
	@Override
	public int damageRoll() {
		if (Dungeon.isChallenged( Challenges.STRONGER_MOBS)){
			return berserk ?
			Random.NormalIntRange( 12, 46 ) :	
			Random.NormalIntRange( 9, 21 );
		}
		return berserk ?
			Random.NormalIntRange( 10, 36 ) :	
			Random.NormalIntRange( 8, 18 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 20;
	}
	
	@Override
	public int dr() {
		return 8;
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		super.damage( dmg, src );
		
		if (isAlive() && !berserk && HP < HT / 4) {
			berserk = true;
			spend( TICK );
			if (Dungeon.visible[pos]) {
				GLog.w( TXT_BERSERK, name );
				sprite.showStatus( CharSprite.NEGATIVE, "berserk" );
			}
		}
	}
	
	@Override
	public String description() {
		return
			"Gnoll Brutes are the largest, strongest and toughest of all gnolls. Tasked with protecting their tribe, they strike at enemies with their immense might. " +
			"When severely wounded, they go berserk, inflicting even more damage to their enemies.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
