package ru.mrflaxe.revoeco.transaction;

import java.util.HashMap;

import ru.mrflaxe.revoeco.database.Transaction;

public class InterpreterRegistrator {
    
    private static final TransactionInterpreter DEFAULT_INTERPRETER;

    private final HashMap<String, TransactionInterpreter> interpreters;
    
    public InterpreterRegistrator() {
        this.interpreters = new HashMap<>();
    }
    
    public boolean registerInterpreter(TransactionInterpreter interpreter, String type) {
        if(interpreters.containsKey(type)) return false;
        interpreters.put(type, interpreter);
        return true;
    }
    
    public boolean isRegistered(String type) {
        return interpreters.containsKey(type);
    }

    public boolean unregister(String type) {
        return interpreters.remove(type) != null;
    }
    
    public String interprete(Transaction transaction) {
        TransactionInterpreter interpreter = interpreters.getOrDefault(transaction.getType(), DEFAULT_INTERPRETER);
        return interpreter.interpreteTransaction(transaction);
    }
    
    public boolean registerInterpreter(TransactionInterpreter interpreter, String... types) {
        if(types.length == 0) return false;
        
        for(String type : types) {
            if(isRegistered(type)) return false;
            
            interpreters.put(type, interpreter);
        }

        return true;
    }
    
    static {
        DEFAULT_INTERPRETER = transaction -> {
            String involved = transaction.getInvolved();
            float balanceBefore = transaction.getBalanceBefore();
            float balanceAfter = transaction.getBalanceAfter();
            String type = transaction.getType();
            
            return String.format(
                    "Transaction for %s (%s), balance: %.2f -> %.2f",
                    involved,
                    type,
                    balanceBefore,
                    balanceAfter
            );
        };
    }
}
