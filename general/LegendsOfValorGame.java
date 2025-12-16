package general;

import java.util.*;

import Items.Armor;
import Items.Item;
import Items.Potion;
import Items.Spell;
import Items.Weapon;
import Monsters.*;
import Board.*;
import Heroes.*;

/**
 * Main controller for Legends of Valor.
 * Reuses domain classes from Monsters & Heroes (Hero, Monster, Items).
 */
public class LegendsOfValorGame extends Game {

    private final LegendsOfValorBoard board;
    private final List<HeroUnit> heroes;
    private final List<MonsterUnit> monstersOnBoard = new ArrayList<>();

    private final DamageCalculator damageCalculator;
    private final MonsterBehavior monsterBehavior;

    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    private Difficulty difficulty = Difficulty.MEDIUM;
    private int roundsSinceLastSpawn = 0;
    private final Market market;

    private final Scanner scanner;

    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String RESET = "\u001B[0m";
    private int heroKills = 0;
    private int monsterKills = 0;
    private boolean quitRequested = false;

    private int heroComboStreak = 0;
    private int monsterComboStreak = 0;

    private final int spawnFrequency; // e.g. every 8 rounds

    /**
     * Creates a Legends of Valor game with a default monster spawn frequency.
     */
    public LegendsOfValorGame(Scanner scanner) {
        this(scanner, 8);
    }

    /**
     * Initializes a Legends of Valor game instance with configurable monster spawn
     * frequency,
     * sets up the board, heroes, initial monsters, market inventory, and core game
     * systems.
     */
    public LegendsOfValorGame(Scanner scanner, int spawnFrequency) {
        this.board = new LegendsOfValorBoard.Builder().build();
        this.scanner = scanner;
        this.spawnFrequency = spawnFrequency;
        this.damageCalculator = new DefaultDamageCalculator();
        this.monsterBehavior = new DefaultMonsterBehavior();

        this.heroes = chooseHeroesAndLanes();
        spawnInitialMonsters();
        List<Item> marketStock = HeroFactoryAdapter.loadAllItems();
        this.market = new Market(marketStock);

    }

    /**
     * Returns the current game board instance.
     */
    public LegendsOfValorBoard getBoard() {
        return board;
    }

    /**
     * Returns the list of active hero units in the game.
     */
    public List<HeroUnit> getHeroes() {
        return heroes;
    }

    /**
     * Returns the list of monster units currently on the board.
     */
    public List<MonsterUnit> getMonstersOnBoard() {
        return monstersOnBoard;
    }

    /**
     * Returns the damage calculator used for combat resolution.
     */
    public DamageCalculator getDamageCalculator() {
        return damageCalculator;
    }

    @Override
    protected void initializeGame() {
        System.out.println("⚔️ === LEGENDS OF VALOR === ⚔️");

        System.out.println("\n🔔 --- ROUND " + roundNumber + " BEGINS --- 🔔");

    }

    /**
     * Returns the number of rounds between monster spawns
     * based on the current game difficulty.
     */
    private int spawnInterval() {
        switch (difficulty) {
            case EASY:
                return 6;
            case MEDIUM:
                return 4;
            case HARD:
                return 2;
            default:
                return 4;
        }
    }

    /**
     * Allows the player to select heroes and assign each to a unique lane,
     * initializing hero units at their corresponding hero nexus positions.
     */
    private List<HeroUnit> chooseHeroesAndLanes() {
        List<Hero> pool = HeroFactoryAdapter.loadAllHeroes();
        List<HeroUnit> result = new ArrayList<>();
        System.out.println("🧙 Choose 3 heroes to defend the realm:");

        System.out.println("0) 🚪 Quit Game");

        for (int i = 0; i < pool.size(); i++) {
            Hero h = pool.get(i);
            System.out.printf("%d) %s (lvl %d, HP %.1f, STR %.1f, DEX %.1f, AGI %.1f)%n",
                    i + 1, h.getName(), h.getLevel(), h.getHp(),
                    h.getStrength(), h.getDexterity(), h.getAgility());
        }

        Lane[] lanes = Lane.values();
        Set<Integer> pickedHeroes = new HashSet<>();
        Set<Lane> pickedLanes = new HashSet<>();
        for (int heroIndex = 0; heroIndex < 3; heroIndex++) {
            int idx;
            while (true) {
                System.out.println("👉 Select hero #" + (heroIndex + 1) + ":");

                System.out.println("0) Quit Game");
                System.out.println("Enter a number displayed : ");

                int choice = readInt(scanner, 0, pool.size());
                if (choice == 0) {
                    System.out.println("You chose to quit Legends of Valor.");
                    showPostQuitMenu();
                    return new ArrayList<>();
                }
                idx = choice - 1;

                if (pickedHeroes.contains(idx)) {
                    System.out.println("❌ This hero is already chosen. Pick a different one.");

                    continue;
                }
                pickedHeroes.add(idx);
                break;
            }
            Hero chosen = pool.get(idx);

            Lane lane;
            while (true) {
                System.out.println("🛣️ Assign a lane to " + chosen.getName() + ":");

                System.out.println("0) Quit Game");

                for (int li = 0; li < lanes.length; li++) {
                    System.out.printf("%d) %s%n", li + 1, lanes[li]);
                }
               System.out.println("choose an option in range (0-3)");
                int laneChoice = readInt(scanner, 0, lanes.length);
                if (laneChoice == 0) {
                    System.out.println("👋 You chose to leave Legends of Valor.");

                    showPostQuitMenu();
                    return new ArrayList<>();
                }
                lane = lanes[laneChoice - 1];

                if (pickedLanes.contains(lane)) {
                    System.out.println("❌ This lane is already occupied. Choose another lane.");
                    continue;
                }
                pickedLanes.add(lane);
                break;
            }

            Position nexus = board.heroNexusForLane(lane);
            HeroUnit unit = new HeroUnit(chosen, lane, nexus);
            board.getTile(nexus).placeHero(chosen);
            result.add(unit);
        }

        return result;
    }

