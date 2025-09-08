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
package com.retrodevxp.pixeldungeon.scenes;

import java.io.IOException;
import java.util.ArrayList;

import com.badlogic.gdx.utils.IntMap;
import com.retrodevxp.input.NoosaInputProcessor;
import com.retrodevxp.noosa.Camera;
import com.retrodevxp.noosa.Game;
import com.retrodevxp.noosa.Group;
import com.retrodevxp.noosa.SkinnedBlock;
import com.retrodevxp.noosa.Visual;
import com.retrodevxp.noosa.audio.Music;
import com.retrodevxp.noosa.audio.Sample;
import com.retrodevxp.noosa.particles.Emitter;
import com.retrodevxp.pixeldungeon.Assets;
import com.retrodevxp.pixeldungeon.Badges;
import com.retrodevxp.pixeldungeon.Dungeon;
import com.retrodevxp.pixeldungeon.DungeonTilemap;
import com.retrodevxp.pixeldungeon.FogOfWar;
import com.retrodevxp.pixeldungeon.PixelDungeon;
import com.retrodevxp.pixeldungeon.Statistics;
import com.retrodevxp.pixeldungeon.actors.Actor;
import com.retrodevxp.pixeldungeon.actors.blobs.Blob;
import com.retrodevxp.pixeldungeon.actors.buffs.Buff;
import com.retrodevxp.pixeldungeon.actors.buffs.Invisibility;
import com.retrodevxp.pixeldungeon.actors.hero.HeroSubClass;
import com.retrodevxp.pixeldungeon.actors.mobs.Mob;
import com.retrodevxp.pixeldungeon.effects.BannerSprites;
import com.retrodevxp.pixeldungeon.effects.BlobEmitter;
import com.retrodevxp.pixeldungeon.effects.EmoIcon;
import com.retrodevxp.pixeldungeon.effects.Flare;
import com.retrodevxp.pixeldungeon.effects.FloatingText;
import com.retrodevxp.pixeldungeon.effects.Ripple;
import com.retrodevxp.pixeldungeon.effects.SpellSprite;
import com.retrodevxp.pixeldungeon.items.Heap;
import com.retrodevxp.pixeldungeon.items.Item;
import com.retrodevxp.pixeldungeon.items.SmokeBomb;
import com.retrodevxp.pixeldungeon.items.armor.ClothArmor;
import com.retrodevxp.pixeldungeon.items.armor.Armor.Glyph;
import com.retrodevxp.pixeldungeon.items.armor.glyphs.Stealth;
import com.retrodevxp.pixeldungeon.items.potions.Potion;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfExperience;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfHealing;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfInvisibility;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfLiquidFlame;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfParalyticGas;
import com.retrodevxp.pixeldungeon.items.potions.PotionOfToxicGas;
import com.retrodevxp.pixeldungeon.items.rings.RingOfAccuracy;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfIdentify;
import com.retrodevxp.pixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.retrodevxp.pixeldungeon.items.wands.WandOfBlink;
import com.retrodevxp.pixeldungeon.items.weapon.melee.Sword;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.Dart;
import com.retrodevxp.pixeldungeon.items.weapon.missiles.WoodenDart;
import com.retrodevxp.pixeldungeon.levels.Level;
import com.retrodevxp.pixeldungeon.levels.RegularLevel;
import com.retrodevxp.pixeldungeon.levels.features.Chasm;
import com.retrodevxp.pixeldungeon.plants.Hardthorn;
import com.retrodevxp.pixeldungeon.plants.Icecap;
import com.retrodevxp.pixeldungeon.plants.Plant;
import com.retrodevxp.pixeldungeon.sprites.CharSprite;
import com.retrodevxp.pixeldungeon.sprites.DiscardedItemSprite;
import com.retrodevxp.pixeldungeon.sprites.HeroSprite;
import com.retrodevxp.pixeldungeon.sprites.ItemSprite;
import com.retrodevxp.pixeldungeon.sprites.PlantSprite;
import com.retrodevxp.pixeldungeon.ui.AttackIndicator;
import com.retrodevxp.pixeldungeon.ui.Banner;
import com.retrodevxp.pixeldungeon.ui.BusyIndicator;
import com.retrodevxp.pixeldungeon.ui.GameLog;
import com.retrodevxp.pixeldungeon.ui.HealthIndicator;
import com.retrodevxp.pixeldungeon.ui.QuickSlot;
import com.retrodevxp.pixeldungeon.ui.StatusPane;
import com.retrodevxp.pixeldungeon.ui.Toast;
import com.retrodevxp.pixeldungeon.ui.Toolbar;
import com.retrodevxp.pixeldungeon.ui.Window;
import com.retrodevxp.pixeldungeon.utils.GLog;
import com.retrodevxp.pixeldungeon.windows.WndBag;
import com.retrodevxp.pixeldungeon.windows.WndGame;
import com.retrodevxp.pixeldungeon.windows.WndHero;
import com.retrodevxp.pixeldungeon.windows.WndInfoCell;
import com.retrodevxp.pixeldungeon.windows.WndInfoItem;
import com.retrodevxp.pixeldungeon.windows.WndInfoMob;
import com.retrodevxp.pixeldungeon.windows.WndInfoPlant;
import com.retrodevxp.pixeldungeon.windows.WndMessage;
import com.retrodevxp.pixeldungeon.windows.WndOptions;
import com.retrodevxp.pixeldungeon.windows.WndStory;
import com.retrodevxp.pixeldungeon.windows.WndTradeItem;
import com.retrodevxp.pixeldungeon.windows.WndBag.Mode;
import com.retrodevxp.utils.Random;

