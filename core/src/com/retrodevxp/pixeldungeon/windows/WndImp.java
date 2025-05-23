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

import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.actors.hero.Hero;
import com.retrodevxp.pixeldungeon.actors.mobs.npcs.Imp;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.quest.DwarfToken;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.ui.RedButton;
import com.retrodevxp.pixeldungeon.ui.Window;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.utils.Utils;
import com.retrodevxp.noosa.BitmapTextMultiline;

public class WndImp extends WndQuest {
	
	private static final String TXT_MESSAGE	= 
		"Oh yes! You are my hero!\n" +
		"Regarding your reward, I don't have cash with me right now, but I have something better for you. " +
		"This is my family heirloom ring: my granddad took it off a dead paladin's finger.";
	private static final String TXT_REWARD		= "Take the ring";
	
	private Imp imp;
	private DwarfToken tokens;
	
	public WndImp( final Imp imp, final DwarfToken tokens ) {
		super(imp, TXT_MESSAGE, TXT_REWARD);
        this.imp = imp;
        this.tokens = tokens;
	}
	
	@Override
    protected void onSelect(int index) {
        tokens.detachAll( Dungeon.hero.belongings.backpack );

        Item reward = Imp.Quest.reward;
        reward.identify();

        if (reward.doPickUp(Dungeon.hero)) {
            GLog.i(Hero.TXT_YOU_NOW_HAVE, reward.name());
        } else {
            Dungeon.level.drop(reward, imp.pos).sprite.drop();
        }

        imp.flee();
        Imp.Quest.complete();
    }
}
