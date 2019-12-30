package yy.gourlitburo.yeyanick;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

class Executor implements CommandExecutor {
    private Main plugin;

    private int SET = 0;
    private int CLEAR = 1;

    public Executor(Main instance) { this.plugin = instance; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int sub = -1;

        if (args.length < 1) return false;

        String cmd = args[0];
        if (!cmd.equals("set") && !cmd.equals("clear")) return false;

        if (cmd.equals("set")) sub = SET;
        else if (cmd.equals("clear")) sub = CLEAR;

        if (sub == SET && args.length < 2) {
            sender.sendMessage("Error: No nick specified.");
            return true;
        }

        String nick = null;
        String targetName = null;
        if (sub == SET) {
            nick = Color.colorize(args[1] + "&r");
            targetName = args.length > 2 ? args[2] : null;
        } else if (sub == CLEAR) {
            targetName = args.length > 1 ? args[1] : null;
        }

        Player target;
        if (targetName == null) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage("Error: Console must specify a player whose nick to manage.");
                return true;
            }
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(String.format("Error: Player '%s' not found.", targetName));
                return true;
            }

            boolean isOtherPlayer =
                sender instanceof ConsoleCommandSender ||
                (sender instanceof Player && !((Player) sender).getUniqueId().equals(target.getUniqueId()));
            if (isOtherPlayer && !sender.hasPermission("yeyanick.manage")) {
                sender.sendMessage("Error: 'yeyanick.manage' permission is needed in order to manage other players' nicks.");
                return true;
            }
        }

        UUID id = target.getUniqueId();
        if (sub == SET) {
            plugin.putNick(id, nick);
            sender.sendMessage(String.format("Set %s's nick to '%s'.", target.getName(), nick));
        } else if (sub == CLEAR) {
            plugin.clearNick(id);
            sender.sendMessage(String.format("Cleared %s's nick.", target.getName()));
        }

        return true;
    }
}