public class GameScene extends PixelScene {
	
	private static final String TXT_WELCOME			= "Welcome to level %d of Pixel Dungeon!";
	private static final String TXT_WELCOME_BACK	= "Welcome back to level %d of Pixel Dungeon!";
	private static final String TXT_NIGHT_MODE		= "Be cautious, since the dungeon is even more dangerous at night!";
	
	private static final String TXT_CHASM	= "Your steps echo across the dungeon.";
	private static final String TXT_WATER	= "You hear the water splashing around you.";
	private static final String TXT_GRASS	= "The smell of vegetation is thick in the air.";
	private static final String TXT_SECRETS	= "The atmosphere hints that this floor hides many secrets.";
	
	static GameScene scene;
	
	private SkinnedBlock water;
	private DungeonTilemap tiles;
	private FogOfWar fog;
	private HeroSprite hero;
	
	private GameLog log;
	
	private BusyIndicator busy;
	
	private static CellSelector cellSelector;
	
	private Group terrain;
	private Group ripples;
	private Group plants;
	private Group heaps;
	private Group mobs;
	private Group emitters;
	private Group effects;
	private Group gases;
	private Group spells;
	private Group statuses;
	private Group emoicons;
	
	private Toolbar toolbar;
	private Toast prompt;
	
	private String itemA;
	private String itemB;
	private String itemC;
	private String itemD;
	private String itemE;
	
