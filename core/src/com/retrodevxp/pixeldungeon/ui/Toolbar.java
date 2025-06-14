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

import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.DungeonTilemap;
import com.retrodevxp.pixeldungeon.input.GameAction;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.scenes.CellSelector;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.windows.WndBag;
import com.retrodevxp.pixeldungeon.windows.WndCatalogus;
import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.Gizmo;
import com.retrodevxp.noosa.Image;
import com.retrodevxp.noosa.ui.Button;
import com.retrodevxp.noosa.ui.Component;

public class Toolbar extends Component {

	private Tool btnWait;
	private Tool btnSearch;
	private Tool btnInfo;
	private Tool btnSwap;
	private Tool btnInventory;
	private Tool btnQuick1;
	private Tool btnQuick2;
	private Tool btnQuick3;
	
	private PickedUpItem pickedUp;
	
	private boolean lastEnabled = true;
	
	private static Toolbar instance;
	
	public Toolbar() {
		super();
		
		instance = this;
		
		height = btnInventory.height();
	}
	
	@Override
	protected void createChildren() {
		
		add( btnWait = new Tool( 0, 7, 20, 25, GameAction.REST ) {
			
			@Override
			protected void onClick() {
                restOneTurn();
			}
			protected boolean onLongClick() {
				restFull();
				return true;
			}

			private void restOneTurn() {
				Dungeon.hero.rest( false );
			}

			private void restFull() {
				Dungeon.hero.rest( true );
			}
		} );
		
		add( btnSearch = new Tool( 20, 7, 20, 25, GameAction.SEARCH ) {
			@Override
			protected void onClick() {
				doSearch();
			}

			private void doSearch() {
				Dungeon.hero.search( true );
			}
		} );
		
		add( btnInfo = new Tool( 40, 7, 21, 25, GameAction.CELL_INFO) {
			@Override
			protected void onClick() {
				getCellInfo();
			}

			private void getCellInfo() {
				GameScene.selectCell(informer);
			}
		} );
		
		add( btnInventory = new Tool( 60, 7, 23, 25, GameAction.BACKPACK ) {
			private GoldIndicator gold;
			@Override
			protected void onClick() {
                showBackpack();
			}

            @Override
			protected boolean onLongClick() {
				showCatalogus();
				return true;
			}

			private void showBackpack() {
				GameScene.show(new WndBag(Dungeon.hero.belongings.backpack, null, WndBag.Mode.ALL, null));
			}
			private void showCatalogus() {
				GameScene.show(new WndCatalogus());
			}

			@Override
			protected void createChildren() {
				super.createChildren();
				gold = new GoldIndicator();
				add( gold );
			}
			@Override
			protected void layout() {
				super.layout();
				gold.fill( this );
			}
		} );
		
		// add( btnSwap = new Tool( 20, 7, 20, 25, GameAction.SWAP) {
		// 	@Override
		// 	protected void onClick() {
		// 		// Object swaps = QuickSlot.secondaryValue;
		// 		// QuickSlot.secondaryValue = QuickSlot.primaryValue;
		// 		// QuickSlot.primaryValue = swaps;
		// 		// QuickSlot.refresh();
		// 		Dungeon.gold += 1;
		// 	}

		// 	private void getCellInfo() {
		// 		GameScene.selectCell(informer);
		// 	}
		// } );
		add( btnQuick1 = new QuickslotTool( 83, 7, 22, 25, 1 ) );
		add( btnQuick2 = new QuickslotTool( 83, 7, 22, 25, 2 ) );
		add( btnQuick3 = new QuickslotTool( 83, 7, 22, 25, 3 ) );
		// btnQuick2.visible = (QuickSlot.secondaryValue != null);
		// btnQuick1.hotKey = GameAction.QUICKSLOT;
		// btnQuick2.hotKey = GameAction.QUICKSLOT2;
		
		add( pickedUp = new PickedUpItem() );
	}
	
