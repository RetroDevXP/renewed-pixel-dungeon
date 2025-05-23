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
import com.retrodevxp.pixeldungeon.actors.buffs.Bleeding;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.utils.Random;

public class Tamahawk extends MissileWeapon {

	{
		name = "tomahawk";
		image = ItemSpriteSheet.TOMAHAWK;
		
		STR = 17;
	}
	
	public Tamahawk() {
		this( 1 );
	}
	
	public Tamahawk( int number ) {
		super();
		quantity = number;
	}
	
	@Override
	public int min() {
		return 9;
	}
	
	@Override
	public int max() {
		return 20;
	}
	
	@Override
	public void proc( Char attacker, Char defender, int damage ) {
		super.proc( attacker, defender, damage );
		Buff.affect( defender, Bleeding.class ).set( damage );
		if (attacker instanceof Hero && ((Hero)attacker).rangedWeapon == this) {
			Dungeon.level.drop( this, defender.pos ).sprite.drop();
		}
	}	
	
	@Override
	public String desc() {
		return 
			"This sharp, powerful throwing axe " +
			"requires significant strength to be used effectively. \n This thrown weapon is re-usable.";
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 1, 5 );
		return this;
	}
	
	@Override
	public int price() {
		return 20 * quantity;
	}
}