	@Override
	public void create() {
		Music.INSTANCE.volume( 1f * (PixelDungeon.musicVolume() * 0.01f) );
		Music.INSTANCE.play( Assets.TUNE, true );
		Music.INSTANCE.volume( 1f * (PixelDungeon.musicVolume() * 0.01f) );
		System.out.println("Game volume");
		
		PixelDungeon.lastClass( Dungeon.hero.heroClass.ordinal() );
		
		super.create();
		Camera.main.zoom( defaultZoom + PixelDungeon.zoom() );
		
		scene = this;

		terrain = new Group();
		add( terrain );
		
		water = new SkinnedBlock( 
			Level.WIDTH * DungeonTilemap.SIZE, 
			Level.HEIGHT * DungeonTilemap.SIZE,
			Dungeon.level.waterTex() );
		water.autoAdjust = true;
		terrain.add( water );
		
		ripples = new Group();
		terrain.add( ripples );
		
		tiles = new DungeonTilemap();
		terrain.add( tiles );
		
		Dungeon.level.addVisuals( this );
		
		plants = new Group();
		add( plants );

		for (IntMap.Entry<Plant> plant : Dungeon.level.plants) {
			addPlantSprite( plant.value );
		}
		
		heaps = new Group();
		add( heaps );

		for (IntMap.Entry<Heap> heap : Dungeon.level.heaps) {
			addHeapSprite( heap.value );
		}

		emitters = new Group();
		effects = new Group();
		emoicons = new Group();

		mobs = new Group();
		add( mobs );
		
		for (Mob mob : Dungeon.level.mobs) {
			addMobSprite( mob );
			if (Statistics.amuletObtained) {
				mob.beckon( Dungeon.hero.pos );
			}
		}
		
		add( emitters );
		add( effects );
		
		gases = new Group();
		add( gases );
		
		for (Blob blob : Dungeon.level.blobs.values()) {
			blob.emitter = null;
			addBlobSprite( blob );
		}
		
		fog = new FogOfWar( Level.WIDTH, Level.HEIGHT );
		fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
		add( fog );
		
		brightness( PixelDungeon.brightness() );
		
		spells = new Group();
		add( spells );
		
		statuses = new Group();
		add( statuses );
		
		add( emoicons );
		
		hero = new HeroSprite();
		hero.place( Dungeon.hero.pos );
		hero.updateArmor();
		mobs.add( hero );

		add( new HealthIndicator() );
		
		add( cellSelector = new CellSelector( tiles ) );
		
		StatusPane sb = new StatusPane();
		sb.camera = uiCamera;
		sb.setSize( uiCamera.width, 0 );
		add( sb );
		
		toolbar = new Toolbar();
		toolbar.camera = uiCamera;
		toolbar.setRect( 0,uiCamera.height - toolbar.height(), uiCamera.width, toolbar.height() );
		add( toolbar );
		
		AttackIndicator attack = new AttackIndicator();
		attack.camera = uiCamera;
		attack.setPos( 
			uiCamera.width - attack.width(), 
			toolbar.top() - attack.height() );
		add( attack );
		
		log = new GameLog();
		log.camera = uiCamera;
		log.setRect( 0, toolbar.top(), attack.left(),  0 );
		add( log );
		
		busy = new BusyIndicator();
		busy.camera = uiCamera;
		busy.x = 1;
		busy.y = sb.bottom() + 1;
		add( busy );
		
		switch (InterlevelScene.mode) {
		case RESURRECT:
			WandOfBlink.appear( Dungeon.hero, Dungeon.level.entrance );
			new Flare( 8, 32 ).color( 0xFFFF66, true ).show( hero, 2f ) ;
			break;
		case RETURN:
			WandOfBlink.appear(  Dungeon.hero, Dungeon.hero.pos );
			break;
		case FALL:
			Chasm.heroLand();
			break;
		case DESCEND:
			switch (Dungeon.depth) {
			case 1:
				WndStory.showChapter( WndStory.ID_SEWERS );
				extraitems(Dungeon.hero.heroClass.toString());
				break;
			case 6:
				WndStory.showChapter( WndStory.ID_PRISON );
				break;
			case 11:
				WndStory.showChapter( WndStory.ID_CAVES );
				break;
			case 16:
				WndStory.showChapter( WndStory.ID_METROPOLIS );
				break;
			case 22:
				WndStory.showChapter( WndStory.ID_HALLS );
				break;
			}
			if (Dungeon.hero.isAlive() && Dungeon.depth != 22) {
				Badges.validateNoKilling();
			}
			break;
		default:
		}
		
		ArrayList<Item> dropped = Dungeon.droppedItems.get( Dungeon.depth );
		if (dropped != null) {
			for (Item item : dropped) {
				int pos = Dungeon.level.randomRespawnCell();
				if (item instanceof Potion) {
					((Potion)item).shatter( pos );
				} else if (item instanceof Plant.Seed) {
					Dungeon.level.plant( (Plant.Seed)item, pos );
				} else {
					Dungeon.level.drop( item, pos );
				}
			}
			Dungeon.droppedItems.remove( Dungeon.depth );
		}
		
		Camera.main.target = hero;

		if (InterlevelScene.mode != InterlevelScene.Mode.NONE) {
			if (Dungeon.depth < Statistics.deepestFloor) {
				GLog.h( TXT_WELCOME_BACK, Dungeon.depth );
			} else {
				GLog.h( TXT_WELCOME, Dungeon.depth );
				Sample.INSTANCE.play( Assets.SND_DESCEND );
			}
			if (Dungeon.hero.subClass == HeroSubClass.CRAWLER){
				// Buff.affect( Dungeon.hero, Invisibility.class, Invisibility.DURATION );
				int countcrawl = 0;
				for (Mob mob : Dungeon.level.mobs) {
						if (Level.fieldOfView[mob.pos]) {
							countcrawl += 1;
						}
					}
					if (countcrawl == 0){
						// if (Dungeon.hero.buffs(Invisibility.class) == null){
						Buff.affect( Dungeon.hero, Invisibility.class, 5 );
						// }
					}
			}
			switch (Dungeon.level.feeling) {
				case CHASM:
					GLog.w( TXT_CHASM );
					break;
				case WATER:
					GLog.w( TXT_WATER );
					break;
				case GRASS:
					GLog.w( TXT_GRASS );
					break;
				default:
			}
			if (Dungeon.level instanceof RegularLevel &&
					((RegularLevel) Dungeon.level).secretDoors > Random.IntRange( 3, 4 )) {
				GLog.w( TXT_SECRETS );
			}
			if (Dungeon.nightMode && !Dungeon.bossLevel()) {
				GLog.w( TXT_NIGHT_MODE );
			}

			InterlevelScene.mode = InterlevelScene.Mode.NONE;

			fadeIn();
		}
        selectCell( defaultCellListener );

	}
	public void extraitems(String heroclass){
		switch (heroclass){
			case "WARRIOR":
				itemA = "Wooden Darts x 10";
				itemB = "Extra Sword";
				itemC = "An unknown potion";
				itemD = "Wooden Darts x5";
				itemE = "Wooden Darts x5";
				break;
			case "ROGUE":
				itemA = "Darts x 3";
				itemB = "Smoke Bomb x 5";
				itemC = "Armor of Stealth";
				itemD = "Wooden Darts x5";
				itemE = "Wooden Darts x5";
				break;
			case "MAGE":
				itemA = "Scroll of Identify";
				itemB = "Icecap x 3";
				itemC = "Armor of Charging";
				itemD = "Wooden Darts x5";
				itemE = "Wooden Darts x5";
				break;
			case "HUNTRESS":
				itemA = "Darts x 5";
				itemB = "Ring of Accuracy";
				itemC = "Hardthorn x 5";
				itemD = "Wooden Darts x5";
				itemE = "Wooden Darts x5";
				break;
			default:
				itemA = "Darts x 3";
				itemB = "Smoke Bomb x 3";
				itemC = "An unknown potion";
				itemD = "Wooden Darts x5";
				itemE = "Wooden Darts x5";
				break;

		}
		//Currently has 3 choices. Might expand into 5 choices.
		GameScene.show( 
			new WndOptions( "Which item did you prepare?", "Select one", itemA, itemB, itemC ) {
							@Override
							protected void onSelect(int index) {
								switch (heroclass){
									case "WARRIOR":
									if (index == 0) {
										new WoodenDart( 10 ).identify().collect();
										QuickSlot.secondaryValue = WoodenDart.class;
									}
									else if (index == 1) {
										new Sword().identify().collect();
									}
									else if (index == 2) {
										int unknownPotion = Random.Int( 1, 5 );
										switch (unknownPotion){
											case 1:
												new PotionOfHealing().collect();
												break;
											case 2:
												new PotionOfInvisibility().collect();
												break;
											case 3:
												new PotionOfLiquidFlame().collect();
												break;
											case 4:
												new PotionOfParalyticGas().collect();
												break;
											case 5:
												new PotionOfToxicGas().collect();
												break;
											default:
												new PotionOfExperience().collect();
												break;
										}
									}
										break;
									case "ROGUE":
									if (index == 0) {
										new Dart( 3 ).identify().collect();
										QuickSlot.secondaryValue = Dart.class;
									}
									else if (index == 1) {
										new SmokeBomb( 5 ).identify().collect();
										QuickSlot.secondaryValue = SmokeBomb.class;
									}
									else if (index == 2) {
										ClothArmor StealthArmor = new ClothArmor();
										StealthArmor.inscribestealth();
										StealthArmor.identify();
										// StealthArmor.identify().collect();
										Dungeon.hero.belongings.armor = StealthArmor;
									}
										break;
									case "MAGE":
									if (index == 0) {
										new ScrollOfIdentify().identify().collect();
										QuickSlot.primaryValue = ScrollOfIdentify.class;
									}
									else if (index == 1) {
										for (int i = 0; i < 3; i++){
											new Icecap.Seed().identify().collect();
										}
									}
									else if (index == 2) {
										ClothArmor ChargeArmor = new ClothArmor();
										ChargeArmor.inscribecharge();
										ChargeArmor.identify();
										// ChargeArmor.identify().collect();
										Dungeon.hero.belongings.armor = ChargeArmor;
									}
										break;
									case "HUNTRESS":
									if (index == 0) {
										new Dart( 5 ).identify().collect();
										QuickSlot.primaryValue = Dart.class;
									}
									else if (index == 1) {
										new RingOfAccuracy().identify().collect();
									}
									else if (index == 2) {
										for (int i = 0; i < 5; i++){
											new Hardthorn.Seed().identify().collect();
										}
									}
										break;
									default:
									if (index == 0) {
										new WoodenDart( 5 ).identify().collect();
									}
									else if (index == 1) {
										new WoodenDart( 5 ).identify().collect();
									}
									else if (index == 2) {
										new WoodenDart( 5 ).identify().collect();
									}
										break;
						
								
							};
						}
					}
					);
	}
	
