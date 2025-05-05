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
package com.retrodevxp.pixeldungeon.actors.hero;

import com.retrodevxp.utils.Bundle;

public enum HeroSubClass {

	NONE( null, null ),
	
	GLADIATOR( "gladiator", 
		"A successful attack with a melee weapon allows the _Gladiator_ to start a combo, " +
		"in which every next successful hit inflicts more damage." ),
	BERSERKER( "berserker", 
		"The more enemies surround the _Berserker_, the faster he performs his attacks. " +
		"When severely wounded, the _Berserker_ enters a state of wild fury " +
		"significantly increasing his damage output." ),
	KNIGHT( "knight", 
		"The _Knight_ defends well against strong attacks, receiving reduced damage. " +
		"The _Knight_ is also stronger in a duel, dealing more damage if there aren't any other enemies surrounding him." ),
	
	WARLOCK( "warlock", 
		"The _Warlock_ extracts energy from the soul of defeated enemies. " +
		"It heals his wounds and satisfies his hunger." ),
	BATTLEMAGE( "battlemage", 
		"When fighting with a wand in his hands, the _Battlemage_ inflicts additional damage depending " +
		"on the current number of charges. Every successful hit restores 1 charge to this wand." ),
	SCRIBE( "scribe", 
		"With his proficiency, the _Scribe_ is more resistant or even immune to most negative effects from enchantments. " +
		"When he upgrades an enchanted item, the enchantment will never be removed despite the collision of different magic." ),
	
	SPY( "spy", 
		"Being proficient at detection, the _Spy_ senses enemies even if there are obtacles in the way." +
		" When performing a surprise attack, he inflicts additional damage to his target." ),
	FREERUNNER( "freerunner", 
		"The _Freerunner_ can move much faster than most of the monsters. When he " +
		"is running, the Freerunner is much harder to hit. For that he must be unencumbered and not starving." ),
	CRAWLER( "crawler", 
		"The _Crawler_ is stealthier than other classes. When he becomes invisible, he stays invisible longer. " +
		"The _Cralwer_ stealthily enters each depth, becoming invisible for a duration." ),
		
	DEADEYE( "sniper", 
		"_Deadeyes_ are able to precisely detect weak points in an enemy's armor, ignoring it completely when using a missile weapon. " +
		"_Deadeyes_ locks on to a target for a quicker next attack when using a missile weapon." ),
	WARDEN( "warden", 
		"Having a strong connection with forces of nature gives _Wardens_ an ability to gather dewdrops and " +
		"seeds from plants. Also trampling a high grass grants them a temporary armor buff. _Wardens_ also regenerate health slightly quicker." ),
	CHASER( "chaser", 
		"The thrill of the hunt grants the _Chaser_ a movement speed boost " +
		"whenever attacking enemies with a missile weapon." );
		
	
	private String title;
	private String desc;
	
	private HeroSubClass( String title, String desc ) {
		this.title = title;
		this.desc = desc;
	}
	
	public String title() {
		return title;
	}
	
	public String desc() {
		return desc;
	}
	
	private static final String SUBCLASS	= "subClass";
	
	public void storeInBundle( Bundle bundle ) {
		bundle.put( SUBCLASS, toString() );
	}
	
	public static HeroSubClass restoreInBundle( Bundle bundle ) {
		String value = bundle.getString( SUBCLASS );
		try {
			return valueOf( value );
		} catch (Exception e) {
			return NONE;
		}
	}
	
}
