package Board;

import java.util.*;

import Heros.AttackAction;
import Heros.Hero;
import Heros.HeroAction;
import Heros.HeroFactoryAdapter;
import Heros.HeroUnit;
import Heros.MoveAction;
import Heros.PassAction;
import Heros.RecallAction;
import Heros.TeleportAction;
import Items.Position;
import Monsters.DefaultMonsterBehavior;
import Monsters.Monster;
import Monsters.MonsterBehavior;
import Monsters.MonsterUnit;
import general.DamageCalculator;
import general.DefaultDamageCalculator;

/**
 * Main controller for Legends of Valor.
 * Reuses domain classes from Monsters & Heroes (Hero, Monster, Items).
 */
public class LegendsOfValorGame {

    private final LegendsOfValorBoard board;
    private final List<HeroUnit> heroes;
    private final List<MonsterUnit> monstersOnBoard = new ArrayList<>();

    private final DamageCalculator damageCalculator;
    private final MonsterBehavior monsterBehavior;

    private final Scanner scanner;

    private int roundNumber = 1;
    private final int spawnFrequency; // e.g. every 8 rounds

    public LegendsOfValorGame(Scanner scanner) {
        this(scanner, 8);
    }

    public LegendsOfValorGame(Scanner scanner, int spawnFrequency) {
        this.board = new LegendsOfValorBoard.Builder().build();
        this.scanner = scanner;
        this.spawnFrequency = spawnFrequency;
        this.damageCalculator = new DefaultDamageCalculator();
        this.monsterBehavior = new DefaultMonsterBehavior();

        this.heroes = chooseHeroesAndLanes();
        spawnInitialMonsters();
    }

    public LegendsOfValorBoard getBoard() {
        return board;
    }

    public List<HeroUnit> getHeroes() {
        return heroes;
    }

    // ======================= Game Loop =======================

    public void start() {
        System.out.println("=== Legends of Valor ===");
        boolean running = true;
        while (running) {
            System.out.println("\n--- ROUND " + roundNumber + " ---");
            renderBoard();

            heroesTurn();
            if (checkVictoryConditions())
                break;

            monstersTurn();
            if (checkVictoryConditions())
                break;

            regenHeroes();
            maybeRespawnHeroes();
            maybeSpawnNewMonsters();

            roundNumber++;
        }
    }

    // ================ Hero & Monster selection =================

    private List<HeroUnit> chooseHeroesAndLanes() {
        List<Hero> pool = HeroFactoryAdapter.loadAllHeroes();
        List<HeroUnit> result = new ArrayList<>();
        System.out.println("Choose 3 heroes for Legends of Valor:");
        for (int i = 0; i < pool.size(); i++) {
            Hero h = pool.get(i);
            System.out.printf("%d) %s (lvl %d, HP %.1f, STR %.1f, DEX %.1f, AGI %.1f)%n",
                    i + 1, h.getName(), h.getLevel(), h.getHp(),
                    h.getStrength(), h.getDexterity(), h.getAgility());
        }

        Lane[] lanes = Lane.values();
        for (int heroIndex = 0; heroIndex < 3; heroIndex++) {
            System.out.println("Select hero #" + (heroIndex + 1) + ":");
            int idx = readInt(scanner, 1, pool.size()) - 1;
            Hero chosen = pool.remove(idx);

            System.out.println("Assign a lane for " + chosen.getName() + ":");
            for (int li = 0; li < lanes.length; li++) {
                System.out.printf("%d) %s%n", li + 1, lanes[li]);
            }
            int laneIdx = readInt(scanner, 1, lanes.length) - 1;
            Lane lane = lanes[laneIdx];

            Position nexus = board.heroNexusForLane(lane);
            HeroUnit unit = new HeroUnit(chosen, lane, nexus);
            board.getTile(nexus).placeHero(chosen);
            result.add(unit);
        }

        return result;
    }

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

    private Monster cloneForCurrentLevel(Monster base) {
        Monster copy = base.copy();
        copy.setLevel(base.getLevel());
        return copy;
    }

    // ========================== Rounds ==========================

    private void heroesTurn() {
        for (HeroUnit unit : heroes) {
            if (!unit.isAlive())
                continue;

            System.out.println("\n--- Hero turn: " + unit.getHero().getName()
                    + " at " + unit.getPosition() + " ---");
            renderBoard();

            HeroAction action = chooseHeroAction(unit);
            action.execute(this, unit, scanner);
        }
    }

