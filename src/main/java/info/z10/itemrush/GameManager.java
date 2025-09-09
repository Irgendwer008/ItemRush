package info.z10.itemrush;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.*;

public class GameManager {

    private final ItemRush plugin;
    private Material targetItem;
    private List<Player> players;
    private final Map<UUID, Integer> itemCounts = new HashMap<>();
    private boolean running = false;
    private BukkitRunnable cycle;

    private Scoreboard scoreboard;
    private Objective objective;
    private final String TIMER_LINE_1 = "Time Left:";
    private String timerLine2 = "00:00";  // will update every second
    private int gameDurationSeconds;
    private int timeLeftSeconds = gameDurationSeconds;

    private World gameworld;

    public GameManager(ItemRush plugin) {
        this.plugin = plugin;
        gameDurationSeconds = plugin.getConfig().getInt("gameDuration");
    }

    public void setGameDuration(int gameDurationSeconds) {
        this.gameDurationSeconds = gameDurationSeconds;
    }

    public void startGame(CommandSender sender) {
        if (running) {
            Bukkit.broadcast(Component.text("Game is already running!", NamedTextColor.RED));
            return;
        }

        if (sender instanceof Player player) {
            gameworld = player.getWorld();
        } else {
            sender.sendMessage("Game can only be started by a player");
        }

        players = new ArrayList<>(gameworld.getPlayers());
        //if (players.size() < 2) {
        //    Bukkit.broadcast(Component.text("Not enough players to start the game.", NamedTextColor.RED));
        //    return;
        //}

        targetItem = getRandomItem();
        itemCounts.clear();
        running = true;
        timeLeftSeconds = gameDurationSeconds;

        Bukkit.broadcast(Component.text("Game has started.", NamedTextColor.GOLD));
        Bukkit.broadcast(Component.text("Collect as many " + FormatHelper.getFormattedItemName(targetItem) + "s as you can!", NamedTextColor.GOLD));
        Bukkit.broadcast(Component.text("You have " + FormatHelper.formatSeconds(timeLeftSeconds), NamedTextColor.GOLD));

        setupScoreboards();

        cycle = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeLeftSeconds < 1) {
                    finishGame();
                }

                if (!running) {
                    cancel();
                    return;
                }

                updateScoreboard();
                timeLeftSeconds--;
            }
        };
        cycle.runTaskTimer(plugin, 0, 20);
    }

    private void updateScoreboard() {
        String formattedTime = String.format("%d:%02d", timeLeftSeconds / 60, timeLeftSeconds % 60);

        // Remove old timerLine2 score to avoid duplicates
        scoreboard.resetScores(timerLine2);

        timerLine2 = formattedTime;

        // Set updated time with score below "Time Left:" line
        objective.getScore(timerLine2).setScore(-3);

        for (Player player : players) {
            int count = countPlayerItem(player, this.targetItem);

            if (count > 0) {
                itemCounts.put(player.getUniqueId(), count);
            }

            // Update the score for this player on the shared scoreboard
            if (objective != null) {
                objective.getScore(player.getName()).setScore(count);
            }
        }
    }

    public int countPlayerItem(Player player, Material item) {
        if (!running || item != targetItem) return 0;

        int count = 0;
        for (ItemStack item_in_inventory : player.getInventory().getContents()) {
            if (item_in_inventory != null && item_in_inventory.getType() == item) {
                count += item_in_inventory.getAmount();
            }
        }

        return count;
    }

    private void setupScoreboards() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("ItemRush", Criteria.DUMMY, Component.text(targetItem.name().replace("_", "-") + "-SCORE"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Timer lines - added right away
        objective.getScore("").setScore(-1);
        objective.getScore(TIMER_LINE_1).setScore(-2);
        objective.getScore(timerLine2).setScore(-3);

        for (Player player : players) {
            objective.getScore(player.getName()).setScore(0);
            player.setScoreboard(scoreboard);
        }
    }

    public void cancelGame() {
        running = false;

        for (Player player : gameworld.getPlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        Bukkit.broadcast(Component.text("The Game was cancelled!", NamedTextColor.RED));

        players.clear();
    }

    public void finishGame() {
        running = false;

        for (Player player : new ArrayList<>(Bukkit.getOnlinePlayers())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        if (itemCounts.isEmpty()) {
            Bukkit.broadcast(Component.text("No one collected any " + targetItem.name() + "!", NamedTextColor.RED));
            return;
        }

        UUID winnerId = Collections.max(itemCounts.entrySet(), Map.Entry.comparingByValue()).getKey();

        while (Bukkit.getPlayer(winnerId) == null) {
            if (itemCounts.isEmpty()) {
                Bukkit.broadcast(Component.text("No one that participated is online anymore!", NamedTextColor.RED));
                return;
            }

            itemCounts.remove(winnerId);
            winnerId = Collections.max(itemCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        }

        Player winner = Bukkit.getPlayer(winnerId);


        if (winner == null) {
            return;
        }

        Bukkit.broadcast(Component.text("Time's up!", NamedTextColor.AQUA));
        Bukkit.broadcast(Component.text("The winner is " + winner.getName() + " with " +
                itemCounts.get(winnerId) + " " + FormatHelper.getFormattedItemName(targetItem) + "s!", NamedTextColor.GREEN));

        players.clear();
    }

    public boolean isRunning() {
        return running;
    }

    private List<Material> getPossibleItems(List<String> itemNames) {
        if (itemNames == null || itemNames.isEmpty()) {
            return null;
        }

        ArrayList<Material> itemObjects = new ArrayList<>(Collections.emptyList());

        for (String item : itemNames) {
            itemObjects.add(Material.getMaterial(item.toUpperCase(Locale.ROOT)));
        }

        return itemObjects;
    }

    private Material getRandomItem() {
        List<Material> itemObjects = getPossibleItems((List<String>) plugin.getConfig().getList("possibleItems"));

        if (itemObjects == null) {
            return null;
        }

        Collections.shuffle(itemObjects);

        return itemObjects.get(0);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Material getTargetItem() {
        return targetItem;
    }
}
