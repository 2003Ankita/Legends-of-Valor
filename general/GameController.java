package general;

import java.util.*;

import Board.*;
import Heroes.*;
import Items.*;
import Monsters.*;

/**
 * Main controller for the game. Handles:
 * - Loading data (heroes, items, monsters)
 * - Board creation and monster placement
 * - Movement, markets, battles
 * - User input and UI interactions
 * - Win/lose conditions and main game loop
 */
public class GameController {

    private static final int DEFAULT_BOARD_SIZE = 8;
    private static final double DEFAULT_BATTLE_PROB = 0.30;

    private static final double INACCESSIBLE_RATE = 0.20;
    private static final double MARKET_RATE = 0.30;
    private static final double COMMON_RATE = 0.50;

    private static final String MSG_INVALID = "❌ Invalid input! Try again.";
    private static final String MSG_INVALID_MOVE = "❌ Cannot move there.";
    private static final String MSG_DANGER = "⚠ This common tile feels dangerous...";
    private static final String MSG_WIN = "\n🎉 Triumph! All monster tiles have fallen — the realm is safe again!\n";
    private static final String MSG_QUIT = "Goodbye!";

    private int boardSize = DEFAULT_BOARD_SIZE;
    private double battleProbability = DEFAULT_BATTLE_PROB;

    private Board board;
    private Party party;
    private GameView view;
    private Scanner scanner;
    private Random random;
    private List<Hero> heroPool;
    private List<Monster> monsterPool;
    private List<Item> itemPool;
    // Extra-credit features
    private Weather currentWeather = Weather.values()[0]; // Default SUNNY
    private boolean isDay = true;
    private int turnCounter = 0;
    private int weatherCounter = 0;
    private String playerName;

    public GameController() {
        scanner = new Scanner(System.in);
        view = new ConsoleGameView(scanner);
        random = new Random();
    }

    /* ================= GAME START ================= */

    /**
     * Entry point for starting a new game session.
     * Loads all data, allows player to pick heroes,
     * builds the board, places monsters,
     * shows instructions, and enters the main loop.
     */
    public void startGame() {

        // NEW MENU — put at the top of startGame()
        while (true) {
            System.out.println("====================================");
            System.out.println("            GAME MODE MENU          ");
            System.out.println("====================================");
            System.out.println("1) Classic Monsters & Heroes");
            System.out.println("2) Legends of Valor");
            System.out.println("3) Exit");
            System.out.println("====================================");
            System.out.print("Choose your game mode: ");

            int mode = readIntInRange(1, 3);

            switch (mode) {
                case 1:
                    // Classic Monsters & Heroes
                    startMonsterAndHeroes();
                    return; // end after classic game finishes
                case 2:
                    // Legends of Valor
                    startLegendsOfValorMode();
                    return; // exit after LoV ends
                case 3:
                    view.showMessage("Farewell, adventurer.");
                    return;
                default:
                    // readIntInRange already guards the range, but keep safe
                    view.showMessage("Please enter a valid option.");
                    break;
            }
        }
    }

    /* ================= DATA LOADING ================= */

    /**
     * Loads hero, monster, and item pools from hard-coded data.
     * This acts as your "game database".
     */
    private void loadData() {

        heroPool = new ArrayList<>();
        heroPool.add(new Warrior("Gaerdal_Ironhand", 1, 100, 40, 30, 50, 2000));
        heroPool.add(new Warrior("Sehanine_Moonbow", 1, 100, 60, 40, 30, 2500));
        heroPool.add(new Sorcerer("Rillifane_Rallathil", 1, 100, 70, 45, 35, 2200));
        heroPool.add(new Sorcerer("Skoraeus_Stonebones", 1, 100, 80, 55, 25, 2400));
        heroPool.add(new Paladin("Wrathbringer", 1, 100, 55, 60, 20, 2600));
        heroPool.add(new Paladin("Aethelred", 1, 100, 50, 45, 30, 2100));

        monsterPool = new ArrayList<>();
        monsterPool.add(new Dragon("Desghidorrah", 1, 30, 0.20, 0.10));
        monsterPool.add(new Dragon("Chrysophylax", 1, 28, 0.22, 0.10));
        monsterPool.add(new Exoskeleton("Vexia", 1, 38, 50, 0.10, 0.20));
        monsterPool.add(new Exoskeleton("ShadeReaper", 1, 40, 48, 0.12, 0.20));
        monsterPool.add(new Spirit("Andromalius", 1, 45, 20, 0.45));
        monsterPool.add(new Spirit("Sephiroth", 1, 48, 22, 0.40));

        itemPool = new ArrayList<>();
        itemPool.add(new Weapon("Sword", 500, 1, 100, 1));
        itemPool.add(new Weapon("Bow", 300, 1, 75, 2));
        itemPool.add(new Armor("Shield", 400, 1, 40));
        itemPool.add(new Armor("Breastplate", 600, 1, 60));
        itemPool.add(new Potion("Health Potion", 100, 1, HeroStat.HP, 50));
        itemPool.add(new Potion("Mana Potion", 120, 1, HeroStat.MANA, 40));
        itemPool.add(new Spell("Lightning Bolt", 700, 1, SpellType.LIGHTNING, 200, 50));
        itemPool.add(new Spell("Fire Blast", 750, 1, SpellType.FIRE, 250, 60));
        itemPool.add(new Spell("Ice Shard", 600, 1, SpellType.ICE, 180, 40));
    }

