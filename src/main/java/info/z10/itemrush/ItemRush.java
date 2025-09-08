package info.z10.itemrush;

import org.bukkit.plugin.java.JavaPlugin;

public class ItemRush extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("ItemRush plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ItemRush plugin disabled.");
    }
}
