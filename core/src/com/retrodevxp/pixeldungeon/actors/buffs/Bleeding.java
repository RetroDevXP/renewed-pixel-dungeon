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

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.ResultDescriptions;
import com.retrodevxp.pixeldungeon.effects.Splash;
import com.retrodevxp.pixeldungeon.ui.BuffIndicator;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.utils.Bundle;
import com.retrodevxp.utils.PointF;
import com.retrodevxp.utils.Random;

public class Bleeding extends Buff {
	
	protected int level;
	
	private static final String LEVEL	= "level";
	
	@Override
	public void storeInBundle( Bundle bundle ) {
		super.storeInBundle( bundle );
		bundle.put( LEVEL, level );
		
	}
	
	@Override
	public void restoreFromBundle( Bundle bundle ) {
		super.restoreFromBundle( bundle );
		level = bundle.getInt( LEVEL );
	}
	
	public void set( int level ) {
		this.level = level;
	};
	
	@Override
	public int icon() {
		return BuffIndicator.BLEEDING;
	}
	
	@Override
	public String toString() {
		return "Bleeding";
	}
	
	@Override
	public boolean act() {
		if (target.isAlive()) {
			
			if ((level = Random.Int( level / 2, level )) > 0) {
				
				target.damage( level, this );
				if (target.sprite.visible) {
					Splash.at( target.sprite.center(), -PointF.PI / 2, PointF.PI / 6, 
							target.sprite.blood(), Math.min( 10 * level / target.HT, 10 ) );
				}
				
				if (target == Dungeon.hero && !target.isAlive()) {
					Dungeon.fail( Utils.format( ResultDescriptions.BLEEDING, Dungeon.depth ) );
					GLog.n( "You bled to death..." );
				}
				
				spend( TICK );
			} else {
				detach();
			}
			
		} else {
			
			detach();
			
		}
		
		return true;
	}
}
