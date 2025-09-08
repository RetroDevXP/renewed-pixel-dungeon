Renewed Pixel Dungeon
=================

Renewed Pixel Dungeon adds or changes many features from Vanilla.

This mod includes many new features, such as a new subclass for each class, new extra item choice mechanic, melee weapons dealing damage when thrown (to different degrees of effectiveness), some thrown weapons being reusable, changed hunger system, new melee weapons, new thrown weapons, new items, new plants, new wand, new enchants, new enemy, etc.

This file is for the features of 1.0.0 as well as the full list of changes made in later updates. Please see each version's sections.

# 1.3.0

Platforms
----------

Renewed Pixel Dungeon is out for Android on Google Play Store!

The APK is also available on itch.io directly!

Classes
----------

The Dungeon
----------


Calculations
----------


Challenges
----------


Items, Companions, and Enchants
----------

Added new weapons: Sai and Falx. Sai is a tier-3 weapon with weak damage but a very fast attack speed, great for activating Enchantments! Falx is a tier-4 weapon that ignores part of the enemy's defense!


Enemies and NPCs
----------


Hunger System
----------



Buffs
----------



UI, Lore, and Others
----------

UI size in landscape mode isn't as small.

Slightly adjusted portrait mode UI, hopefully fixing the overlapping buttons that occurs on some devices.

Fixed layout issues on Landscape mode where the rankings details don't fit in the screen. Currently, quickslot items don't show in landscape mode Rankings, however.

Slightly improved Surprise Attack effects to be more noticeable.

Added adjustable Sound FX and Music volumes.

Added 6 new Badges.

Adjusted Badges layout.

Added titles for each Badge.

Changed some more texts.

Revised the older version changes specified in this file for more details and readability.


# 1.2.0

Platforms
----------

Renewed Pixel Dungeon is coming to Android soon!

Classes
----------

Battlemage melee wand damage is weakened, from 1 extra damage per charge to 0.5 extra damage per charge, to balance out the extra effects he gets when the wand is at full charges.

When the Battlemage attacks enemies with a fully charged Wand of Firebolt in melee, he now has a less chance to set his target on fire.

Spy bonus damage on surprise attack changed from 1-(100% of damage) to (10% of damage)-(75% of damage). This makes it less reliant on RNG. While the max damage is decreased, the min damage is increased, especially deep into the dungeon where an unlucky 1 bonus damage doesn't do much.

Fixed a feature in this file that was not mentioned in the last update (Berserker receives less damage while berserk).

The Dungeon
----------

Altar rooms are barricaded in. Mobs are now only beckoned to the sacrificial fire when there is something in the fire. This is to prevent all enemies on the level from gathering in the altar room.

Calculations
----------

To slightly decrease combat luck, the RNG for armor in damage calculation is changed from 0%-100% to 20%-100%. Attacks no longer ignore the armor entirely if unlucky. This affects enemies as well as the hero.

The Health Regeneration is increased from 1 to 1 + (Max HP / 30). This is for the regeneration to be stronger late game, where the hero might struggle to regenerate health without enough Sungrass or Potions of Healing. It is also more consistent with the health decrease from starving being more severe.

Challenges
----------

This game is changed to be slightly less difficult. However, there are new challenges added to make it more difficult. This allows you to customize your own difficulty more. Challenges are unlocked upon winning with any class at least once.

Added new challenge: Dire dungeon, makes enemies deal more damage.

Added new challenge: Undurable items: Reactivates the vanilla durability mechanic.

On Diet changed to Unnutritious. This challenge has been changed from no food drops, to food being 1/3 as effective, since not having any food drop is way too punishing with the changed hunger system.

Items, Companions, and Enchants
----------

When using a Wand of Poison on an already poisoned enemy, it now deals some extra damage. This also applies to the Battlemage using this wand in melee with a max charge. This makes investing in it stronger, while also balancing out some more enemies now being immune to poison.

Added new weapon: Cutlass. This tier-2 weapon is fast and accurate, and is even faster when slashing wildly while being surrounded by multiple enemies!


Enemies and NPCs
----------

Many enemies deals slightly less damage. To make the game more difficult, activate the Dire dungeon challenge. This gives you a choice of customizing your own difficulty to an extent. With Dire Dungeon, some enemies are as strong as previously, while others are even stronger!

Dwarf Brawler (formerly Senior Monk) damage increased from 12-20 to 15-21, but has their paralysis chance reduced from 1/10 to 1/15 to reduce the chances of them "stun-locking" the hero. Being a rare enemy, it also grants increased EXP from 15 to 17.

King of Dwarves deals less max damage from 35 to 32. 

Undead dwarves deal less damage from 12-16 to 10-15, and their paralysis chance is down from 1/5 to 1/10 to reduce the chances of them "stun-locking" the hero.

To balance out, King of Dwarves' max army size increased from 5 to 7.

Gnoll Brute deals less max damage while berserk from 40 to 36.

Evileye deals less damage from 14-20 to 12-18. 

