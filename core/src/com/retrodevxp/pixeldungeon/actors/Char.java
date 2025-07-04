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
package com.retrodevxp.pixeldungeon.actors;

import java.util.HashSet;

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.retrodevxp.noosa.Camera;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.ResultDescriptions;
import com.retrodevxp.pixeldungeon.actors.buffs.Amok;
import com.retrodevxp.pixeldungeon.actors.buffs.Bleeding;
import com.retrodevxp.pixeldungeon.actors.buffs.Blindness;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Burning;
import com.retrodevxp.pixeldungeon.actors.buffs.Charm;
import com.retrodevxp.pixeldungeon.actors.buffs.Cripple;
import com.retrodevxp.pixeldungeon.actors.buffs.Frost;
import com.retrodevxp.pixeldungeon.actors.buffs.Invisibility;
import com.retrodevxp.pixeldungeon.actors.buffs.Levitation;
import com.retrodevxp.pixeldungeon.actors.buffs.Light;
import com.retrodevxp.pixeldungeon.actors.buffs.MindVision;
import com.retrodevxp.pixeldungeon.actors.buffs.Paralysis;
import com.retrodevxp.pixeldungeon.actors.buffs.Poison;
import com.retrodevxp.pixeldungeon.actors.buffs.Roots;
import com.retrodevxp.pixeldungeon.actors.buffs.Shadows;
import com.retrodevxp.pixeldungeon.actors.buffs.Sleep;
import com.retrodevxp.pixeldungeon.actors.buffs.Slow;
import com.retrodevxp.pixeldungeon.actors.buffs.Speed;
import com.retrodevxp.pixeldungeon.actors.buffs.Terror;
import com.retrodevxp.pixeldungeon.actors.buffs.Vertigo;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.actors.mobs.Bestiary;
import com.retrodevxp.pixeldungeon.actors.mobs.Mob;
import com.retrodevxp.pixeldungeon.effects.CellEmitter;
import com.retrodevxp.pixeldungeon.effects.particles.PoisonParticle;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Terrain;
import com.retrodevxp.pixeldungeon.levels.features.Door;
import com.retrodevxp.pixeldungeon.sprites.CharSprite;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Bundlable;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.GameMath;
import com.retrodevxp.utils.Random;

public abstract class Char extends Actor {

	protected static final String TXT_HIT		= "%s hit %s";
	protected static final String TXT_KILL		= "%s killed you...";
	protected static final String TXT_DEFEAT	= "%s defeated %s";
	
	private static final String TXT_YOU_MISSED	= "%s %s your attack";
	private static final String TXT_SMB_MISSED	= "%s %s %s's attack";
	
	private static final String TXT_OUT_OF_PARALYSIS	= "The pain snapped %s out of paralysis";
	
	public int pos = 0;
	
	public CharSprite sprite;
	
	public String name = "mob";
	
	public int HT;
	public int HP;
	
	protected float baseSpeed	= 1;
	
	public boolean blocking     = false;
	public boolean paralysed	= false;
	public boolean rooted		= false;
	public boolean flying		= false;
	public int invisible		= 0;
	
	public int viewDistance	= 8;
	
	private HashSet<Buff> buffs = new HashSet<Buff>();
	
	@Override
	protected boolean act() {
		Dungeon.level.updateFieldOfView( this );
		return false;
	}
	
	private static final String POS			= "pos";
	private static final String TAG_HP		= "HP";
	private static final String TAG_HT		= "HT";
	private static final String BUFFS		= "buffs";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		
		super.storeInBundle( bundle );
		
		bundle.put( POS, pos );
		bundle.put( TAG_HP, HP );
		bundle.put( TAG_HT, HT );
		bundle.put( BUFFS, buffs );
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		
		super.restoreFromBundle( bundle );
		
		pos = bundle.getInt( POS );
		HP = bundle.getInt( TAG_HP );
		HT = bundle.getInt( TAG_HT );
		
