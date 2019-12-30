package yy.gourlitburo.yeyanick;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

class PlayerJoinEventListener implements Listener {
    private Main plugin;

    public PlayerJoinEventListener(Main instance) { this.plugin = instance; }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        if (plugin.hasNick(id)) {
            plugin.setNick(id, plugin.getNick(id));
        }
    }
}