	public void destroy() {
		
		scene = null;
		Badges.saveGlobal();
		
		super.destroy();
	}
	
	@Override
	public synchronized void pause() {
		try {
			Dungeon.saveAll();
			Badges.saveGlobal();
		} catch (IOException e) {
			//
		}
	}
	
	@Override
	public synchronized void update() {
		if (Dungeon.hero == null) {
			return;
		}
			
		super.update();
		
		water.offset( 0, -4 * Game.elapsed );
		
		Actor.process();
		
		if (Dungeon.hero.ready && !Dungeon.hero.paralysed) {
			log.newLine();
		}
		
		cellSelector.enabled = Dungeon.hero.ready;
	}
	
	@Override
	protected void onBackPressed() {
		if (!cancel()) {
			add( new WndGame() );
		}
	}
	
	@Override
	protected void onMenuPressed() {
		if (Dungeon.hero.ready) {
			selectItem( null, WndBag.Mode.ALL, null );
		}
	}

	public void brightness( boolean value ) {
		water.rm = water.gm = water.bm = 
		tiles.rm = tiles.gm = tiles.bm = 
			value ? 1.5f : 1.0f;
		if (value) {
			fog.am = +2f;
			fog.aa = -1f;
		} else {
			fog.am = +1f;
			fog.aa =  0f;
		}
	}
	
