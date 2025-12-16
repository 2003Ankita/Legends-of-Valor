package general;

import java.util.*;

import Board.*;
import Heroes.*;
import Items.*;
import Monsters.*;

/**
 * FINAL CORRECTED CONTROLLER
 * Handles the full Monsters & Heroes gameplay loop cleanly and correctly.
 */
public  class MonstersAndHeroesGame extends  Game {

    /* ================= CONSTANTS ================= */
    private static final int DEFAULT_BOARD_SIZE = 8;
    private static final double DEFAULT_BATTLE_PROB = 0.30;

    private static final String MSG_INVALID = "❌ Invalid input! Try again.";
    private static final String MSG_INVALID_MOVE = "❌ Invalid move — you cannot go there.";
    private static final String MSG_WIN = "🎉 Triumph! All monster tiles have fallen — the realm is safe again!";

    /* ================= GAME STATE ================= */
    private Board board;
    private Party party;
    private GameView view;
    private Scanner scanner;
    private Random random;
    private List<Hero> heroPool;
    private List<Monster> monsterPool;
    private List<Item> itemPool;

    private Weather currentWeather = Weather.SUNNY;
    private boolean isDay = true;
    private String playerName;
    public MonstersAndHeroesGame(Scanner scanner) {
        this.scanner = scanner;
        this.view = new ConsoleGameView(scanner);
        this.random = new Random();
    }



@Override
protected void initializeGame() {
    view.showMessage("Welcome to the Classic Monsters & Heroes!");
    view.showMessage("Please enter your name:");
    playerName = scanner.nextLine().trim();
    view.showMessage("Greetings, " + playerName + "! Your adventure begins...\n");

    loadData();
    chooseHeroes();

    board = new Board(DEFAULT_BOARD_SIZE);
    party.setPosition(board.getStartRow(), board.getStartCol());

    placeInitialMonsters();
    instructions();
}
    @Override
    protected void heroesTurn() {
        mainLoop();
    }

    @Override
    protected void monstersTurn() {
        // no-op (mainLoop already processes turns/events)
    }
    @Override
    protected void checkWinCondition() {
        if (board != null && board.allMonstersCleared()) {
            gameOver = true;
        }
    }

    @Override
    protected void endGame() {
        view.showMessage("Game Over. Thanks for playing!");
    }


