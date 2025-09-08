package info.z10.itemrush;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GameListener implements Listener {

    private final GameManager gameManager;

    public GameListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (!gameManager.isRunning()) return;

        Player player = event.getPlayer();
        Material item = event.getItem().getItemStack().getType();

        gameManager.recordItem(player, item);

    }
}
