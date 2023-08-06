package me.xhyrom.spawnergenz;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import me.xhyrom.spawnergenz.commands.SpawnerGenzCommand;
import me.xhyrom.spawnergenz.hooking.Hooks;
import me.xhyrom.spawnergenz.listeners.BlockListener;
import me.xhyrom.spawnergenz.listeners.ClickListener;
import me.xhyrom.spawnergenz.listeners.SpawnerListener;
import me.xhyrom.spawnergenz.structs.queue.GlobalQueueManager;
import me.xhyrom.spawnergenz.structs.Spawner;
import me.xhyrom.spawnergenz.structs.TTLHashMap;
import me.xhyrom.spawnergenz.structs.actions.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class SpawnerGenz extends JavaPlugin {
    @Getter
    private static SpawnerGenz instance;
    @Getter
    private TTLHashMap spawners;
    @Getter
    private HashMap<ActionOpportunity, HashMap<ActionStatus, ArrayList<Action>>> actions = new HashMap<>();

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        saveDefaultConfig();
        getConfig().getConfigurationSection("actions").getKeys(false).forEach(key -> {
            ActionOpportunity opportunity = ActionOpportunity.valueOf(key.toUpperCase());
            HashMap<ActionStatus, ArrayList<Action>> map = new HashMap<>();

            getConfig().getConfigurationSection("actions." + key).getKeys(false).forEach(key1 -> {
                ActionStatus status = ActionStatus.valueOf(key1.toUpperCase());
                ArrayList<Action> actions = new ArrayList<>();

                ((ArrayList<HashMap<String, Object>>) getConfig().get("actions." + key + "." + key1)).forEach(action -> {
                    if (action.containsKey("title"))
                        actions.add(new TitleAction(
                                action.get("title").toString(),
                                action.containsKey("subtitle") ? action.get("subtitle").toString() : ""
                        ));
                    else if (action.containsKey("message"))
                        actions.add(new MessageAction(
                                action.get("message").toString(),
                                action.containsKey("broadcast") && (boolean) action.get("broadcast")
                        ));
                    else if (action.containsKey("command"))
                        actions.add(new CommandAction(
                                action.get("command").toString()
                        ));
                    else if (action.containsKey("sound"))
                        actions.add(new SoundAction(
                                action.get("sound").toString(),
                                action.containsKey("volume") ? Float.parseFloat(action.get("volume").toString()) : 1,
                                action.containsKey("pitch") ? Float.parseFloat(action.get("pitch").toString()) : 1
                        ));
                });

                map.put(status, actions);
            });

            this.actions.put(opportunity, map);
        });

        instance = this;
        spawners = new TTLHashMap(300000);

        getServer().getPluginManager().registerEvents(new ClickListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        Hooks.init();
        SpawnerGenzCommand.register();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            Queue<GlobalQueueManager.LootEntry> lootQueue = GlobalQueueManager.getQueue();

            while (!lootQueue.isEmpty()) {
                GlobalQueueManager.LootEntry currentSpawner = lootQueue.poll();
                Bukkit.getScheduler().runTaskAsynchronously(SpawnerGenz.getInstance(), () -> {
                    while (!currentSpawner.getSpawner().isReady()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                for (ItemStack item : currentSpawner.getItem()) {
                    currentSpawner.getSpawner().addItemToStorage(item);
                }
            }
        }, 0, 20 * 20L);
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
        for (Spawner spawner : spawners.values()) {
            spawner.saveToPDCSync();
        }
    }
}