	private void addHeapSprite( Heap heap ) {
		ItemSprite sprite = heap.sprite = (ItemSprite)heaps.recycle( ItemSprite.class );
		sprite.revive();
		sprite.link( heap );
		heaps.add( sprite );
	}
	
	private void addDiscardedSprite( Heap heap ) {
		heap.sprite = (DiscardedItemSprite)heaps.recycle( DiscardedItemSprite.class );
		heap.sprite.revive();
		heap.sprite.link( heap );
		heaps.add( heap.sprite );
	}
	
	private void addPlantSprite( Plant plant ) {
		(plant.sprite = (PlantSprite)plants.recycle( PlantSprite.class )).reset( plant );
	}
	
	private void addBlobSprite( final Blob gas ) {
		if (gas.emitter == null) {
			gases.add( new BlobEmitter( gas ) );
		}
	}
	
	private void addMobSprite( Mob mob ) {
		CharSprite sprite = mob.sprite();
		sprite.visible = Dungeon.visible[mob.pos];
		mobs.add( sprite );
		sprite.link( mob );
	}
	
	private void prompt( String text ) {
		
		if (prompt != null) {
			prompt.killAndErase();
			prompt = null;
		}
		
		if (text != null) {
			prompt = new Toast( text ) {
				@Override
				protected void onClose() {
					cancel();
				}
			};
			prompt.camera = uiCamera;
			prompt.setPos( (uiCamera.width - prompt.width()) / 2, uiCamera.height - 60 );
			add( prompt );
		}
	}
	