    private void startLegendsOfValorMode() {
        System.out.println("\n⚔ Starting Legends of Valor ⚔\n");
        LegendsOfValorGame lov = new LegendsOfValorGame(scanner);
        lov.start();
    }

    private void startMonsterAndHeroes() {
        // ----------------------------
        // ORIGINAL MONSTERS & HEROES GAME
        // ----------------------------

        view.showMessage("Welcome to the Classic Monsters & Heroes!");
        view.showMessage("Please enter your name:");
        playerName = scanner.nextLine().trim();
        view.showMessage("Greetings, " + playerName + "! Your adventure begins...\n");

        loadData();
        chooseHeroes();

        board = new Board(boardSize, INACCESSIBLE_RATE, MARKET_RATE, COMMON_RATE);
        party.setPosition(board.getStartRow(), board.getStartCol());

        placeInitialMonsters();

        instructions();
        mainLoop();

    }

    /**
     * Places a number of main-monsters on random COMMON tiles.
     * These monsters are required for the win condition.
     * Ensures:
     * - Not placed on non-common tiles
     * - No duplicates
     * - Not placed on the party start tile
     */
    private void placeInitialMonsters() {
        int monstersToPlace = party.getHeroes().size();
        int placed = 0;
        int size = board.getSize();

        while (placed < monstersToPlace) {
            int r = random.nextInt(size);
            int c = random.nextInt(size);

            Tile t = board.getTile(r, c);
            if (t.getType() != TileType.COMMON)
                continue;
            if (t.hasMonster())
                continue;
            if (r == party.getRow() && c == party.getCol())
                continue;

            if (board.registerMainMonsterAt(r, c)) {
                placed++;
            }

            placed++;
        }
    }

    /**
     * Allows the user to pick 1–3 heroes from the available hero pool.
     * Prevents selecting the same hero twice.
     * Creates a Party using deep copies of the selected heroes.
     */
    private void chooseHeroes() {
        view.showMessage("How many heroes will join your quest? (Choose 1–3): ");
        int n = readIntInRange(1, 3);

        List<Hero> chosen = new ArrayList<>();

        while (chosen.size() < n) {
            view.showMessage("\nHeroes ready for adventure:");
            for (int i = 0; i < heroPool.size(); i++)
                view.showMessage((i + 1) + ") " + heroPool.get(i).shortInfo());

            view.showMessage((heroPool.size() + 1) + ") Quit");

            view.showMessage("\nChoose a hero by entering its number:");
            int choice = readIntInRange(1, heroPool.size() + 1);

            if (choice == heroPool.size() + 1) {
                view.showMessage("Sad to see you go. Farewell, adventurer.");
                System.exit(0);
            }

            Hero picked = heroPool.get(choice - 1);

            if (chosen.stream().anyMatch(h -> h.getName().equals(picked.getName()))) {
                view.showMessage("❌ Already chosen, pls pick another.");
                continue;
            }
            chosen.add(picked.copy());
        }

        party = new Party(chosen);
    }

