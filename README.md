#
## Overview
This project implements a modular, object-oriented Java framework for board games, including:
- **Classic Monsters & Heroes** 
- **Legends of Valor** 

## Legends of Valor
Legends of Valor is a turn-based, grid-based strategy RPG inspired by MOBA-style gameplay.  
The game is built on top of the *Monsters & Heroes* framework and extends it with a board-based map, lane mechanics, terrain effects, hero actions, monster AI, and Nexus-based victory conditions.

Players control a team of heroes, navigate the board strategically, manage combat and inventory, and attempt to reach the enemy Nexus before monsters reach theirs.

## Student Information
- Ankita Patra (U38177365)
- Ying Huang (U13787608)
- Xiju Jiang (U03732023)

## File Structure
src/
├── Board/                    # Board, tiles, lanes, terrain, positions
│   ├── LegendsOfValorBoard.java
│   ├── LegendsTile.java
│   ├── TerrainType.java
│   ├── Position.java
│   └── ...
├── general/                  # Game loop, controllers, combat, views
│   ├── LegendsOfValorGame.java
│   ├── GameController.java
│   ├── DefaultDamageCalculator.java
│   └── ...
├── Heroes/                   # Hero units, hero actions, hero types
│   ├── Hero.java
│   ├── HeroUnit.java
│   ├── MoveAction.java
│   ├── AttackAction.java
│   └── ...
├── Items/                    # Weapons, armors, spells, potions
│   ├── Weapon.java
│   ├── Armor.java
│   ├── Spell.java
│   └── Potion.java
├── Monsters/                 # Monsters, monster units, AI behavior
│   ├── Monster.java
│   ├── MonsterUnit.java
│   ├── DefaultMonsterBehavior.java
│   └── ...
└── Main.java                 # Program entry point

## File Information
### Board
| `Board.java`| Abstract or base board class that defines common board behaviors and validation utilities.|
| `LegendsOfValorBoard.java`| Concrete implementation of the board for Legends of Valor, including Nexus placement and lane layout. |
| `LegendsTile.java`| Represents a single tile on the board, storing terrain type and possible hero/monster occupancy.|
| `Tile.java`| Generic tile abstraction used for board elements.|
| `TileType.java`| Enum defining logical tile categories (e.g., normal, obstacle, nexus).|
| `Lane.java`| Enum representing the three lanes used in the game.|
| `Position.java`| Immutable row–column coordinate class used to locate units on the board.|
| `TerrainType.java`| Enum defining terrain types such as Plain, Bush, Cave, Koulou, Obstacle, and Inaccessible.|
| `TerrainEffect.java`| Encapsulates attribute multipliers applied by a specific terrain type.|
| `TerrainEffectFactory.java`| Factory class that returns the appropriate `TerrainEffect` based on terrain type.|
| `Weather.java`| Represents weather conditions that may globally affect gameplay or combat calculations.|

### General
| `Game.java`| Abstract base class defining the structure of a game (initialization, loop, and termination).|
| `LegendsOfValorGame.java`| Main game loop implementation for Legends of Valor, handling rounds, turns, and victory conditions.|
| `GameController.java`| Coordinates input handling, game state updates, and interaction between model and view.|
| `GameView.java`| Interface defining how the game state should be displayed.|
| `ConsoleGameView.java`| Console-based implementation of `GameView` for rendering the board and game status.|
| `Battle.java`| Handles localized combat logic between heroes and monsters. |
| `DamageCalculator.java`| Interface defining damage computation for hero and monster attacks.|
| `DefaultDamageCalculator.java`| Default implementation of combat damage formulas, including armor and terrain effects.|
| `Character.java`| Abstract superclass for all living entities (heroes and monsters), storing HP, level, and name.|
| `Inventory.java`| Stores and manages all items owned by a hero (weapons, armors, spells, potions).|
| `Market.java`| Handles item purchasing and selling interactions.|
| `DataLoader.java`| Loads heroes, monsters, and items from configuration or data files. |
| `MonstersAndHeroesGame.java`| Legacy game mode reused from the original Monsters & Heroes project.|

### Heroes

| `Hero.java`| Abstract base class for all heroes, extending `Character` with attributes, inventory, and leveling logic.|
| `Warrior.java`| Concrete hero class specializing in strength-based combat. |
| `Sorcerer.java`| Concrete hero class specializing in spell-based combat. |
| `Paladin.java`| Hybrid hero class balancing strength and magic. |
| `HeroUnit.java`| Wrapper class binding a `Hero` to a board position and lane.|
| `HeroStat.java`| Encapsulates hero attribute values and derived statistics.|
| `Party.java`| Represents a collection of heroes acting as a team.|
| `HeroFactoryAdapter.java`| Adapter class for loading hero instances from the Monsters & Heroes framework.|
| `HeroAction.java`| Interface defining a single hero action during a turn.|
| `MoveAction.java`| Allows a hero to move to an adjacent valid tile.|
| `AttackAction.java`| Handles physical attacks against adjacent monsters.|
| `CastSpellAction.java`| Allows a hero to cast spells on monsters in range.|
| `UsePotionAction.java`| Allows a hero to consume a potion and apply its effects.|
| `EquipAction.java`| Allows a hero to equip a weapon or armor.|
| `TeleportAction.java`| Allows a hero to teleport to another lane near a teammate. |
| `RecallAction.java`| Sends a hero back to the Hero Nexus.|
| `PassAction.java`| Allows a hero to skip their turn without acting.|

