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

import com.retrodevxp.pixeldungeon.Chrome;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.ui.Window;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.BitmapTextMultiline;
import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.TouchArea;
import com.retrodevxp.utils.SparseArray;

public class WndStory extends Window {

	private static final int WIDTH = 120;
	private static final int MARGIN = 6;
	
	private static final float bgR	= 0.77f;
	private static final float bgG	= 0.73f;
	private static final float bgB	= 0.62f;
	
	public static final int ID_SEWERS		= 0;
	public static final int ID_PRISON		= 1;
	public static final int ID_CAVES		= 2;
	public static final int ID_METROPOLIS	= 3;
	public static final int ID_HALLS		= 4;
	
	private static final SparseArray<String> CHAPTERS = new SparseArray<String>();
	
	static {
		CHAPTERS.put( ID_SEWERS, 
		"The Dungeon lies right beneath the City, its upper levels actually constitute the City's sewer system. " +
		"Being nominally a part of the City, these levels are not that dangerous. No one will call it a safe place, " +
		"but at least you won't need to deal with evil magic here." );
		
		CHAPTERS.put( ID_PRISON, 
		"Many years ago an underground prison was built here for the most dangerous criminals. At the time it seemed " +
		"like a very clever idea, because this place indeed was very hard to escape. But soon dark miasma started to permeate " +
		"from below. Some prisoners and guards who weren't able to escape became corrupted into undead. In the end the prison was abandoned, though some convicts " +
		"were left locked up here." );
		
		CHAPTERS.put( ID_CAVES, 
		"The caves, which stretch down under the abandoned prison, are sparcely populated. They lie too deep to be exploited " +
		"by the City and they are too poor in minerals to interest the dwarves. In the past there was a trade outpost " +
		"somewhere here on the route between these two states, but it has perished since the decline of Dwarven Metropolis. " +
		"Only omnipresent gnolls and subterranean animals dwell here now." );
		
		CHAPTERS.put( ID_METROPOLIS, 
		"Dwarven Metropolis was once the greatest of dwarven city-states. It, however, steeply declined " +
		"after the invasion of the old god and his demon army. Even though the dwarves successfully repelled the demons, " +
		"they suffered great losses to their numbers and their city, and that victory was the beginning of the end for the underground kingdom." );
		
		CHAPTERS.put( ID_HALLS,
		"In the past these levels were the deepest parts of the Metropolis. After the costly victory in the war with the old god, the " +
		"dwarves were too weakened to eradiate all of the remaining demons. Gradually, demons have tightened their grip on this place, taking the old city as their own,  " +
		"and it is now called the Demon Halls.\n\n" +
		"Very few adventurers have ever descended this far..." );
	};
	
	private BitmapTextMultiline tf;
	
	private float delay;
	
	public WndStory( String text ) {
		super( 0, 0, Chrome.get( Chrome.Type.SCROLL ) );
		
		tf = PixelScene.createMultiline( text, 7 );
		tf.maxWidth = WIDTH - MARGIN * 2;
		tf.measure();
		tf.ra = bgR;
		tf.ga = bgG;
		tf.ba = bgB;
		tf.rm = -bgR;
		tf.gm = -bgG;
		tf.bm = -bgB;
		tf.x = MARGIN;
		add( tf );
		
		add( new TouchArea( chrome ) {
			@Override
			protected void onClick( NoosaInputProcessor.Touch touch ) {
				hide();
			}
		} );
		
		resize( (int)(tf.width() + MARGIN * 2), (int)Math.min( tf.height(), 180 ) );
	}
	
	@Override
	public void update() {
		super.update();
		
		if (delay > 0 && (delay -= Game.elapsed) <= 0) {
			shadow.visible = chrome.visible = tf.visible = true;
		}
	}
	
	public static void showChapter( int id ) {
		
		if (Dungeon.chapters.contains( id )) {
			return;
		}
		
		String text = CHAPTERS.get( id );
		if (text != null) {
			WndStory wnd = new WndStory( text );
			if ((wnd.delay = 0.6f) > 0) {
				wnd.shadow.visible = wnd.chrome.visible = wnd.tf.visible = false;
			}
			
			Game.scene().add( wnd );
			
			Dungeon.chapters.add( id );
		}
	}
}
