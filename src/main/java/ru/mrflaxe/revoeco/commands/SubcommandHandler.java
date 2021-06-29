package ru.mrflaxe.revoeco.commands;

import ru.mrflaxe.revoeco.RevoEco;
import ru.mrflaxe.revoeco.commands.sub.SubcommandConfigRefresh;
import ru.mrflaxe.revoeco.commands.sub.SubcommandGive;
import ru.mrflaxe.revoeco.commands.sub.SubcommandGlobalNullify;
import ru.mrflaxe.revoeco.commands.sub.SubcommandHelp;
import ru.mrflaxe.revoeco.commands.sub.SubcommandNullify;
import ru.mrflaxe.revoeco.commands.sub.SubcommandTake;
import ru.mrflaxe.revoeco.commands.sub.SubcommandHistory;
import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.transaction.InterpreterRegistrator;
import ru.soknight.lib.command.preset.ModifiedDispatcher;
import ru.soknight.lib.configuration.Messages;

public class SubcommandHandler extends ModifiedDispatcher {

    public SubcommandHandler(Messages messages, DatabaseManager databaseManager, InterpreterRegistrator container, RevoEco plugin) {
        super("revoeco", messages);
        
        super.setExecutor("give", new SubcommandGive(messages, databaseManager));
        super.setExecutor("take", new SubcommandTake(messages, databaseManager));
        super.setExecutor("configrefresh", new SubcommandConfigRefresh(messages, plugin));
        super.setExecutor("nullify", new SubcommandNullify(messages, databaseManager));
        super.setExecutor("globalnullify", new SubcommandGlobalNullify(messages, databaseManager));
        super.setExecutor("help", new SubcommandHelp(messages));
        super.setExecutor("history", new SubcommandHistory(messages, databaseManager, container));
        
        super.register(plugin);
    }

}
