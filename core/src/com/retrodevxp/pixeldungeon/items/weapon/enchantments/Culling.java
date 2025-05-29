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
package com.retrodevxp.pixeldungeon.items.weapon.enchantments;

import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.effects.particles.ShadowParticle;
import com.retrodevxp.pixeldungeon.items.weapon.Weapon;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite.Glowing;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Random;

public class Culling extends Weapon.Enchantment {

	private static final String TXT_CULLING	= "culling %s";
	
	private static ItemSprite.Glowing PINK = new ItemSprite.Glowing( 0xCC33FF );
	
	@Override
	public boolean proc( Weapon weapon, Char attacker, Char defender, int damage ) {
		int level = Math.max( 0, weapon.effectiveLevel() );
		int enchantlevel = 0;
		if (level <= 2){
			enchantlevel = 1;
		}
		else if (level <= 5){
			enchantlevel = 3;
		}
		else if (level <= 7){
			enchantlevel = 5;
		}
		else{
			enchantlevel = 7;
		}
		
		if ( defender.HP < defender.HT) {
			//Culling is less powerful on boss depths.
			try{
				if (Dungeon.depth % 5 == 0){
					defender.damage( Random.Int(1, ( 1 + (int)Math.floor((defender.HT - defender.HP)/ (15 - enchantlevel)))  + (int)(Math.floor(Dungeon.depth / 10))), this );
					defender.sprite.emitter().burst( ShadowParticle.CURSE, enchantlevel + 2 );
				}
				else{
					defender.damage( Random.Int(1, ( 1 + (int)Math.floor((defender.HT - defender.HP)/ (10 - enchantlevel)))  + (int)(Math.floor(Dungeon.depth / 5))), this );
					defender.sprite.emitter().burst( ShadowParticle.CURSE, enchantlevel + 2 );
				}
			}
			catch(Exception e){

			}
			
			
			// if (!defender.isAlive() && attacker instanceof Hero) {
			// 	Badges.validateGrimWeapon();
			// }
			
			return true;
			
		} else {
			
			return false;
			
		}
	}
	
	@Override
	public Glowing glowing() {
		return PINK;
	}
	
	@Override
	public String name( String weaponName) {
		return Utils.format( TXT_CULLING, weaponName );
	}

	@Override
	public String description(){
		return "The dark magic in this weapon tugs at its target's soul. The more wounded its target is, the more powerful the damage dealt";
	}

}
