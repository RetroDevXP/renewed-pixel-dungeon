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
package com.retrodevxp.pixeldungeon.actors.mobs.npcs;

import java.util.HashSet;

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Poison;
import com.retrodevxp.pixeldungeon.actors.mobs.Mob;
import com.retrodevxp.pixeldungeon.effects.particles.ElmoParticle;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.sprites.SpiritWolfSprite;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.Random;

public class SpiritWolf extends NPC {
	
	{
		name = "spirit wolf";
		spriteClass = SpiritWolfSprite.class;
		
		viewDistance = 5;
		
		WANDERING = new Wandering();
		
		flying = true;
		state = WANDERING;
	}

	private int level;
	
	private static final String LEVEL	= "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		spawn( bundle.getInt( LEVEL ) );
	}
	
	public void spawn( int level ) {
		this.level = level;
		
		HT = 5 + (2 + level) * 3;
		defenseSkill = 3 + level;
	}
	
	@Override
	public int attackSkill( Char target ) {
		return defenseSkill;
	}
	
	@Override
	public int damageRoll() {
		return (int) Random.Float( 2 + (0.75f * level) + (HP / 20f), 5 + (1.5f * level) + (HP / 5f) );
		// return Random.NormalIntRange( 2 + level + (int)(HP / 30), 5 + (2 * level) + (int)(HP / 10) );
	}
	
	@Override
	public int attackProc( Char enemy, int damage ) {
		if (enemy instanceof Mob) {
			// ((Mob)enemy).aggro( this );
			if (Level.adjacent(Dungeon.hero.pos, enemy.pos)){
				if ( Random.IntRange( 1, 5 ) == 1){
					((Mob)enemy).aggro( this );
				}
			}
			else{
				((Mob)enemy).aggro( this );
			}
			//If the enemy is adjacent to the hero, Spirit Wolf only sometimes draw aggro from that enemy, otherwise it protects the hero too well.
			//If the hero isn't adjacent to the enemy, however, the enemy would aggro the Spirit Wolf.
		}
		enemy.sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
		return damage;
	}
	
	@Override
	protected boolean act() {
		HP--;
		if (HP <= 0) {
			die( null );
			return true;
		} else {
			return super.act();
		}
	}
	
	protected Char chooseEnemy() {
		
		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob:Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos]) {
					enemies.add( mob );
				}
			}
			
			return enemies.size() > 0 ? Random.element( enemies ) : null;
			
		} else {
			
			return enemy;
			
		}
	}
	
	@Override
	public String description() {
		return
			"Called by magic, these spirit wolves are not as large as actual wolves. " +
			"Despite this, they guard their master fiercely till the condensation of magic becomes too weakened for them to function.";
	}

	@Override
	public void interact() {
		
		int curPos = pos;
		
		moveSprite( pos, Dungeon.hero.pos );
		move( Dungeon.hero.pos );
		
		Dungeon.hero.sprite.move( Dungeon.hero.pos, curPos );
		Dungeon.hero.move( curPos );
		
		Dungeon.hero.spend( 1 / Dungeon.hero.speed() );
		Dungeon.hero.busy();
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( Poison.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public class Wandering implements AiState {

		@Override
		public boolean act( boolean enemyInFOV, boolean justAlerted ) {
			if (enemyInFOV) {
				
				enemySeen = true;
				
				notice();
				state = HUNTING;
				target = enemy.pos;
				
			} else {
				
				enemySeen = false;
				
				int oldPos = pos;
				if (getCloser( Dungeon.hero.pos )) {
					spend( 1 / speed() );
					return moveSprite( oldPos, pos );
				} else {
					spend( TICK );
				}
				
			}
			return true;
		}
		
		@Override
		public String status() {
			return Utils.format( "This %s is wandering", name );
		}
	}
}