    /**
     * Spawns the initial set of monsters at each lane’s monster nexus,
     * scaling their levels to match the current heroes.
     */
    private void spawnInitialMonsters() {
        List<Monster> pool = HeroFactoryAdapter.loadAllMonsters();
        Random random = new Random();
        for (Lane lane : Lane.values()) {
            Monster base = pool.get(random.nextInt(pool.size()));
            Monster clone = cloneForCurrentLevel(base);
            Position spawn = board.monsterNexusForLane(lane);
            MonsterUnit mu = new MonsterUnit(clone, lane, spawn);
            monstersOnBoard.add(mu);
            board.getTile(spawn).placeMonster(clone);
        }
    }

    /**
     * Creates a copy of a monster and scales its level to match
     * the highest current hero level.
     */
    private Monster cloneForCurrentLevel(Monster base) {
        Monster copy = base.copy();
        int maxHeroLevel = 1;
        for (HeroUnit h : heroes) {
            maxHeroLevel = Math.max(maxHeroLevel, h.getHero().getLevel());
        }
        copy.setLevel(maxHeroLevel);
        return copy;
    }

    /**
     * Executes the turn sequence for all heroes, repeatedly prompting each hero
     * for an action until a valid turn-ending action is completed.
     */
    @Override
    protected void heroesTurn() {
        for (HeroUnit unit : heroes) {
            Hero hero = unit.getHero();
            if (hero == null) {
                continue;
            }
            if (hero.getHP() <= 0) {
                continue;
            }

            boolean turnDone = false;

            while (!turnDone) {
                System.out.println("\n⚔️ Hero Turn → " + hero.getName()
                        + " | Position: " + unit.getPosition()
                        + " | Lane: " + unit.getLane());

                renderBoard();
                // printSingleHeroStatus(unit);
                showHeroInfo(unit);
                printHeroMenu();

                int choice = readInt(scanner, 0, 12);

                switch (choice) {
                    case 1: // Move
                        turnDone = new MoveAction().execute(this, unit, scanner);
                        break;

                    case 2: // Attack
                        turnDone = new AttackAction().execute(this, unit, scanner);
                        break;

                    case 3:
                        turnDone = new CastSpellAction().execute(this, unit, scanner);
                        break;

                    case 4:
                        turnDone = new UsePotionAction().execute(this, unit, scanner);
                        break;

                    case 5: // Teleport
                        turnDone = new TeleportAction().execute(this, unit, scanner);
                        break;

                    case 6: // Recall
                        turnDone = new RecallAction().execute(this, unit, scanner);
                        break;

                    case 7: // Equip Weapon/Armor
                        turnDone = new EquipAction().execute(this, unit, scanner);
                        break;

                    case 8: // Show hero info
                        showHeroInfo(unit);
                        break;

                    case 9: // Show inventory
                        showHeroInventory(unit);
                        break;
                    case 10:
                        if (board.getTile(unit.getPosition()).isHeroNexus()) {
                            openMarketMenu(unit);
                        } else {
                            System.out.println("\n(You can buy/sell items only at the Hero Nexus.)");
                        }
                        break;
                    case 11: // Pass
                        turnDone = new PassAction().execute(this, unit, scanner);
                        break;
                    case 12:
                        System.out.println("You chose to quit Legends of Valor.");
                        quitRequested = true;
                        gameOver = true;
                        showPostQuitMenu();
                        return;

                    default:
                        System.out.println("❌ Invalid choice. Please select a valid option.");

                }
            }
        }
    }

    /**
     * Executes a turn for each active monster by delegating behavior
     * to the monster AI controller.
     */
    // @Override
    // protected void monstersTurn() {
    // for (MonsterUnit unit : new ArrayList<>(monstersOnBoard)) {
    // monsterBehavior.takeTurn(unit, this);
    // }
    // }
    @Override
    protected void monstersTurn() {
        for (MonsterUnit unit : new ArrayList<>(monstersOnBoard)) {
            monsterBehavior.takeTurn(unit, this);
        }

        // END-OF-ROUND LOGIC (previously in start loop)
        // regenHeroes();
        maybeRespawnHeroes();
        maybeSpawnNewMonsters();
        heroComboStreak = 0;
        monsterComboStreak = 0;
    }

