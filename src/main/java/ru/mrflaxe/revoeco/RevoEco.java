package ru.mrflaxe.revoeco;

import org.bukkit.plugin.java.JavaPlugin;

import ru.mrflaxe.revoeco.commands.CommandBalance;
import ru.mrflaxe.revoeco.commands.CommandBalancetop;
import ru.mrflaxe.revoeco.commands.CommandPay;
import ru.mrflaxe.revoeco.commands.SubcommandHandler;
import ru.mrflaxe.revoeco.database.Database;
import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.transaction.Interpreter;
import ru.mrflaxe.revoeco.transaction.InterpreterRegistrator;
import ru.mrflaxe.revoeco.transaction.TransactionInterpreter;
import ru.soknight.lib.configuration.Configuration;
import ru.soknight.lib.configuration.Messages;


public class RevoEco extends JavaPlugin {
    
    private Configuration config;
    private Messages messages;
    
    private static RevoEcoAPI api;
    
    private DatabaseManager databaseManager;
    private InterpreterRegistrator interpreterRegistrator;
    
    @Override
    public void onEnable() {
        initconfigs();
        
        try {
            Database database = new Database(this, config);
            this.databaseManager = new DatabaseManager(this, database);
        } catch (Exception e) {
            getLogger().severe("Failed to connect SQLite database");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        
        this.interpreterRegistrator = new InterpreterRegistrator();
        registerInterpreters();
        
        api = new RevoEcoAPI(databaseManager, interpreterRegistrator);
        
        registerCommands();
    }
    
    @Override
    public void onDisable() {
        if(databaseManager != null) databaseManager.shutdown();
    }

    private void initconfigs() {
        this.config = new Configuration(this, "config.yml");
        config.refresh();
        
        this.messages = new Messages(this, "messages.yml");
        messages.refresh();
    }
    
    public void refreshConfigs() {
        config.refresh();
        messages.refresh();
    }
    
    private void registerInterpreters() {
        TransactionInterpreter interpreter = new Interpreter(messages, config, this);
        interpreterRegistrator.registerInterpreter(interpreter, "transfer", "admin", "nullify");
    }
    
    private void registerCommands() {
        new SubcommandHandler(messages, databaseManager, interpreterRegistrator, this);
        
        new CommandBalance(messages, databaseManager, this);
        new CommandPay(messages, databaseManager, this);
        new CommandBalancetop(messages, databaseManager, this);
    }

    static RevoEcoAPI getAPI() {
        return api;
    }
    
}
