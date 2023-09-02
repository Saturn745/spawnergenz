package me.xhyrom.spawnergenz.utils;

import me.xhyrom.spawnergenz.SpawnerGenz;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    public static int getRandomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public static String convertUpperSnakeCaseToPascalCase(String upperSnakeCase) {
        String[] parts = upperSnakeCase.split("_");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(part.substring(0, 1).toUpperCase());
            sb.append(part.substring(1).toLowerCase());
        }
        return sb.toString();
    }

    public static String formatNumber(int number) {
        if (number < 1000) {
            return String.valueOf(number);
        }
        int exp = (int) (Math.log(number) / Math.log(1000));
        return String.format("%.1f%c", number / Math.pow(1000, exp), "kMGTPE".charAt(exp - 1));
    }

    public static TagResolver.Single[] transformPlaceholders(me.xhyrom.spawnergenz.structs.Placeholder[] placeholders) {
        ArrayList<TagResolver.Single> placeholderList = new ArrayList<>();

        for (me.xhyrom.spawnergenz.structs.Placeholder placeholder : placeholders) {
            placeholderList.add(Placeholder.parsed(placeholder.key(), placeholder.value()));
        }

        return placeholderList.toArray(new TagResolver.Single[0]);
    }

    public static ArrayList<ItemStack> getLootFromConfig(String spawnerType) {
        // Jesus chris I about pulled my hair out trying to get this to work. I'm not sure if this is the best way to do it, but it works.
        List<Map<?, ?>> lootSection = SpawnerGenz.getInstance().getConfig().getConfigurationSection("loot").getMapList(spawnerType);
        ArrayList<ItemStack> loot = new ArrayList<>();

        for (Map<?, ?> map : lootSection) {
            String item = (String) map.get("item");
            int minimum = (int) map.get("minimum");
            int maximum = (int) map.get("maximum");
            double chance;
            if (map.containsKey("chance")) {
                chance = (double) map.get("chance");
            } else {
                chance = 100;
            }

            if (Utils.getRandomInt(0, 100) <= chance) {
                ItemStack lootItem = new ItemStack(Material.valueOf(item));
                lootItem.setAmount(Utils.getRandomInt(minimum, maximum + 1));
                loot.add(lootItem);
            }
        }

        return loot;
    }
}
