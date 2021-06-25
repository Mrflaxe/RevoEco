package ru.mrflaxe.revoeco.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.database.Profile;
import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.standalone.PermissibleCommand;
import ru.soknight.lib.configuration.Messages;

public class CommandBalancetop extends PermissibleCommand {

    private final Messages messages;
    private final DatabaseManager databaseManager;
    
    public CommandBalancetop(Messages messages, DatabaseManager databaseManager, JavaPlugin plugin) {
        super("balancetop", "reco.command.balancetop", messages);
        
        this.messages = messages;
        this.databaseManager = databaseManager;
        
        super.register(plugin);
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        List<String> message = new ArrayList<>();
        
        int page;
        if(args.isEmpty()) page = 1;
        else page = args.getAsInteger(0);
        
        if(page <= 0) {
            messages.getAndSend(sender, "error.low-page");
            return;
        }
        
        message.add(" ");
        
        String head = messages.getFormatted("command.baltop.head", "%page%", page);
        message.add(head);
        
        int dozen = (page - 1) * 10;
        List<Profile> sortedProfiles = sortProfiles();
        
        if(sortedProfiles.size() < dozen + 1) {
            messages.getAndSend(sender, " ");
            messages.getAndSend(sender, "command.baltop.empty-page");
            return;
        }
        
        for(int i = 1; i <= 10; i++) {
            int place = i + dozen;
            
            if(i > sortedProfiles.size()) break;
            
            Profile profile = sortedProfiles.get(place - 1);
            String name = profile.getName();
            int balance = profile.getBalance();
            
            String bodyFragment = messages.getFormatted("command.baltop.place", "%place%", place, "%name%", name, "%balance%", balance);
            message.add(bodyFragment);
        }
        
        message.forEach(m -> sender.sendMessage(m));
    }

    // comb srot
    private List<Profile> sortProfiles() {
        List<Profile> profiles = databaseManager.getAllProfiles();
        
        int gap = profiles.size();
        boolean swapped = true;
        
        while (gap > 1 || swapped) {
            if (gap > 1)
                gap = (int) (gap / 1.247330950103979);

            int i = 0;
            swapped = false;
            while (i + gap < profiles.size()) {
                if (profiles.get(i).getBalance() < profiles.get(i + gap).getBalance()) {
                    Profile t = profiles.get(i);
                    
                    profiles.set(i, profiles.get(i + gap));
                    profiles.set(i + gap, t);
                    
                    swapped = true;
                }
                i++;
            }
        }
        
        return profiles;
    }
    
}
