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
import ru.soknight.lib.command.preset.standalone.PlayerOnlyCommand;
import ru.soknight.lib.configuration.Messages;

public class CommandBalance extends PlayerOnlyCommand {
    
    private final Messages messages;
    private final DatabaseManager databaseManager;

    public CommandBalance(Messages messages, DatabaseManager databaseManager, JavaPlugin plugin) {
        super("balance", "reco.command.balance", messages);
        
        this.messages = messages;
        this.databaseManager = databaseManager;
        register(plugin);
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        if(args.isEmpty()) {
            Profile profile = databaseManager.getOrCreateProfile(sender.getName());
            int balance = profile.getBalance();
            
            String color = balance < 0 ? "\u00a7c" : "\u00a7a";
            messages.sendFormatted(sender, "command.balance.self", "%color", color, "%count%", balance);
        } else {
            String name = args.get(0);
            
            if(!databaseManager.hasProfile(name)) {
                messages.sendFormatted(sender, "error.no-profile", "%player%", name);
                return;
            }

            Profile otherProfile = databaseManager.getProfileById(name);
            int balance = otherProfile.getBalance();
            
            String color = balance < 0 ? "\u00a7c" : "\u00a7a";
            messages.sendFormatted(sender, "command.balance.other", "%color", color, "%player%", name, "%count%", otherProfile.getBalance());
        }
    }
    
    @Override
    protected List<String> executeTabCompletion(CommandSender sender, CommandArguments args) {
        List<String> suggestions = Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .collect(Collectors.toList());
        
        String arg = args.get(0).toLowerCase();
        
        List<Profile> profiles = databaseManager.getAllProfiles();
        if(profiles == null) return null;
        
        List<String> OfflineRegisteredSuggestions = profiles.stream()
                .map(Profile::getName)
                .filter(n -> !suggestions.contains(n))
                .filter(n -> n.startsWith(arg))
                .collect(Collectors.toList());
        
        suggestions.addAll(OfflineRegisteredSuggestions);
        
        return suggestions;
    }
}
