package me.xhyrom.spawnergenz.hooks;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public interface ShopHook {
    Double getSellPrice(Player player, ArrayList<ItemStack> itemStacks);
}
