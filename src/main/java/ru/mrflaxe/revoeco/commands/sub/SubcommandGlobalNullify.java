package ru.mrflaxe.revoeco.commands.sub;

import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.database.Profile;
import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.subcommand.PermissibleSubcommand;
import ru.soknight.lib.configuration.Messages;

public class SubcommandGlobalNullify extends PermissibleSubcommand {

    private final Messages messages;
    private final DatabaseManager databaseManager;
    
    public SubcommandGlobalNullify(Messages messages, DatabaseManager databaseManager) {
        super("reco.command.globalnullify", messages);
        
        this.messages = messages;
        this.databaseManager = databaseManager;
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        List<Profile> profiles = databaseManager.getAllProfiles();
        
        profiles = profiles.stream()
                .peek(profile -> databaseManager.addTransaction(profile.getName(), 0, profile.getBalance(), null, "nullify"))
                .peek(profile -> profile.setBalance(0))
                .collect(Collectors.toList());
        
        databaseManager.updateProfiles(profiles);
        
        messages.getAndSend(sender, "command.globalnullify");
    }

}
