package info.z10.itemrush.commands;

import info.z10.itemrush.FormatHelper;
import info.z10.itemrush.GameManager;
import info.z10.itemrush.ItemRush;
import info.z10.itemrush.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ListItemsCommand implements SubCommand {

    private final ItemRush plugin;

    public ListItemsCommand(ItemRush plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "listitems";
    }

    @Override
    public String getDescription() {
        return "Lists all items used in the game's random selection";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage("Currently possible items are: " + FormatHelper.getAllPossibleItems(plugin));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
