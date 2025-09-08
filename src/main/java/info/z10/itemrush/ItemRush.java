package info.z10.itemrush;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ItemRush extends JavaPlugin {

    private static ItemRush instance;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        gameManager = new GameManager(this);
        Objects.requireNonNull(getCommand("itemrush")).setExecutor(new ItemRushCommand(gameManager));
        getLogger().info("ItemRush enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ItemRush disabled.");
    }

    public static ItemRush getInstance() {
        return instance;
    }
}
