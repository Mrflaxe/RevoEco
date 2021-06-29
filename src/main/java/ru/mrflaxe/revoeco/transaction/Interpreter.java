package ru.mrflaxe.revoeco.transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.plugin.Plugin;

import ru.mrflaxe.revoeco.database.Transaction;
import ru.soknight.lib.configuration.Configuration;
import ru.soknight.lib.configuration.Messages;

public class Interpreter implements TransactionInterpreter {

    private final Messages messages;
    private final DateFormat dateFormatter;
    
    public Interpreter(Messages messages, Configuration config, Plugin plugin) {
        this.messages = messages;
        dateFormatter = getDateFormat(config, plugin);
    }
    
    private DateFormat getDateFormat(Configuration config, Plugin plugin) {
        String formatPattern = config.getString("dateformat-pattern");
        
        try {
            return new SimpleDateFormat(formatPattern);
        } catch (Exception e) {
            plugin.getLogger().severe("You use wrong date format: " + formatPattern + ". Will be used default format");
            return new SimpleDateFormat("dd.MM.yy - kk:mm:ss");
        }
    }

    @Override
    public String interpreteTransaction(Transaction transaction) {
        String type = transaction.getType();
        String involved = transaction.getInvolved();
        
        int after = transaction.getBalanceAfter();
        int before = transaction.getBalanceBefore();
        
        Date date = transaction.getDate();
        
        boolean isTransfer = type.equals("transfer");
        boolean isAdmin = type.equals("admin");
        
        if(isTransfer || isAdmin) {
            boolean decrease = after < before;
            
            String transfer = " " + (decrease ? "\u00a7c-" : "\u00a7a+") + "" + Math.abs(after - before);
            
            
            String action = isTransfer ?
                        messages.getFormatted(decrease ? "command.history.action.transfer.decrease"
                                : "command.history.action.transfer.increase", "%name%", involved)
                        : messages.getFormatted(decrease ? "command.history.action.admin.decrease"
                                : "command.history.action.admin.increase", "%name%", involved);
            
            String formattedDate = dateFormatter.format(date.getTime());
            
            return messages.getFormatted("command.history.build", "%transfer%", transfer, "%action%", action, "%date%", formattedDate);
        }
        
        if(type.equals("nullify")) {
            boolean isZero = false;
            boolean decrease = false;
            
            if(after == before) isZero = true;
            else decrease = after < before;
            
            String transfer = " " + (isZero ? "\u00a7f" : decrease ? "\u00a7c-" : "\u00a7a+") + "" + Math.abs(after - before);
            String action = messages.getColoredString("command.history.action.nullify");
            String formattedDate = dateFormatter.format(date.getTime());
            
            return  messages.getFormatted("command.history.build", "%transfer%", transfer, "%action%", action, "%date%", formattedDate);
        }
        
        return "error";
    }

}
