package ru.mrflaxe.revoeco.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

public class DatabaseManager {

    private final Logger logger;
    private final ConnectionSource connection;
    
    private final Dao<Profile, String> profileDao;
    
    public DatabaseManager(Plugin plugin, Database database) throws SQLException {
        logger = plugin.getLogger();
        connection = database.getConnection();
        
        profileDao = DaoManager.createDao(connection, Profile.class);
    }
    
    public void shutdown() {
        try {
            if(connection != null) connection.close();
        } catch (IOException e) {
            logger.severe("Failed to shutdown database connection: " + e.getLocalizedMessage());
        }
    }
    
    public Profile createProfile(String name) {
        Profile profile = new Profile(name);
        try {
            profileDao.create(profile);
            return profile;
        } catch (SQLException e) {
            logger.severe("Failed to create balance data for database: " + e.getLocalizedMessage());
            return null;
        }
    }
    
    public void updateProfile(Profile profile) {
        try {
            profileDao.update(profile);
        } catch (SQLException e) {
            logger.severe("Failed to update balance data in database: " + e.getLocalizedMessage());
        }
    }
    
    public void updateProfiles(Collection<Profile> collection) {
        for(Profile profile : collection) {
            updateProfile(profile);
        }
    }
    
    public void deleteProfile(Profile profile) {
        try {
            profileDao.delete(profile);
        } catch (SQLException e) {
            logger.severe("Failed to delete balance data from database: " + e.getLocalizedMessage());
        }
    }
    /**
     * 
     * @param id - player name in lower case
     * @return
     */
    public Profile getProfileById(String name) {
        try {
            return profileDao.queryForId(name.toLowerCase());
        } catch (SQLException e) {
            logger.severe("Failed to get balance data by ID from database: " + e.getLocalizedMessage());
            return null;
        }
    }
    
    public Profile getOrCreateProfile(String name) {
        Profile profile = getProfileById(name.toLowerCase());
        if(profile != null) return profile;
        
        return createProfile(name);
    }
    
    public boolean hasEnough(String name, int sum) {
        Profile profile = getOrCreateProfile(name);
        int balance = profile.getBalance();
        
        return balance > sum;
    }
    
    public boolean hasProfile(String name) {
        return getProfileById(name.toLowerCase()) != null;
    }
    
    public List<Profile> getAllProfiles() {
        try {
            return profileDao.queryForAll();
        } catch (SQLException e) {
            logger.severe("Failed to get all profile data from the database: " + e.getLocalizedMessage());
            return null;
        }
    }
}