	private void showBanner( Banner banner ) {
		banner.camera = uiCamera;
		banner.x = align( uiCamera, (uiCamera.width - banner.width) / 2 );
		banner.y = align( uiCamera, (uiCamera.height - banner.height) / 3 );
		add( banner );
	}
	
	// -------------------------------------------------------
	
	public static void add( Plant plant ) {
		if (scene != null) {
			scene.addPlantSprite( plant );
		}
	}
	
	public static void add( Blob gas ) {
		Actor.add( gas );
		if (scene != null) {
			scene.addBlobSprite( gas );
		}
	}
	
	public static void add( Heap heap ) {
		if (scene != null) {
			scene.addHeapSprite( heap );
		}
	}
	
	public static void discard( Heap heap ) {
		if (scene != null) {
			scene.addDiscardedSprite( heap );
		}
	}
	
	public static void add( Mob mob ) {
		Dungeon.level.mobs.add( mob );
		Actor.add( mob );
		Actor.occupyCell( mob );
		scene.addMobSprite( mob );
	}
	
	public static void add( Mob mob, float delay ) {
		Dungeon.level.mobs.add( mob );
		Actor.addDelayed( mob, delay );
		Actor.occupyCell( mob );
		scene.addMobSprite( mob );
	}
	
	public static void add( EmoIcon icon ) {
		scene.emoicons.add( icon );
	}
	
	public static void effect( Visual effect ) {
		scene.effects.add( effect );
	}
	
	public static Ripple ripple( int pos ) {
		Ripple ripple = (Ripple)scene.ripples.recycle( Ripple.class );
		ripple.reset( pos );
		return ripple;
	}
	
	public static SpellSprite spellSprite() {
		return (SpellSprite)scene.spells.recycle( SpellSprite.class );
	}
	
	public static Emitter emitter() {
		if (scene != null) {
			Emitter emitter = (Emitter)scene.emitters.recycle( Emitter.class );
			emitter.revive();
			return emitter;
		} else {
			return null;
		}
	}
	
	public static FloatingText status() {
		return scene != null ? (FloatingText)scene.statuses.recycle( FloatingText.class ) : null;
	}
	
	public static void pickUp( Item item ) {
		scene.toolbar.pickup( item );
	}
	
	public static void updateMap() {
		if (scene != null) {
			scene.tiles.updated.set( 0, 0, Level.WIDTH, Level.HEIGHT );
		}
	}
	
	public static void updateMap( int cell ) {
		if (scene != null) {
			scene.tiles.updated.union( cell % Level.WIDTH, cell / Level.WIDTH );
		}
	}
	
	public static void discoverTile( int pos, int oldValue ) {
		if (scene != null) {
			scene.tiles.discover( pos, oldValue );
		}
	}
	
	public static void show( Window wnd ) {
		cancelCellSelector();
		scene.add( wnd );
	}
	
	public static void afterObserve() {
		if (scene != null) {
			scene.fog.updateVisibility( Dungeon.visible, Dungeon.level.visited, Dungeon.level.mapped );
			
			for (Mob mob : Dungeon.level.mobs) {
				mob.sprite.visible = Dungeon.visible[mob.pos];
			}
		}
	}
	
