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

import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.Image;
import com.retrodevxp.noosa.ui.Button;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.DungeonTilemap;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.Char;
import com.retrodevxp.pixeldungeon.actors.hero.Belongings;
import com.retrodevxp.pixeldungeon.actors.mobs.Mob;
import com.retrodevxp.pixeldungeon.input.GameAction;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.mechanics.Ballistica;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.windows.WndBag;
import com.retrodevxp.utils.Bundle;

public class QuickSlot extends Button<GameAction> implements WndBag.Listener {

	private static final String TXT_SELECT_ITEM = "Select an item for the quickslot";
	
	private static QuickSlot primary;
	private static QuickSlot secondary;
	private static QuickSlot tertiary;
	
	private Item itemInSlot;
	private ItemSlot slot;
	
	private Image crossB;
	private Image crossM;
	
	private boolean targeting = false;
	
	private static Char lastTarget= null;
	public static Object primaryValue;
	public static Object secondaryValue;
	public static Object tertiaryValue;
	
	public void primary() {
		primary = this;
		this.hotKey = GameAction.ADDQUICKSLOT;
		item( select() );
	}
	
	public void secondary() {
		secondary = this;
		this.hotKey = GameAction.ADDQUICKSLOT2;
		item( select() );
	}

	public void tertiary() {
		tertiary = this;
		this.hotKey = GameAction.ADDQUICKSLOT3;
		item( select() );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		
		if (this == primary) {
			primary = null;
		} else if (this == secondary){
			secondary = null;
		}
		else{
			tertiary = null;
		}
		
		lastTarget = null;
	}
	
	@Override
	protected void createChildren() {
		super.createChildren();
		// System.out.println("Quickslot" + this.toString());
		slot = new ItemSlot() {
			@Override
			protected void onClick() {

				if (NoosaInputProcessor.modifier) {
					onLongClick();
					return;
				}
				
				if (targeting) {
					GameScene.handleCell( lastTarget.pos );
				} else {
					useTargeting();
					select().execute( Dungeon.hero );
				}
			}
			@Override
			protected boolean onLongClick() {
				try{
				return QuickSlot.this.onLongClick();
				}
				catch(Exception e){
					return false;
				}
			}
			@Override
			protected void onTouchDown() {
				icon.lightness( 0.7f );
			}
			@Override
			protected void onTouchUp() {
				icon.resetColor();
			}
		};
		Dungeon.slots += 1;
			// if (Dungeon.slots <= 1){
			// 	slot.hotKey = GameAction.QUICKSLOT2;
			// 	System.out.println("Quickslot2");
			// }
			// else{
			// 	slot.hotKey = GameAction.QUICKSLOT;
			// 	System.out.println("Quickslot1");
			// }
			// if (this == primary){
			// 	slot.hotKey = GameAction.QUICKSLOT2;
			// 	System.out.println("Quickslot2?");
			// }
			// else{
			// 	slot.hotKey = GameAction.QUICKSLOT;
			// 	System.out.println("Quickslot1?");
			// }
			// Dungeon.gold = Dungeon.slots;
			
		// if (slot == primary){
		// 	slot.hotKey = GameAction.QUICKSLOT;
		// }
		// else{
		// 	slot.hotKey = GameAction.QUICKSLOT2;
		// }
		add( slot );
		
		crossB = Icons.TARGET.get();
		crossB.visible = false;
		add( crossB );
		
		crossM = new Image();
		crossM.copy( crossB );
	}
	
	@Override
	public boolean onKeyDown(NoosaInputProcessor.Key<GameAction> key) {

        switch (key.action) {
		case QUICKSLOT3:
            try{
				tertiary.selectCheck().execute( Dungeon.hero );
			}
			catch(Exception e){
				
			}
            return true;
		case QUICKSLOT2:
            try{
				primary.selectCheck().execute( Dungeon.hero );
			}
			catch(Exception e){
				
			}
            return true;
		case QUICKSLOT:
			try{
				secondary.selectCheck().execute( Dungeon.hero );
			}
			catch(Exception e){
			
			}
            return true;
		}
		return true;
        }
	
