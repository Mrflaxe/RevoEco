package ru.mrflaxe.revoeco.commands.sub;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.database.Profile;
import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.subcommand.ArgumentableSubcommand;
import ru.soknight.lib.configuration.Messages;

public class SubcommandGive extends ArgumentableSubcommand {

    private final Messages messages;
    private final DatabaseManager databaseManager;
    
    public SubcommandGive(Messages messages, DatabaseManager databaseManager) {
        super("reco.command.give", 2, messages);
        
        this.messages = messages;
        this.databaseManager = databaseManager;
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        String name = args.get(0);
        
        if(!databaseManager.hasProfile(name)) {
            messages.sendFormatted(sender, "error.no-profile", "%player%", name);
            return;
        }
        
        int count = args.getAsInteger(1);
        
        if(count == -1) {
            messages.sendFormatted(sender, "error.arg-is-not-int", "%arg%", args.get(1));
            return;
        }
        
        Profile profile = databaseManager.getOrCreateProfile(name);
        profile.add(count);
        databaseManager.updateProfile(profile);
        
        messages.sendFormatted(sender, "command.give.to-sender", "%player%", name, "%count%", count);
        
        Player reciver = Bukkit.getPlayer(name);
        if(reciver != null) messages.sendFormatted(sender, "command.give.to-reciver", "%count%", count);
    }
    
    @Override
    protected List<String> executeTabCompletion(CommandSender sender, CommandArguments args) {
        String arg = args.get(0).toLowerCase();
        
        List<Profile> profiles = databaseManager.getAllProfiles();
        if(profiles == null) return null;
        
        List<String> suggestions = profiles.stream()
                .map(Profile::getName)
                .filter(n -> n.startsWith(arg))
                .collect(Collectors.toList());
        
        return suggestions;
    }
}
