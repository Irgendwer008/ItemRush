package info.z10.itemrush;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ItemRushCommand implements CommandExecutor {

    private final GameManager gameManager;

    public ItemRushCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        gameManager.startGame();
        return true;
    }
}