    // Menu text
    public void printHeroMenu() {
        System.out.println("\nChoose action:");
        System.out.println("1)  🚶 Move");
        System.out.println("2)  ⚔️ Attack");
        System.out.println("3)  🔮 Cast Spell");
        System.out.println("4)  🧪 Use Potion");
        System.out.println("5)  ✨ Teleport");
        System.out.println("6)  🔙 Recall to Nexus");
        System.out.println("7)  🛡️ Equip Weapon / Armor");
        System.out.println("8)  ℹ️ View Hero Info (does NOT end turn)");
        System.out.println("9)  🎒 View Inventory (does NOT end turn)");
        System.out.println("10) 🏪 Open Market");
        System.out.println("11) ⏭️ Pass Turn");
        System.out.println("12) 🚪 Quit Game");
        System.out.println("Choose any option in range (1-12)");

    }

    /**
     * Displays detailed information about a hero, including position,
     * terrain-adjusted
     * effective stats, and currently equipped weapon and armor.
     */
    private void showHeroInfo(HeroUnit unit) {
        Hero h = unit.getHero();

        System.out.println("\n=== Hero Info ===");
        System.out.println(h.fullInfo());
        System.out.println("Position: " + unit.getPosition() + " in lane " + unit.getLane());

        // Terrain + effective stats (so the printed values match combat calculations)
        TerrainType terrain = board.getTile(unit.getPosition()).getTerrainType();
        TerrainEffect eff = TerrainEffectFactory.forTerrain(terrain);

        double effStr = h.getStrength() * eff.getStrengthMultiplier();
        double effDex = h.getDexterity() * eff.getDexterityMultiplier();
        double effAgi = h.getAgility() * eff.getAgilityMultiplier();

        System.out.println("Current terrain: " + terrain);
        System.out.printf("Effective STR/DEX/AGI: %.1f / %.1f / %.1f%n", effStr, effDex, effAgi);

        Weapon w = h.getWeapon();
        if (w != null) {
            System.out.println("Equipped weapon: " + w.getName() +
                    " (damage " + w.getDamage() + ", hands " + w.getHandsRequired() + ")");
        } else {
            System.out.println("Equipped weapon: none");
        }

        Armor a = h.getArmor();
        if (a != null) {
            System.out.println("Equipped armor: " + a.getName() +
                    " (reduction " + a.getDamageReduction() + ")");
        } else {
            System.out.println("Equipped armor: none");
        }
    }

    /**
     * Displays the specified hero’s inventory to the console, grouped by item type
     * and formatted with relevant item statistics.
     */
    private void showHeroInventory(HeroUnit unit) {
        Hero h = unit.getHero();
        Inventory inv = h.getInventory();

        System.out.println("\n=== Inventory of " + h.getName() + " ===");

        // Weapons
        List<Weapon> weapons = inv.getWeapons();
        System.out.println("\n-- Weapons --");
        if (weapons.isEmpty())
            System.out.println("  (none)");
        else {
            for (int i = 0; i < weapons.size(); i++) {
                Weapon w = weapons.get(i);
                System.out.printf("  %d) %s (dmg %.1f, lvlReq %d, hands %d)%n",
                        i + 1, w.getName(), w.getDamage(),
                        w.getLevelRequirement(), w.getHandsRequired());
            }
        }

        // Armors
        List<Armor> armors = inv.getArmors();
        System.out.println("\n-- Armors --");
        if (armors.isEmpty())
            System.out.println("  (none)");
        else {
            for (int i = 0; i < armors.size(); i++) {
                Armor a = armors.get(i);
                System.out.printf("  %d) %s (reduction %.1f, lvlReq %d)%n",
                        i + 1, a.getName(),
                        a.getDamageReduction(), a.getLevelRequirement());
            }
        }

        // Spells
        List<Spell> spells = inv.getSpells();
        System.out.println("\n-- Spells --");
        if (spells.isEmpty())
            System.out.println("  (none)");
        else {
            for (int i = 0; i < spells.size(); i++) {
                Spell s = spells.get(i);
                System.out.printf("  %d) %s (dmg %.1f, mana %.1f, type %s)%n",
                        i + 1, s.getName(), s.getDamage(),
                        s.getManaCost(), s.getType());
            }
        }

        // Potions
        List<Potion> potions = inv.getPotions();
        System.out.println("\n-- Potions --");
        if (potions.isEmpty())
            System.out.println("  (none)");
        else {
            for (int i = 0; i < potions.size(); i++) {
                Potion p = potions.get(i);
                System.out.printf("  %d) %s (effect %s)%n",
                        i + 1, p.getName(), p.getStat());
            }
        }

        System.out.println("\n-- Other Items --");
        List<Item> all = inv.getAllItems();
        boolean printed = false;
        for (Item it : all) {
            if (!(it instanceof Weapon) && !(it instanceof Armor)
                    && !(it instanceof Spell) && !(it instanceof Potion)) {
                System.out.println("  - " + it.getName());
                printed = true;
            }
        }
        if (!printed)
            System.out.println("  (none)");
    }