### Items
| `Item.java`| Abstract base class for all items with name, level requirement, and price. |
| `Weapon.java`| Represents weapons that increase hero physical damage.|
| `Armor.java`| Represents armor that reduces incoming monster damage.|
| `Spell.java`| Represents magical spells that heroes can cast in combat.|
| `SpellType.java`| Enum defining different categories of spells.|
| `Potion.java`| Consumable items that restore HP, mana, or boost attributes.|

### Monsters
| `Monster.java`| Abstract base class for all monsters, extending `Character` with defense and damage attributes. |
| `Dragon.java`| Concrete monster class with high damage and defense. |
| `Exoskeleton.java`| Concrete monster class emphasizing strong armor.|
| `Spirit.java`| Concrete monster class emphasizing agility and dodge chance. |
| `MonsterUnit.java`| Wrapper class binding a `Monster` to a board position and lane. |
| `MonsterBehavior.java`| Interface defining monster AI decision-making. |
| `DefaultMonsterBehavior.java`| Default monster AI: attack if in range, otherwise move forward.|

### Main

| `Main.java` | Entry point.  Create a new GameController to call `controller.startGame()` to launch the game selection menu. |


## How to compile and run
-----------------------------------------------------------------------------------------------------------------------------------
•   Save all the code in a file named Main.java.
•   Open a terminal/command prompt in the same directory.
•   Compile the code:Javac  Main.java
•   Run the program:java Main

### Requirements
- Java 8 

