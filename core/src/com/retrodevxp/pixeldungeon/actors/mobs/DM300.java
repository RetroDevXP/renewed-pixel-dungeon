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

import com.retrodevxp.noosa.Camera;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.Challenges;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.Statistics;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.blobs.Blob;
import com.retrodevxp.pixeldungeon.actors.blobs.ToxicGas;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Burning;
import com.retrodevxp.pixeldungeon.actors.buffs.Frost;
import com.retrodevxp.pixeldungeon.actors.buffs.Paralysis;
import com.retrodevxp.pixeldungeon.actors.buffs.Poison;
import com.retrodevxp.pixeldungeon.effects.CellEmitter;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.effects.particles.ElmoParticle;
import com.retrodevxp.pixeldungeon.items.keys.SkeletonKey;
import com.retrodevxp.pixeldungeon.items.rings.RingOfThorns;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.retrodevxp.pixeldungeon.items.weapon.enchantments.Death;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Terrain;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.DM300Sprite;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.utils.Random;

public class DM300 extends Mob {
	
	{
		name = Dungeon.depth == Statistics.deepestFloor ? "DM-300" : "DM-350";
		spriteClass = DM300Sprite.class;
		
		HP = HT = 200;
		EXP = 30;
		defenseSkill = 18;
		
		loot = new RingOfThorns().random();
		lootChance = 0.333f;
	}
	
	@Override
	public int damageRoll() {
		if (Dungeon.isChallenged( Challenges.STRONGER_MOBS)){
			return Random.NormalIntRange( 16, 24 );
		}
		return Random.NormalIntRange( 15, 23 );
	}
	
	@Override
	public int attackSkill( Char target ) {
		return 28;
	}
	
	@Override
	public int dr() {
		return 10;
	}
	
	@Override
	public boolean act() {
		GameScene.add( Blob.seed( pos, 30, ToxicGas.class ) );
		return super.act();
	}
	
	@Override
	public void move( int step ) {
		super.move( step );
		
		if (Dungeon.level.map[step] == Terrain.INACTIVE_TRAP && HP < HT) {
			
			HP += Random.Int( 1, Math.min(10, HT-HP) );
			if (HP > HT){
				HP = HT;
			}
			sprite.emitter().burst( ElmoParticle.FACTORY, 5 );
			
			if (Dungeon.visible[step] && Dungeon.hero.isAlive()) {
				GLog.n( "DM-300 repairs itself!" );
				// GLog.n( "HP: " + HP + "/" + HT );
			}
		}

		int[] cells = {
			step-1, step+1, step-Level.WIDTH, step+Level.WIDTH, 
			step-1-Level.WIDTH, 
			step-1+Level.WIDTH, 
			step+1-Level.WIDTH, 
			step+1+Level.WIDTH
		};
		int cell = cells[Random.Int( cells.length )];
		
		if (Dungeon.visible[cell]) {
			CellEmitter.get( cell ).start( Speck.factory( Speck.ROCK ), 0.07f, 10 );
			Camera.main.shake( 3, 0.7f );
			Sample.INSTANCE.play( Assets.SND_ROCKS );
			
			if (Level.water[cell]) {
				GameScene.ripple( cell );
			} else if (Dungeon.level.map[cell] == Terrain.EMPTY) {
				Level.set( cell, Terrain.EMPTY_DECO );
				GameScene.updateMap( cell );
			}
		}

		Char ch = Actor.findChar( cell );
		if (ch != null && ch != this) {
			Buff.prolong( ch, Paralysis.class, 2 );
		}
	}
	
	@Override
	public void die( Object cause ) {
		
		super.die( cause );
		
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		
		Badges.validateBossSlain();
		
		yell( "Mission failed. Shutting down." );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "Unauthorized personnel detected. Activating defense protocol." );
	}
	
	@Override
	public String description() {
		return
			"The last known remaining machine of its kind. The DM-300 is one of the numerous machines created by the Dwarves centuries ago. " +
			"Later, Dwarves started to replace machines with golems, elementals and even demons. Eventually it contributed to the decline of their civilization. " +
			"These dwarven machines were typically used for construction and mining, and in some cases, for city defense.";
	}
	
	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
	static {
		RESISTANCES.add( Death.class );
		RESISTANCES.add( ScrollOfPsionicBlast.class );
	}

	@Override
	public void add( Buff buff ) {
		if (buff instanceof Burning) {
			damage( Random.NormalIntRange( 1, HT * 1 / 15 ), buff );
		} else {
			if (buff instanceof Frost) {
				damage( Random.NormalIntRange( 1, HT * 1 / 30 ), buff );
			}
			super.add( buff );
		}
	}
	
	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( Poison.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
