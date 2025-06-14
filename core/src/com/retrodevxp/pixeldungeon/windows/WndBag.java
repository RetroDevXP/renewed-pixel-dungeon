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

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.PixelDungeon;
import com.retrodevxp.pixeldungeon.actors.hero.Belongings;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.input.GameAction;
import com.retrodevxp.pixeldungeon.items.Gold;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.armor.Armor;
import com.retrodevxp.pixeldungeon.items.bags.Bag;
import com.retrodevxp.pixeldungeon.items.bags.Keyring;
import com.retrodevxp.pixeldungeon.items.bags.ScrollHolder;
import com.retrodevxp.pixeldungeon.items.bags.SeedPouch;
import com.retrodevxp.pixeldungeon.items.bags.WandHolster;
import com.retrodevxp.pixeldungeon.items.wands.Wand;
import com.retrodevxp.pixeldungeon.items.weapon.melee.MeleeWeapon;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.Boomerang;
import com.retrodevxp.pixeldungeon.plants.Plant.Seed;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.pixeldungeon.ui.Icons;
import com.retrodevxp.pixeldungeon.ui.ItemSlot;
import com.retrodevxp.pixeldungeon.ui.QuickSlot;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.gltextures.TextureCache;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.BitmapText;
import com.retrodevxp.noosa.ColorBlock;
import com.retrodevxp.noosa.Image;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.utils.GameMath;
import com.retrodevxp.utils.RectF;

public class WndBag extends WndTabbed {
	
	public static enum Mode {
		ALL,
		UNIDENTIFED,
		UPGRADEABLE,
		QUICKSLOT,
		FOR_SALE,
		WEAPON,
		ARMOR,
		ENCHANTABLE,
		WAND,
		SEED
	}
	
	protected static final int COLS_P	= 4;
	protected static final int COLS_L	= 6;
	
	protected static final int SLOT_SIZE	= 28;
	protected static final int SLOT_MARGIN	= 1;
	
	protected static final int TAB_WIDTH	= 25;
	
	protected static final int TITLE_HEIGHT	= 12;
	
	private Listener listener;
	private WndBag.Mode mode;
	private String title;
	
	private int nCols;
	private int nRows;
	
	protected int count;
	protected int col;
	protected int row;
	
	private static Mode lastMode;
	private static Bag lastBag;

	public static Class<? extends Wand> wandClass;
	
	public WndBag( Bag bag, Listener listener, Mode mode, String title ) {
		
		super();
		
		this.listener = listener;
		this.mode = mode;
		this.title = title;
		
		lastMode = mode;
		lastBag = bag;
		
		nCols = PixelDungeon.landscape() ? COLS_L : COLS_P;
		nRows = (Belongings.BACKPACK_SIZE + 4 + 1) / nCols + ((Belongings.BACKPACK_SIZE + 4 + 1) % nCols > 0 ? 1 : 0);
		
		int slotsWidth = SLOT_SIZE * nCols + SLOT_MARGIN * (nCols - 1);
		int slotsHeight = SLOT_SIZE * nRows + SLOT_MARGIN * (nRows - 1);
		
		BitmapText txtTitle = PixelScene.createText( title != null ? title : Utils.capitalize( bag.name() ), 9 );
		txtTitle.hardlight( TITLE_COLOR );
		txtTitle.measure();
		txtTitle.x = (int)(slotsWidth - txtTitle.width()) / 2;
		txtTitle.y = (int)(TITLE_HEIGHT - txtTitle.height()) / 2;
		add( txtTitle );
		
		placeItems( bag );
		
		resize( slotsWidth, slotsHeight + TITLE_HEIGHT );
		
		Belongings stuff = Dungeon.hero.belongings;
		Bag[] bags = {
			stuff.backpack, 
			stuff.getItem( SeedPouch.class ), 
			stuff.getItem( ScrollHolder.class ),
			stuff.getItem( WandHolster.class ),
			stuff.getItem( Keyring.class )};
		
		for (Bag b : bags) {
			if (b != null) {
				BagTab tab = new BagTab( b );
				tab.setSize( TAB_WIDTH, tabHeight() );
				add( tab );
				
				tab.select( b == bag );
			}
		}
	}

	@Override
	protected void onKeyUp( NoosaInputProcessor.Key<GameAction> key ) {
		if (key.action == GameAction.BACKPACK) {
			hide();
		} else {
			super.onKeyUp( key );
		}
	}
	
