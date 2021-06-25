package ru.mrflaxe.revoeco.commands.sub;

import org.bukkit.command.CommandSender;

import ru.mrflaxe.revoeco.RevoEco;
import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.subcommand.PermissibleSubcommand;
import ru.soknight.lib.configuration.Messages;

public class SubcommandConfigRefresh extends PermissibleSubcommand {

    private final Messages messages;
    private final RevoEco plugin;
    
    public SubcommandConfigRefresh(Messages messages, RevoEco plugin) {
        super("reco.command.configrefresh", messages);
        
        this.messages = messages;
        this.plugin = plugin;
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        plugin.refreshConfigs();
        messages.getAndSend(sender, "command.configrefresh");
    }

}
