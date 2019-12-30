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

    public Executor(Main instance) { this.plugin = instance; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) return false;

        String cmd = args[0];
        if (!cmd.equalsIgnoreCase("set") && !cmd.equalsIgnoreCase("clear")) return false;

        String nick = Color.colorize(args[1] + "&r");
        Player target;
        if (args.length == 2) {
            if (sender instanceof ConsoleCommandSender) {
                sender.sendMessage("Error: Console must specify a player whose nick to manage.");
                return true;
            }
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayer(args[2]);
            if (target == null) {
                sender.sendMessage(String.format("Error: Player '%s' not found.", args[1]));
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
        switch (cmd) {
            case "set": 
                plugin.putNick(id, nick);
                sender.sendMessage(String.format("Set %s's nick to '%s'.", target.getName(), nick));
                break;
            case "clear":
                plugin.clearNick(id);
                sender.sendMessage(String.format("Cleared %s's nick.", target.getName()));
                break;
        }

        return true;
    }
}
