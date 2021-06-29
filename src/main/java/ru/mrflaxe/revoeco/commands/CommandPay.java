package ru.mrflaxe.revoeco.commands;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.database.Profile;
import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.standalone.ArgumentableCommand;
import ru.soknight.lib.configuration.Messages;

public class CommandPay extends ArgumentableCommand {

    private final Messages messages;
    private final DatabaseManager databaseManager;
    
    public CommandPay(Messages messages, DatabaseManager databaseManager, JavaPlugin plugin) {
        super("pay", "reco.command.pay", 2, messages);
        
        this.messages = messages;
        this.databaseManager = databaseManager;
        register(plugin);
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        String targetName = args.get(0);
        
        if(Bukkit.getPlayer(targetName) == null) {
            messages.sendFormatted(sender, "error.player-not-found", "%player%", targetName);
            return;
        }
        
        if(targetName.equals(sender.getName())) {
            messages.getAndSend(sender, "error.self-trans");
            return;
        }
        
        int count = args.getAsInteger(1);
        
        if(count == -1) {
            messages.sendFormatted(sender, "error.arg-is-not-int", "%arg%", args.get(1));
            return;
        }
        
        if(count == 0) {
            messages.getAndSend(sender, "error.not-positive-arg");
            return;
        }
        
        String senderName = sender.getName();
        if(!databaseManager.hasEnough(senderName, count)) {
            messages.getAndSend(sender, "error.no-enough");
            return;
        }
        
        Profile senderProfile = databaseManager.getProfileById(senderName);
        Profile targetProfile = databaseManager.getOrCreateProfile(targetName);
        
        int senderBalanceBefore = senderProfile.getBalance();
        int targetBalanceBefore = targetProfile.getBalance();
        
        senderProfile.add(-count);
        targetProfile.add(count);
        
        int senderBalanceAfter = senderProfile.getBalance();
        int targetBalanceAfter = targetProfile.getBalance();
        
        databaseManager.updateProfile(targetProfile);
        databaseManager.updateProfile(senderProfile);
        
        databaseManager.addTransaction(senderName, senderBalanceAfter, senderBalanceBefore, targetName, "transfer");
        databaseManager.addTransaction(targetName, targetBalanceAfter, targetBalanceBefore, senderName, "transfer");
        
        String transfer = "\u00a7a+" + Math.abs(targetBalanceAfter - targetBalanceBefore);
        
        messages.sendFormatted(sender, "command.pay.to-sender", "%count%", count, "%player%", targetName);
        messages.sendFormatted(Bukkit.getPlayer(targetName), "command.pay.to-reciver", "%count%", count, "%player%", sender.getName(), "%transfer%", transfer);
    }
    
    @Override
    protected List<String> executeTabCompletion(CommandSender sender, CommandArguments args) {
        List<String> players = Bukkit.getOnlinePlayers().stream()
                        .map(Player::getName)
                        .filter(n -> !n.toLowerCase().equals(sender.getName().toLowerCase()))
                        .collect(Collectors.toList());
        
        return players;
    }

}
