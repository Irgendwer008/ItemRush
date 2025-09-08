package info.z10.itemrush;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ItemRush extends JavaPlugin {

    private static ItemRush instance;
    private static GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        gameManager = new GameManager(this);

        // Register the command executor and tab completer
        getCommand("itemrush").setExecutor(
                new ItemRushCommand(
                        new StartCommand(gameManager),
                        new CancelCommand(gameManager),
                        new FinishCommand(gameManager)
                )
        );
        getCommand("itemrush").setTabCompleter(
                new ItemRushCommand(
                        new StartCommand(gameManager),
                        new CancelCommand(gameManager),
                        new FinishCommand(gameManager)
                )
        );

        getLogger().info("ItemRush enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ItemRush disabled.");
    }

    public GameManager getGameManager() {
        return gameManager;
    }
}