    /**
     * Shows the basic control instructions at the start of the game.
     */
    private void instructions() {
        view.showMessage("\nControls for playing the game:");
        view.showMessage("W = move up, A = move left, S = move down, D = move right");
        view.showMessage("I - Show Info");
        view.showMessage("M - For entering into Market");
        view.showMessage("H - Help");
        view.showMessage("Q - Quit\n");
    }

    /**
     * Displays the in-game help menu listing all controls.
     */
    private void showHelpMenu() {
        view.showMessage("\n=== HELP MENU ===");
        view.showMessage("W = move up, A = move left, S = move down, D = move right");
        view.showMessage("Info: I");
        view.showMessage("Market: M");
        view.showMessage("Help: H");
        view.showMessage("Quit: Q");
        view.showMessage("Goal: Clear all 👹 monster tiles on the map.");
    }

    private void changeWeather() {
        Weather[] w = Weather.values();
        currentWeather = w[random.nextInt(w.length)];
        view.showMessage("🌤 Weather changed to: " + currentWeather);
    }

    private void placeMiniBoss() {
        int size = board.getSize();

        while (true) {
            int r = random.nextInt(size);
            int c = random.nextInt(size);

            Tile t = board.getTile(r, c);

            if (t.getType() == TileType.COMMON && !t.hasMonster()) {
                if (board.registerMainMonsterAt(r, c)) {
                    view.showMessage("🔥 A Mini-Boss has spawned at (" + r + ", " + c + ")!");
                    return;
                }
                view.showMessage("🔥 A Mini-Boss has spawned at (" + r + ", " + c + ")!");
                return;
            }
        }
    }

    /**
     * Main interactive loop of the game.
     * Handles:
     * - Rendering board
     * - Reading user commands
     * - Moving party
     * - Entering markets
     * - Triggering battles
     * - Checking win conditions
     * Runs until player quits or all monsters are cleared.
     */
    private void mainLoop() {
        boolean running = true;

        int turnCount = 0;
        int weatherCounter = 0;

        while (running) {
            turnCount++;
            weatherCounter++;

            // --- DAY/NIGHT flips every turn ---
            if (turnCount % 2 == 0) {
                isDay = !isDay;
                view.showMessage(isDay ? "🌞 It is now DAY." : "🌙 It is now NIGHT.");
            }

            // --- Weather changes every 5 turns ---
            if (weatherCounter % 5 == 0) {
                changeWeather(); // you already created this method
            }

            // --- Mini–boss spawns whenever highest hero reaches level 5 or 10 ---
            int highestLevel = party.getHighestLevel();
            if (highestLevel == 5 || highestLevel == 10) {
                placeMiniBoss();
            }

            view.render(board, party);

            view.showMessage("HP/MP summary: " + party.getSummary());

            view.showMessage(
                    "\nCommands: W =move up, A = move left, S = move down, D = move right, | I = show information | M = enter market | H = Help | Q = Quit");
            view.showMessage("Please enter the command (W/A/S/D/I/H/M/Q) : ");

            String line = scanner.nextLine().trim();
            if (line.isEmpty())
                continue;

            char cmd = Character.toUpperCase(line.charAt(0));

            switch (cmd) {
                case 'W':
                    moveParty(-1, 0);
                    break;
                case 'A':
                    moveParty(0, -1);
                    break;
                case 'S':
                    moveParty(1, 0);
                    break;
                case 'D':
                    moveParty(0, 1);
                    break;
                case 'I':
                    view.showInfo(party, null);
                    break;
                case 'M':
                    enterMarket();
                    break;
                case 'H':
                    showHelpMenu();
                    break;
                case 'Q':
                    running = false;
                    continue;
                default:
                    view.showMessage(MSG_INVALID);
                    continue;
            }

            if (board.allMonstersCleared()) {
                view.showMessage("🎉 Congratulations, " + playerName + "! " + MSG_WIN);
                return;
            }

            Tile t = board.getTile(party.getRow(), party.getCol());

            // ===== guaranteed battle (main monster tile) =====
            if (t.hasMonster()) {
                view.showMessage("👹 You stepped on a monster tile!");
                startBattle();

                t.setMonster(false);
                board.mainMonsterDefeated();

                if (board.allMonstersCleared()) {
                    view.render(board, party);
                    view.showMessage("🎉 Congratulations, " + playerName + "! " + MSG_WIN);
                    return;
                }
                continue;
            }

            // Random battle
            if (t.getType() == TileType.COMMON) {
                view.showMessage(MSG_DANGER);

                if (random.nextDouble() < battleProbability) {
                    t.setMonster(true);
                    view.render(board, party);
                    view.showMessage("👹 Monsters appear!");

                    view.showMessage("A random battle is about to start!");
                    view.showMessage("1) Fight");
                    view.showMessage("2) Quit");
                    view.showMessage("Choose 1 or 2:");

                    int choice = readIntInRange(1, 2);
                    if (choice == 2) {
                        view.showMessage(playerName + ", you fled the battle and left the realm behind... 💔");
                        System.exit(0);
                    }

                    startBattle();
                    t.setMonster(false);
                }
            }

        }

        view.showMessage("🌙 " + playerName + ", the winds whisper your departure... Farewell, brave soul.");

    }