    private HeroAction chooseHeroAction(HeroUnit unit) {
        System.out.println("Choose action: ");
        System.out.println("1) Move");
        System.out.println("2) Attack");
        System.out.println("3) Teleport");
        System.out.println("4) Recall");
        System.out.println("5) Pass");

        int choice = readInt(scanner, 1, 5);
        switch (choice) {
            case 1:
                return new MoveAction();
            case 2:
                return new AttackAction();
            case 3:
                return new TeleportAction();
            case 4:
                return new RecallAction();
            default:
                return new PassAction();
        }
    }

    private void monstersTurn() {
        for (MonsterUnit unit : new ArrayList<>(monstersOnBoard)) {
            monsterBehavior.takeTurn(unit, this);
        }
    }

    // ========= Movement & range queries used by actions / AI =========

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
        Lane lane = unit.getLane();
        for (MonsterUnit mu : monstersOnBoard) {
            if (!mu.isAlive())
                continue;
            if (mu.getLane() == lane) {
                Position mp = mu.getPosition();
                // monsters move downwards; "behind" = row index less than monster
                if (dest.col == mp.col && dest.row < mp.row) {
                    return false;
                }
            }
        }

        return true;
    }

    public void moveHero(HeroUnit unit, Position dest) {
        LegendsTile fromTile = board.getTile(unit.getPosition());
        LegendsTile toTile = board.getTile(dest);
        fromTile.removeHero();
        toTile.placeHero(unit.getHero());
        unit.setPosition(dest);
    }

    public void recallHero(HeroUnit unit) {
        moveHero(unit, unit.getNexusPosition());
    }

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

    public void moveMonster(MonsterUnit unit, Position dest) {
        LegendsTile fromTile = board.getTile(unit.getPosition());
        LegendsTile toTile = board.getTile(dest);
        fromTile.removeMonster();
        toTile.placeMonster(unit.getMonster());
        unit.setPosition(dest);
    }

    public List<HeroUnit> getHeroesInRange(Position center, int radius) {
        List<HeroUnit> result = new ArrayList<>();
        for (HeroUnit h : heroes) {
            if (!h.isAlive())
                continue;
            Position pos = h.getPosition();
            if (Math.abs(pos.row - center.row) <= radius
                    && Math.abs(pos.col - center.col) <= radius) {
                result.add(h);
            }
        }
        return result;
    }

    public List<MonsterUnit> getMonstersInRange(Position center, int radius) {
        List<MonsterUnit> result = new ArrayList<>();
        for (MonsterUnit m : monstersOnBoard) {
            if (!m.isAlive())
                continue;
            Position pos = m.getPosition();
            if (Math.abs(pos.row - center.row) <= radius
                    && Math.abs(pos.col - center.col) <= radius) {
                result.add(m);
            }
        }
        return result;
    }

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

    // ==================== Combat helpers ====================

    public void heroAttack(HeroUnit attacker, MonsterUnit target) {
        Position pos = attacker.getPosition();
        LegendsTile tile = board.getTile(pos);
        double dmg = damageCalculator.heroAttacksMonster(
                attacker.getHero(),
                target.getMonster(),
                tile.getTerrainType(),
                attacker.getHero().getWeapon());
        target.getMonster().takeDamage(dmg);
        System.out.printf("%s hits %s for %.1f damage%n",
                attacker.getHero().getName(),
                target.getMonster().getName(), dmg);

        if (!target.isAlive()) {
            System.out.println(target.getMonster().getName() + " is defeated!");
            board.getTile(target.getPosition()).removeMonster();
            monstersOnBoard.remove(target);
            rewardHeroesForKill(target.getMonster());
        }
    }

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
        target.getHero().takeDamage(dmg);
        System.out.printf("%s hits %s for %.1f damage%n",
                attacker.getMonster().getName(),
                target.getHero().getName(), dmg);

        if (!target.isAlive()) {
            System.out.println(target.getHero().getName() + " has fallen!");
        }
    }

    private void rewardHeroesForKill(Monster monster) {
        double goldReward = 500 * monster.getLevel(); // spec suggestion
        int expReward = 2 * monster.getLevel();

        for (HeroUnit hu : heroes) {
            Hero h = hu.getHero();
            h.gainExperience(expReward);
            h.addGold(goldReward);
        }
    }

    // ==================== Round bookkeeping ====================

    private void regenHeroes() {
        // At end of each round, each alive hero regains ~10% HP.
        // :contentReference[oaicite:3]{index=3}
        for (HeroUnit h : heroes) {
            Hero hero = h.getHero();
            if (hero.getHp() > 0) {
                double maxHp = hero.getLevel() * 100.0;
                double heal = maxHp * 0.10;
                hero.takeDamage(-heal); // negative damage heals
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
                System.out.println(hero.getName() + " respawns at Nexus.");
            }
        }
    }

    private void maybeSpawnNewMonsters() {
        if (roundNumber % spawnFrequency != 0)
            return;

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
                Position below = new Position(spawn.row + 1, spawn.col);
                if (board.inBounds(below)
                        && board.getTile(below).isEmptyForMonster()) {
                    spawn = below;
                }
            }

            MonsterUnit mu = new MonsterUnit(clone, lane, spawn);
            monstersOnBoard.add(mu);
            board.getTile(spawn).placeMonster(clone);
        }
    }

    private boolean checkVictoryConditions() {
        // Heroes win if any hero reaches a monster Nexus
        for (HeroUnit hu : heroes) {
            Position p = hu.getPosition();
            if (board.getTile(p).isMonsterNexus()) {
                System.out.println("Heroes have reached the Monsters' Nexus. Victory!");
                return true;
            }
        }

        // Monsters win if any monster reaches a hero Nexus
        for (MonsterUnit mu : monstersOnBoard) {
            Position p = mu.getPosition();
            if (board.getTile(p).isHeroNexus()) {
                System.out.println("A monster has reached the Heroes' Nexus. Defeat!");
                return true;
            }
        }

        return false;
    }

    // ==================== Rendering ====================

    private void renderBoard() {
        int size = board.getSize();
        System.out.println();

        for (int r = 0; r < size; r++) {

            // ------------------------------
            // 1) TOP BORDER
            // ------------------------------
            StringBuilder top = new StringBuilder();
            for (int c = 0; c < size; c++) {
                String t = terrainSymbol(board.getTile(r, c));
                top.append(t + " - " + t + " - " + t + "   ");
            }
            System.out.println(top.toString());

            // ------------------------------
            // 2) MIDDLE EMPTY ROW (left & right borders)
            // ------------------------------
            StringBuilder mid1 = new StringBuilder();
            for (int c = 0; c < size; c++) {
                mid1.append("|       |   ");
            }
            System.out.println(mid1.toString());

            // ------------------------------
            // 3) MIDDLE CONTENT ROW (hero/monster centered)
            // ------------------------------
            StringBuilder mid2 = new StringBuilder();
            for (int c = 0; c < size; c++) {

                LegendsTile tile = board.getTile(r, c);

                String content = "";

                if (tile.getHero() != null) {
                    content += "H" + (indexOfHero(tile.getHero()) + 1);
                }
                if (tile.getMonster() != null) {
                    if (!content.isEmpty())
                        content += " ";
                    content += "M" + (indexOfMonster(tile.getMonster()) + 1);
                }

                // center content in a 7-width space
                String centered = String.format("%-7s", String.format("%3s", content));

                mid2.append("|" + centered + "|   ");
            }
            System.out.println(mid2.toString());

            // ------------------------------
            // 4) BOTTOM BORDER
            // ------------------------------
            StringBuilder bottom = new StringBuilder();
            for (int c = 0; c < size; c++) {
                String t = terrainSymbol(board.getTile(r, c));
                bottom.append(t + " - " + t + " - " + t + "   ");
            }
            System.out.println(bottom.toString());

            System.out.println();
        }
    }

    private int indexOfHero(Hero hero) {
        for (int i = 0; i < heroes.size(); i++) {
            if (heroes.get(i).getHero() == hero)
                return i;
        }
        return -1;
    }

    private int indexOfMonster(Monster monster) {
        for (int i = 0; i < monstersOnBoard.size(); i++) {
            if (monstersOnBoard.get(i).getMonster() == monster)
                return i;
        }
        return -1;
    }

    // ==================== Utility ====================

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

}
