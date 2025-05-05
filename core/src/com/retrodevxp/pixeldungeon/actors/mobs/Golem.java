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

import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Amok;
import com.retrodevxp.pixeldungeon.actors.buffs.Sleep;
import com.retrodevxp.pixeldungeon.actors.buffs.Terror;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.Imp;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.retrodevxp.pixeldungeon.sprites.GolemSprite;
import com.retrodevxp.utils.Random;

public class Golem extends Mob {
	
	{
		name = "golem";
		spriteClass = GolemSprite.class;
		
		HP = HT = 85;
		defenseSkill = 18;
		
		EXP = 12;
		maxLvl = 22;

		baseSpeed = 0.75f;
	}
	
	@Override
	public int damageRoll() {
		return Random.NormalIntRange( 25, 40 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 25;
	}
	
	@Override
	protected float attackDelay() {
		return 1.5f;
	}
	
	@Override
	public int dr() {
		return 12;
	}
	
	@Override
	public String defenseVerb() {
		return "blocked";
	}
	
	@Override
	public void die( Object cause ) {
		Imp.Quest.process( this );
		
		super.die( cause );
	}
	
	@Override
	public String description() {
		return
			"The Dwarves tried to combine their knowledge of mechanisms with their newfound power of elemental binding. " +
			"They fused magic into the mechanical bodies of golems, which were believed to be " +
			"most controllable of all. Despite this, the tiniest mistake in the ritual could cause an outbreak.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Terror.class );
		IMMUNITIES.add( Sleep.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
