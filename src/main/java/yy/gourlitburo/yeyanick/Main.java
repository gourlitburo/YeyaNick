package yy.gourlitburo.yeyanick;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Logger logger = this.getLogger();

    private Map<UUID, String> nicks = new HashMap<>();
    
    private YamlConfiguration store;
    private final String STORE_FILENAME = "nicks.yml";
    private File folder = this.getDataFolder();
    private File storeFile = new File(folder, STORE_FILENAME);

    @Override
    public void onEnable() {
        int count = loadNicks();
        logger.info(String.format("Loaded %d nicks from store.", count));

        PluginCommand command = this.getCommand("yn");
        command.setExecutor(new Executor(this));

        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new PlayerJoinEventListener(this), this);
        
        logger.info("YeyaNick ready.");
    }

    @Override
    public void onDisable() {
        boolean success = saveNicks();
        if (success) {
            logger.info("Saved nicks to store.");
        } else {
            logger.warning("Failed to save nicks to store.");
        }
    }

    public boolean hasNick(UUID id) {
        return nicks.containsKey(id);
    }

    public String getNick(UUID id) {
        return nicks.get(id);
    }

    public void putNick(UUID id, String nick) {
        nicks.put(id, nick);
        setNick(id, nick);
    }

    public void clearNick(UUID id) {
        nicks.remove(id);

        setNick(id, null);
    }

    public boolean setNick(UUID id, String nick) {
        Player player = Bukkit.getPlayer(id);
        if (player == null) return false;
        player.setDisplayName(nick);
        player.setPlayerListName(nick);
        return true;
    }

    private int loadNicks() {
        int count = 0;
        store = YamlConfiguration.loadConfiguration(storeFile);
        Set<String> serIds = store.getKeys(false);
        for (String serId : serIds) {
            UUID id = UUID.fromString(serId);
            String nick = store.getString(serId);
            nicks.put(id, nick);
            ++count;
        }
        return count;
    }

    private boolean saveNicks() {
        // clear store
        for (String key : store.getKeys(false)) {
            store.set(key, null);
        }

        // write current values
        for (Entry<UUID, String> entry : nicks.entrySet()) {
            String serId = entry.getKey().toString();
            String nick = entry.getValue();
            store.set(serId, nick);
        }

        // save to disk
        try {
            store.save(storeFile);
            return true;
        } catch (IOException exp) {
            return false;
        }
    }
}
