package info.z10.itemrush;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class CancelCommand implements SubCommand {

    private final GameManager gameManager;

    public CancelCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "cancel";
    }

    @Override
    public String getDescription() {
        return "Cancels the current game.";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        gameManager.cancelGame();
        sender.sendMessage("§cGame cancelled.");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
