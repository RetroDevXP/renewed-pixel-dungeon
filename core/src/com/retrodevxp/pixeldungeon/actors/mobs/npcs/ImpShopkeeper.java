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
import com.retrodevxp.pixeldungeon.effects.CellEmitter;
import com.retrodevxp.pixeldungeon.effects.Speck;
import com.retrodevxp.pixeldungeon.effects.particles.ElmoParticle;
import com.retrodevxp.pixeldungeon.items.Heap;
import com.retrodevxp.pixeldungeon.sprites.ImpSprite;
import com.retrodevxp.pixeldungeon.utils.Utils;

public class ImpShopkeeper extends Shopkeeper {

	private static final String TXT_GREETINGS = "Hello, friend!";
	
	{
		name = "ambitious imp";
		spriteClass = ImpSprite.class;
	}
	
	private boolean seenBefore = false;
	
	@Override
	protected boolean act() {

		if (!seenBefore && Dungeon.visible[pos]) {
			yell( Utils.format( TXT_GREETINGS ) );
			seenBefore = true;
		}
		
		return super.act();
	}
	
	@Override
	protected void flee() {
		for (Heap heap: Dungeon.level.heaps.values()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
				heap.destroy();
			}
		}
		
		destroy();
		
		sprite.emitter().burst( Speck.factory( Speck.WOOL ), 15 );
		sprite.killAndErase();
	}
	
	@Override
	public String description() {
		return 
			"Imps are lesser demons. They are notable for neither their strength nor their magic talent. " +
			"But they are quite smart and sociable, and many of imps prefer to live and do business among non-demons.";
	}
}
