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
package com.retrodevxp.pixeldungeon.ui;


import com.badlogic.gdx.Input;
import com.retrodevxp.pixeldungeon.Chrome;
import com.retrodevxp.pixeldungeon.effects.ShadowBox;
import com.retrodevxp.pixeldungeon.input.GameAction;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.Camera;
import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.Group;
import com.retrodevxp.noosa.NinePatch;
import com.retrodevxp.noosa.TouchArea;
import com.retrodevxp.utils.Signal;

public class Window extends Group implements Signal.Listener<NoosaInputProcessor.Key<GameAction>> {

	protected int width;
	protected int height;
	
	protected TouchArea blocker;
	protected ShadowBox shadow;
	protected NinePatch chrome;
	
	public static final int TITLE_COLOR = 0xFFFF44;
	
	public Window() {
		this( 0, 0, Chrome.get( Chrome.Type.WINDOW ) );
	}
	
	public Window( int width, int height ) {
		this( width, height, Chrome.get( Chrome.Type.WINDOW ) );
	}
			
	public Window( int width, int height, NinePatch chrome ) {
		super();
		
		blocker = new TouchArea( 0, 0, PixelScene.uiCamera.width, PixelScene.uiCamera.height ) {
			@Override
			protected void onClick( NoosaInputProcessor.Touch touch ) {
				if (!Window.this.chrome.overlapsScreenPoint( 
					(int)touch.current.x, 
					(int)touch.current.y )) {
					
					onBackPressed();
				}
			}
		};
		blocker.camera = PixelScene.uiCamera;
		add( blocker );
		
		this.chrome = chrome;
		
		this.width = width;
		this.height = height;
		
		shadow = new ShadowBox();
		shadow.am = 0.5f;
		shadow.camera = PixelScene.uiCamera.visible ? 
			PixelScene.uiCamera : Camera.main;
		add( shadow );
		
		chrome.x = -chrome.marginLeft();
		chrome.y = -chrome.marginTop();
		chrome.size( 
			width - chrome.x + chrome.marginRight(),
			height - chrome.y + chrome.marginBottom() );
		add( chrome );
		
		camera = new Camera( 0, 0, 
			(int)chrome.width, 
			(int)chrome.height, 
			PixelScene.defaultZoom );
		camera.x = (int)(Game.width - camera.width * camera.zoom) / 2;
		camera.y = (int)(Game.height - camera.height * camera.zoom) / 2;
		camera.scroll.set( chrome.x, chrome.y );
		Camera.add( camera );
		
		shadow.boxRect( 
			camera.x / camera.zoom, 
			camera.y / camera.zoom, 
			chrome.width(), chrome.height );
		
		Game.instance.getInputProcessor().addKeyListener(this);
	}
	
	public void resize( int w, int h ) {
		this.width = w;
		this.height = h;
		
		chrome.size( 
			width + chrome.marginHor(),
			height + chrome.marginVer() );
		
		camera.resize( (int)chrome.width, (int)chrome.height );
		camera.x = (int)(Game.width - camera.screenWidth()) / 2;
		camera.y = (int)(Game.height - camera.screenHeight()) / 2;
		
		shadow.boxRect( camera.x / camera.zoom, camera.y / camera.zoom, chrome.width(), chrome.height );
	}
	
	public void hide() {
		parent.erase( this );
		destroy();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		Camera.remove( camera );
		Game.instance.getInputProcessor().removeKeyListener(this);
	}

	@Override
	public void onSignal( NoosaInputProcessor.Key<GameAction> key ) {
		if (key.pressed) {
			switch (key.code) {
			case Input.Keys.BACK:
			case Input.Keys.ESCAPE:
				onBackPressed();
				break;
			case Input.Keys.MENU:
				onMenuPressed();
				break;
			default:
				onKeyDown(key);
				break;
			}
		} else {
			onKeyUp( key );
		}

		Game.instance.getInputProcessor().cancelKeyEvent();
	}

	protected void onKeyDown(NoosaInputProcessor.Key key) {
	}

	protected void onKeyUp( NoosaInputProcessor.Key<GameAction> key ) {
	}

	public void onBackPressed() {
		hide();
	}
	
	public void onMenuPressed() {
	}
}