	public static void flash( int color ) {
		scene.fadeIn( 0xFF000000 | color, true );
	}
	
	public static void gameOver() {
		Banner gameOver = new Banner( BannerSprites.get( BannerSprites.Type.GAME_OVER ) );
		gameOver.show( 0x000000, 1f );
		scene.showBanner( gameOver );
		
		Sample.INSTANCE.play( Assets.SND_DEATH );
	}
	
	public static void bossSlain() {
		if (Dungeon.hero.isAlive()) {
			Banner bossSlain = new Banner( BannerSprites.get( BannerSprites.Type.BOSS_SLAIN ) );
			bossSlain.show( 0xFFFFFF, 0.3f, 5f );
			scene.showBanner( bossSlain );
			
			Sample.INSTANCE.play( Assets.SND_BOSS );
		}
	}
	
	public static void handleCell( int cell ) {
		cellSelector.select( cell );
	}
	
	public static void selectCell( CellSelector.Listener listener ) {
		cellSelector.listener = listener;
		scene.prompt( listener.prompt() );
	}
	
	private static boolean cancelCellSelector() {
		if (cellSelector.listener != null && cellSelector.listener != defaultCellListener) {
			cellSelector.cancel();
			return true;
		} else {
			return false;
		}
	}
	
	public static WndBag selectItem( WndBag.Listener listener, WndBag.Mode mode, String title ) {
		WndBag wnd;
		cancelCellSelector();
		
		switch (mode) {
			case WAND:
				wnd = WndBag.wandHolster(listener, mode, title);
				break;
			case SEED:
				wnd = WndBag.seedPouch(listener, mode, title);
				break;
			default:
				wnd = WndBag.lastBag(listener, mode, title);
				break;
		}

		scene.add( wnd );
		
		return wnd;
	}

	static boolean cancel() {
		if (Dungeon.hero.curAction != null || Dungeon.hero.restoreHealth) {
			
			Dungeon.hero.curAction = null;
			Dungeon.hero.restoreHealth = false;
			return true;
			
		} else {
			
			return cancelCellSelector();
			
		}
	}
	
	public static void ready() {
		selectCell( defaultCellListener );
		QuickSlot.cancel();
	}

    public static void examineCell( Integer cell ) {
        if (cell == null) {
            return;
        }

        if (cell < 0 || cell > Level.LENGTH || (!Dungeon.level.visited[cell] && !Dungeon.level.mapped[cell])) {
            GameScene.show( new WndMessage( "You don't know what is there." ) ) ;
            return;
        }

        if (!Dungeon.visible[cell]) {
            GameScene.show( new WndInfoCell( cell ) );
            return;
        }

        if (cell == Dungeon.hero.pos) {
            GameScene.show( new WndHero() );
            return;
        }

        Mob mob = (Mob)Actor.findChar( cell );
        if (mob != null) {
            GameScene.show( new WndInfoMob( mob ) );
            return;
        }

        Heap heap = Dungeon.level.heaps.get( cell );
        if (heap != null) {
            if (heap.type == Heap.Type.FOR_SALE && heap.size() == 1 && heap.peek().price() > 0) {
                GameScene.show( new WndTradeItem( heap, false ) );
            } else {
                GameScene.show( new WndInfoItem( heap ) );
            }
            return;
        }

        Plant plant = Dungeon.level.plants.get( cell );
        if (plant != null) {
            GameScene.show( new WndInfoPlant( plant ) );
            return;
        }

        GameScene.show( new WndInfoCell( cell ) );
    }
	
	private static final CellSelector.Listener defaultCellListener = new CellSelector.Listener() {
		@Override
		public void onSelect( Integer cell ) {
			if (NoosaInputProcessor.modifier) {
				examineCell( cell );
			} else {
				if (Dungeon.hero.handle(cell)) {
					Dungeon.hero.next();
				}
			}
		}
		@Override
		public String prompt() {
			return null;
		}
	};
}
