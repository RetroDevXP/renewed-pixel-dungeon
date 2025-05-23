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

import com.retrodevxp.noosa.Game;
import com.retrodevxp.pixeldungeon.items.Gold;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.rings.Ring;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.Random;

public class Bones {

	private static final String BONES_FILE	= "bones.dat";
	
	private static final String LEVEL	= "level";
	private static final String ITEM	= "item";
	
	private static int depth = -1;
	private static Item item;
	
	public static void leave() {
		
		item = null;
		switch (Random.Int( 4 )) {
		case 0:
			item = Dungeon.hero.belongings.weapon;
			break;
		case 1:
			item = Dungeon.hero.belongings.armor;
			break;
		case 2:
			item = Dungeon.hero.belongings.ring1;
			break;
		case 3:
			item = Dungeon.hero.belongings.ring2;
			break;
		}
		if (item == null) {
			if (Dungeon.gold > 0) {
				item = new Gold( Random.IntRange( 1, Dungeon.gold ) );
			} else {
				item = new Gold( 1 );
			}
		}
		
		depth = Dungeon.depth;
		
		Bundle bundle = new Bundle();
		bundle.put( LEVEL, depth );
		bundle.put( ITEM, item );
		
		Game.instance.writeFile( BONES_FILE, Bundle.write(bundle) );
	}
	
	public static Item get() {
		if (depth == -1) {
			
			try {
				Bundle bundle = Bundle.read( Game.instance.readFile( BONES_FILE ) );
				
				depth = bundle.getInt( LEVEL );
				item = (Item)bundle.get( ITEM );
				
				return get();
				
			} catch (IOException e) {
				return null;
			}
			
		} else {
			try{
			if (depth == Dungeon.depth) {
				Game.instance.deleteFile( BONES_FILE );
				depth = 0;
				if(item == null){
					return null;
				}
				
				if (!item.stackable) {
					item.cursed = true;
					item.cursedKnown = true;
					if (item.isUpgradable()) {
						int lvl = (Dungeon.depth - 1) * 3 / 5 + 1;
						if (lvl < item.level()) {
							item.degrade( item.level() - lvl );
						}
						item.levelKnown = false;
					}
				}
				
				if (item instanceof Ring) {
					((Ring)item).syncGem();
				}
				
				return item;
			} else {
				return null;
			}
		}
		catch(Exception e){
			return null;
		}

		}
	}
}