    /**
     * Attempts to move the party by a delta row/col.
     * Prevents movement:
     * - Out of bounds
     * - Onto inaccessible tiles
     * 
     * @param dr row delta
     * @param dc column delta
     */
    private void moveParty(int dr, int dc) {
        int nr = party.getRow() + dr;
        int nc = party.getCol() + dc;

        if (!board.inBounds(nr, nc)) {
            view.showMessage(MSG_INVALID_MOVE);
            return;
        }
        if (board.getTile(nr, nc).getType() == TileType.INACCESSIBLE) {
            view.showMessage("❌ That tile is inaccessible.");
            return;
        }

        party.setPosition(nr, nc);
    }

    /**
     * Enters a market on the current tile (if present).
     * Creates a new Market instance if tile does not have one already.
     * Handles Buy/Sell/Exit loop.
     */
    private void enterMarket() {
        Tile tile = board.getTile(party.getRow(), party.getCol());

        if (!isDay) {
            view.showMessage("🌙 Markets are closed at night.");
            return;
        }

        if (tile.getType() != TileType.MARKET) {
            view.showMessage("❌ You are not on a market tile.");
            return;
        }

        if (tile.getMarket() == null)
            tile.setMarket(new Market(randomMarketItems()));

        Market market = tile.getMarket();

        boolean done = false;
        while (!done) {
            view.showMessage("\nMarket Menu:");
            view.showMessage("1) Buy");
            view.showMessage("2) Sell");
            view.showMessage("3) Exit");
            view.showMessage("4) Quit Game");

            view.showMessage("Choose 1 to Buy, 2 to Sell, 3 to Exit,  4 to Quit:: ");

            int choice = readIntInRange(1, 4);

            if (choice == 4) {
                view.showMessage(playerName + ", the world fades as you abandon the quest...");
                System.exit(0);
            }

            switch (choice) {
                case 1:
                    marketBuy(market);
                    break;
                case 2:
                    marketSell(market);
                    break;
                case 3:
                    done = true;
                    break;
            }
        }
    }

    /**
     * Generates a random selection of market items
     * by shuffling the global item pool and picking up to 10 items.
     * 
     * @return list of random items for a market
     */
    private List<Item> randomMarketItems() {
        List<Item> result = new ArrayList<>();
        Collections.shuffle(itemPool);
        for (int i = 0; i < Math.min(10, itemPool.size()); i++)
            result.add(itemPool.get(i).copy());
        return result;
    }

    /**
     * Handles buying items inside a market.
     * Allows repeated purchases until user chooses to exit.
     * 
     * @param market the market to buy from
     */
    private void marketBuy(Market market) {
        Hero hero = chooseHero();
        if (hero == null)
            return;

        while (true) {
            List<Item> items = market.getStock();
            if (items.isEmpty()) {
                view.showMessage("Market empty.");
                return;
            }

            view.showMessage("\nItems:");
            for (int i = 0; i < items.size(); i++)
                view.showMessage((i + 1) + ") " + items.get(i).info());
            view.showMessage((items.size() + 1) + ") Back");
            view.showMessage((items.size() + 2) + ") Quit Game");

            view.showMessage("Enter a number:");
            int choice = readIntInRange(1, items.size() + 2);

            if (choice == items.size() + 1)
                break;

            if (choice == items.size() + 2) {
                view.showMessage(playerName + ", your journey ends here...");
                System.exit(0);
            }

            Item item = items.get(choice - 1);
            if (market.buy(hero, item)) {
                view.showMessage("Bought " + item.getName());
                view.showMessage("Gold left: " + (int) hero.getGold());
                view.showMessage("Buy more? Enter (Y/N): ");

                String ans = scanner.nextLine().trim().toUpperCase();
                if (ans.equalsIgnoreCase("Y"))
                    continue;
                break;
            } else {
                view.showMessage("❌ Cannot buy (level/gold issue).");
            }
        }
    }

