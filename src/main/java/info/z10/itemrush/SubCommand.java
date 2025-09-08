package info.z10.itemrush;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SubCommand {
    String getName(); // e.g., "start"
    String getDescription(); // for help
    default String getSyntax() { return "/itemrush " + getName(); };

    void execute(CommandSender sender, String[] args);
    List<String> tabComplete(CommandSender sender, String[] args);
}
