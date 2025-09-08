package info.z10.itemrush;

import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class FinishCommand implements SubCommand {

    private final GameManager gameManager;

    public FinishCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getName() {
        return "finish";
    }

    @Override
    public String getDescription() {
        return "Finishes the current game and shows the winner.";
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        gameManager.finishGame();
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}