		for (Bundlable b : bundle.getCollection( BUFFS )) {
			if (b != null) {
				((Buff)b).attachTo( this );
			}
		}
	}
	
	public boolean attack( Char enemy ) {
		
		boolean visibleFight = Dungeon.visible[pos] || Dungeon.visible[enemy.pos];
		
		if (hit( this, enemy, false )) {
			
			if (visibleFight) {
				GLog.i( TXT_HIT, name, enemy.name );
			}
			
			// FIXME
			int dr = Random.IntRange( 0, enemy.dr() );
			if (this instanceof Hero && ((Hero)this).rangedWeapon != null && ((Hero)this).subClass == HeroSubClass.DEADEYE){
				dr = 0;
				enemy.sprite.showStatus(CharSprite.NEGATIVE, "Pierced");
			}
			
			int dmg = damageRoll();
			int effectiveDamage = Math.max( dmg - dr, 0 );
			
			effectiveDamage = attackProc( enemy, effectiveDamage );
			effectiveDamage = enemy.defenseProc( this, effectiveDamage );
			float multiplyDamage = effectiveDamage;
			if (enemy == Dungeon.hero) {
				if(Dungeon.hero.subClass == HeroSubClass.KNIGHT){
					if (multiplyDamage > enemy.HT / 3f) {
						multiplyDamage *= 0.75f;
						enemy.sprite.showStatus( CharSprite.NEUTRAL, "Shielded" );
					}
					else if (multiplyDamage > enemy.HT / 5f) {
						multiplyDamage *= 0.9f;
						enemy.sprite.showStatus( CharSprite.NEUTRAL, "Shielded" );
					}
				}
			}
			enemy.damage( (int)Math.round(multiplyDamage), this );
			
			if (visibleFight) {
				Sample.INSTANCE.play( Assets.SND_HIT, 1, 1, Random.Float( 0.8f, 1.25f ) );
			}

			if (enemy == Dungeon.hero) {
				Dungeon.hero.interrupt();
				if (effectiveDamage > enemy.HT / 4) {
					Camera.main.shake( GameMath.gate( 1, effectiveDamage / (enemy.HT / 4), 5), 0.3f );
				}
			}
			
			enemy.sprite.bloodBurstA( sprite.center(), effectiveDamage );
			enemy.sprite.flash();
			
			if (!enemy.isAlive() && visibleFight) {
				if (enemy == Dungeon.hero) {
					
					if (Dungeon.hero.killerGlyph != null) {
						
					// FIXME
					//	Dungeon.fail( Utils.format( ResultDescriptions.GLYPH, Dungeon.hero.killerGlyph.name(), Dungeon.depth ) );
					//	GLog.n( TXT_KILL, Dungeon.hero.killerGlyph.name() );
						
					} else {
						if (Bestiary.isBoss( this )) {
							Dungeon.fail( Utils.format( ResultDescriptions.BOSS, name, Dungeon.depth ) );
						} else {
							Dungeon.fail( Utils.format( ResultDescriptions.MOB, 
								Utils.indefinite( name ), Dungeon.depth ) );
						}
						
						GLog.n( TXT_KILL, name );
					}
					
				} else {
					GLog.i( TXT_DEFEAT, name, enemy.name );
				}
			}
			
			return true;
			
		} else {
			
			if (visibleFight) {
				String defense = enemy.defenseVerb();
				enemy.sprite.showStatus( CharSprite.NEUTRAL, defense );
				if (this == Dungeon.hero) {
					GLog.i( TXT_YOU_MISSED, enemy.name, defense );
				} else {
					GLog.i( TXT_SMB_MISSED, enemy.name, defense, name );
				}
				
				Sample.INSTANCE.play( Assets.SND_MISS );
			}
			
			return false;
			
		}
	}
	
	public static boolean hit( Char attacker, Char defender, boolean magic ) {
		float acuRoll = Random.Float( attacker.attackSkill( defender ) );
		float defRoll = Random.Float( defender.defenseSkill( attacker ) );
		acuRoll *= (Dungeon.hero.subClass == HeroSubClass.DEADEYE ? 1.05f : 1);
		acuRoll = (attacker.buff(Blindness.class) == null) ? acuRoll : acuRoll / 2;
		return (magic ? acuRoll * 2 : acuRoll) >= defRoll;
	}
	
	public int attackSkill( Char target ) {
		return 0;
	}
	
	public int defenseSkill( Char enemy ) {
		return 0;
	}
	
	public String defenseVerb() {
		return "dodged";
	}
	
	public int dr() {
		return 0;
	}
	
	public int damageRoll() {
		return 1;
	}
	
	public int attackProc( Char enemy, int damage ) {
		return damage;
	}
	
	public int defenseProc( Char enemy, int damage ) {
		return damage;
	}
	
	public float speed() {
		return buff( Cripple.class ) == null ? baseSpeed : baseSpeed * 0.5f;
	}
	
	public void damage( int dmg, Object src ) {
		
		if (HP <= 0) {
			return;
		}
		
		Buff.detach( this, Frost.class );
		
		Class<?> srcClass = src.getClass();
		if (immunities().contains( srcClass )) {
			dmg = 0;
		} else if (resistances().contains( srcClass )) {
			dmg = Random.IntRange( 0, dmg );
		}
		
		if (buff( Paralysis.class ) != null) {
			if (Random.Int( dmg ) >= Random.Int( HP )) {
				Buff.detach( this, Paralysis.class );
				if (Dungeon.visible[pos]) {
					GLog.i( TXT_OUT_OF_PARALYSIS, name );
				}
			}
		}
		
		HP -= dmg;
		if (dmg > 0 || src instanceof Char) {
			sprite.showStatus( HP > HT / 2 ? 
				CharSprite.WARNING : 
				CharSprite.NEGATIVE,
				Integer.toString( dmg ) );
		}
		if (HP <= 0) {
			die( src );
		}
	}
	
	public void destroy() {
		HP = 0;
		Actor.remove( this );
		Actor.freeCell( pos );
	}
	
	public void die( Object src ) {
		destroy();
		sprite.die();
	}
	
	public boolean isAlive() {
		return HP > 0;
	}
	
	@Override
	protected void spend( float time ) {
		float timeScale = 1f;
		Slow s = buff( Slow.class );
		if (s != null) {
			timeScale *= s.timeScale();
		}
		if (buff( Speed.class ) != null) {
			timeScale *= 2.0f;
		}
		
		super.spend( time / timeScale );

		try{
			if (HP <= 0) {
				die( null );
			}
		}
			catch(Exception e){
				
			
		}
	}
	
	public HashSet<Buff> buffs() {
		return buffs;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Buff> HashSet<T> buffs( Class<T> c ) {
		HashSet<T> filtered = new HashSet<T>();
		for (Buff b : buffs) {
			if (ClassReflection.isInstance( c, b )) {
				filtered.add( (T)b );
			}
		}
		return filtered;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Buff> T buff( Class<T> c ) {
		for (Buff b : buffs) {
			if (ClassReflection.isInstance( c, b )) {
				return (T)b;
			}
		}
		return null;
	}
	
	public boolean isCharmedBy( Char ch ) {
		int chID = ch.id();
		for (Buff b : buffs) {
			if (b instanceof Charm && ((Charm)b).object == chID) {
				return true;
			}
		}
		return false;
	}
	
	public void add( Buff buff ) {
		
		buffs.add( buff );
		Actor.add( buff );
		
		if (sprite != null) {
			if (buff instanceof Poison) {
				
				CellEmitter.center( pos ).burst( PoisonParticle.SPLASH, 5 );
				sprite.showStatus( CharSprite.NEGATIVE, "poisoned" );
				
			} else if (buff instanceof Amok) {
				
				sprite.showStatus( CharSprite.NEGATIVE, "amok" );

			} else if (buff instanceof Blindness) {
				
				sprite.showStatus( CharSprite.NEGATIVE, "blinded" );

			} else if (buff instanceof Slow) {

				sprite.showStatus( CharSprite.NEGATIVE, "slowed" );
				
			} else if (buff instanceof MindVision) {
				
				sprite.showStatus( CharSprite.POSITIVE, "mind" );
				sprite.showStatus( CharSprite.POSITIVE, "vision" );
				
			} else if (buff instanceof Paralysis) {

				sprite.add( CharSprite.State.PARALYSED );
				sprite.showStatus( CharSprite.NEGATIVE, "paralysed" );
				
			} else if (buff instanceof Terror) {
				
				sprite.showStatus( CharSprite.NEGATIVE, "frightened" );
				
			} else if (buff instanceof Roots) {
				
				sprite.showStatus( CharSprite.NEGATIVE, "rooted" );
				
			} else if (buff instanceof Cripple) {

				sprite.showStatus( CharSprite.NEGATIVE, "crippled" );
				
			} else if (buff instanceof Bleeding) {

				sprite.showStatus( CharSprite.NEGATIVE, "bleeding" );
				
			} else if (buff instanceof Vertigo) {

				sprite.showStatus( CharSprite.NEGATIVE, "dizzy" );
				
			} else if (buff instanceof Sleep) {
				sprite.idle();
			}
			
			  else if (buff instanceof Burning) {
				sprite.add( CharSprite.State.BURNING );
			} else if (buff instanceof Levitation) {
				sprite.add( CharSprite.State.LEVITATING );
			} else if (buff instanceof Frost) {
				sprite.add( CharSprite.State.FROZEN );
			} else if (buff instanceof Invisibility) {
				if (!(buff instanceof Shadows)) {
					sprite.showStatus( CharSprite.POSITIVE, "invisible" );
				}
				sprite.add( CharSprite.State.INVISIBLE );
			}
		}
	}
	
	public void remove( Buff buff ) {
		
		buffs.remove( buff );
		Actor.remove( buff );
		
		if (buff instanceof Burning) {
			sprite.remove( CharSprite.State.BURNING );
		} else if (buff instanceof Levitation) {
			sprite.remove( CharSprite.State.LEVITATING );
		} else if (buff instanceof Invisibility && invisible <= 0) {
			sprite.remove( CharSprite.State.INVISIBLE );
		} else if (buff instanceof Paralysis) {
			sprite.remove( CharSprite.State.PARALYSED );
		} else if (buff instanceof Frost) {
			sprite.remove( CharSprite.State.FROZEN );
		} 
	}
	
	public void remove( Class<? extends Buff> buffClass ) {
		for (Buff buff : buffs( buffClass )) {
			remove( buff );
		}
	}
	
	
	
	@Override
	protected void onRemove() {
		for (Buff buff : buffs.toArray( new Buff[0] )) {
			buff.detach();
		}
	}
	
	public void updateSpriteState() {
		for (Buff buff:buffs) {
			if (buff instanceof Burning) {
				sprite.add( CharSprite.State.BURNING );
			} else if (buff instanceof Levitation) {
				sprite.add( CharSprite.State.LEVITATING );
			} else if (buff instanceof Invisibility) {
				sprite.add( CharSprite.State.INVISIBLE );
			} else if (buff instanceof Paralysis) {
				sprite.add( CharSprite.State.PARALYSED );
			} else if (buff instanceof Frost) {
				sprite.add( CharSprite.State.FROZEN );
			} else if (buff instanceof Light) {
				sprite.add( CharSprite.State.ILLUMINATED );
			}
		}
	}
	
	public int stealth() {
		return 0;
	}
	
	public void move( int step ) {
		
		if (Level.adjacent( step, pos ) && buff( Vertigo.class ) != null) {
			step = pos + Level.NEIGHBOURS8[Random.Int( 8 )];
			if (!(Level.passable[step] || Level.avoid[step]) || Actor.findChar( step ) != null) {
				return;
			}
		}
		
		if (Dungeon.level.map[pos] == Terrain.OPEN_DOOR) {
			// System.out.println("Open door" + "!");
			//FIXED: Crawler gets invisibility when anything is on an open door, not just himself!
			Door.leave( pos );
			int countcrawl = 0;
			try{
				if(this instanceof Hero){
				if (Dungeon.hero.subClass == HeroSubClass.CRAWLER){
					for (Mob mob : Dungeon.level.mobs) {
						if (Level.fieldOfView[mob.pos]) {
							countcrawl += 1;
						}
					}
					if (countcrawl == 0){
						// if (Dungeon.hero.buffs(Invisibility.class) == null){
						Buff.affect( Dungeon.hero, Invisibility.class, 5 );
						// }
					}
				}
			}
		}
			catch(Exception e){
				
			}
		}
		
		pos = step;
		
		if (flying && Dungeon.level.map[pos] == Terrain.DOOR) {
			Door.enter( pos );
		}
		
		if (this != Dungeon.hero) {
			sprite.visible = Dungeon.visible[pos];
		}
	}
	
	public int distance( Char other ) {
		return Level.distance( pos, other.pos );
	}
	
	public void onMotionComplete() {
		next();
	}
	
	public void onAttackComplete() {
		next();
	}
	
	public void onOperateComplete() {
		next();
	}
	
	private static final HashSet<Class<?>> EMPTY = new HashSet<Class<?>>();
	
	public HashSet<Class<?>> resistances() {
		return EMPTY;
	}
	
	public HashSet<Class<?>> immunities() {
		return EMPTY;
	}
}
