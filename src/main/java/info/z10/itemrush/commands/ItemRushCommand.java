package info.z10.itemrush.commands;

import info.z10.itemrush.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemRushCommand implements CommandExecutor, TabCompleter {

    private final List<SubCommand> subCommands = new ArrayList<>();

    public ItemRushCommand(SubCommand... commands) {
        Collections.addAll(subCommands, commands);

        // Add help command last so it has full list
        subCommands.add(new HelpCommand(subCommands));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§eUse /itemrush help for available commands.");
            return true;
        }

        String input = args[0].toLowerCase();

        for (SubCommand subCommand : subCommands) {
            if (subCommand.getName().equalsIgnoreCase(input)) {
                subCommand.execute(sender, args);
                return true;
            }
        }

        sender.sendMessage("§cUnknown command. Try /itemrush help.");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            for (SubCommand subCommand : subCommands) {
                completions.add(subCommand.getName());
            }
            return completions;
        }

        if (args.length >= 2) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(args[0])) {
                    return subCommand.tabComplete(sender, args);
                }
            }
        }

        return List.of();
    }
}