    /**
     * Determines whether a hero can legally move to the specified position,
     * enforcing board bounds, terrain rules, occupancy constraints,
     * and lane-based monster blocking logic.
     */
    public boolean canHeroMoveTo(HeroUnit unit, Position dest) {
        if (!board.inBounds(dest))
            return false;

        LegendsTile tile = board.getTile(dest);
        TerrainType type = tile.getTerrainType();
        if (type == TerrainType.INACCESSIBLE || type == TerrainType.OBSTACLE) {
            return false;
        }

        // Cannot move onto another hero
        if (tile.getHero() != null) {
            return false;
        }

        // Cannot move behind a monster in same lane
        Lane lane = board.laneForColumn(dest.col); // lane must be based on destination

        for (MonsterUnit mu : monstersOnBoard) {
            if (!mu.isAlive())
                continue;
            if (mu.getLane() == lane) {
                Position mp = mu.getPosition();
                // monsters move downwards; "behind" = row index less than monster
                if (dest.row < mp.row) {
                    return false; // cannot move/teleport past a monster in that lane
                }

            }
        }

        return true;
    }

    /**
     * Prints the current status of all monsters on the board,
     * including stats, position, and lane information.
     */
    private void printMonstersStatus() {
        if (monstersOnBoard.isEmpty()) {
            System.out.println("(No monsters on board)");
            return;
        }
        System.out.println("\nMonsters on board:");
        for (int i = 0; i < monstersOnBoard.size(); i++) {
            MonsterUnit mu = monstersOnBoard.get(i);
            Monster m = mu.getMonster();
            System.out.printf("M%d) %s (Lvl %d, HP %.1f, DMG %.1f, DEF %.1f, Dodge %.0f%%) at %s lane %s%n",
                    i + 1, m.getName(), m.getLevel(), m.getHp(), m.getDamage(), m.getDefense(),
                    m.getDodgeChance() * 100, mu.getPosition(), mu.getLane());
        }
    }

    /**
     * Computes and returns the hero’s effective strength based on
     * the terrain the hero is currently standing on.
     */
    private double effectiveStrength(HeroUnit unit) {
        TerrainType t = board.getTile(unit.getPosition()).getTerrainType();
        TerrainEffect e = TerrainEffectFactory.forTerrain(t);
        return unit.getHero().getStrength() * e.getStrengthMultiplier();
    }

    /**
     * Computes and returns the hero’s effective dexterity based on
     * the terrain the hero is currently standing on.
     */
    private double effectiveDexterity(HeroUnit unit) {
        TerrainType t = board.getTile(unit.getPosition()).getTerrainType();
        TerrainEffect e = TerrainEffectFactory.forTerrain(t);
        return unit.getHero().getDexterity() * e.getDexterityMultiplier();
    }

    /**
     * Computes and returns the hero’s effective agility based on
     * the terrain the hero is currently standing on.
     */
    private double effectiveAgility(HeroUnit unit) {
        TerrainType t = board.getTile(unit.getPosition()).getTerrainType();
        TerrainEffect e = TerrainEffectFactory.forTerrain(t);
        return unit.getHero().getAgility() * e.getAgilityMultiplier();
    }

    // Short status line at the top of a hero's turn
    public void printSingleHeroStatus(HeroUnit unit) {
        Hero h = unit.getHero();
        TerrainType t = board.getTile(unit.getPosition()).getTerrainType();

        double str = effectiveStrength(unit);
        double dex = effectiveDexterity(unit);
        double agi = effectiveAgility(unit);

        System.out.printf("%s (Lvl %d) HP %.0f MP %.0f Gold %.0f Pos %s Terrain %s | STR %.1f DEX %.1f AGI %.1f%n",
                h.getName(), h.getLevel(), h.getHp(), h.getMana(), h.getGold(),
                unit.getPosition(), t, str, dex, agi);
    }

    /**
     * Opens and manages the market interaction loop for a hero, allowing
     * buying, selling, exiting the market, or quitting the game.
     */
    private void openMarketMenu(HeroUnit unit) {
        Hero hero = unit.getHero();

        while (true) {

            showFullMarketCatalog();

            System.out.println("\n🏪 === MARKETPLACE (Hero Nexus) === 🏪");
            System.out.println("💰 Gold Available: " + (int) hero.getGold());

            System.out.println("1) 🛒 Buy Items");
            System.out.println("2) 💰 Sell Items");
            System.out.println("3) 🚪 Leave Market");
            System.out.println("4) ❌ Quit Game");
            System.out.println("Choose any option in range (1-4)");

            int choice = readInt(scanner, 1, 4);

            if (choice == 4) {
                System.out.println("You chose to quit Legends of Valor.");
                quitRequested = true;
                gameOver = true;
                showPostQuitMenu();
                return;
            }

            if (choice == 3)
                return;

            if (choice == 1) {
                buyCategoryMenu(hero);
            } else { // choice == 2
                handleSell(hero);
            }
        }
    }

