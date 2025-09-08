package info.z10.itemrush;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class HelpCommand implements SubCommand {

    private final List<SubCommand> subCommands;

    public HelpCommand(List<SubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Shows help for all ItemRush commands.";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("§6ItemRush Commands:");
        for (SubCommand command : subCommands) {
            sender.sendMessage("§e" + command.getSyntax() + " §7- " + command.getDescription());
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
