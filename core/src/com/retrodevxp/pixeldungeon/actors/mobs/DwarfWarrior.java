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
import com.retrodevxp.pixeldungeon.actors.buffs.Amok;
import com.retrodevxp.pixeldungeon.actors.buffs.Bleeding;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Terror;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.Imp;
import com.retrodevxp.pixeldungeon.items.KindOfWeapon;
import com.retrodevxp.pixeldungeon.items.food.Food;
import com.retrodevxp.pixeldungeon.items.weapon.melee.BattleAxe;
import com.retrodevxp.pixeldungeon.items.weapon.melee.DwarvenAxe;
import com.retrodevxp.pixeldungeon.items.weapon.melee.Knuckles;
import com.retrodevxp.pixeldungeon.items.weapon.melee.SpikedKnuckles;
import com.retrodevxp.pixeldungeon.sprites.DwarfWarriorSprite;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.utils.Random;

public class DwarfWarrior extends Mob {

	public static final String TXT_DISARM	= "%s has disarmed the %s from your hands!";
	
	{
		name = "dwarf warrior";
		spriteClass = DwarfWarriorSprite.class;
		
		HP = HT = 90;
		defenseSkill = 25;
		
		EXP = 12;
		maxLvl = 21;
		
		loot = new DwarvenAxe();
		lootChance = 0.75f;
	}
	
	@Override
	public int damageRoll() {
		if (Dungeon.isChallenged( Challenges.STRONGER_MOBS)){
			return Random.NormalIntRange( 22, 39 );
		}
		return Random.NormalIntRange( 17, 37 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 30;
	}
	
	@Override
	protected float attackDelay() {
		return 1.5f;
	}
	
	@Override
	public int dr() {
		return 17;
	}
	
	@Override
	public String defenseVerb() {
		return "parried";
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		
		if (Random.Int( 6 ) == 0 && enemy == Dungeon.hero) {
			
			
			if (Random.Int( 2 ) == 0) {
			Buff.affect( enemy, Bleeding.class ).set( (int)(damage / 2) );
			}
			// Hero hero = Dungeon.hero;
			// KindOfWeapon weapon = hero.belongings.weapon;
			
			// if (weapon != null && !(weapon instanceof Knuckles) && !(weapon instanceof SpikedKnuckles) && !weapon.cursed) {
			// 	hero.belongings.weapon = null;
			// 	Dungeon.level.drop( weapon, hero.pos ).sprite.drop();
			// 	GLog.w( TXT_DISARM, name, weapon.name() );
			// }
		}
		
		return damage;
	}
	
	@Override
	public String description() {
		return
			"These dwarven warriors are the most experienced in combat among all dwarves. " +
			"When the metropolis was at its peak, these powerful soldiers protected the most crutial parts of their kingdom. " +
			"Their sturdy armor protects them while their heavy axes deals with intruders.";
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Terror.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
