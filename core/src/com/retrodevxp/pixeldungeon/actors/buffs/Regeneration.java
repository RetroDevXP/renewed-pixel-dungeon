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
package com.retrodevxp.pixeldungeon.actors.buffs;

import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.items.rings.RingOfMending;

public class Regeneration extends Buff {
	
	private static final float REGENERATION_DELAY = 10;
	
	@Override
	public boolean act() {
		if (target.isAlive()) {

			if (target.HP < target.HT && !((Hero)target).isStarving()) {
				target.HP += 1;
			}
			
			int bonus = 0;
			for (Buff buff : target.buffs( RingOfMending.Rejuvenation.class )) {
				bonus += ((RingOfMending.Rejuvenation)buff).level;
			}
			try{
				if (((Hero)target).subClass == HeroSubClass.WARDEN){
					bonus += 1;
				}
			}
			catch(Exception e){
				
			}
			spend( (float)(REGENERATION_DELAY / Math.pow( 1.2, bonus )) );
			
		} else {
			
			diactivate();
			
		}

		return true;
	}
}