Scorpio deals less damage from 20-32 to 18-28. 

Rotting Fist deals less damage from 24-36 to 20-32. 

Burning Fist deals less damage from 20-32 to 18-28. 

Gnoll Shaman deals less max zap damage from 12 to 11.

DM-300 deals less damage from 16-24 to 15-23. 

Golem deals less max damage from 40 to 36.

Dwarf Warlock deals less zap max damage from 18 to 17.

Goo is now immune to poison, and is now also completely immune to toxic gas instead of just having a resistance.

DM-300 is now immune to poison.

Golems are now immune to poison and toxic gas.

Old Wandmaker is now immune to Scrolls of Wipe Out, mainly for lore reasons.


Hunger System
----------

Ascending or Descending a floor uses less hunger, from 1/10 to 1/20.


Buffs
----------



UI, Lore, and Others
----------

Fixed a bug where quickslot items that were dropped are still usable with the usage of hotkeys.

Fixed a bug with not properly unloading quickslots when returning to main menu, causing crashes and other bugs.

You can now assign melee weapons to quickslots, to throw them.

Fixed thrown melee weapons not having a sound effect when it hits.

Added some effects to indicate surprise attacks.

Fixed the order of some keys in keybindings.

Removed the non-functional "swap quickslots" in key bindings. It was a planned feature to have a hotkey that swaps the items between the quickslots.

Changed some layouts to handle different screen sizes.

Changed some more texts.

Changed the layout of this file slightly for better readability.

Updated Gradle version from 8.9 to 8.11 in preparation of Android port.

Changed LibGDX version.

# 1.1.0

Classes
----------

Berserker receives slightly less damage when berserk. This effect is stronger when he has less than 10% HP remaining.

Knights now have indicators when their ability activates. Also has roundings in its formula for a smoother ability strength.

When a Warlock tries to use out-of-charge wands, he can extract energy from his own soul to be converted to a wand charge. This costs significant HP. If he does not have enough HP for it, he charges the wand anyways but becomes crippled at 1 HP. To charge wands this way, he must spend at least some HP. It won't work if he is already at 1 HP when trying to use it.

When the Battlemage charges a wand already at full charges by attacking enemies using that wand as a melee weapon, the excess energy from the wand now has a chance to cause an extra effect. This effect differs for each wand. The higher the wand's level, the more often this effect occurs. Most effects tend to be stronger based on the wand's level as well. 

Scribes are immune to the blindness effect from Scrolls of Psionic Blasts. Might have a complete overhaul for this subclass later.

Crawler has been changed. He now also becomes invisible when going behind a door without any enemies in sight. His invisibility when entering a depth now also only occurs without any enemies in sight as well.

Fixed Deadeye's name in some cases.

Chaser has been changed. Instead of the "thrill of the hunt" buff being obtained upon damaging enemies with a ranged weapon, the buff is now obtained by damaging an already wounded enemy. Using a ranged weapon for this leads to a longer buff duration. The "thrill of the hunt" buff also grants a slight damage increase as well.

Items, Companions, and Enchants
----------

Whip has been changed to reach multiple tiles (currently the only weapon with this effect). It deals less damage but is more accurate.

Added a new weapon: Flail, has the old stats of Whip.

You can actually retract Brandistocks now, trading some damage for speed.

Spirit Wolves are often ignored by enemies already adjacent to the hero, the enemy continues to focus on the Hero. This is so they won't protect the hero too well.

Spirit Wolf deals less damage, have less accuracy, dodges less often, and has less HP. By extension, it also doesn't last as long since its duration is based on HP.

Spirit Wolf's damage output is now partially based on its current HP. Weakened Spirit Wolves deals less damage.

Modified Spirit Wolf's sprite since it was hard to see when it attacks.

Culling enchantment has a changed formula. This enchantment is also weaker on boss depths.

More Scrolls of Upgrades and Potions of Strength generates in the Demon Halls, encouraging you to explore it more.

Bulbush Fruits are now also cooked when burning.


Enemies and NPCs
----------

Gnoll Scouts are less rare deeper into the dungeon. When a Gnoll Socut is damaged, it alerts other Gnolls to your position.

Swarm Of Flies deals slightly more damage.

Hunger System
----------

All wands take longer to charge while starving.

Buffs
----------

Thrill of the hunt also gives a slight damage increase. The way to obtain this buff is different.

UI, Lore, and Others
----------

Added a 3rd quickslot. If you already played version 1.0.0, you might need to manually bind the key of this quickslot. Otherwise it defaults to "E".

Slightly modified Tome of Mastery UI size to potentially handle longer subclass descriptions.

There are descriptions for enchantments now.

Changed some more texts.

# 1.0.0

Classes
----------
Added a new subclass for each class.

Warrior's subclass, Knight, specializes in duels as well as dealing with strong enemies.

Rogue's subclass, Crawler, utilizes stealth to his advantage.

Mage's subclass, Scribe, specializes in enchants.

