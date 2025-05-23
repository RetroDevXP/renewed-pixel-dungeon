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

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.Journal;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.mobs.Golem;
import com.retrodevxp.pixeldungeon.actors.mobs.Mob;
import com.retrodevxp.pixeldungeon.actors.mobs.Monk;
import com.retrodevxp.pixeldungeon.items.Generator;
import com.retrodevxp.pixeldungeon.items.quest.DwarfToken;
import com.retrodevxp.pixeldungeon.items.rings.Ring;
import com.retrodevxp.pixeldungeon.levels.CityLevel;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.Room;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.ImpSprite;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.pixeldungeon.windows.WndImp;
import com.retrodevxp.pixeldungeon.windows.WndQuest;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.Random;

public class Imp extends NPC {

	{
		name = "ambitious imp";
		spriteClass = ImpSprite.class;
	}
	
	private static final String TXT_GOLEMS1	=
		"Oh, are you an adventurer? Care to help me? For a bounty, of course ;)\n " +
		"You see, I started a little business here, but these wandering lumps of granite keeps trampling my shop! \n" +
		"These stupid golems are horrible for business! They don't buy anything either! \n" +
		"Some of them needs to be killed before I re-open my shop. \n" +
		"So please, kill... about _6 of them_ and a reward is yours.";
	
	private static final String TXT_MONKS1	=
		"Oh, are you an adventurer? Care to help me? For a bounty, of course ;)\n " +
		"You see, I started a little business here, but these thugs don't buy anything themselves. \n" +
		"Instead, they sometimes even take my stuff by force. \n" +
		"These stupid thugs are horrible for business! \n" +
		"Some of them needs to be killed before I re-open my shop. \n" +
		"So please, kill... about _8 of them_ and a reward is yours.";
	
	private static final String TXT_GOLEMS2	=
		"How is your golem safari going?";	
	
	private static final String TXT_MONKS2	=
		"Oh, you are still alive! I knew that your kung-fu is stronger ;) " +
		"Just don't forget to grab these thugs' tokens.";	
	
	private static final String TXT_CYA	= "See you, %s!";
	private static final String TXT_HEY	= "Psst, %s!";
	
	private boolean seenBefore = false;
	
	@Override
	protected boolean act() {
		
		if (!Quest.given && Dungeon.visible[pos]) {
			if (!seenBefore) {
				yell( Utils.format( TXT_HEY, Dungeon.hero.className() ) );
			}
			seenBefore = true;
		} else {
			seenBefore = false;
		}
		
		throwItem();
		
		return super.act();
	}
	
	@Override
	public int defenseSkill( Char enemy ) {
		return 1000;
	}
	
	@Override
	public String defenseVerb() {
		return "evaded";
	}
	
	@Override
	public void damage( int dmg, Object src ) {
	}
	
	@Override
	public void add( Buff buff ) {
	}
	
	@Override
	public boolean reset() {
		return true;
	}
	
	@Override
	public void interact() {
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		if (Quest.given) {
			
			DwarfToken tokens = Dungeon.hero.belongings.getItem( DwarfToken.class );
			if (tokens != null && (tokens.quantity() >= 8 || (!Quest.alternative && tokens.quantity() >= 6))) {
				GameScene.show( new WndImp( this, tokens ) );
			} else {
				tell( Quest.alternative ? TXT_MONKS2 : TXT_GOLEMS2, Dungeon.hero.className() );
			}
			
		} else {
			tell( Quest.alternative ? TXT_MONKS1 : TXT_GOLEMS1 );
			Quest.given = true;
			Quest.completed = false;
			
			Journal.add( Journal.Feature.IMP );
		}
	}
	
	private void tell( String format, Object...args ) {
		GameScene.show( 
			new WndQuest( this, Utils.format( format, args ) ) );
	}
	
	public void flee() {
		
		yell( Utils.format( TXT_CYA, Dungeon.hero.className() ) );
		
		destroy();
		sprite.die();
	}
	
	@Override
	public String description() {
		return 
			"Imps are lesser demons. They are notable for neither their strength nor their magic talent, " +
			"but they are quite smart and sociable. Many imps prefer to live among non-demons.";
	}
	
	public static class Quest {
		
		private static boolean alternative;
		
		private static boolean spawned;
		private static boolean given;
		private static boolean completed;
		
		public static Ring reward;
		
		public static void reset() {
			spawned = false;

			reward = null;
		}
		
		private static final String NODE		= "demon";
		
		private static final String ALTERNATIVE	= "alternative";
		private static final String SPAWNED		= "spawned";
		private static final String GIVEN		= "given";
		private static final String COMPLETED	= "completed";
		private static final String REWARD		= "reward";
		
		public static void storeInBundle( Bundle bundle ) {
			
			Bundle node = new Bundle();
			
			node.put( SPAWNED, spawned );
			
			if (spawned) {
				node.put( ALTERNATIVE, alternative );
				
				node.put( GIVEN, given );
				node.put( COMPLETED, completed );
				node.put( REWARD, reward );
			}
			
			bundle.put( NODE, node );
		}
		
		public static void restoreFromBundle( Bundle bundle ) {

			Bundle node = bundle.getBundle( NODE );
			
			if (!node.isNull() && (spawned = node.getBoolean( SPAWNED ))) {
				alternative	= node.getBoolean( ALTERNATIVE );
				
				given = node.getBoolean( GIVEN );
				completed = node.getBoolean( COMPLETED );
				reward = (Ring)node.get( REWARD );
			}
		}
		
		public static void spawn( CityLevel level, Room room ) {
			if (!spawned && Dungeon.depth > 16 && Random.Int( 20 - Dungeon.depth ) == 0) {
				
				Imp npc = new Imp();
				do {
					npc.pos = level.randomRespawnCell();
				} while (npc.pos == -1 || level.heaps.get( npc.pos ) != null);
				level.mobs.add( npc );
				Actor.occupyCell( npc );
				try{
					if (Level.water[npc.pos]){
						npc.name = "amphibious imp";
					}
				}
				catch(Exception ex){
					
				}
				spawned = true;	
				alternative = Random.Int( 2 ) == 0;
				
				given = false;
				
				do {
					reward = (Ring)Generator.random( Generator.Category.RING );
				} while (reward.cursed);
				reward.upgrade( 2 );
				reward.cursed = true;
			}
		}
		
		public static void process( Mob mob ) {
			if (spawned && given && !completed) {
				if ((alternative && mob instanceof Monk) ||
					(!alternative && mob instanceof Golem)) {
					
					Dungeon.level.drop( new DwarfToken(), mob.pos ).sprite.drop();
				}
			}
		}
		
		public static void complete() {
			reward = null;
			completed = true;
			
			Journal.remove( Journal.Feature.IMP );
		}
		
		public static boolean isCompleted() {
			return completed;
		}
	}
}
