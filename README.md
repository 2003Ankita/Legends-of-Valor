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

## How to Run

### Requirements
- Java 8 

### Compile
```bash
javac Main.java