	@Override
	protected void layout() {
		btnWait.setPos( x, y );
		btnSearch.setPos( btnWait.right(), y );
		btnInfo.setPos( btnSearch.right(), y );
		// btnSwap.setPos( width - btnSwap.width(), y - btnSwap.height() );
		btnQuick3.setPos( width - btnQuick3.width(), y );
		if (btnQuick2.visible && btnQuick3.visible) {
			btnQuick1.setPos(btnQuick3.left() - btnQuick1.width(), y );
			btnQuick2.setPos(btnQuick1.left() - btnQuick2.width(), y );
			btnInventory.setPos( btnQuick2.left() - btnInventory.width(), y );
		} else {
			btnInventory.setPos( btnQuick1.left() - btnInventory.width(), y );
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		if (lastEnabled != Dungeon.hero.ready) {
			lastEnabled = Dungeon.hero.ready;
			
			for (Gizmo tool : members) {
				if (tool instanceof Tool) {
					((Tool)tool).enable( lastEnabled );
				}
			}
		}
		
		if (!Dungeon.hero.isAlive()) {
			btnInventory.enable( true );
		}
	}
	
	public void pickup( Item item ) {
		pickedUp.reset( item, 
			btnInventory.centerX(), 
			btnInventory.centerY() );
	}
	
	public static boolean secondQuickslot() {
		return instance.btnQuick2.visible;
	}
	
	public static void secondQuickslot( boolean value ) {
		instance.btnQuick2.visible = 
		instance.btnQuick2.active = 
			value;
		instance.layout();
	}
	
	private static CellSelector.Listener informer = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
            GameScene.examineCell( cell );
		}	
		@Override
		public String prompt() {
			return "Select a cell to examine";
		}
	};
	
	public static class Tool extends Button<GameAction> {
		
		private static final int BGCOLOR = 0x7B8073;
		
		protected Image base;
		
		public Tool(int x, int y, int width, int height, GameAction hotKey ) {
			super();

			base.frame( x, y, width, height );
			
			this.width = width;
			this.height = height;

            this.hotKey = hotKey;
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			base = new Image( Assets.TOOLBAR );
			add( base );
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			base.x = x;
			base.y = y;
		}
		
		@Override
		protected void onTouchDown() {
			base.brightness( 1.4f );
		}
		
		@Override
		protected void onTouchUp() {
			if (active) {
				base.resetColor();
			} else {
				base.tint( BGCOLOR, 0.7f );
			}
		}
		
		public void enable( boolean value ) {
			if (value != active) {
				if (value) {
					base.resetColor();
				} else {
					base.tint( BGCOLOR, 0.7f );
				}
				active = value;
			}
		}
	}
	
	public static class QuickslotTool extends Tool {
		
		private QuickSlot slot;
		
		public QuickslotTool( int x, int y, int width, int height, int slotnumber ) {
			super( x, y, width, height, GameAction.UNKNOWN );
			if (slotnumber == 1) {
				slot.hotKey = GameAction.QUICKSLOT;
				slot.primary();
			} else if (slotnumber == 2) {
				slot.hotKey = GameAction.QUICKSLOT2;
				slot.secondary();
			}
			else if (slotnumber == 3) {
				slot.hotKey = GameAction.QUICKSLOT3;
				slot.tertiary();
			}
		}
		
		@Override
		protected void createChildren() {
			super.createChildren();
			
			slot = new QuickSlot();
			add( slot );
		}
		
		@Override
		protected void layout() {
			super.layout();
			slot.setRect( x + 1, y + 2, width - 2, height - 2 );
		}
		
		@Override
		public void enable( boolean value ) {
			slot.enable( value );
			super.enable( value );
		}
	}
	
	public static class PickedUpItem extends ItemSprite {
		
		private static final float DISTANCE = DungeonTilemap.SIZE;
		private static final float DURATION = 0.2f;
		
		private float dstX;
		private float dstY;
		private float left;
		
		public PickedUpItem() {
			super();
			
			originToCenter();
			
			active = 
			visible = 
				false;
		}
		
		public void reset( Item item, float dstX, float dstY ) {
			view( item.image(), item.glowing() );
			
			active = 
			visible = 
				true;
			
			this.dstX = dstX - ItemSprite.SIZE / 2;
			this.dstY = dstY - ItemSprite.SIZE / 2;
			left = DURATION;
			
			x = this.dstX - DISTANCE;
			y = this.dstY - DISTANCE;
			alpha( 1 );
		}
		
		@Override
		public void update() {
			super.update();
			
			if ((left -= Game.elapsed) <= 0) {
				
				visible = 
				active = 
					false;
				
			} else {
				float p = left / DURATION; 
				scale.set( (float)Math.sqrt( p ) );
				float offset = DISTANCE * p;
				x = dstX - offset;
				y = dstY - offset;
			}
		}
	}

	public Tool getQuickslot1(){
		return btnQuick1;
	}

	public Tool getQuickslot2(){
		return btnQuick2;
	}
}
