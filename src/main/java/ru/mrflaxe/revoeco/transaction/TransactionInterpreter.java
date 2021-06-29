package ru.mrflaxe.revoeco.transaction;

import ru.mrflaxe.revoeco.database.Transaction;

@FunctionalInterface
public interface TransactionInterpreter {
    
    String interpreteTransaction(Transaction transaction);
    
}
