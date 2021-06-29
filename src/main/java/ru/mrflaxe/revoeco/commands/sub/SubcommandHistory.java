package ru.mrflaxe.revoeco.commands.sub;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;

import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.database.Transaction;
import ru.mrflaxe.revoeco.transaction.InterpreterRegistrator;
import ru.soknight.lib.argument.CommandArguments;
import ru.soknight.lib.command.preset.subcommand.PermissibleSubcommand;
import ru.soknight.lib.configuration.Messages;

public class SubcommandHistory extends PermissibleSubcommand {

    private final Messages messages;
    private final DatabaseManager databaseManager;
    private final InterpreterRegistrator interpreter;
    
    public SubcommandHistory(Messages messages, DatabaseManager databaseManager, InterpreterRegistrator container) {
        super("reco.command.history", messages);
        
        this.messages = messages;
        this.databaseManager = databaseManager;
        this.interpreter = container;
        
    }

    @Override
    protected void executeCommand(CommandSender sender, CommandArguments args) {
        if(args.size() > 0) {
            
        }
        
        int page;
        if(args.isEmpty()) page = 1;
        else page = args.getAsInteger(0);
        
        if(page <= 0) {
            messages.getAndSend(sender, "error.low-page");
            return;
        }
        
        List<Transaction> transactions = databaseManager.getTransactionsByPlayer(sender.getName());
        transactions = Lists.reverse(transactions);
        
        List<String> message = new ArrayList<>();
        
        int dozen = (page - 1) * 10;
        
        if(transactions.size() <= dozen) {
            sender.sendMessage(" ");
            messages.getAndSend(sender, "command.baltop.empty-page");
            return;
        }
        
        message.add(" ");
        message.add(messages.getFormatted("command.history.header", "%page%", page, "%pages%", transactions.size()/10 + 1));
        
        for(int i = 1; i <= 10; i++) {
            int index = i + dozen;
            
            if(index >= transactions.size()) break;
            
            Transaction transaction = transactions.get(index - 1);
            message.add(interpreter.interprete(transaction));
        }
        
        message.forEach(m -> {
            sender.sendMessage(m);
        });
        
    }

}
