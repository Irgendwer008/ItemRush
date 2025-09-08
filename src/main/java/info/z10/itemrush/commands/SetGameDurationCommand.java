package info.z10.itemrush.commands;

import info.z10.itemrush.FormatHelper;
import info.z10.itemrush.GameManager;
import info.z10.itemrush.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class SetGameDurationCommand implements SubCommand {

    private final GameManager gameManager;

    public SetGameDurationCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "setgameduration";
    }

    @Override
    public String getDescription() {
        return "Changes the time the players have to gather the items";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage("§cUsage: /itemrush setgameduration <seconds>");
            return;
        }

        try {
            int seconds = Integer.parseInt(args[1]);
            if (seconds < 0) {
                sender.sendMessage("§cPlease enter a number larger than zero.");
                return;
            }

            gameManager.setGameDuration(seconds);
            sender.sendMessage("§aGame duration set to " + FormatHelper.formatSeconds(seconds));
        } catch (NumberFormatException e) {
            sender.sendMessage("§cThat (\"" + args[1] + "\") is not a valid number.");
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
