package ru.mrflaxe.revoeco.commands.sub;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.database.Profile;
import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.subcommand.ArgumentableSubcommand;
import ru.soknight.lib.configuration.Messages;

public class SubcommandNullify extends ArgumentableSubcommand {

    private final Messages messages;
    private final DatabaseManager databaseManager;
    
    public SubcommandNullify(Messages messages, DatabaseManager databaseManager) {
        super("reco.command.nullify", 1, messages);
        
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
        
        Profile profile = databaseManager.getProfileById(name);
        int balanceBefore = profile.getBalance();
        
        profile.setBalance(0);
        databaseManager.updateProfile(profile);
        
        databaseManager.addTransaction(name, 0, balanceBefore, null, "nullify");
        
        messages.sendFormatted(sender, "command.nullify", "%player%", name);
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
