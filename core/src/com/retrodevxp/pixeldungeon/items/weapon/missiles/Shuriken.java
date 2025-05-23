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
package com.retrodevxp.pixeldungeon.items.weapon.missiles;

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.utils.Random;

public class Shuriken extends MissileWeapon {

	{
		name = "shuriken";
		image = ItemSpriteSheet.SHURIKEN;
		
		STR = 13;
		
		DLY = 0.5f;
	}
	
	public Shuriken() {
		this( 1 );
	}
	
	public Shuriken( int number ) {
		super();
		quantity = number;
	}
	
	@Override
	public int min() {
		return 2;
	}
	
	@Override
	public int max() {
		return 6;
	}
	
	@Override
	public String desc() {
		return 
			"Star-shaped pieces of metal with razor-sharp blades do significant damage " +
			"when they hit a target. They can be thrown at very high rate. \n This thrown weapon is re-usable.";
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 3, 7 );
		return this;
	}

	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		super.proc( attacker, defender, damage );
		if (attacker instanceof Hero && ((Hero)attacker).rangedWeapon == this) {
			Dungeon.level.drop( this, defender.pos ).sprite.drop();
		}
	}
	
	@Override
	public int price() {
		return 15 * quantity;
	}
}
