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

import java.util.ArrayList;
import java.util.HashSet;

import com.retrodevxp.noosa.Camera;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.ResultDescriptions;
import com.retrodevxp.pixeldungeon.Statistics;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.blobs.Blob;
import com.retrodevxp.pixeldungeon.actors.blobs.Fire;
import com.retrodevxp.pixeldungeon.actors.blobs.ToxicGas;
import com.retrodevxp.pixeldungeon.actors.buffs.Amok;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Burning;
import com.retrodevxp.pixeldungeon.actors.buffs.Charm;
import com.retrodevxp.pixeldungeon.actors.buffs.Ooze;
import com.retrodevxp.pixeldungeon.actors.buffs.Poison;
import com.retrodevxp.pixeldungeon.actors.buffs.Sleep;
import com.retrodevxp.pixeldungeon.actors.buffs.Terror;
import com.retrodevxp.pixeldungeon.actors.buffs.Vertigo;
import com.retrodevxp.pixeldungeon.effects.Pushing;
import com.retrodevxp.pixeldungeon.effects.particles.ShadowParticle;
import com.retrodevxp.pixeldungeon.items.keys.SkeletonKey;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfPsionicBlast;
import com.retrodevxp.pixeldungeon.items.weapon.enchantments.Death;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.mechanics.Ballistica;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.BurningFistSprite;
import com.retrodevxp.pixeldungeon.sprites.CharSprite;
import com.retrodevxp.pixeldungeon.sprites.LarvaSprite;
import com.retrodevxp.pixeldungeon.sprites.RottingFistSprite;
import com.retrodevxp.pixeldungeon.sprites.YogSprite;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Random;

public class Yog extends Mob {
	
	{
		name = Dungeon.depth == Statistics.deepestFloor ? "Yog-Dzewa" : "echo of Yog-Dzewa";
		spriteClass = YogSprite.class;
		
		HP = HT = 300;
		
		EXP = 50;
		
		state = PASSIVE;
	}
	
	private static final String TXT_DESC =
		"Yog-Dzewa is an old demon god, who once controlled a vast army of demons. " +
		"A century ago, Yog-Dzewa, along with its army of demons, tried to take over the Dwarven Metropolis. " +
		"After a costly war, the Dwarves eradicated the majority of the demons from most parts of their city. " +
		"The rest of the demons resided in the lower parts of the metropolis, eventually controlling it entirely. The demon god itself, mostly vaporized, was " +
		"imprisoned in the deepest part of the halls below the dwarven city. The dwarves believed it to be too weak to rise ever again...";	
	
	private static int fistsCount = 0;
	
	public Yog() {
		super();
	}
	
	public void spawnFists() {
		RottingFist fist1 = new RottingFist();
		BurningFist fist2 = new BurningFist();
		
		do {
			fist1.pos = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
			fist2.pos = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
		} while (!Level.passable[fist1.pos] || !Level.passable[fist2.pos] || fist1.pos == fist2.pos);
		
		GameScene.add( fist1 );
		GameScene.add( fist2 );
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		
		if (fistsCount > 0) {
			
			for (Mob mob : Dungeon.level.mobs) {
				if (mob instanceof BurningFist || mob instanceof RottingFist) {
					mob.beckon( pos );
				}
			}
			
			dmg >>= fistsCount;
		}
		
		super.damage( dmg, src );
	}
	
	@Override
	public int defenseProc( Char enemy, int damage ) {

		ArrayList<Integer> spawnPoints = new ArrayList<Integer>();
		
		for (int i=0; i < Level.NEIGHBOURS8.length; i++) {
			int p = pos + Level.NEIGHBOURS8[i];
			if (Actor.findChar( p ) == null && (Level.passable[p] || Level.avoid[p])) {
				spawnPoints.add( p );
			}
		}
		
		if (spawnPoints.size() > 0) {
			Larva larva = new Larva();
			larva.pos = Random.element( spawnPoints );
			
			GameScene.add( larva );
			Actor.addDelayed( new Pushing( larva, pos, larva.pos ), -1 );
		}

		return super.defenseProc(enemy, damage);
	}
	
	@Override
	public void beckon( int cell ) {
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void die( Object cause ) {

		for (Mob mob : (Iterable<Mob>)Dungeon.level.mobs.clone()) {
			if (mob instanceof BurningFist || mob instanceof RottingFist) {
				mob.die( cause );
			}
		}
		this.sprite.emitter().burst( ShadowParticle.UP, 25 );
		GameScene.bossSlain();
		Dungeon.level.drop( new SkeletonKey(), pos ).sprite.drop();
		super.die( cause );
		Camera.main.shake( 1, 2.5f );
		yell( "..." );
	}
	
	@Override
	public void notice() {
		super.notice();
		yell( "Hope is an illusion..." );
	}
	
	@Override
	public String description() {
		return TXT_DESC;
			
	}
	
	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		
		IMMUNITIES.add( Death.class );
		IMMUNITIES.add( Terror.class );
		IMMUNITIES.add( Amok.class );
		IMMUNITIES.add( Charm.class );
		IMMUNITIES.add( Sleep.class );
		IMMUNITIES.add( Burning.class );
		IMMUNITIES.add( ToxicGas.class );
		IMMUNITIES.add( ScrollOfPsionicBlast.class );
	}
	
	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
	
