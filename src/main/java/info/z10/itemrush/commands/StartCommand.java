package info.z10.itemrush.commands;

import info.z10.itemrush.GameManager;
import info.z10.itemrush.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class StartCommand implements SubCommand {

    private final GameManager gameManager;

    public StartCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Starts a new ItemRush game.";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        gameManager.startGame(sender);
        sender.sendMessage("§aGame started.");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
