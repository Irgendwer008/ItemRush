package info.z10.itemrush;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

    private Scoreboard scoreboard;
    private Objective objective;
    private final String TIMER_LINE_1 = "Time Left:";
    private String timerLine2 = "2:00";  // will update every second
    private int timeLeftSeconds = 120; // 2 minutes countdown

    public GameManager(ItemRush plugin) {
        this.plugin = plugin;
    }

    public void startGame() {
        if (running) {
            Bukkit.broadcast(Component.text("Game is already running!", NamedTextColor.RED));
            return;
        }

        players = new ArrayList<>(Bukkit.getOnlinePlayers());
        //if (players.size() < 2) {
        //    Bukkit.broadcast(Component.text("Not enough players to start the game.", NamedTextColor.RED));
        //    return;
        //}

        targetItem = getRandomItem();
        itemCounts.clear();
        running = true;
        timeLeftSeconds = 120;

        Bukkit.broadcast(Component.text("ItemRush has started.", NamedTextColor.GOLD));
        Bukkit.broadcast(Component.text("Collect as many " + targetItem.name().toLowerCase().replace("_"," ") + "s as you can!", NamedTextColor.GOLD));
        Bukkit.broadcast(Component.text("You have 2 minutes.", NamedTextColor.GOLD));

        setupScoreboards();


        new BukkitRunnable() {
            @Override
            public void run() {
                if (timeLeftSeconds <= 0) {
                    endGame();
                    cancel();
                    return;
                }

                updateScoreboard();
                timeLeftSeconds--;
            }
        }.runTaskTimer(plugin, 0, 20); // every second
    }

    private void updateScoreboard() {
        String formattedTime = String.format("%d:%02d", timeLeftSeconds / 60, timeLeftSeconds % 60);

        // Remove old timerLine2 score to avoid duplicates
        scoreboard.resetScores(timerLine2);

        timerLine2 = formattedTime;

        // Set updated time with score below "Time Left:" line
        objective.getScore(timerLine2).setScore(14);

        for (Player player : players) {
            int count = countPlayerItem(player, this.targetItem);

            itemCounts.put(player.getUniqueId(), count);

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
            assert item_in_inventory != null;
            if (item_in_inventory.getType() == item) {
                count += item_in_inventory.getAmount();
            }
        }

        return count;
    }

    private void setupScoreboards() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();

        scoreboard = manager.getNewScoreboard();
        objective = scoreboard.registerNewObjective("ItemRush", Criteria.DUMMY, Component.text("ItemRush Score"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Timer lines - added right away
        objective.getScore(TIMER_LINE_1).setScore(15);
        objective.getScore(timerLine2).setScore(14);

        for (Player player : players) {
            objective.getScore(player.getName()).setScore(0);
            player.setScoreboard(scoreboard);
        }
    }

    public void endGame() {
        running = false;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        if (itemCounts.isEmpty()) {
            Bukkit.broadcast(Component.text("No one collected any " + targetItem.name() + "!", NamedTextColor.RED));
            return;
        }

        UUID winnerId = Collections.max(itemCounts.entrySet(), Map.Entry.comparingByValue()).getKey();
        Player winner = Bukkit.getPlayer(winnerId);

        if (winner == null) {
            return;
        }

        Bukkit.broadcast(Component.text("Time's up!", NamedTextColor.AQUA));
        Bukkit.broadcast(Component.text("The winner is " + winner.getName() + " with " +
                itemCounts.get(winnerId) + " " + targetItem.name() + "s!", NamedTextColor.GREEN));
    }

    public boolean isRunning() {
        return running;
    }

    private Material getRandomItem() {
        List<Material> items = Arrays.asList(
                Material.DIAMOND,
                Material.FEATHER,
                Material.APPLE,
                Material.IRON_INGOT,
                Material.BONE
        );
        Collections.shuffle(items);
        return items.get(0);
    }
}
