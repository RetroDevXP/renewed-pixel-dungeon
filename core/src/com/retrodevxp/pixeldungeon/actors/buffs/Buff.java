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

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.items.rings.RingOfElements.Resistance;
import com.retrodevxp.pixeldungeon.ui.BuffIndicator;

public class Buff extends Actor {

	public Char target;
	
	public boolean attachTo( Char target ) {

		if (target.immunities().contains( getClass() )) {
			return false;
		}
		
		this.target = target;
		target.add( this );
		
		return true;
	}
	
	public void detach() {
		target.remove( this );
	}
	
	@Override
	public boolean act() {
		diactivate();
		return true;
	}
	
	public int icon() {
		return BuffIndicator.NONE;
	}
	
	public static<T extends Buff> T append( Char target, Class<T> buffClass ) {
		try {
			T buff = ClassReflection.newInstance( buffClass );
			buff.attachTo( target );
			return buff;
		} catch (Exception e) {
			return null;
		}
	}
	
	public static<T extends FlavourBuff> T append( Char target, Class<T> buffClass, float duration ) {
		T buff = append( target, buffClass );
		buff.spend( duration );
		return buff;
	}
	
	public static<T extends Buff> T affect( Char target, Class<T> buffClass ) {
		T buff = target.buff( buffClass );
		if (buff != null) {
			return buff;
		} else {
			try {
				buff = ClassReflection.newInstance(buffClass);
				buff.attachTo( target );
				return buff;
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public static<T extends FlavourBuff> T affect( Char target, Class<T> buffClass, float duration ) {
		T buff = affect( target, buffClass );
		buff.spend( duration );
		return buff;
	}
	
	public static<T extends FlavourBuff> T prolong( Char target, Class<T> buffClass, float duration ) {
		T buff = affect( target, buffClass );
		buff.postpone( duration );
		return buff;
	}
	
	public static void detach( Buff buff ) {
		if (buff != null) {
			buff.detach();
		}
	}
	
	public static void detach( Char target, Class<? extends Buff> cl ) {
		detach( target.buff( cl ) );
	}

	public static float durationFactor(Char ch) {
        Resistance r = ch.buff(Resistance.class);
        if (r != null) {
            return r.durationFactor();
        }
        return 1.0f;
    }
}