	public static WndBag lastBag( Listener listener, Mode mode, String title ) {
		
		if (mode == lastMode && lastBag != null && 
			Dungeon.hero.belongings.backpack.contains( lastBag )) {
			
			return new WndBag( lastBag, listener, mode, title );
			
		} else {
			
			return new WndBag( Dungeon.hero.belongings.backpack, listener, mode, title );
			
		}
	}
	
	public static WndBag seedPouch( Listener listener, Mode mode, String title ) {
		SeedPouch pouch = Dungeon.hero.belongings.getItem( SeedPouch.class );
		return pouch != null ?
			new WndBag( pouch, listener, mode, title ) :
			new WndBag( Dungeon.hero.belongings.backpack, listener, mode, title );
	}
	
	public static WndBag wandHolster(Listener listener, Mode mode, String title) {
        WandHolster holster = Dungeon.hero.belongings.getItem(WandHolster.class);
        if (holster != null) {
            return new WndBag(holster, listener, mode, title);
        }
        return new WndBag(Dungeon.hero.belongings.backpack, listener, mode, title);
    }

	protected void placeItems( Bag container ) {
		
		// Equipped items
		Belongings stuff = Dungeon.hero.belongings;
		placeItem( stuff.weapon != null ? stuff.weapon : new Placeholder( ItemSpriteSheet.WEAPON ) );
		placeItem( stuff.armor != null ? stuff.armor : new Placeholder( ItemSpriteSheet.ARMOR ) );
		placeItem( stuff.ring1 != null ? stuff.ring1 : new Placeholder( ItemSpriteSheet.RING ) );
		placeItem( stuff.ring2 != null ? stuff.ring2 : new Placeholder( ItemSpriteSheet.RING ) );
		
		boolean backpack = (container == Dungeon.hero.belongings.backpack);
		if (!backpack) {
			count = nCols;
			col = 0;
			row = 1;
		}
		
		// Items in the bag
		for (Item item : container.items) {
			placeItem( item );
		}
		
		// Free space
		while (count-(backpack ? 4 : nCols) < container.size) {
			placeItem( null );
		}
		
		// Gold in the backpack
		if (container == Dungeon.hero.belongings.backpack) {
			row = nRows - 1;
			col = nCols - 1;
			placeItem( new Gold( Dungeon.gold ) );
		}
	}
	
	protected void placeItem( final Item item ) {
		
		int x = col * (SLOT_SIZE + SLOT_MARGIN);
		int y = TITLE_HEIGHT + row * (SLOT_SIZE + SLOT_MARGIN);
		
		add( new ItemButton( item ).setPos( x, y ) );
		
		if (++col >= nCols) {
			col = 0;
			row++;
		}
		
		count++;
	}
	
	@Override
	public void onMenuPressed() {
		if (listener == null) {
			hide();
		}
	}
	
	@Override
	public void onBackPressed() {
		if (listener != null) {
			listener.onSelect( null );
		}
		super.onBackPressed();
	}
	
	@Override
	protected void onClick( Tab tab ) {
		hide();
		GameScene.show( new WndBag( ((BagTab)tab).bag, listener, mode, title ) );
	}
	
	@Override
	protected int tabHeight() {
		return 20;
	}
	
	public class BagTab extends Tab {
		
		private Image icon;

		private Bag bag;
		
		public BagTab( Bag bag ) {
			super();
			
			this.bag = bag;
			
			icon = icon();
			add( icon );
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			icon.am = selected ? 1.0f : 0.6f;
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			icon.copy( icon() );
			icon.x = x + (width - icon.width) / 2;
			icon.y = y + (height - icon.height) / 2 - 2 - (selected ? 0 : 1);
			if (!selected && icon.y < y + CUT) {
				RectF frame = icon.frame();
				// FIXME: Don't we need to update bottom as well?
				icon.frame( new RectF(frame.left, frame.top + (y + CUT - icon.y) / icon.texture.height, frame.right, frame.bottom) );
				icon.y = y + CUT;
			}
		}
		
		private Image icon() {
			if (bag instanceof SeedPouch) {
				return Icons.get( Icons.SEED_POUCH );
			} else if (bag instanceof ScrollHolder) {
				return Icons.get( Icons.SCROLL_HOLDER );
			} else if (bag instanceof WandHolster) {
				return Icons.get( Icons.WAND_HOLSTER );
			} else if (bag instanceof Keyring) {
				return Icons.get( Icons.KEYRING );
			} else {
				return Icons.get( Icons.BACKPACK );
			}
		}
	}
	
	public static class Placeholder extends Item {		
		{
			name = null;
		}
		
		public Placeholder( int image ) {
			this.image = image;
		}
		
		@Override
		public boolean isIdentified() {
			return true;
		}
		
