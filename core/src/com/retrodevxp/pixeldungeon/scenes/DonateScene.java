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
package com.retrodevxp.pixeldungeon.scenes;

import com.badlogic.gdx.Gdx;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.*;
import com.retrodevxp.pixeldungeon.PixelDungeon;
import com.retrodevxp.pixeldungeon.effects.Flare;
import com.retrodevxp.pixeldungeon.ui.Archs;
import com.retrodevxp.pixeldungeon.ui.ExitButton;
import com.retrodevxp.pixeldungeon.ui.Icons;
import com.retrodevxp.pixeldungeon.ui.Window;

public class DonateScene extends PixelScene {

	private static final String TXT = 
		"This mod is available for free.\n" +
		"If you enjoy it, please consider \n" +
		"making a donation, either by specifying your own price on the download page at itch.io, \n" +
		"or at BuyMeACoffee.\n" +
		"Donate:";
	
	private static final String LNK1 = "retrodevxp.itch.io/renewed-pixel-dungeon";
	private static final String LNK2 = "buymeacoffee.com/retrodevxp";
	
	@Override
	public void create() {
		super.create();
		
		BitmapTextMultiline text = createMultiline( TXT, 8 );
		text.maxWidth = Math.min( Camera.main.width, 120 );
		text.measure();
		add( text );
		
		text.x = align( (Camera.main.width - text.width()) / 2 );
		text.y = align( (Camera.main.height - text.height()) / 2 );
		
		BitmapTextMultiline link = createMultiline( LNK1, 8 );
		link.maxWidth = Math.min( Camera.main.width, 120 );
		link.measure();
		link.hardlight( Window.TITLE_COLOR );
		add( link );
		
		link.x = text.x;
		link.y = text.y + text.height();
		
		TouchArea hotArea = new TouchArea( link ) {
			@Override
			protected void onClick( NoosaInputProcessor.Touch touch ) {
				Gdx.net.openURI("http://" + LNK1);
			}
		};
		add( hotArea );

		BitmapTextMultiline link2 = createMultiline( LNK2, 8 );
		link2.maxWidth = Math.min( Camera.main.width, 120 );
		link2.measure();
		link2.hardlight( Window.TITLE_COLOR );
		add( link2 );
		
		link2.x = text.x;
		link2.y = text.y + text.height() + 10;
		
		TouchArea hotArea2 = new TouchArea( link2 ) {
			@Override
			protected void onClick( NoosaInputProcessor.Touch touch ) {
				Gdx.net.openURI("http://" + LNK2);
			}
		};
		add( hotArea );
		
		Image wata = Icons.WATA.get();
		wata.x = align( (Camera.main.width - wata.width) / 2 );
		wata.y = text.y - wata.height - 8;
		add( wata );
		
		new Flare( 7, 64 ).color( 0x112233, true ).show( wata, 0 ).angularSpeed = +20;
		
		Archs archs = new Archs();
		archs.setSize( Camera.main.width, Camera.main.height );
		addToBack( archs );
		
		ExitButton btnExit = new ExitButton();
		btnExit.setPos( Camera.main.width - btnExit.width(), 0 );
		add( btnExit );
		
		fadeIn();
	}
	
	@Override
	protected void onBackPressed() {
		PixelDungeon.switchNoFade( TitleScene.class );
	}
}
