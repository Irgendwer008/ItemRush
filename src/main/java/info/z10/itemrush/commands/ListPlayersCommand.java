package info.z10.itemrush.commands;

import info.z10.itemrush.FormatHelper;
import info.z10.itemrush.GameManager;
import info.z10.itemrush.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ListPlayersCommand implements SubCommand {

    private final GameManager gameManager;

    public ListPlayersCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "listplayers";
    }

    @Override
    public String getDescription() {
        return "Lists all players participating in the game";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Running: " + gameManager.isRunning());
        if (!gameManager.isRunning()){
            sender.sendMessage("Not currently in a Game!");
            return;
        }

        sender.sendMessage("Currently Playing: " + FormatHelper.getPlayers(gameManager));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