    /**
     * Displays a purchase-category menu and routes the hero to the
     * corresponding item-buying workflow until the user exits.
     */
    private void buyCategoryMenu(Hero hero) {
        while (true) {
            System.out.println("\n--- What do you want to buy? ---");
            System.out.println("1) Weapons");
            System.out.println("2) Armors");
            System.out.println("3) Potions");
            System.out.println("4) Spells");
            System.out.println("0) Back");

            int choice = readInt(scanner, 0, 4);
            if (choice == 0)
                return;

            switch (choice) {
                case 1:
                    handleBuyByType(hero, Weapon.class);
                    break;
                case 2:
                    handleBuyByType(hero, Armor.class);
                    break;
                case 3:
                    handleBuyByType(hero, Potion.class);
                    break;
                case 4:
                    handleBuyByType(hero, Spell.class);
                    break;
            }
        }
    }

    /**
     * Displays the complete market catalog of available items to the console.
     */
    private void showFullMarketCatalog() {
        System.out.println("\n===== MARKET CATALOG =====");
        market.printAllItems();
    }

    /**
     * Handles purchasing items of a specific type for a hero by filtering market
     * stock,
     * processing user selection, and completing or rejecting the transaction.
     */
    private void handleBuyByType(Hero hero, Class<? extends Item> clazz) {
        List<Item> stock = market.getStock();
        List<Item> filtered = new ArrayList<>();

        for (Item it : stock) {
            if (clazz.isInstance(it))
                filtered.add(it);
        }

        if (filtered.isEmpty()) {
            System.out.println("📦 No items available in this category.");
            return;
        }

        System.out.println("\n--- Buy " + clazz.getSimpleName() + "s ---");
        System.out.println("Gold: " + (int) hero.getGold());

        for (int i = 0; i < filtered.size(); i++) {
            Item it = filtered.get(i);
            System.out.printf("%d) %s | price %d | lvl req %d%n",
                    i + 1, it.getName(), it.getPrice(), it.getLevelRequired());
        }
        System.out.println("0) Cancel");
        System.out.println("-1) Quit Game");

        int choice = readInt(scanner, -1, filtered.size());

        if (choice == -1) {
            System.out.println("You chose to quit Legends of Valor.");
            quitRequested = true;
            gameOver = true;
            showPostQuitMenu();
            return;
        }

        if (choice == 0)
            return;

        Item selected = filtered.get(choice - 1);

        if (market.buy(hero, selected)) {
            System.out.println("✅ Purchased " + selected.getName() + " successfully.");

            System.out.println("💰 Remaining Gold: " + (int) hero.getGold());
        } else {
            System.out.println("❌ Purchase failed — level too low or insufficient gold.");
            System.out.println("Current Gold: " + (int) hero.getGold());
        }
    }

    /**
     * Handles the item-selling interaction for a hero, allowing the player to
     * select, sell, cancel, or quit, and updates inventory and gold accordingly.
     */
    private void handleSell(Hero hero) {
        List<Item> items = hero.getInventory().getAllItems();

        if (items.isEmpty()) {
            System.out.println("📦 You have no items available to sell.");
            return;
        }

        System.out.println("\n--- Sell Items ---");
        for (int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            System.out.printf("%d) %s | sell price %d%n",
                    i + 1,
                    it.getName(),
                    it.getPrice() / 2);
        }
        System.out.println("0) Cancel");
        System.out.println("-1) Quit Game");

        int choice = readInt(scanner, -1, items.size());

        if (choice == -1) {
            System.out.println("You chose to quit Legends of Valor.");
            quitRequested = true;
            gameOver = true;
            showPostQuitMenu();
            return;
        }

        if (choice == 0)
            return;

        Item selected = items.get(choice - 1);
        market.sell(hero, selected);
        System.out.println("💰 Sold " + selected.getName() + " successfully.");
        System.out.println("💰 Updated Gold Balance: " + (int) hero.getGold());

    }

    /**
     * Moves a hero to a new position, updates board occupancy and hero state,
     * and applies terrain-based buff notifications when entering special tiles.
     */
    public void moveHero(HeroUnit unit, Position dest) {
        LegendsTile fromTile = board.getTile(unit.getPosition());
        TerrainType fromType = fromTile.getTerrainType();

        LegendsTile toTile = board.getTile(dest);
        TerrainType toType = toTile.getTerrainType();

        fromTile.removeHero();
        toTile.placeHero(unit.getHero());
        unit.setPosition(dest);

        // Print a buff message when entering a special terrain (avoid repeats if
        // terrain doesn't change)
        if (toType != fromType) {
            TerrainEffect eff = TerrainEffectFactory.forTerrain(toType);

            double strM = eff.getStrengthMultiplier();
            double dexM = eff.getDexterityMultiplier();
            double agiM = eff.getAgilityMultiplier();

            // Only print for terrains that actually grant a bonus
            if (toType == TerrainType.BUSH && dexM > 1.0) {
                System.out.printf(
                        "[Terrain Buff] %s entered BUSH: Dexterity +%.0f%% (buff ends when leaving this terrain)%n",
                        unit.getHero().getName(), (dexM - 1.0) * 100);
            } else if (toType == TerrainType.CAVE && agiM > 1.0) {
                System.out.printf(
                        "[Terrain Buff] %s entered CAVE: Agility +%.0f%% (buff ends when leaving this terrain)%n",
                        unit.getHero().getName(), (agiM - 1.0) * 100);
            } else if (toType == TerrainType.KOULOU && strM > 1.0) {
                System.out.printf(
                        "[Terrain Buff] %s entered KOULOU: Strength +%.0f%% (buff ends when leaving this terrain)%n",
                        unit.getHero().getName(), (strM - 1.0) * 100);
            }
        }
    }