    private void loadData() {
        heroPool = new ArrayList<>();
        monsterPool = new ArrayList<>();
        itemPool = new ArrayList<>();

        // heroes
        heroPool.add(new Warrior("Gaerdal_Ironhand", 1, 100, 40, 30, 50, 2000));
        heroPool.add(new Warrior("Sehanine_Moonbow", 1, 100, 60, 40, 30, 2500));
        heroPool.add(new Sorcerer("Rillifane_Rallathil", 1, 100, 70, 45, 35, 2200));
        heroPool.add(new Sorcerer("Skoraeus_Stonebones", 1, 100, 80, 55, 25, 2400));
        heroPool.add(new Paladin("Wrathbringer", 1, 100, 55, 60, 20, 2600));
        heroPool.add(new Paladin("Aethelred", 1, 100, 50, 45, 30, 2100));

        // monsters
        monsterPool.add(new Dragon("Desghidorrah", 1, 30, 0.20, 0.10));
        monsterPool.add(new Dragon("Chrysophylax", 1, 28, 0.22, 0.10));
        monsterPool.add(new Exoskeleton("Vexia", 1, 38, 50, 0.10, 0.20));
        monsterPool.add(new Exoskeleton("ShadeReaper", 1, 40, 48, 0.12, 0.20));
        monsterPool.add(new Spirit("Andromalius", 1, 45, 20, 0.45));
        monsterPool.add(new Spirit("Sephiroth", 1, 48, 22, 0.40));

        // items
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


    private void chooseHeroes() {
        view.showMessage("How many heroes join your quest? (1–3): ");
        int n = readIntInRange(1, 3);

        List<Hero> chosen = new ArrayList<>();

        while (chosen.size() < n) {
            view.showMessage("\nHeroes available:");
            for (int i = 0; i < heroPool.size(); i++)
                view.showMessage((i + 1) + ") " + heroPool.get(i).shortInfo());
            view.showMessage((heroPool.size() + 1) + ") Quit");

            int choice = readIntInRange(1, heroPool.size() + 1);

            if (choice == heroPool.size() + 1) {
                view.showMessage("Farewell, " + playerName + ".");
                promptNextAction();
                return;
            }

            Hero picked = heroPool.get(choice - 1);

            if (chosen.stream().anyMatch(h -> h.getName().equals(picked.getName()))) {
                view.showMessage("❌ Already chosen.");
                continue;
            }

            chosen.add(picked.copy());
        }

        party = new Party(chosen);
    }


    private void placeInitialMonsters() {
        int count = party.getHeroes().size();
        int placed = 0;

        while (placed < count) {
            int r = random.nextInt(board.getSize());
            int c = random.nextInt(board.getSize());

            Tile t = board.getTile(r, c);

            if (t.getType() != TileType.COMMON) continue;
            if (t.hasMonster()) continue;
            if (r == party.getRow() && c == party.getCol()) continue;

            board.registerMainMonsterAt(r, c);  // FIXED: single placement
            placed++;
        }
    }
    private void promptNextAction() {
        while (true) {
            System.out.println("\nWhat would you like to do next?");
            System.out.println("1) 🧙 Classic Monsters & Heroes");
            System.out.println("2) ⚔️ Legends of Valor");
            System.out.println("3) 🚪 Quit Game Completely");
            System.out.print("> ");

            int choice = readIntInRange(1, 3);

            switch (choice) {
                case 1:
                    gameOver = true;
                    new MonstersAndHeroesGame(scanner).startGame();
                    return;


                case 2:
                    gameOver = true;
                    new LegendsOfValorGame(scanner).startGame();
                    return;


                case 3:
                    view.showMessage("Thank you for playing. Goodbye! 👋");
                    System.exit(0);
            }
        }
    }


    private void mainLoop() {

        boolean running = true;
        int turn = 0;

        while (running) {

            turn++;

            /* --- Day/Night --- */
            if (turn % 2 == 0) {
                isDay = !isDay;
                view.showMessage(isDay ? "🌞 Day begins." : "🌙 Night falls.");
            }

            /* --- Weather --- */
            if (turn % 5 == 0) {
                Weather[] w = Weather.values();
                currentWeather = w[random.nextInt(w.length)];
                view.showMessage("⛅ Weather shift: " + currentWeather);
            }

            /* --- Board Render --- */
            view.render(board, party);
            view.showMessage("\nMove (W/A/S/D):W =move up, A = move left, S = move down, D = move right  | I = info | M = market | Q = quit");
            view.showMessage("Enter command:");

            char cmd = readChar();

            switch (cmd) {
                case 'W': if (!moveParty(-1, 0)) continue; break;
                case 'A': if (!moveParty(0, -1)) continue; break;
                case 'S': if (!moveParty(1, 0)) continue; break;
                case 'D': if (!moveParty(0, 1)) continue; break;
                case 'I': view.showInfo(party, null); continue;
                case 'M': enterMarket(); continue;
                case 'Q':
                    view.showMessage(playerName + ", you leave the realm behind… 💔");
                    promptNextAction();
                    return;
                default:
                    view.showMessage(MSG_INVALID);
                    continue;
            }

            /* --- WIN CHECK --- */
            if (board.allMonstersCleared()) {
                view.showMessage("🎉 Congratulations, " + playerName + "! " + MSG_WIN);
                return;
            }


            /* --- Tile Events --- */
            Tile t = board.getTile(party.getRow(), party.getCol());

            // main battle
            if (t.hasMonster()) {
                view.showMessage("⚔ A monster blocks your path!");
                view.showMessage("1) Fight");
                view.showMessage("2) Retreat");

                int choice = readIntInRange(1, 2);
                if (choice == 2) {
                    view.showMessage("You chose to retreat.");
                continue;
                }

                Monster selected = chooseMonsterForTile();
                if (selected == null) continue;

                startBattleWithSelectedMonster(selected);

                // ✅ CLEAR TILE ONLY AFTER BATTLE
                t.setMonster(false);
                board.mainMonsterDefeated();
                continue;
            }




            // random battle
            if (t.getType() == TileType.COMMON) {

                if (random.nextDouble() < DEFAULT_BATTLE_PROB) {
                    view.showMessage("⚠ Monsters appear!");

                    view.showMessage("1) Fight");
                    view.showMessage("2) Quit Game");

                    int choice = readIntInRange(1, 2);
                    if (choice == 2) {
                        view.showMessage(playerName + ", you fled the battle… the realm mourns. 💔");
                        promptNextAction();
                        return;

                    }

                    startBattle();
                    if (board.allMonstersCleared()) {
                        view.showMessage("🎉 Congratulations, " + playerName + "! " + MSG_WIN);
                        promptNextAction();
                        return;
                    }

                }
            }
        }
    }

    private boolean moveParty(int dr, int dc) {
        int nr = party.getRow() + dr;
        int nc = party.getCol() + dc;

        if (!board.inBounds(nr, nc)) {
            view.showMessage(MSG_INVALID_MOVE);
            return false;
        }

        Tile next = board.getTile(nr, nc);

        if (next.getType() == TileType.INACCESSIBLE) {
            view.showMessage("❌ That is a wall.");
            return false;
        }

        party.setPosition(nr, nc);  // FIXED: single-position model
        return true;
    }


    private void enterMarket() {
        Tile tile = board.getTile(party.getRow(), party.getCol());

        if (tile.getType() != TileType.MARKET) {
            view.showMessage("❌ Not a market tile.");
            return;
        }

        if (!isDay) {
            view.showMessage("🌙 The market sleeps at night.");
            return;
        }

        if (tile.getMarket() == null)
            tile.setMarket(new Market(randomMarketItems()));

        Market market = tile.getMarket();

        while (true) {
            view.showMessage("\nMarket:");
            view.showMessage("1) Buy");
            view.showMessage("2) Sell");
            view.showMessage("3) Exit Market");
            view.showMessage("4) Quit Game");

            int choice = readIntInRange(1, 4);

            switch (choice) {
                case 1:
                    marketBuy(market);
                    break;   // 🔑 COME BACK TO MENU
                case 2:
                    marketSell(market);
                    break;   // 🔑 COME BACK TO MENU
                case 3:
                    return;  // Exit Market only
                case 4:
                    view.showMessage(playerName + ", your journey ends.");
                    promptNextAction();
                    return;
            }
        }
    }

    private void marketBuy(Market market) {
        Hero hero = chooseHero();
        if (hero == null) return;

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

            int choice = readIntInRange(1, items.size() + 2);

            if (choice == items.size() + 1)
                break;

            if (choice == items.size() + 2) {
                view.showMessage(playerName + ", your journey ends here...");
                promptNextAction();
                return;
            }

            Item item = items.get(choice - 1);
            if (market.buy(hero, item)) {
                view.showMessage("Bought " + item.getName());
                view.showMessage("Gold left: " + (int) hero.getGold());

                view.showMessage("Buy another item?");
                view.showMessage("1) Yes");
                view.showMessage("2) No");

                int again = readIntInRange(1, 2);
                if (again == 2) return;   // back to market menu
            } else {
                view.showMessage("❌ Cannot buy (level/gold issue).");
            }


        }
    }
    private void marketSell(Market market) {
        Hero hero = chooseHero();
        if (hero == null) return;

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
            view.showMessage((items.size() + 2) + ") Quit Game");

            int choice = readIntInRange(1, items.size() + 2);

            if (choice == items.size() + 1)
                break;

            if (choice == items.size() + 2) {
                view.showMessage(playerName + ", your journey ends here...");
                promptNextAction();
                return;
            }

            Item item = items.get(choice - 1);
            if (market.sell(hero, item))
                view.showMessage("Sold " + item.getName());
            else
                view.showMessage("❌ Cannot sell item.");
        }
    }
    private Hero chooseHero() {
        List<Hero> heroes = party.getHeroes();

        view.showMessage("\nChoose a hero to act:");
        for (int i = 0; i < heroes.size(); i++)
            view.showMessage((i + 1) + ") " + heroes.get(i).shortInfo());

        int idx = readIntInRange(1, heroes.size()) - 1;
        return heroes.get(idx);
    }



    private List<Item> randomMarketItems() {
        Collections.shuffle(itemPool);
        return new ArrayList<>(itemPool.subList(0, Math.min(10, itemPool.size())));
    }


    private void startBattle() {
        view.showMessage("\n⚔ A battle begins!");

        int lvl = party.getHighestLevel();
        int count = party.getHeroes().size();

        List<Monster> avail = new ArrayList<>();
        for (Monster m : monsterPool)
            if (m.getLevel() == lvl)
                avail.add(m);

        if (avail.isEmpty()) avail = monsterPool;

        List<Monster> chosen = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int idx = random.nextInt(avail.size());
            Monster m = avail.get(idx).copy();
            m.setLevel(lvl);
            chosen.add(m);
        }

        new Battle(
                party, chosen, view, scanner, random, currentWeather, isDay, playerName
        ).start();
    }

    private int readIntInRange(int min, int max) {
        while (true) {
            try {
                int n = Integer.parseInt(scanner.nextLine().trim());
                if (n >= min && n <= max) return n;
                view.showMessage("Enter " + min + "–" + max);
            } catch (Exception e) {
                view.showMessage(MSG_INVALID);
            }
        }
    }

    private char readChar() {
        while (true) {
            String s = scanner.nextLine().trim().toUpperCase();
            if (s.length() > 0) return s.charAt(0);
            view.showMessage(MSG_INVALID);
        }
    }

    private void instructions() {
        view.showMessage("\nControls:");
        view.showMessage("W A S D : Move");
        view.showMessage("I : Info");
        view.showMessage("M : Market");
        view.showMessage("Q : Quit");
    }
    private Monster chooseMonsterForTile() {
        view.showMessage("\nChoose a monster to fight:");

        for (int i = 0; i < monsterPool.size(); i++) {
            view.showMessage((i + 1) + ") " + monsterPool.get(i).shortInfo());
        }
        view.showMessage((monsterPool.size() + 1) + ") Cancel");

        int choice = readIntInRange(1, monsterPool.size() + 1);
        if (choice == monsterPool.size() + 1) return null;

        Monster m = monsterPool.get(choice - 1).copy();
        m.setLevel(party.getHighestLevel());
        return m;
    }
    private void startBattleWithSelectedMonster(Monster monster) {
        List<Monster> enemies = new ArrayList<>();
        enemies.add(monster);

        new Battle(
                party, enemies, view, scanner, random, currentWeather, isDay, playerName
        ).start();
    }

}
