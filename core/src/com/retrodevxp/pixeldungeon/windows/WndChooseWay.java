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

import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.items.TomeOfMastery;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.ui.HighlightedText;
import com.retrodevxp.pixeldungeon.ui.RedButton;
import com.retrodevxp.pixeldungeon.ui.Window;
import com.retrodevxp.pixeldungeon.utils.Utils;

public class WndChooseWay extends Window {
	
	private static final int WIDTH		= 150;
	private static final int BTN_HEIGHT	= 18;
	private static final float GAP		= 2;
	
	public WndChooseWay( final TomeOfMastery tome, final HeroSubClass way1, final HeroSubClass way2, final HeroSubClass way3 ) {
		
		super();
		
		final String TXT_MASTERY	= "Which way will you follow?";
		final String TXT_CANCEL		= "I'll decide later";
		
		float bottom = createCommonStuff( tome, way1.desc() + "\n\n" + way2.desc() + "\n\n" + way3.desc() + "\n\n" + TXT_MASTERY );
		
		RedButton btnWay1 = new RedButton( Utils.capitalize( way1.title() ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way1 );
			}
		};
		btnWay1.setRect( 0, bottom + GAP, (WIDTH - GAP) / 2, BTN_HEIGHT );
		add( btnWay1 );
		
		RedButton btnWay2 = new RedButton( Utils.capitalize( way2.title() ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way2 );
			}
		};
		btnWay2.setRect( btnWay1.right() + GAP, btnWay1.top(), btnWay1.width(), BTN_HEIGHT );
		add( btnWay2 );

		RedButton btnWay3 = new RedButton( Utils.capitalize( way3.title() ) ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way3 );
			}
		};
		btnWay3.setRect( 0, btnWay1.bottom() + GAP, btnWay1.width(), BTN_HEIGHT );
		add( btnWay3 );
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( btnWay3.right() + GAP, btnWay3.top(), btnWay3.width(), BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
	
	public WndChooseWay( final TomeOfMastery tome, final HeroSubClass way ) {
		
		super();
		
		final String TXT_REMASTERY	= "Do you want to respec into %s?";
		
		final String TXT_OK		= "Yes, I want to respec";
		final String TXT_CANCEL	= "Maybe later";
		
		float bottom = createCommonStuff( tome, way.desc() + "\n\n" + Utils.format( TXT_REMASTERY, Utils.indefinite( way.title() ) ) );
		
		RedButton btnWay = new RedButton( TXT_OK ) {
			@Override
			protected void onClick() {
				hide();
				tome.choose( way );
			}
		};
		btnWay.setRect( 0, bottom + GAP, WIDTH, BTN_HEIGHT );
		add( btnWay );
		
		RedButton btnCancel = new RedButton( TXT_CANCEL ) {
			@Override
			protected void onClick() {
				hide();
			}
		};
		btnCancel.setRect( 0, btnWay.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnCancel );
		
		resize( WIDTH, (int)btnCancel.bottom() );
	}
	
	private float createCommonStuff( TomeOfMastery tome, String text ) {
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( tome.image(), null ) );
		titlebar.label( tome.name() );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		HighlightedText hl = new HighlightedText( 6 );
		hl.text( text, WIDTH );
		hl.setPos( titlebar.left(), titlebar.bottom() + GAP );
		add( hl );
		
		return hl.bottom();
	}
}