    public void recallHero(HeroUnit unit) {
        moveHero(unit, unit.getNexusPosition());
    }

    /**
     * Determines whether a monster can legally move to the specified position,
     * validating board bounds, terrain restrictions, and tile occupancy.
     */
    public boolean canMonsterMoveTo(MonsterUnit unit, Position dest) {
        if (!board.inBounds(dest))
            return false;
        LegendsTile tile = board.getTile(dest);
        TerrainType type = tile.getTerrainType();
        if (type == TerrainType.INACCESSIBLE || type == TerrainType.OBSTACLE) {
            return false;
        }
        if (tile.getMonster() != null)
            return false; // no stacking monsters
        if (tile.getHero() != null)
            return false; // heroes can block
        return true;
    }

    /**
     * Moves a monster to a new board position by updating tile occupancy
     * and the monster unit’s internal position state.
     */
    public void moveMonster(MonsterUnit unit, Position dest) {
        LegendsTile fromTile = board.getTile(unit.getPosition());
        LegendsTile toTile = board.getTile(dest);
        fromTile.removeMonster();
        toTile.placeMonster(unit.getMonster());
        unit.setPosition(dest);
    }

    /**
     * Returns all living heroes within a given range of the specified position,
     * based on row and column distance constraints.
     */
    public List<HeroUnit> getHeroesInRange(Position center, int radius) {
        List<HeroUnit> result = new ArrayList<>();
        for (HeroUnit h : heroes) {
            if (!h.isAlive())
                continue;
            Position pos = h.getPosition();
            if (Math.abs(pos.row - center.row) + Math.abs(pos.col - center.col) <= radius) {
                result.add(h);
            }
        }
        return result;
    }

    /**
     * Returns all living monsters within a given Manhattan distance
     * from the specified center position.
     */
    public List<MonsterUnit> getMonstersInRange(Position center, int radius) {
        List<MonsterUnit> result = new ArrayList<>();
        for (MonsterUnit m : monstersOnBoard) {
            if (!m.isAlive())
                continue;
            Position pos = m.getPosition();
            if (Math.abs(pos.row - center.row) + Math.abs(pos.col - center.col) <= radius) {
                result.add(m);
            }
        }
        return result;
    }

    /**
     * Returns a list of all heroes excluding the specified hero unit.
     */
    public List<HeroUnit> getOtherHeroes(HeroUnit unit) {
        List<HeroUnit> result = new ArrayList<>();
        for (HeroUnit h : heroes) {
            if (h != unit)
                result.add(h);
        }
        return result;
    }

    /** Legal teleport tiles adjacent to target hero, in a different lane. */
    public List<Position> validTeleportDestinations(HeroUnit unit, HeroUnit target) {
        List<Position> result = new ArrayList<>();

        if (unit.getLane() == target.getLane()) {
            return result; // must teleport between lanes
        }

        Position targetPos = target.getPosition();
        int[][] dirs = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int[] d : dirs) {
            Position candidate = targetPos.translate(d[0], d[1]);
            if (!board.inBounds(candidate))
                continue;
            if (!canHeroMoveTo(unit, candidate))
                continue;

            // Cannot teleport to a tile "ahead" of the target hero
            if (candidate.row < targetPos.row)
                continue;

            result.add(candidate);
        }

