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

public class AddItemCommand implements SubCommand {

    private final ItemRush plugin;

    public AddItemCommand(ItemRush plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "additem";
    }

    @Override
    public String getDescription() {
        return "Adds a new item for the game's random selection";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /itemrush additem <item1> [item2] ...");
            return;
        }

        List<String> currentItems = plugin.getConfig().getStringList("possibleItems");

        int added = 0;
        for (int i = 1; i < args.length; i++) {
            String input = args[i].toUpperCase(Locale.ROOT);

            try {
                Material material = Material.valueOf(input);

                if (!currentItems.contains(material.name().toLowerCase(Locale.ROOT))) {
                    currentItems.add(material.name().toLowerCase(Locale.ROOT));
                    added++;
                }
            } catch (IllegalArgumentException e) {
                sender.sendMessage("§cInvalid material: " + input);
            }
        }

        plugin.getConfig().set("possibleItems", currentItems);
        plugin.saveConfig();

        sender.sendMessage("§aAdded " + added + " items to the target list.");
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
                    .filter(name -> !currentItems.contains(name))
                    .limit(25) // avoid overfilling tab suggestions
                    .toList();
        }

        return Collections.emptyList();
    }
}