    /**
     * Handles selling items from a hero’s inventory to the market.
     * 
     * @param market the market to sell to
     */
    private void marketSell(Market market) {
        Hero hero = chooseHero();
        if (hero == null)
            return;

        while (true) {
            List<Item> items = hero.getInventory().getAllItems();
            if (items.isEmpty()) {
                view.showMessage("Nothing to sell.");
                return;
            }

            view.showMessage("Your items:");
            for (int i = 0; i < items.size(); i++)
                view.showMessage((i + 1) + ") " + items.get(i).info());
            view.showMessage((items.size() + 1) + ") Back");

            int choice = readIntInRange(1, items.size() + 1);
            if (choice == items.size() + 1)
                break;

            Item item = items.get(choice - 1);
            if (market.sell(hero, item))
                view.showMessage("Sold " + item.getName());
            else
                view.showMessage("❌ Cannot sell item.");
        }
    }

    /**
     * Initializes a battle based on:
     * - Party size (same number of monsters)
     * - Highest hero level (matching monster level)
     * Lets the player choose the enemy monsters. Then starts a Battle instance.
     */
    private void startBattle() {
        view.showMessage("\nA battle begins!");

        int lvl = party.getHighestLevel();
        int count = party.getHeroes().size();

        List<Monster> enemies = new ArrayList<>();
        for (Monster t : monsterPool)
            if (t.getLevel() == lvl)
                enemies.add(t);

        if (enemies.isEmpty())
            enemies.addAll(monsterPool);

        List<Monster> chosen = new ArrayList<>();

        view.showMessage("\nChoose exactly " + count + " monsters to fight:");
        for (int i = 0; i < enemies.size(); i++)
            view.showMessage((i + 1) + ") " + enemies.get(i).shortInfo());

        for (int i = 0; i < count; i++) {
            view.showMessage("Enter the number of the monster you want to fight:");
            int idx = readIntInRange(1, enemies.size()) - 1;
            Monster m;
            if (enemies.get(idx).isBoss())
                m = enemies.get(idx);
            else
                m = enemies.get(idx).copy();
            m.setLevel(lvl);
            chosen.add(m);
        }

        new Battle(party, chosen, view, scanner, random, currentWeather,
                isDay, playerName).start();
    }

    /**
     * Lets the player select a hero from the party.
     * 
     * @return the selected Hero object
     */
    private Hero chooseHero() {
        List<Hero> heroes = party.getHeroes();

        view.showMessage("✨ Welcome to the world of Legends of Valor!");
        view.showMessage("Your journey will be dangerous… choose your heroes wisely.\n");

        view.showMessage("\nChoose hero:");
        for (int i = 0; i < heroes.size(); i++)
            view.showMessage((i + 1) + ") " + heroes.get(i).shortInfo());

        int idx = readIntInRange(1, heroes.size()) - 1;
        return heroes.get(idx);
    }

    /**
     * Reads an integer from input; repeats until the user enters a number
     * in the specified inclusive range.
     *
     * @param min minimum acceptable value
     * @param max maximum acceptable value
     * @return validated integer in range [min, max]
     */
    private int readIntInRange(int min, int max) {
        while (true) {
            try {
                int v = Integer.parseInt(scanner.nextLine().trim());
                if (v >= min && v <= max)
                    return v;
                view.showMessage("Enter number between " + min + " and " + max);
            } catch (Exception e) {
                view.showMessage(MSG_INVALID);
            }
        }
    }

    private Monster generateMiniBoss(int level) {
        Monster base = monsterPool.get(random.nextInt(monsterPool.size())).copy();
        base.setLevel(level);

        // Boss stat multipliers
        base.takeDamage(-base.getHp());
        base.SetHP(base.getLevel() * 200);
        base.SetDamage(base.getDamage() * 1.5);
        base.SetDefense(base.getDefense() * 1.3);
        base.SetDodgeChance(base.getDodgeChance() * 1.2);
        base.setBoss(true);

        return base;
    }

}