		@Override
		public boolean isEquipped( Hero hero ) {
			return true;
		}
	}
	
	public class ItemButton extends ItemSlot {
		
		private static final int NORMAL		= 0xFF4A4D44;
		private static final int EQUIPPED	= 0xFF63665B;
		
		private static final int NBARS	= 3;
		
		private Item item;
		private ColorBlock bg;
		
		private ColorBlock durability[];
		
		public ItemButton( Item item ) {
			
			super( item );

			this.item = item;
			if (item instanceof Gold) {
				bg.visible = false;
			}
			
			width = height = SLOT_SIZE;
		}
		
		@Override
		protected void createChildren() {	
			bg = new ColorBlock( SLOT_SIZE, SLOT_SIZE, NORMAL );
			add( bg );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			if (durability != null) {
				for (int i=0; i < NBARS; i++) {
					durability[i].x = x + 1 + i * 3;
					durability[i].y = y + height - 3;
				}
			}
			
			super.layout();
		}
		
		@Override
		public void item( Item item ) {
			
			super.item( item );
			if (item != null) {

				bg.texture( TextureCache.createSolid( item.isEquipped( Dungeon.hero ) ? EQUIPPED : NORMAL ) );
				if (item.cursed && item.cursedKnown) {
					bg.ra = +0.2f;
					bg.ga = -0.1f;
				} else if (!item.isIdentified()) {
					bg.ra = 0.1f;
					bg.ba = 0.1f;
				}
				
				if (lastBag.owner.isAlive() && item.isUpgradable() && item.levelKnown) {
					durability = new ColorBlock[NBARS];
					int nBars = (int)GameMath.gate( 0, Math.round( (float)NBARS * item.durability() / item.maxDurability() ), NBARS );
					for (int i=0; i < nBars; i++) {
						durability[i] = new ColorBlock( 2, 2, 0xFF00EE00 );
						add( durability[i] );
					}
					for (int i=nBars; i < NBARS; i++) {
						durability[i] = new ColorBlock( 2, 2, 0xFFCC0000 );
						add( durability[i] );
					}
				}
				
				if (item.name() == null) {
					enable( false );
				} else {
					enable( 
						mode == Mode.QUICKSLOT && (item.defaultAction != null) ||
						mode == Mode.FOR_SALE && (item.price() > 0) && (!item.isEquipped( Dungeon.hero ) || !item.cursed) ||
						mode == Mode.UPGRADEABLE && item.isUpgradable() || 
						mode == Mode.UNIDENTIFED && !item.isIdentified() ||
						mode == Mode.WEAPON && (item instanceof MeleeWeapon || item instanceof Boomerang) ||
						mode == Mode.ARMOR && (item instanceof Armor) ||
						mode == Mode.ENCHANTABLE && (item instanceof MeleeWeapon || item instanceof Boomerang || item instanceof Armor) ||
						mode == Mode.WAND && (item instanceof Wand) && (WndBag.wandClass == null || ClassReflection.isInstance( WndBag.wandClass, item)) ||
						mode == Mode.SEED && (item instanceof Seed) ||
						mode == Mode.ALL
					);
				}
			} else {
				bg.color( NORMAL );
			}
		}
		
		@Override
		protected void onTouchDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.SND_CLICK, 0.7f, 0.7f, 1.2f );
		};
		
		protected void onTouchUp() {
			bg.brightness( 1.0f );
		};
		
		@Override
		protected void onClick() {
			if (listener != null) {
				
				hide();
				listener.onSelect( item );
				
			} else {

                if (NoosaInputProcessor.modifier) {
                    onLongClick();
                } else {
                    WndBag.this.add(new WndItem(WndBag.this, item));
                }
				
			}
		}
		
		@Override
		protected boolean onLongClick() {
			if (listener == null && item.defaultAction != null) {
				hide();
				if (QuickSlot.secondaryValue == null && QuickSlot.primaryValue != null && QuickSlot.tertiaryValue != null){
					QuickSlot.secondaryValue = item.stackable ? item.getClass() : item;
					QuickSlot.refresh();
				}
				else if (QuickSlot.tertiaryValue == null && QuickSlot.primaryValue != null && QuickSlot.secondaryValue != null){
					QuickSlot.tertiaryValue = item.stackable ? item.getClass() : item;
					QuickSlot.refresh();
				}
				else{
				QuickSlot.primaryValue = item.stackable ? item.getClass() : item;
				QuickSlot.refresh();
				}
				return true;
			} else {
				return false;
			}
		}
	}
	
	public interface Listener {
		void onSelect( Item item );
	}
}
