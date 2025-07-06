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
package com.retrodevxp.pixeldungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.audio.Music;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.pixeldungeon.input.GameAction;
import com.retrodevxp.pixeldungeon.scenes.GameScene;
import com.retrodevxp.pixeldungeon.scenes.PixelScene;
import com.retrodevxp.pixeldungeon.scenes.TitleScene;
import com.retrodevxp.utils.PDPlatformSupport;
import com.retrodevxp.utils.Signal;

public class PixelDungeon extends Game<GameAction> {

	public PixelDungeon(final PDPlatformSupport<GameAction> platformSupport) {
		super( TitleScene.class, platformSupport );

		Game.version = platformSupport.getVersion();

		com.retrodevxp.utils.Bundle.addAlias(
			com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfUpgrade.class, 
			"com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfEnhancement" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.actors.blobs.WaterOfHealth.class, 
			"com.retrodevxp.pixeldungeon.actors.blobs.Light" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.rings.RingOfMending.class, 
			"com.retrodevxp.pixeldungeon.items.rings.RingOfRejuvenation" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.wands.WandOfReach.class, 
			"com.retrodevxp.pixeldungeon.items.wands.WandOfTelekenesis" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.actors.blobs.Foliage.class, 
			"com.retrodevxp.pixeldungeon.actors.blobs.Blooming" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.actors.buffs.Shadows.class, 
			"com.retrodevxp.pixeldungeon.actors.buffs.Rejuvenation" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfPsionicBlast.class, 
			"com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfNuclearBlast" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.actors.hero.Hero.class, 
			"com.retrodevxp.pixeldungeon.actors.Hero" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.actors.mobs.npcs.Shopkeeper.class,
			"com.retrodevxp.pixeldungeon.actors.mobs.Shopkeeper" );
		// 1.6.1
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.quest.DriedRose.class,
			"com.retrodevxp.pixeldungeon.items.DriedRose" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.actors.mobs.npcs.MirrorImage.class,
			"com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfMirrorImage$MirrorImage" );
		// 1.6.4
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.rings.RingOfElements.class,
			"com.retrodevxp.pixeldungeon.items.rings.RingOfCleansing" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.rings.RingOfElements.class,
			"com.retrodevxp.pixeldungeon.items.rings.RingOfResistance" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.weapon.missiles.Boomerang.class,
			"com.retrodevxp.pixeldungeon.items.weapon.missiles.RangersBoomerang" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.rings.RingOfPower.class,
			"com.retrodevxp.pixeldungeon.items.rings.RingOfEnergy" );
		// 1.7.2
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.plants.Dreamweed.class,
			"com.retrodevxp.pixeldungeon.plants.Blindweed" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.plants.Dreamweed.Seed.class,
			"com.retrodevxp.pixeldungeon.plants.Blindweed$Seed" );
		// 1.7.4
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.weapon.enchantments.Shock.class,
			"com.retrodevxp.pixeldungeon.items.weapon.enchantments.Piercing" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.weapon.enchantments.Shock.class,
			"com.retrodevxp.pixeldungeon.items.weapon.enchantments.Swing" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfEnchantment.class,
			"com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfWeaponUpgrade" );
		// 1.7.5
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfEnchantment.class,
			"com.retrodevxp.pixeldungeon.items.Stylus" );
		// 1.8.0
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.actors.mobs.FetidRat.class,
			"com.retrodevxp.pixeldungeon.actors.mobs.npcs.Ghost$FetidRat" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.plants.Rotberry.class,
			"com.retrodevxp.pixeldungeon.actors.mobs.npcs.Wandmaker$Rotberry" );
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.plants.Rotberry.Seed.class,
			"com.retrodevxp.pixeldungeon.actors.mobs.npcs.Wandmaker$Rotberry$Seed" );
		// 1.9.0
		com.retrodevxp.utils.Bundle.addAlias( 
			com.retrodevxp.pixeldungeon.items.wands.WandOfReach.class,
			"com.retrodevxp.pixeldungeon.items.wands.WandOfTelekinesis" );
		// 1.9.2
		com.retrodevxp.utils.Bundle.addAlias(
			com.retrodevxp.pixeldungeon.items.weapon.enchantments.Slowness.class, 
			"com.retrodevxp.pixeldungeon.items.weapon.enchantments.Slow");
	}
	
	@Override
	public void create() {
		super.create();
		
		boolean landscape = Gdx.graphics.getWidth() > Gdx.graphics.getHeight();

		final Preferences prefs = Preferences.INSTANCE;
		if (prefs.getBoolean(Preferences.KEY_LANDSCAPE, false) != landscape) {
			landscape( !landscape );
		}
		fullscreen( prefs.getBoolean(Preferences.KEY_WINDOW_FULLSCREEN, instance.getPlatformSupport().fullscreenDefault()) );
		
		Music.INSTANCE.enable( music() );
		Sample.INSTANCE.enable( soundFx() );
		
		Sample.INSTANCE.load( 
			Assets.SND_CLICK, 
			Assets.SND_BADGE, 
			Assets.SND_GOLD,
			
			Assets.SND_DESCEND,
			Assets.SND_STEP,
			Assets.SND_WATER,
			Assets.SND_OPEN,
			Assets.SND_UNLOCK,
			Assets.SND_ITEM,
			Assets.SND_DEWDROP, 
			Assets.SND_HIT, 
			Assets.SND_MISS,
			Assets.SND_EAT,
			Assets.SND_READ,
			Assets.SND_LULLABY,
			Assets.SND_DRINK,
			Assets.SND_SHATTER,
			Assets.SND_ZAP,
			Assets.SND_LIGHTNING,
			Assets.SND_LEVELUP,
			Assets.SND_DEATH,
			Assets.SND_CHALLENGE,
			Assets.SND_CURSED,
			Assets.SND_EVOKE,
			Assets.SND_TRAP,
			Assets.SND_TOMB,
			Assets.SND_ALERT,
			Assets.SND_MELD,
			Assets.SND_BOSS,
			Assets.SND_BLAST,
			Assets.SND_PLANT,
			Assets.SND_RAY,
			Assets.SND_BEACON,
			Assets.SND_TELEPORT,
			Assets.SND_CHARMS,
			Assets.SND_MASTERY,
			Assets.SND_PUFF,
			Assets.SND_ROCKS,
			Assets.SND_BURNING,
			Assets.SND_FALLING,
			Assets.SND_GHOST,
			Assets.SND_SECRET,
			Assets.SND_BONES,
			Assets.SND_BEE,
			Assets.SND_DEGRADE,
			Assets.SND_MIMIC );
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		Graphics.DisplayMode mode = Gdx.graphics.getDisplayMode();
		boolean maximized = width >= mode.width || height >= mode.height;

		if (!maximized && !isFullscreen()) {
			final Preferences prefs = Preferences.INSTANCE;
			prefs.put(Preferences.KEY_WINDOW_WIDTH, width);
			prefs.put(Preferences.KEY_WINDOW_HEIGHT, height);
		}
	}
	/*
	 * ---> Prefernces
	 */
	
	public static void landscape( boolean value ) {
		// FIXME
//		Game.instance.setRequestedOrientation( value ?
//			ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
//			ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
//		Preferences.INSTANCE.put( Preferences.KEY_LANDSCAPE, value );
	}
	
	public static boolean landscape() {
		return width > height;
	}

	public static void fullscreen(boolean value) {
		final Preferences prefs = Preferences.INSTANCE;
		final PDPlatformSupport ps = instance.getPlatformSupport();
		if (value) {
			prefs.put(Preferences.KEY_WINDOW_FULLSCREEN, true);
			ps.fullscreen();
		} else {
			int w = prefs.getInt(Preferences.KEY_WINDOW_WIDTH, Preferences.DEFAULT_WINDOW_WIDTH);
			int h = prefs.getInt(Preferences.KEY_WINDOW_HEIGHT, Preferences.DEFAULT_WINDOW_HEIGHT);
			prefs.put(Preferences.KEY_WINDOW_FULLSCREEN, false);
			ps.windowed(w, h);
		}
	}

	public static boolean isFullscreen() {
		return instance.getPlatformSupport().isFullscreen();
	}
	
	public static void scaleUp( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_SCALE_UP, value );
		switchScene( TitleScene.class );
	}
	
	public static boolean scaleUp() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SCALE_UP, true );
	}

	public static void zoom( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_ZOOM, value );
	}
	
	public static int zoom() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_ZOOM, 0 );
	}
	
	public static void music( boolean value ) {
		Music.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_MUSIC, value );
	}
	
	public static boolean music() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_MUSIC, instance.getPlatformSupport().musicDefault() );
	}
	
	public static void soundFx( boolean value ) {
		Sample.INSTANCE.enable( value );
		Preferences.INSTANCE.put( Preferences.KEY_SOUND_FX, value );
	}
	
	public static boolean soundFx() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_SOUND_FX, true );
	}
	
	public static void brightness( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_BRIGHTNESS, value );
		if (scene() instanceof GameScene) {
			((GameScene)scene()).brightness( value );
		}
	}
	
	public static boolean brightness() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_BRIGHTNESS, false );
	}
	
	public static void donated( String value ) {
		Preferences.INSTANCE.put( Preferences.KEY_DONATED, value );
	}
	
	public static String donated() {
		return Preferences.INSTANCE.getString( Preferences.KEY_DONATED, "" );
	}
	
	public static void lastClass( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_LAST_CLASS, value );
	}
	
	public static int lastClass() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_LAST_CLASS, 0 );
	}
	
	public static void challenges( int value ) {
		Preferences.INSTANCE.put( Preferences.KEY_CHALLENGES, value );
	}
	
	public static int challenges() {
		return Preferences.INSTANCE.getInt( Preferences.KEY_CHALLENGES, 0 );
	}
	
	public static void intro( boolean value ) {
		Preferences.INSTANCE.put( Preferences.KEY_INTRO, value );
	}
	
	public static boolean intro() {
		return Preferences.INSTANCE.getBoolean( Preferences.KEY_INTRO, true );
	}
	
	
	public static void switchNoFade( Class<? extends PixelScene> c ) {
		PixelScene.noFade = true;
		switchScene( c );
	}
	
	/*
	 * <--- Preferences
	 */
	
	public static void reportException( Exception e ) {
		Gdx.app.error("PD", e.getMessage(), e);
	}

	public void refreshDungeon(){
		try{
			Dungeon.hero = null;
			Dungeon.depth = 0;
			Game.instance.render();
		}
		catch(Exception e){
			
		}
	}

	// @Override
	// public void dispose(){
	// 	super.dispose();
	// 	try{
	// 		if(Dungeon.hero != null){
	// 		System.out.println("Disposing game");
	// 		Dungeon.saveAll();
	// 		}
	// 	}
	// 	catch(Exception e){
	// 		System.out.println("Exit:" + e.toString());
	// 	}
		
	// }
	
}
