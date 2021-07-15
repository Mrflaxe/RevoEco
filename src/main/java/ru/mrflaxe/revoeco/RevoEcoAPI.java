package ru.mrflaxe.revoeco;

import lombok.AllArgsConstructor;
import ru.mrflaxe.revoeco.database.DatabaseManager;
import ru.mrflaxe.revoeco.database.Profile;
import ru.mrflaxe.revoeco.transaction.InterpreterRegistrator;
import ru.mrflaxe.revoeco.transaction.TransactionInterpreter;

@AllArgsConstructor
public class RevoEcoAPI {

    private final DatabaseManager databaseManager;
    private final InterpreterRegistrator interpreterRegistrator;
    
    /**
     * This method registrate your transaction interpreter for type Id.
     * When the manager requests the interpretation of a transaction of this type,
     * this interpreter will be called
     * 
     * @param interpreter - intrepreter of this transaction
     * @param type - typeId of transaction
     * @return true if interpreter successfully registered or false if it's registered already
     */
    public boolean registerTransactionInterpreter(TransactionInterpreter interpreter, String type) {
        if(interpreterRegistrator.isRegistered(type)) return false;
        
        interpreterRegistrator.registerInterpreter(interpreter, type);
        return true;
    }
    
    /**
     * This method just unregistrate your interpreter for typeId.
     * 
     * @param type - typeId of transaction
     * @return - true if interpreter successfully unregistered or false if it's unregistered already
     */
    public boolean unregisterTransactionInterperter(String type) {
        if(!interpreterRegistrator.isRegistered(type)) return false;
        interpreterRegistrator.unregister(type);
        return true;
    }
    
    /**
     * Checks are registered any interpreter for this type, or not.
     * @param type - typeId of transaction
     * @return true if registered or false if not
     */
    public boolean isRegistered(String type) {
        return interpreterRegistrator.isRegistered(type);
    }
    
    
    /**
     * Gets current player balance
     * @param playerName - name of player
     * @return int balance
     */
    public int getPlayerBalance(String playerName) {
        return databaseManager.getOrCreateProfile(playerName).getBalance();
    }
    
    /**
     * Checks have a player enough money on his balance or not
     * @param playerName - name of player
     * @param count - count to check
     * @return true if he has or not if he doesn't
     */
    public boolean hasEnough(String playerName, int count) {
        return databaseManager.hasEnough(playerName, count);
    }
    
    /**
     * Takes count of money from a player balance
     * @param name - name of player
     * @param count - count to take
     */
    public void take(String name, int count) {
        Profile profile = databaseManager.getOrCreateProfile(name);
        profile.setBalance(profile.getBalance() - count);
        databaseManager.updateProfile(profile);
    }
    
    /**
     * Adds count of money to a player balance
     * @param name - name of player
     * @param count - count to add
     */
    public void add(String name, int count) {
        Profile profile = databaseManager.getOrCreateProfile(name);
        profile.setBalance(profile.getBalance() + count);
        databaseManager.updateProfile(profile);
    }
}
