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
package com.retrodevxp.pixeldungeon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.retrodevxp.noosa.Game;
import com.retrodevxp.pixeldungeon.actors.hero.HeroClass;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Bundlable;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.SystemTime;

public enum Rankings {
	
	INSTANCE;
	
	public static final int TABLE_SIZE	= 6;
	
	public static final String RANKINGS_FILE = "rankings.dat";
	public static final String DETAILS_FILE = "game_%d.dat";
	
	public ArrayList<Record> records;
	public int lastRecord;
	public int totalNumber;
	public int wonNumber;
	
	public void submit( boolean win ) {
		
		load();
		
		Record rec = new Record();
		
		rec.info	= Dungeon.resultDescription;
		rec.win		= win;
		rec.heroClass	= Dungeon.hero.heroClass;
		rec.armorTier	= Dungeon.hero.tier();
		rec.score	= score( win );
		
		String gameFile = Utils.format( DETAILS_FILE, SystemTime.now );
		try {
			Dungeon.saveGame( gameFile );
			rec.gameFile = gameFile;
		} catch (IOException e) {
			rec.gameFile = "";
			System.out.println(e.toString());
		}
		
		try{
			records.add( rec );
		}
		catch(Exception e){
			System.out.println("Error occured while saving." + e.toString());
		}
		
		
		Collections.sort( records, scoreComparator );
		
		try{
		lastRecord = records.indexOf( rec );
		int size = records.size();
		if (size > TABLE_SIZE) {
			
			Record removedGame;
			if (lastRecord == size - 1) {
				removedGame = records.remove( size - 2 );
				lastRecord--;
			} else {
				removedGame = records.remove( size - 1 );
			}
			
			if (removedGame != null){
				if (removedGame.gameFile != null){
					if (removedGame.gameFile.length() > 0) {
						Game.instance.deleteFile( removedGame.gameFile );
					}
				}
			}
		}
		
		totalNumber++;
		if (win) {
			wonNumber++;
		}
		
		Badges.validateGamesPlayed();
	}
	catch(Exception e){

	}
		
		save();
	}
	
	private int score( boolean win ) {
		return (Statistics.goldCollected + Dungeon.hero.lvl * Statistics.deepestFloor * 100) * (win ? 2 : 1);
	}
	
	private static final String RECORDS	= "records";
	private static final String LATEST	= "latest";
	private static final String TOTAL	= "total";
	private static final String WON		= "won";
	
	public void save() {
		Bundle bundle = new Bundle();
		bundle.put( RECORDS, records );
		bundle.put( LATEST, lastRecord );
		bundle.put( TOTAL, totalNumber );
		bundle.put( WON, wonNumber );
		
		Game.instance.writeFile( RANKINGS_FILE, Bundle.write(bundle) );
	}
	
	public void load() {
		
		if (records != null) {
			return;
		}
		
		records = new ArrayList<Rankings.Record>();
		
		try {
			Bundle bundle = Bundle.read( Game.instance.readFile( RANKINGS_FILE ) );
			
			for (Bundlable record : bundle.getCollection( RECORDS )) {
				records.add( (Record)record );
			}			
			lastRecord = bundle.getInt( LATEST );
			
			totalNumber = bundle.getInt( TOTAL );
			if (totalNumber == 0) {
				totalNumber = records.size();
			}
			
			wonNumber = bundle.getInt( WON );
			if (wonNumber == 0) {
				for (Record rec : records) {
					if (rec.win) {
						wonNumber++;
					}
				}
			}
			
		} catch (Exception e) {
			System.out.println("Error loading : " + e.toString());
		}
	}
	
	public static class Record implements Bundlable {
		
		private static final String REASON	= "reason";
		private static final String WIN		= "win";
		private static final String SCORE	= "score";
		private static final String TIER	= "tier";
		private static final String GAME	= "gameFile";
		
		public String info;
		public boolean win;
		
		public HeroClass heroClass;
		public int armorTier;
		
		public int score;
		
		public String gameFile;
		
		@Override
		public void restoreFromBundle( Bundle bundle ) {
			
			info	= bundle.getString( REASON );
			win		= bundle.getBoolean( WIN );
			score	= bundle.getInt( SCORE );
			
			heroClass	= HeroClass.restoreInBundle( bundle );
			armorTier	= bundle.getInt( TIER );
			
			gameFile	= bundle.getString( GAME );
		}
		
		@Override
		public void storeInBundle( Bundle bundle ) {
			
			bundle.put( REASON, info );
			bundle.put( WIN, win );
			bundle.put( SCORE, score );
			
			heroClass.storeInBundle( bundle );
			bundle.put( TIER, armorTier );
			
			bundle.put( GAME, gameFile );
		}
	}

	private static final Comparator<Record> scoreComparator = new Comparator<Rankings.Record>() {
		@Override
		public int compare( Record lhs, Record rhs ) {
			try{
			return (int)Math.signum( rhs.score - lhs.score );
		}
		catch (Exception e){
			return 1;
		}
		}
	};
}
