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
package com.retrodevxp.pixeldungeon.items;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.actors.blobs.Blob;
import com.retrodevxp.pixeldungeon.actors.blobs.ConfusionGas;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSpriteSheet;
import com.retrodevxp.utils.Random;

public class DizzyBomb extends Item {

	public static final float DURATION	= 3f;
	
	{
		name = "dizzy bomb";
		image = ItemSpriteSheet.DIZZYBOMB;
		
		defaultAction = AC_THROW;
		stackable = true;
	}
	
	public DizzyBomb() {
		this( 1 );
	}
	
	public DizzyBomb( int number ) {
		super();
		quantity = number;
	}
	
	
	@Override
	public void onThrow( int cell ) {
		Sample.INSTANCE.play( Assets.SND_BLAST, 0.3f );
		GameScene.add( Blob.seed( cell, 300, ConfusionGas.class ) );
	}

	@Override
	public boolean isIdentified() {
		return true;
	}
	
	@Override
	public String desc() {
		return 
			"This dizzy bomb activates when thrown, after its safety pin has been pulled. " +
			"It releases a cloud of gas that disorients any caught within it.";
	}
	
	@Override
	public Item random() {
		quantity = Random.Int( 2, 5 );
		return this;
	}
	
	@Override
	public int price() {
		return 12 * quantity;
	}
}
