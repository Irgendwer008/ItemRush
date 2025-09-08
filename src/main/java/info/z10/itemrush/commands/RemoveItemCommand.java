package info.z10.itemrush.commands;

import info.z10.itemrush.FormatHelper;
import info.z10.itemrush.ItemRush;
import info.z10.itemrush.SubCommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RemoveItemCommand implements SubCommand {

    private final ItemRush plugin;

    public RemoveItemCommand(ItemRush plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "removeitem";
    }

    @Override
    public String getDescription() {
        return "Removes an item from the game's random selection";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /itemrush removeitem <item1> [item2] ...");
            return;
        }

        List<String> currentItems = plugin.getConfig().getStringList("possibleItems");

        int removed = 0;
        for (int i = 1; i < args.length; i++) {
            String input = args[i];

            if (currentItems.contains(input)) {
                currentItems.remove(input.toLowerCase(Locale.ROOT));
                removed++;
            }
        }

        plugin.getConfig().set("possibleItems", currentItems);
        plugin.saveConfig();

        sender.sendMessage("§aRemoved " + removed + " items to the target list.");
        sender.sendMessage("§aNew List:  " + FormatHelper.getAllPossibleItems(plugin));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            String partial = args[args.length - 1].toLowerCase();

            List<String> currentItems = plugin.getConfig().getStringList("possibleItems");

            return Arrays.stream(Material.values())
                    .filter(Material::isItem)
                    .map(Enum::name) // dont show if already in list
                    .map(String::toLowerCase)
                    .filter(name -> name.startsWith(partial))
                    .filter(name -> currentItems.contains(name.toLowerCase(Locale.ROOT)))
                    .filter(name -> !Arrays.stream(args).toList().contains(name))
                    .limit(25) // avoid overfilling tab suggestions
                    .toList();
        }

        return Collections.emptyList();
    }
}