Huntress' subclass, Chaser, has the mobility to chase or escape while using ranged weapons.

The Assassin subclass has been changed to Spy.

Spy (formerly Assassin) also senses enemies behind obstacles.

The Sniper subclass has been changed to Deadeye.

Deadeye (formerly Sniper) is more accurate with thrown weapons, including thrown melee weapons.

Wardens have slightly quicker HP Regneration.

Mages are less likely to have their enchantments removed when upgrading an enchanted item.

Rogues start with 15 points of health. However, their subclasses benefits greatly from some mechanic changes in this mod. For example, the abundance of food keeps the Freerunner's ability activated. Rogues also have rather strong extra items.

Huntresses identify Potions of Paralytic Gas from the beginning.

It is not required to defeat DM-300 to unlock Huntress.

Items, Companions, and Enchants
----------
Added extra items, a mechanic that grants you an item from a choice of 3 when starting each game. Each class have different choices.

Darts that some classes start with are instead now a choice in extra items.

Some weapons have custom strength and damage stats instead of being based on a fixed formula.

Melee weapons deal damage when thrown, with its own accuracy formula which includes several factors such as identified, cursed, etc. Each weapon have different effectiveness as a thrown weapon. Each weapon also have different required strengths to be thrown properly. This might be different from its required strength to equip.

Removed item degradation.

Added new weapon enchantments: Culling, Binding, and Initializing. Culling deals more damage to already injured enemies. The more damaged they are, the stronger then enchantment. Binding has a chance to bind an enemy in place, preventing it from moving. Initializing (might have a better name in a future update) deals more damage to enemies at full health, being a stronger opening strike.

Changed Tempered enchantment to simply deal slightly more damage, since item degradation is removed.

Added new Armor enchantments: Stealth and Charging. Stealth has a chance to make its wearer invisible upon taking damage. Charging has a chance to charge the wearer's wands upon taking damage.

Removed Self-Repair enchantment since degradation is removed.

Some thrown weapons are reusable. Others, such as Curare Dart, aren't reusable.

Added new melee weapons: Brandistock, Spiked Knuckles, Dwarven Axe, Club, Whip, and Guandao.

Added new thrown weapons: Wooden Dart, Frost Dart, and Serrated Spike. Wooden Darts are similar to the old metallic darts. The metallic darts are instead reusable in this mod. Frost Darts freeze the target. Serrated Spikes are rare weapons dropped by Scorpio.

Added new thrown items: Fire Bomb, Dizzy Bomb, and Smoke Bomb. Fire Bomb deals damage and sets an area on fire. Dizzy Bomb releases gas that makes everything in the area dizzy. Smoke Bomb releases gas that blinds everything in the area.

Added new Wands: Wand of Spirits and Wand of Blindness. Wand of Spirits summons a Spirit Wolf to help fight enemies. Wand of Blindness blinds the target.

Added new companion, Spirit Wolf, summoned by the Wand of Spirits.

Some balance adjustments to some weapon stats.

Added new Plants: The Bulbush and the Hardthorn. The Bulbush drops a food. The Hardthorn cripples any who steps on it while also making them bleed.

Added new food: The Bulbush fruit, along with its cooked versions.


Enemies and NPCs
----------

Added new enemy: Dwarven Warrior, encountered in the Dwarven Metropolis. A rare enemy with a powerful attack.

Golems deal more damage but are slower, allowing you to run from them.

Bats actually have slightly more accuracy at night (midnight - 7 a.m.) though this won't have much of an effect on the difficulty.

Slightly modified Goo's effect when it affects you with its ooze.

Goo's Pumped up attack is slightly weakened.

DM-300 has a limit when healing itself.

DM-300 takes some damage from being frozen, and more damage from being burned.

Scorpio's serrated spikes are sometimes dropped to be usable as a thrown weapon.

Gnoll Scouts also spawn all the way to Caves. More of a lore reason than a balance reason. They are, however, not common after the sewers.

Gnoll Shamans drop scrolls more often.

Bats drop potions of healing slightly more often, not game-changing though.

Sewer Crabs, Spinners, and Scorpio drops food more often.

...and other balance changes, such as stats of some enemies. In general, late-game is slightly less difficult.

Hunger System
----------

It takes much longer to starve. By extension, each food also keeps you satiated for longer.

However, the penalty for starving is much more severe. Instead of losing 1 health, you lose health based on a percentage of your remaining HP. You also deal significantly less damage when starving.

Buffs
----------
Blindness also decreases accuracy of attacks.

Added new buff: Thrill of the hunt, exclusive to the Chaser subclass.

UI, Lore, and Others
----------

There are always 2 quickslots available. Each has their own hotkey to use, as well as their own hotkey to assign items to.

Added Donate page to Main Menu.

Added an in-game guide page to Main Menu.

Modded the Logo.

Modded the icon.

Changed a lot of texts.

Easter Eggs? Totally didn't add any Easter Eggs. None.

End of Features. Thank you for playing Renewed Pixel Dungeon.


