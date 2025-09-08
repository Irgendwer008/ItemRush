package info.z10.itemrush;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FormatHelper {
    public static String formatSeconds(int seconds) {

        int days = seconds / (24 * 60 * 60);
        seconds -= days * 24 * 60 * 60;
        int hours = seconds / (60 * 60);
        seconds -= hours * 60 * 60;
        int minutes = seconds / 60;
        seconds -= minutes * 60;

        String daysString = "";
        if (days > 1) {
            daysString = days + " days";
        } else if (days == 1) {
            daysString = "1 day";
        }

        String hoursString = "";
        if (hours > 1) {
            hoursString = hours + " hours";
        } else if (hours == 1) {
            hoursString = "1 hour";
        }

        String minutesString = "";
        if (minutes > 1) {
            minutesString = minutes + " minutes";
        } else if (minutes == 1) {
            minutesString = "1 minute";
        }

        String secondsString = "";
        if (seconds > 1) {
            secondsString = seconds + " seconds";
        } else if (seconds == 1) {
            secondsString = "1 second";
        }

        ArrayList<String> strings = new ArrayList<>();
        for (String string : List.of(daysString, hoursString, minutesString, secondsString)) {
            if (!Objects.equals(string, "")) {
                strings.add(string);
            }
        }

        int size = strings.size();
        String finalString = "";

        if (size == 0) {
            finalString = "";
        } else if (size == 1) {
            finalString = strings.get(0);
        } else if (size == 2) {
            finalString = strings.get(0) + " and " + strings.get(1);
        } else {
            finalString = String.join(", ", strings.subList(0, size - 1)) +
                    " and " + strings.get(size - 1);
        }

        return finalString;
    }

    public static String getFormattedItemName(Material item) {
        return item.name().toLowerCase().replace("_"," ");
    }

    public static String getAllPossibleItems(ItemRush plugin) {
        StringBuilder itemsString = new StringBuilder();

        List<String> currentItems = plugin.getConfig().getStringList("possibleItems");

        for (String item : currentItems) {
            itemsString.append(item).append(", ");
        }

        return itemsString.substring(0, itemsString.length() - 2);
    }
}
