package me.xhyrom.spawnergenz.structs.queue;

import me.xhyrom.spawnergenz.structs.Spawner;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class LootEntry {
    private final Spawner spawner;
    private final ArrayList<ItemStack> items;

    public LootEntry(Spawner spawner, ArrayList<ItemStack> item) {
        this.spawner = spawner;
        this.items = item;
    }

    public Spawner getSpawner() {
        return spawner;
    }

    public ArrayList<ItemStack> getItem() {
        return items;
    }
}
