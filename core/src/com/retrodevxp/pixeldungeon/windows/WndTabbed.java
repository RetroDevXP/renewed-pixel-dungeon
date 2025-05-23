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

import java.util.ArrayList;

import com.badlogic.gdx.Input;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Chrome;
import com.retrodevxp.pixeldungeon.input.GameAction;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.ui.Window;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.BitmapText;
import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.NinePatch;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.noosa.ui.Button;

public class WndTabbed extends Window {

	protected ArrayList<Tab> tabs = new ArrayList<Tab>();
	protected Tab selected;
	
	public WndTabbed() {
		super( 0, 0, Chrome.get( Chrome.Type.TAB_SET ) );
	}
	
	protected Tab add( Tab tab ) {
		
		tab.setPos( tabs.size() == 0 ? 
			-chrome.marginLeft() + 1 : 
			tabs.get( tabs.size() - 1 ).right(), height );
		tab.select( false );
		super.add( tab );
		
		tabs.add( tab );
		
		return tab;
	}
	
	public void select( int index ) {
		select( tabs.get( index ) );
	}
	
	public void select( Tab tab ) {
		if (tab != selected) {
			for (Tab t : tabs) {
				if (t == selected) {
					t.select( false );
				} else if (t == tab) {
					t.select( true );
				}
			}
			
			selected = tab;
		}
	}
	
	@Override
	public void resize( int w, int h ) {
		// -> super.resize(...)
		this.width = w;
		this.height = h;
		
		chrome.size( 
			width + chrome.marginHor(),
			height + chrome.marginVer() );
		
		camera.resize( (int)chrome.width, chrome.marginTop() + height + tabHeight());
		camera.x = (int)(Game.width - camera.screenWidth()) / 2;
		camera.y = (int)(Game.height - camera.screenHeight()) / 2;
		
		shadow.boxRect( 
			camera.x / camera.zoom, 
			camera.y / camera.zoom, 
			chrome.width(), chrome.height );
		// <- super.resize(...)
		
		for (Tab tab : tabs) {
			remove( tab );
		}
		
		ArrayList<Tab> tabs = new ArrayList<Tab>( this.tabs );
		this.tabs.clear();
		
		for (Tab tab : tabs) {
			add( tab );
		}
	}
	
	protected int tabHeight() {
		return 25;
	}
	
	protected void onClick( Tab tab ) {
		select( tab );
	}

	@Override
	protected void onKeyDown(NoosaInputProcessor.Key key) {
		if (key.code == Input.Keys.TAB) {
			int next = 0;
			for (int j = 0; j < tabs.size(); j++) {
				Tab t = tabs.get(j);
				if (t == selected) {
					next = (j + 1) % tabs.size();
					break;
				}
			}
			select(next);
		}
	}

	public class Tab extends Button<GameAction> {
		
		protected final int CUT = 5;
		
		protected boolean selected;
		
		protected NinePatch bg;

		@Override
		protected void layout() {
			super.layout();
			
			if (bg != null) {
				bg.x = x;
				bg.y = y;
				bg.size( width, height );
			}
		}
		
		protected void select( boolean value ) {
			
			active = !(selected = value);
			
			if (bg != null) {
				remove( bg );
			}
			
			bg = Chrome.get( selected ? 
				Chrome.Type.TAB_SELECTED : 
				Chrome.Type.TAB_UNSELECTED );
			addToBack( bg );
			
			layout();
		}
		
		@Override
		protected void onClick() {	
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
			WndTabbed.this.onClick( this );
		}
	}
	
	public class LabeledTab extends Tab {
		
		private BitmapText btLabel;
		
		public LabeledTab( String label ) {
			
			super();
			
			btLabel.text( label );
			btLabel.measure();
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			btLabel = PixelScene.createText( 9 );
			add( btLabel );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			btLabel.x = PixelScene.align( x + (width - btLabel.width()) / 2 );
			btLabel.y = PixelScene.align( y + (height - btLabel.baseLine()) / 2 ) - 1;
			if (!selected) {
				btLabel.y -= 2;
			}
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			btLabel.am = selected ? 1.0f : 0.6f;
		}
	}	

}
