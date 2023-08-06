package me.xhyrom.spawnergenz.listeners;

import me.xhyrom.spawnergenz.SpawnerGenz;
import me.xhyrom.spawnergenz.structs.queue.GlobalQueueManager;
import me.xhyrom.spawnergenz.structs.Spawner;
import me.xhyrom.spawnergenz.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.Lootable;

import java.util.ArrayList;
import java.util.Random;

public class SpawnerListener implements Listener {
    @EventHandler
    public void onSpawnerSpawn(SpawnerSpawnEvent event) {
        event.setCancelled(true);

        Spawner spawner = Spawner.fromCreatureSpawner(event.getSpawner());
        if (spawner.isReady()) {
            handle(event, spawner);
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(SpawnerGenz.getInstance(), () -> {
            while (!spawner.isReady()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handle(SpawnerSpawnEvent event, Spawner spawner) {
        LootTable lootTable = ((Lootable) event.getEntity()).getLootTable();

        for (int i = 0; i < spawner.getCount(); i++) {
            LootContext lootContext = new LootContext.Builder(event.getLocation())
                    .lootedEntity(event.getEntity())
                    .lootingModifier(1)
                    .build();

            spawner.setExperience(spawner.getExperience() + Utils.getRandomInt(0, 2));

            ArrayList<ItemStack> loot = new ArrayList<>();
            for (ItemStack item : lootTable.populateLoot(new Random(), lootContext)) {
                if (spawner.getStorage().size() >= SpawnerGenz.getInstance().getConfig().getInt("spawners.storage-multiplier") * spawner.getCount()) {
                    break;
                }
                loot.add(item);
            }
            GlobalQueueManager.addToQueue(spawner, loot);
        }
    }

}
