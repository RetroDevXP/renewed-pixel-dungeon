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

import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.actors.hero.HeroClass;
import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.ui.HighlightedText;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.noosa.BitmapText;
import com.retrodevxp.noosa.BitmapTextMultiline;
import com.retrodevxp.noosa.Group;

public class WndClass extends WndTabbed {
	
	private static final String TXT_MASTERY	= "Mastery";
	
	private static final int WIDTH			= 110;
	
	private static final int TAB_WIDTH	= 50;
	
	private HeroClass cl;
	
	private PerksTab tabPerks;
	private MasteryTab tabMastery;
	
	public WndClass( HeroClass cl ) {
		
		super();
		
		this.cl = cl;
		
		tabPerks = new PerksTab();
		add( tabPerks );
		
		Tab tab = new RankingTab( Utils.capitalize( cl.title() ), tabPerks );
		tab.setSize( TAB_WIDTH, tabHeight() );
		add( tab );
		
		if (Badges.isUnlocked( cl.masteryBadge() )) {
			tabMastery = new MasteryTab();
			add( tabMastery );
			
			tab = new RankingTab( TXT_MASTERY, tabMastery );
			tab.setSize( TAB_WIDTH, tabHeight() );
			add( tab );
			
			resize( 
				(int)Math.max( tabPerks.width, tabMastery.width ), 
				(int)Math.max( tabPerks.height, tabMastery.height ) );
		} else {
			resize( (int)tabPerks.width, (int)tabPerks.height );
		}
		
		select( 0 );
	}

	public class RankingTab extends LabeledTab {
		
		private Group page;
		
		public RankingTab( String label, Group page ) {
			super( label );
			this.page = page;
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}
	
	public class PerksTab extends Group {
		
		private static final int MARGIN	= 4;
		private static final int GAP	= 4;
		
		private static final String DOT	= "\u007F";
		
		public float height;
		public float width;
		
		public PerksTab() {
			super();
			
			float dotWidth = 0;
			
			String[] items = cl.perks();
			float pos = MARGIN;
			
			for (int i=0; i < items.length; i++) {
				
				if (i > 0) {
					pos += GAP;
				}
				
				BitmapText dot = PixelScene.createText( DOT, 6 );
				dot.x = MARGIN;
				dot.y = pos;
				if (dotWidth == 0) {
					dot.measure();
					dotWidth = dot.width();
				}
				add( dot );
				
				BitmapTextMultiline item = PixelScene.createMultiline( items[i], 6 );
				item.x = dot.x + dotWidth;
				item.y = pos;
				item.maxWidth = (int)(WIDTH - MARGIN * 2 - dotWidth);
				item.measure();
				add( item );
				
				pos += item.height();
				float w = item.width();
				if (w > width) {
					width = w;
				}
			}
			
			width += MARGIN + dotWidth;
			height = pos + MARGIN;
		}
	}
	
	public class MasteryTab extends Group {
		
		private static final int MARGIN	= 4;

		public float height;
		public float width;
		
		public MasteryTab() {
			super();
			
			String message = null;
			switch (cl) {
			case WARRIOR:
				message = HeroSubClass.GLADIATOR.desc() + "\n\n" + HeroSubClass.BERSERKER.desc();
				break;
			case MAGE:
				message = HeroSubClass.BATTLEMAGE.desc() + "\n\n" + HeroSubClass.WARLOCK.desc();
				break;
			case ROGUE:
				message = HeroSubClass.FREERUNNER.desc() + "\n\n" + HeroSubClass.SPY.desc();
				break;
			case HUNTRESS:
				message = HeroSubClass.DEADEYE.desc() + "\n\n" + HeroSubClass.WARDEN.desc();
				break;
			}

			HighlightedText text = new HighlightedText( 6 );
			text.text( message, WIDTH - MARGIN * 2 );
			text.setPos( MARGIN, MARGIN );
			add( text );
			
			height = text.bottom() + MARGIN;
			width = text.right() + MARGIN;
		}
	}
}