        return result;
    }

    /**
     * Executes a hero attack against a monster, applying terrain- and weapon-based
     * damage, updating combo statistics, and processing monster defeat and rewards.
     */
    public void heroAttack(HeroUnit attacker, MonsterUnit target) {
        Position pos = attacker.getPosition();
        LegendsTile tile = board.getTile(pos);
        double dmg = damageCalculator.heroAttacksMonster(
                attacker.getHero(),
                target.getMonster(),
                tile.getTerrainType(),
                attacker.getHero().getWeapon());
        double beforeHp = target.getMonster().getHp();

        target.getMonster().takeDamage(dmg);

        System.out.printf(
                "⚔ Damage Recap: %s dealt %.1f damage → Monster HP: %.1f → %.1f%n",
                attacker.getHero().getName(),
                dmg,
                beforeHp,
                target.getMonster().getHp());

        if (!target.isAlive()) {
            heroComboStreak++;
            monsterComboStreak = 0;

            if (heroComboStreak > 1) {
                System.out.println("🔥 HERO COMBO x" + heroComboStreak + "!");
            }

            heroKills++;
            System.out.println(
                    GREEN + "🗡️ Hero " + attacker.getHero().getName() +
                            " has slain Monster " + target.getMonster().getName() + "!" + RESET);

            System.out.println("🏆 " + target.getMonster().getName() + " has been defeated!");

            board.getTile(target.getPosition()).removeMonster();
            monstersOnBoard.remove(target);
            rewardHeroesForKill(attacker, target.getMonster());
        }
    }

    /**
     * Executes a monster attack against a hero, applying terrain- and armor-aware
     * damage, updating combo statistics, and handling hero death outcomes.
     */
    public void monsterAttack(MonsterUnit attacker, HeroUnit target) {
        Position pos = attacker.getPosition();
        LegendsTile tile = board.getTile(pos);
        double dmg = damageCalculator.monsterAttacksHero(
                attacker.getMonster(),
                target.getHero(),
                tile.getTerrainType(),
                target.getHero().getArmor());
        if (dmg == 0) {
            System.out.printf("%s dodges the attack from %s!%n",
                    target.getHero().getName(),
                    attacker.getMonster().getName());
            return;
        }
        double beforeHp = target.getHero().getHp();

        target.getHero().takeDamage(dmg);

        System.out.printf(
                "⚔ Damage Recap: %s dealt %.1f damage → Hero HP: %.1f → %.1f%n",
                attacker.getMonster().getName(),
                dmg,
                beforeHp,
                target.getHero().getHp());

        if (!target.isAlive()) {
            monsterComboStreak++;
            heroComboStreak = 0;

            if (monsterComboStreak > 1) {
                System.out.println("💀 MONSTER COMBO x" + monsterComboStreak + "!");
            }

            monsterKills++;
            System.out.println(
                    RED + "💀 Monster " + attacker.getMonster().getName() +
                            " has killed Hero " + target.getHero().getName() + "!" + RESET);

            System.out.println("💀 " + target.getHero().getName() + " has fallen in battle!");
        }
    }

    /**
     * Grants gold and experience rewards to the hero that kills a monster,
     * with rewards scaled by the monster’s level.
     */
    public void rewardHeroesForKill(HeroUnit killer, Monster monster) {
        double goldReward = 500 * monster.getLevel(); // spec suggestion
        int expReward = 2 * monster.getLevel();

        // Only the killer hero gains rewards and levels up
        Hero h = killer.getHero();
        h.gainExperience(expReward);
        h.addGold(goldReward);
    }

    private void regenHeroes() {
        for (HeroUnit h : heroes) {
            Hero hero = h.getHero();
            if (hero.getHp() > 0) {
                double maxHp = hero.getLevel() * 100.0;
                double healHp = maxHp * 0.10;
                hero.takeDamage(-healHp);

                double healMp = hero.getMana() * 0.10;
                hero.setMana(healMp);
            }
        }
    }

    private void maybeRespawnHeroes() {
        // Heroes respawn at Nexus at start of next round with full HP.
        for (HeroUnit unit : heroes) {
            Hero hero = unit.getHero();
            if (hero.getHp() <= 0) {
                double maxHp = hero.getLevel() * 100.0;
                hero.takeDamage(-(maxHp - hero.getHp())); // heal to full
                recallHero(unit);
                System.out.println("✨ " + hero.getName() + " respawns at the Hero Nexus.");
            }
        }
    }

    /**
     * Displays a post-game menu and routes the player to the selected next action.
     * Allows the user to start another game mode or exit the application.
     *
     * Side effects: reads user input, launches new game controllers, or terminates
     * the program.
     */
    private void showPostQuitMenu() {
        System.out.println("\nWhat would you like to do next?");
        System.out.println("1) 🧙 Classic Monsters & Heroes");
        System.out.println("2) ⚔️ Legends of Valor");
        System.out.println("3) 🚪 Quit Game Completely");

        int choice = readInt(scanner, 1, 3);

        switch (choice) {
            case 1:
                new GameController().startMonsterAndHeroes();
                return;

            case 2:
                Game game = new LegendsOfValorGame(scanner);
                game.startGame();
                return;

            case 3:
                System.out.println("👋 Thank you for playing Legends of Valor. Farewell, hero!");
                System.exit(0);
        }
    }

    /**
     * Spawns a new wave of monsters at lane-specific nexus positions when the
     * spawn interval is reached.
     *
     * Monsters are scaled to the highest current hero level and one monster
     * is spawned per lane, unless the lane’s nexus tile is already occupied.
     *
     * Side effects: mutates board state, updates the active monster list,
     * and resets the spawn counter when spawning occurs.
     */
    private void maybeSpawnNewMonsters() {
        roundsSinceLastSpawn++;

        if (roundsSinceLastSpawn < spawnInterval())
            return;

        roundsSinceLastSpawn = 0;

        int maxHeroLevel = 1;
        for (HeroUnit h : heroes) {
            maxHeroLevel = Math.max(maxHeroLevel, h.getHero().getLevel());
        }

        List<Monster> pool = HeroFactoryAdapter.loadAllMonsters();
        Random random = new Random();

        for (Lane lane : Lane.values()) {
            Monster base = pool.get(random.nextInt(pool.size()));
            Monster clone = base.copy();
            clone.setLevel(maxHeroLevel);

            Position spawn = board.monsterNexusForLane(lane);
            LegendsTile spawnTile = board.getTile(spawn);

            if (spawnTile.getMonster() != null) {
                System.out.println(
                        "[Spawn Blocked] Nexus at lane " + lane + " is occupied. Skipping monster spawn this time.");
                continue; // skip this spawn attempt
            }

            MonsterUnit mu = new MonsterUnit(clone, lane, spawn);
            monstersOnBoard.add(mu);
            board.getTile(spawn).placeMonster(clone);

        }
    }

    /**
     * Checks whether the game has reached a terminal victory condition.
     * Heroes win if any hero reaches a monster nexus tile.
     * Monsters win if any monster reaches a hero nexus tile.
     * 
     * @return true if either side has won; false otherwise
     */
    @Override
    protected void checkWinCondition() {
        // Heroes win
        for (HeroUnit hu : heroes) {
            Position p = hu.getPosition();
            if (board.getTile(p).isMonsterNexus()) {
                System.out.println("Heroes have reached the Monsters' Nexus. Victory!");
                gameOver = true;
                return;
            }
        }

        // Monsters win
        for (MonsterUnit mu : monstersOnBoard) {
            Position p = mu.getPosition();
            if (board.getTile(p).isHeroNexus()) {
                System.out.println("A monster has reached the Heroes' Nexus. Defeat!");
                gameOver = true;
                return;
            }
        }
    }

    /**
     * Renders the current game board to the console in a grid-based layout.
     *
     * Each board row is printed with top and bottom borders, terrain indicators
     * (including inaccessible tiles), and centered entity markers for heroes
     * and monsters occupying each tile.
     *
     * Side effects: prints the board state to standard output.
     */
    void renderBoard() {
        int size = board.getSize();
        System.out.println();

        for (int r = 0; r < size; r++) {

            // TOP border of row r
            System.out.println(borderRow(r));

            // empty / inaccessible row
            StringBuilder mid1 = new StringBuilder();
            for (int c = 0; c < size; c++) {
                LegendsTile tile = board.getTile(r, c);
                if (tile.getTerrainType() == TerrainType.INACCESSIBLE)
                    mid1.append("|  XXX  |");
                else
                    mid1.append("|       |");
                if (c != size - 1)
                    mid1.append(" ");
            }
            System.out.println(mid1);

            // content row
            StringBuilder mid2 = new StringBuilder();
            for (int c = 0; c < size; c++) {
                LegendsTile tile = board.getTile(r, c);
                String content = "";

                if (tile.getHero() != null)
                    content += "H" + (indexOfHero(tile.getHero()) + 1);
                if (tile.getMonster() != null) {
                    if (!content.isEmpty())
                        content += " ";
                    content += "M" + (indexOfMonster(tile.getMonster()) + 1);
                }

                String centered = String.format("%-7s", String.format("%3s", content));
                mid2.append("|").append(centered).append("|");
                if (c != size - 1)
                    mid2.append(" ");
            }
            System.out.println(mid2);

            // BOTTOM border — SAME ROW r
            System.out.println(borderRow(r));
        }

        System.out.println();
    }

    /**
     * Constructs and returns the horizontal border string for a given board row
     * using terrain-specific symbols for visual consistency.
     */
    private String borderRow(int r) {
        int size = board.getSize();
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < size; c++) {
            String t = terrainSymbol(board.getTile(r, c));
            sb.append(t).append(" - ").append(t).append(" - ").append(t);
            if (c != size - 1)
                sb.append(" ");
        }
        return sb.toString();
    }

    private int indexOfHero(Hero hero) {
        for (int i = 0; i < heroes.size(); i++) {
            if (heroes.get(i).getHero() == hero)
                return i;
        }
        return -1;
    }

    /**
     * Returns the index of the specified monster in the active monster list,
     * or -1 if the monster is not currently on the board.
     */
    private int indexOfMonster(Monster monster) {
        for (int i = 0; i < monstersOnBoard.size(); i++) {
            if (monstersOnBoard.get(i).getMonster() == monster)
                return i;
        }
        return -1;
    }

    /**
     * Reads and validates an integer input from the user within a specified range.
     * Re-prompts until a valid integer between min and max (inclusive) is entered.
     */
    public int readInt(Scanner in, int min, int max) {
        while (true) {
            System.out.print("> ");
            String line = in.nextLine();
            try {
                int val = Integer.parseInt(line.trim());
                if (val < min || val > max) {
                    System.out.println("Please enter a number between " + min + " and " + max);
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    /**
     * Returns a single-character symbol representing the terrain type of a tile
     * for use in board border and grid rendering.
     */
    private String terrainSymbol(LegendsTile tile) {
        switch (tile.getTerrainType()) {
            case HERO_NEXUS:
                return "N";
            case MONSTER_NEXUS:
                return "N";
            case INACCESSIBLE:
                return "I";
            case BUSH:
                return "B";
            case CAVE:
                return "C";
            case KOULOU:
                return "K";
            case OBSTACLE:
                return "O";
            default:
                return "P";
        }
    }

    @Override
    protected void endGame() {
        System.out.println("\n🏁 === GAME OVER === 🏁");
        System.out.println("🗡️ Hero Kills: " + heroKills);

        System.out.println("💀 Monster Kills: " + monsterKills);

    }

}
