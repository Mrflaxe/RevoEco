package ru.mrflaxe.revoeco.database;

import com.j256.ormlite.field.DatabaseField;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Profile {
    
    @DatabaseField (id = true)
    private String id;
    @DatabaseField
    private String name;
    @DatabaseField
    private int balance;
    
    public Profile(String name) {
        this.id = name.toLowerCase();
        this.name = name;
        this.balance = 0;
    }
    
    public void add(int count) {
        balance = balance + count;
    }
}