	public static class RottingFist extends Mob {
	
		private static final int REGENERATION	= 4;
		
		{
			name = "rotting fist";
			spriteClass = RottingFistSprite.class;
			
			HP = HT = 280;
			defenseSkill = 25;
			
			EXP = 0;
			
			state = WANDERING;
		}
		
		public RottingFist() {
			super();
			fistsCount++;
		}
		
		@Override
		public void die( Object cause ) {
			super.die( cause );
			fistsCount--;
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 36;
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 24, 36 );
		}
		
		@Override
		public int dr() {
			return 15;
		}
		
		@Override
		public int attackProc( Char enemy, int damage ) {
			if (Random.Int( 3 ) == 0) {
				Buff.affect( enemy, Ooze.class );
				enemy.sprite.burst( 0xFF000000, 5 );
			}
			
			return damage;
		}
		
		@Override
		public boolean act() {
			
			if (Level.water[pos] && HP < HT) {
				sprite.emitter().burst( ShadowParticle.UP, 2 );
				HP += REGENERATION;
			}
			
			return super.act();
		}
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}
		
		private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
		static {
			RESISTANCES.add( ToxicGas.class );
			RESISTANCES.add( Death.class );
			RESISTANCES.add( ScrollOfPsionicBlast.class );
		}
		
		@Override
		public HashSet<Class<?>> resistances() {
			return RESISTANCES;
		}
		
		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( Amok.class );
			IMMUNITIES.add( Sleep.class );
			IMMUNITIES.add( Terror.class );
			IMMUNITIES.add( Poison.class );
			IMMUNITIES.add( Vertigo.class );
		}
		
		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}
	
	public static class BurningFist extends Mob {
		
		{
			name = "burning fist";
			spriteClass = BurningFistSprite.class;
			
			HP = HT = 185;
			defenseSkill = 25;
			
			EXP = 0;
			
			state = WANDERING;
		}
		
		public BurningFist() {
			super();
			fistsCount++;
		}
		
		@Override
		public void die( Object cause ) {
			super.die( cause );
			fistsCount--;
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 36;
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 20, 32 );
		}
		
		@Override
		public int dr() {
			return 15;
		}
		
		@Override
		protected boolean canAttack( Char enemy ) {
			return Ballistica.cast( pos, enemy.pos, false, true ) == enemy.pos;
		}
		
		@Override
		public boolean attack( Char enemy ) {
			
			if (!Level.adjacent( pos, enemy.pos )) {
				spend( attackDelay() );
				
				if (hit( this, enemy, true )) {
					
					int dmg =  damageRoll();
					enemy.damage( dmg, this );
					
					enemy.sprite.bloodBurstA( sprite.center(), dmg );
					enemy.sprite.flash();
					
					if (!enemy.isAlive() && enemy == Dungeon.hero) {
						Dungeon.fail( Utils.format( ResultDescriptions.BOSS, name, Dungeon.depth ) );
						GLog.n( TXT_KILL, name );
					}
					return true;
					
				} else {
					
					enemy.sprite.showStatus( CharSprite.NEUTRAL,  enemy.defenseVerb() );
					return false;
				}
			} else {
				return super.attack( enemy );
			}
		}
		
		@Override
		public boolean act() {
			for (int i=0; i < Level.NEIGHBOURS9.length; i++) {
				GameScene.add( Blob.seed( pos + Level.NEIGHBOURS9[i], 2, Fire.class ) );
			}
			
			return super.act();
		}
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}
		
		private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();
		static {
			RESISTANCES.add( ToxicGas.class );
			RESISTANCES.add( Death.class );
			RESISTANCES.add( ScrollOfPsionicBlast.class );
		}
		
		@Override
		public HashSet<Class<?>> resistances() {
			return RESISTANCES;
		}
		
		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
		static {
			IMMUNITIES.add( Amok.class );
			IMMUNITIES.add( Sleep.class );
			IMMUNITIES.add( Terror.class );
			IMMUNITIES.add( Burning.class );
		}
		
		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}
	
	public static class Larva extends Mob {
		
		{
			name = "god's larva";
			spriteClass = LarvaSprite.class;
			
			HP = HT = 25;
			defenseSkill = 20;
			
			EXP = 0;
			
			state = HUNTING;
		}
		
		@Override
		public int attackSkill( Char target ) {
			return 30;
		}
		
		@Override
		public int damageRoll() {
			return Random.NormalIntRange( 15, 20 );
		}
		
		@Override
		public int dr() {
			return 8;
		}
		
		@Override
		public String description() {
			return TXT_DESC;
				
		}
	}
}
