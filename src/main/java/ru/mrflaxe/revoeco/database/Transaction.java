package ru.mrflaxe.revoeco.database;

import java.util.Date;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import lombok.Data;
import lombok.NoArgsConstructor;

@DatabaseTable (tableName = "transactions")
@Data
@NoArgsConstructor
public class Transaction {
    
    @DatabaseField (generatedId = true)
    private int id;
    @DatabaseField (columnName = "owner")
    private String balanceOwner;
    @DatabaseField
    private int balanceAfter;
    @DatabaseField
    private int balanceBefore;
    @DatabaseField
    private String involved;
    @DatabaseField
    private String type;
    @DatabaseField (dataType = DataType.DATE)
    private Date date;
    
    public Transaction(
                String balanceOwner,
                int balanceAfter,
                int balanceBefore,
                String involved,
                String type) {
        
        this.balanceOwner = balanceOwner;
        this.balanceAfter = balanceAfter;
        this.balanceBefore = balanceBefore;
        this.involved = involved;
        this.type = type;
        this.date = new Date(System.currentTimeMillis());
    }
}
