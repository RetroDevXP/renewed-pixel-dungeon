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
package com.retrodevxp.pixeldungeon.items.food;

import com.retrodevxp.pixeldungeon.actors.buffs.Hunger;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.pixeldungeon.utils.GLog;

public class BulbushFruitFrozen extends Food {

	{
		name = "frozen bulbush fruit";
		image = ItemSpriteSheet.BULBUSHFROZEN;
		energy = Hunger.HUNGRY - 125;
		message = "That fruit tasted horrible!";
	}
	
	// @Override
	// public ArrayList<String> actions( Hero hero ) {
	// 	ArrayList<String> actions = super.actions( hero );
	// 	actions.add( AC_EAT );
	// 	return actions;
	// }
	
	@Override
	public void execute( Hero hero, String action ) {
		
		super.execute( hero, action );
		
		if (action.equals( AC_EAT )) {
			

				GLog.i( "You feel better!" );
				if (hero.HP < hero.HT) {
					hero.HP = Math.min( hero.HP + hero.HT / 5, hero.HT );
					hero.sprite.emitter().burst( Speck.factory( Speck.HEALING ), 1 );
				}
			
		}
	}
	

	
	@Override
	public String info() {
		return 
			"This Bulbush fruit has been frozen. Though difficult to digest, " +
			"the chemical reactions from being frozen leads to substances with healing properties.";
	}
	
	@Override
	public boolean isUpgradable() {
		return false;
	}
	
	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public int price() {
		return 10 * quantity;
	}

	public static Food cook( BulbushFruit ingredient ) {
		BulbushFruitFrozen result = new BulbushFruitFrozen();
		result.quantity = ingredient.quantity();
		return result;
	}
}