### Compile
```bash
javac Main.java

### Input&Output

 🎮 GAME MODE MENU 🎮        
1) 🧙 Classic Monsters & Heroes
2) ⚔️ Legends of Valor
3) 🚪 Exit Game
👉 Choose your adventure : Enter(1/2) to play and for Exit enter 3 
2

⚔ Starting Legends of Valor ⚔

🧙 Choose 3 heroes to defend the realm:
0) 🚪 Quit Game
1) Gaerdal_Ironhand (lvl 1, HP 100.0, STR 700.0, DEX 600.0, AGI 500.0)
2) Sehanine_Moonbow (lvl 1, HP 100.0, STR 700.0, DEX 500.0, AGI 800.0)
3) Muamman_Duathall (lvl 1, HP 100.0, STR 900.0, DEX 750.0, AGI 500.0)
4) Flandal_Steelskin (lvl 1, HP 100.0, STR 750.0, DEX 700.0, AGI 650.0)
5) Undefeated_Yoj (lvl 1, HP 100.0, STR 800.0, DEX 700.0, AGI 400.0)
6) Eunoia_Cyn (lvl 1, HP 100.0, STR 700.0, DEX 600.0, AGI 800.0)
7) Parzival (lvl 1, HP 100.0, STR 750.0, DEX 700.0, AGI 650.0)
8) Sehanine_Moonbow (lvl 1, HP 100.0, STR 750.0, DEX 700.0, AGI 700.0)
9) Skoraeus_Stonebones (lvl 1, HP 100.0, STR 650.0, DEX 350.0, AGI 600.0)
10) Garl_Glittergold (lvl 1, HP 100.0, STR 600.0, DEX 400.0, AGI 500.0)
11) Amaryllis_Astra (lvl 1, HP 100.0, STR 500.0, DEX 500.0, AGI 500.0)
12) Caliber_Heist (lvl 1, HP 100.0, STR 400.0, DEX 400.0, AGI 400.0)
13) Rillifane_Rallathil (lvl 1, HP 100.0, STR 750.0, DEX 500.0, AGI 450.0)
14) Segojan_Earthcaller (lvl 1, HP 100.0, STR 800.0, DEX 650.0, AGI 500.0)
15) Reign_Havoc (lvl 1, HP 100.0, STR 800.0, DEX 800.0, AGI 800.0)
16) Reverie_Ashels (lvl 1, HP 100.0, STR 800.0, DEX 400.0, AGI 700.0)
17) Kalabar (lvl 1, HP 100.0, STR 850.0, DEX 600.0, AGI 400.0)
18) Skye_Soar (lvl 1, HP 100.0, STR 700.0, DEX 500.0, AGI 400.0)
👉 Select hero #1:
0) Quit Game
Enter a number displayed : 
> 
🛣️ Assign a lane to Gaerdal_Ironhand:
0) Quit Game
1) TOP
2) MIDDLE
3) BOTTOM
choose an option in range (0-3)
>
⚔️ === LEGENDS OF VALOR === ⚔️

🔔 --- ROUND 1 BEGINS --- 🔔

⚔️ Hero Turn → Gaerdal_Ironhand | Position: (7,0) | Lane: TOP

N - N - N N - N - N I - I - I N - N - N N - N - N I - I - I N - N - N N - N - N
|       | |       | |  XXX  | |       | |       | |  XXX  | |       | |       |
|       | |M1     | |       | |       | |M2     | |       | |       | |M3     |
N - N - N N - N - N I - I - I N - N - N N - N - N I - I - I N - N - N N - N - N
B - B - B B - B - B I - I - I B - B - B K - K - K I - I - I P - P - P C - C - C
|       | |       | |  XXX  | |       | |       | |  XXX  | |       | |       |
|       | |       | |       | |       | |       | |       | |       | |       |
B - B - B B - B - B I - I - I B - B - B K - K - K I - I - I P - P - P C - C - C
O - O - O B - B - B I - I - I P - P - P B - B - B I - I - I P - P - P K - K - K
|       | |       | |  XXX  | |       | |       | |  XXX  | |       | |       |
|       | |       | |       | |       | |       | |       | |       | |       |
O - O - O B - B - B I - I - I P - P - P B - B - B I - I - I P - P - P K - K - K
B - B - B P - P - P I - I - I O - O - O P - P - P I - I - I P - P - P C - C - C
|       | |       | |  XXX  | |       | |       | |  XXX  | |       | |       |
|       | |       | |       | |       | |       | |       | |       | |       |
B - B - B P - P - P I - I - I O - O - O P - P - P I - I - I P - P - P C - C - C
P - P - P P - P - P I - I - I P - P - P K - K - K I - I - I O - O - O O - O - O
|       | |       | |  XXX  | |       | |       | |  XXX  | |       | |       |
|       | |       | |       | |       | |       | |       | |       | |       |
P - P - P P - P - P I - I - I P - P - P K - K - K I - I - I O - O - O O - O - O
C - C - C C - C - C I - I - I P - P - P K - K - K I - I - I P - P - P K - K - K
|       | |       | |  XXX  | |       | |       | |  XXX  | |       | |       |
|       | |       | |       | |       | |       | |       | |       | |       |
C - C - C C - C - C I - I - I P - P - P K - K - K I - I - I P - P - P K - K - K
B - B - B O - O - O I - I - I B - B - B P - P - P I - I - I C - C - C P - P - P
|       | |       | |  XXX  | |       | |       | |  XXX  | |       | |       |
|       | |       | |       | |       | |       | |       | |       | |       |
B - B - B O - O - O I - I - I B - B - B P - P - P I - I - I C - C - C P - P - P
N - N - N N - N - N I - I - I N - N - N N - N - N I - I - I N - N - N N - N - N
|       | |       | |  XXX  | |       | |       | |  XXX  | |       | |       |
|H1     | |       | |       | |H2     | |       | |       | |H3     | |       |
N - N - N N - N - N I - I - I N - N - N N - N - N I - I - I N - N - N N - N - N


=== Hero Info ===
🧙 Gaerdal_Ironhand | ⭐ Lvl 1 | ❤️ HP 100 | 🔮 MP 100 | ⚔️ STR 700 | 🏹 DEX 600 | 🤸 AGI 500 | 💰 Gold 1354
Position: (7,0) in lane TOP
Current terrain: HERO_NEXUS
Effective STR/DEX/AGI: 700.0 / 600.0 / 500.0
Equipped weapon: none
Equipped armor: none

Choose action:
1)  🚶 Move
2)  ⚔️ Attack
3)  🔮 Cast Spell
4)  🧪 Use Potion
5)  ✨ Teleport
6)  🔙 Recall to Nexus
7)  🛡️ Equip Weapon / Armor
8)  ℹ️ View Hero Info (does NOT end turn)
9)  🎒 View Inventory (does NOT end turn)
10) 🏪 Open Market
11) ⏭️ Pass Turn
12) 🚪 Quit Game

Choose any option in range (1-12)
> 
Move (W/A/S/D):W =move up, A = move left, S = move down, D = move right 
Choose a Move:
===== MARKET CATALOG =====

⚔️ ===== WEAPONS AVAILABLE =====
0) Sword | 💰 500 | 🔓 Lvl 1 | ⚔️ Dmg 800.0 | ✋ Hands 1
1) Bow | 💰 300 | 🔓 Lvl 2 | ⚔️ Dmg 500.0 | ✋ Hands 2
2) Scythe | 💰 1000 | 🔓 Lvl 6 | ⚔️ Dmg 1100.0 | ✋ Hands 2
3) Axe | 💰 550 | 🔓 Lvl 5 | ⚔️ Dmg 850.0 | ✋ Hands 1
4) TSwords | 💰 1400 | 🔓 Lvl 8 | ⚔️ Dmg 1600.0 | ✋ Hands 2
5) Dagger | 💰 200 | 🔓 Lvl 1 | ⚔️ Dmg 250.0 | ✋ Hands 1

🛡️ ===== ARMORS AVAILABLE =====
0) Platinum_Shield | 💰 150 | 🔓 Lvl 1 | 🛡️ Reduction 200.0
1) Breastplate | 💰 350 | 🔓 Lvl 3 | 🛡️ Reduction 600.0
2) Full_Body_Armor | 💰 1000 | 🔓 Lvl 8 | 🛡️ Reduction 1100.0
3) Wizard_Shield | 💰 1200 | 🔓 Lvl 10 | 🛡️ Reduction 1500.0
4) Guardian_Angel | 💰 1000 | 🔓 Lvl 10 | 🛡️ Reduction 1000.0

🧪 ===== POTIONS AVAILABLE =====
0) Healing_Potion | 💰 250 | 🔓 Lvl 1 | ✨ Effect 100.0 | 📜 HP
1) Strength_Potion | 💰 200 | 🔓 Lvl 1 | ✨ Effect 75.0 | 📜 STRENGTH
2) Magic_Potion | 💰 350 | 🔓 Lvl 2 | ✨ Effect 100.0 | 📜 MANA
3) Luck_Elixir | 💰 500 | 🔓 Lvl 4 | ✨ Effect 65.0 | 📜 AGILITY
4) Mermaid_Tears | 💰 850 | 🔓 Lvl 5 | ✨ Effect 100.0 | 📜 HP
5) Ambrosia | 💰 1000 | 🔓 Lvl 8 | ✨ Effect 150.0 | 📜 HP

🔮 ===== SPELLS AVAILABLE =====
0) Flame_Tornado | 💰 700 | 🔓 Lvl 4 | 🔥 Dmg 850.0 | 🔮 Mana 300.0 | 🧬 FIRE
1) Breath_of_Fire | 💰 350 | 🔓 Lvl 1 | 🔥 Dmg 450.0 | 🔮 Mana 100.0 | 🧬 FIRE
2) Heat_Wave | 💰 450 | 🔓 Lvl 2 | 🔥 Dmg 600.0 | 🔮 Mana 150.0 | 🧬 FIRE
3) Lava_Comet | 💰 800 | 🔓 Lvl 7 | 🔥 Dmg 1000.0 | 🔮 Mana 550.0 | 🧬 FIRE
4) Hell_Storm | 💰 600 | 🔓 Lvl 3 | 🔥 Dmg 950.0 | 🔮 Mana 600.0 | 🧬 FIRE
5) Snow_Cannon | 💰 500 | 🔓 Lvl 2 | 🔥 Dmg 650.0 | 🔮 Mana 250.0 | 🧬 ICE
6) Ice_Blade | 💰 250 | 🔓 Lvl 1 | 🔥 Dmg 450.0 | 🔮 Mana 100.0 | 🧬 ICE
7) Frost_Blizzard | 💰 750 | 🔓 Lvl 5 | 🔥 Dmg 850.0 | 🔮 Mana 350.0 | 🧬 ICE
8) Arctic_Storm | 💰 700 | 🔓 Lvl 6 | 🔥 Dmg 800.0 | 🔮 Mana 300.0 | 🧬 ICE
9) Lightning_Dagger | 💰 400 | 🔓 Lvl 1 | 🔥 Dmg 500.0 | 🔮 Mana 150.0 | 🧬 LIGHTNING
10) Thunder_Blast | 💰 750 | 🔓 Lvl 4 | 🔥 Dmg 950.0 | 🔮 Mana 400.0 | 🧬 LIGHTNING
11) Electric_Arrows | 💰 550 | 🔓 Lvl 5 | 🔥 Dmg 650.0 | 🔮 Mana 200.0 | 🧬 LIGHTNING
12) Spark_Needles | 💰 500 | 🔓 Lvl 2 | 🔥 Dmg 600.0 | 🔮 Mana 200.0 | 🧬 LIGHTNING

🏪 === MARKETPLACE (Hero Nexus) === 🏪
💰 Gold Available: 1354
1) 🛒 Buy Items
2) 💰 Sell Items
3) 🚪 Leave Market
4) ❌ Quit Game
Choose any option in range (1-4)
>
--- What do you want to buy? ---
1) Weapons ⚔️
2) Armors  🛡️
3) Potions 🧪
4) Spells  🔮
0) Back   ❌
Choose from above options(0-4)
>
