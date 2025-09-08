package info.z10.itemrush;

import info.z10.itemrush.commands.*;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemRush extends JavaPlugin {

    private static ItemRush instance;
    private static GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        gameManager = new GameManager(this);

        ItemRushCommand itemRushCommand = new ItemRushCommand(
                new StartCommand(gameManager),
                new CancelCommand(gameManager),
                new FinishCommand(gameManager),
                new SetGameDuration(gameManager)
        );

        // Register the command executor and tab completer
        getCommand("itemrush").setExecutor(itemRushCommand);
        getCommand("itemrush").setTabCompleter(itemRushCommand);

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
