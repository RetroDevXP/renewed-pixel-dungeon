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
package com.retrodevxp.pixeldungeon.windows;

import com.retrodevxp.pixeldungeon.items.Heap;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.Heap.Type;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.ui.ItemSlot;
import com.retrodevxp.pixeldungeon.ui.Window;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.noosa.BitmapTextMultiline;

public class WndInfoItem extends Window {
	
	private static final String TXT_CHEST			= "Chest";
	private static final String TXT_LOCKED_CHEST	= "Locked chest";
	private static final String TXT_CRYSTAL_CHEST	= "Crystal chest";
	private static final String TXT_TOMB			= "Tomb";
	private static final String TXT_SKELETON		= "Skeletal remains";
	private static final String TXT_WONT_KNOW		= "You won't know what's inside until you open it!";
	private static final String TXT_NEED_KEY		= TXT_WONT_KNOW + " But to open it you need a golden key.";
	private static final String TXT_INSIDE			= "You can see %s inside, but to open the chest you need a golden key.";
	private static final String TXT_OWNER	= 
		"This ancient tomb may contain something useful, " +
		"but its owner will most certainly object to checking.";
	private static final String TXT_REMAINS	= 
		"This is all that's left from one of your predecessors. " +
		"Maybe it's worth checking for any valuables.";
	
	private static final float GAP	= 2;
	
	private static final int WIDTH = 120;
	
	public WndInfoItem( Heap heap ) {
		
		super();
		
		if (heap.type == Heap.Type.HEAP || heap.type == Heap.Type.FOR_SALE) {
			
			Item item = heap.peek();
			
			int color = TITLE_COLOR;
			if (item.levelKnown) {
				if (item.level() < 0) {
					color = ItemSlot.DEGRADED;				
				} else if (item.level() > 0) {
					color = item.isBroken() ? ItemSlot.WARNING : ItemSlot.UPGRADED;				
				}
			}
			fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
			
		} else {
			
			String title;
			String info;
			
			if (heap.type == Type.CHEST || heap.type == Type.MIMIC) {
				title = TXT_CHEST;
				info = TXT_WONT_KNOW;
			} else if (heap.type == Type.TOMB) {
				title = TXT_TOMB;
				info = TXT_OWNER;
			} else if (heap.type == Type.SKELETON) {
				title = TXT_SKELETON;
				info = TXT_REMAINS;
			} else if (heap.type == Type.CRYSTAL_CHEST) {
				title = TXT_CRYSTAL_CHEST;
				info = Utils.format( TXT_INSIDE, Utils.indefinite( heap.peek().name() ) );
			} else {
				title = TXT_LOCKED_CHEST;
				info = TXT_NEED_KEY;
			}
			
			fillFields( heap.image(), heap.glowing(), TITLE_COLOR, title, info );
			
		}
	}
	
	public WndInfoItem( Item item ) {
		
		super();
		
		int color = TITLE_COLOR;
		if (item.levelKnown) {
			if (item.level() < 0 || item.isBroken()) {
				color = ItemSlot.DEGRADED;				
			} else if (item.level() > 0) {
				color = ItemSlot.UPGRADED;				
			}
		}
		
		fillFields( item.image(), item.glowing(), color, item.toString(), item.info() );
	}
	
	private void fillFields( int image, ItemSprite.Glowing glowing, int titleColor, String title, String info ) {
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( image, glowing ) );
		titlebar.label( Utils.capitalize( title ), titleColor );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		BitmapTextMultiline txtInfo = PixelScene.createMultiline( info, 6 );
		txtInfo.maxWidth = WIDTH;
		txtInfo.measure();
		txtInfo.x = titlebar.left();
		txtInfo.y = titlebar.bottom() + GAP;
		add( txtInfo );
		
		resize( WIDTH, (int)(txtInfo.y + txtInfo.height()) );
	}
}
