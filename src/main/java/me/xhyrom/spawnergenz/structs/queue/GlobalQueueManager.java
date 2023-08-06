package me.xhyrom.spawnergenz.structs.queue;

import me.xhyrom.spawnergenz.structs.Spawner;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GlobalQueueManager {
    private static final Queue<LootEntry> lootQueue = new ConcurrentLinkedQueue<>();

    public static void addToQueue(Spawner spawner, ArrayList<ItemStack> item) {
        LootEntry lootEntry = new LootEntry(spawner, item);
        lootQueue.add(lootEntry);
    }

    public static Queue<LootEntry> getQueue() {
        return lootQueue;
    }

    public static class LootEntry {
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
}