	@Override
	protected void layout() {
		super.layout();
		
		slot.fill( this );
		
		crossB.x = PixelScene.align( x + (width - crossB.width) / 2 );
		crossB.y = PixelScene.align( y + (height - crossB.height) / 2 );
	}
	
	@Override
	protected void onClick() {
        GameScene.selectItem( this, WndBag.Mode.QUICKSLOT, TXT_SELECT_ITEM );
	}
	
	@Override
	protected boolean onLongClick() {
		onClick();
		return true;
	}

	@SuppressWarnings("unchecked")
	public Item selectCheck(){
		Object content = (this == primary ? primaryValue : secondaryValue);
		if (this == tertiary){
			content = tertiaryValue;
		}
		if (content instanceof Item) {
			
			if (Dungeon.hero.belongings.backpack.items.contains(content) || Dungeon.hero.belongings.weapon == (content)){
				return (Item)content;
			}
			else{
				return null;
			}
			
		} else if (content != null) {
			
			Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)content );			
			return item != null ? item : null;
			
		} else {
			
			return null;
			
		}
	}
	
	@SuppressWarnings("unchecked")
	private Item select() {
		
		Object content = (this == primary ? primaryValue : secondaryValue);
		if (this == tertiary){
			content = tertiaryValue;
		}
		if (content instanceof Item) {
			
			return (Item)content;
			
		} else if (content != null) {
			
			Item item = Dungeon.hero.belongings.getItem( (Class<? extends Item>)content );			
			return item != null ? item : Item.virtual( (Class<? extends Item>)content );
			
		} else {
			
			return null;
			
		}
	}

	@Override
	public void onSelect( Item item ) {
		if (item != null) {
			if (this == primary) {
				primaryValue = (item.stackable ? item.getClass() : item);
			} else if (this == secondary) {
				secondaryValue = (item.stackable ? item.getClass() : item);
			}
			else if (this == tertiary) {
				tertiaryValue = (item.stackable ? item.getClass() : item);
			}
			refresh();
		}
	}
	
	public void item( Item item ) {
		slot.item( item );
		itemInSlot = item;
		enableSlot();
	}
	
	public void enable( boolean value ) {
		active = value;
		if (value) {
			enableSlot();
		} else {
			slot.enable( false );
		}
	}
	
	private void enableSlot() {
		slot.enable( 
			itemInSlot != null && 
			itemInSlot.quantity() > 0 && 
			(Dungeon.hero.belongings.backpack.contains( itemInSlot ) || itemInSlot.isEquipped( Dungeon.hero )));
	}
	
	private void useTargeting() {
		
		targeting = lastTarget != null && lastTarget.isAlive() && Dungeon.visible[lastTarget.pos];
		
		if (targeting) {
			int pos = Ballistica.cast( Dungeon.hero.pos, lastTarget.pos, false, true );
			if (pos != lastTarget.pos) {
				lastTarget = null;
				targeting = false;
			}
		}
		
		if (!targeting) {
			int n = Dungeon.hero.visibleEnemies();
			for (int i=0; i < n; i++) {
				Mob enemy = Dungeon.hero.visibleEnemy( i );
				int pos = Ballistica.cast( Dungeon.hero.pos, enemy.pos, false, true );
				if (pos == enemy.pos) { 
					lastTarget = enemy;
					targeting = true;
					break;
				}
			}
		}
		
		if (targeting) {
			if (Actor.all().contains( lastTarget )) {
				lastTarget.sprite.parent.add( crossM );
				crossM.point( DungeonTilemap.tileToWorld( lastTarget.pos ) );
				crossB.visible = true;
			} else {
				lastTarget = null;
			}
		}
	}
	
	public static void refresh() {
		if (primary != null) {
			primary.item( primary.select() );
		}
		if (secondary != null) {
			secondary.item( secondary.select() );
		}
		if (tertiary != null) {
			tertiary.item( tertiary.select() );
		}
	}
	
	public static void target( Item item, Char target ) {
		if (target != Dungeon.hero) {
			lastTarget = target;
			HealthIndicator.instance.target( target );
		}
	}
	
	public static void cancel() {
		if (primary != null && primary.targeting) {
			primary.crossB.visible = false;
			primary.crossM.remove();
			primary.targeting = false;
		}
		if (secondary != null && secondary.targeting) {
			secondary.crossB.visible = false;
			secondary.crossM.remove();
			secondary.targeting = false;
		}
		if (tertiary != null && tertiary.targeting) {
			tertiary.crossB.visible = false;
			tertiary.crossM.remove();
			tertiary.targeting = false;
		}
	}
	
	private static final String QUICKSLOT1	= "quickslot";
	private static final String QUICKSLOT2	= "quickslot2";
	private static final String QUICKSLOT3	= "quickslot3";
	
	@SuppressWarnings("unchecked")
	public static void save( Bundle bundle ) {
		Belongings stuff = Dungeon.hero.belongings;
		
		if (primaryValue instanceof Class && 
			stuff.getItem( (Class<? extends Item>)primaryValue ) != null) {
				
			bundle.put( QUICKSLOT1, ((Class<?>)primaryValue).getName() );
		}
		if (QuickSlot.secondaryValue instanceof Class &&
			stuff.getItem( (Class<? extends Item>)secondaryValue ) != null &&
			Toolbar.secondQuickslot()) {
					
			bundle.put( QUICKSLOT2, ((Class<?>)secondaryValue).getName() );
		}
		if (tertiaryValue instanceof Class && 
			stuff.getItem( (Class<? extends Item>)tertiaryValue ) != null) {
				
			bundle.put( QUICKSLOT3, ((Class<?>)tertiaryValue).getName() );
		}
	}
	
	public static void save( Bundle bundle, Item item ) {
		if (item == primaryValue) {
			bundle.put( QuickSlot.QUICKSLOT1, true );
		}
		if (item == secondaryValue && Toolbar.secondQuickslot()) {
			bundle.put( QuickSlot.QUICKSLOT2, true );
		}
		if (item == tertiaryValue) {
			bundle.put( QuickSlot.QUICKSLOT3, true );
		}
	}
	
	public static void restore( Bundle bundle ) {
		primaryValue = null;
		secondaryValue = null;
		tertiaryValue = null;
		
		String qsClass = bundle.getString( QUICKSLOT1 );
		if (qsClass != null) {
			try {
				primaryValue = ClassReflection.forName( qsClass );
			} catch (Exception e) {
			}
		}
		
		qsClass = bundle.getString( QUICKSLOT2 );
		if (qsClass != null) {
			try {
				secondaryValue = ClassReflection.forName( qsClass );
			} catch (Exception e) {
			}
		}

		qsClass = bundle.getString( QUICKSLOT3 );
		if (qsClass != null) {
			try {
				tertiaryValue = ClassReflection.forName( qsClass );
			} catch (Exception e) {
			}
		}
	}
	
	public static void restore( Bundle bundle, Item item ) {
		if (bundle.getBoolean( QUICKSLOT1 )) {
			primaryValue = item;
		}
		if (bundle.getBoolean( QUICKSLOT2 )) {
			secondaryValue = item;
		}
		if (bundle.getBoolean( QUICKSLOT3 )) {
			tertiaryValue = item;
		}
	}
	
	public static void compress() {
		if ((primaryValue == null && secondaryValue != null) ||
			(primaryValue == secondaryValue)) {
				
			primaryValue = secondaryValue;
			secondaryValue = null;
		}
	}
}
