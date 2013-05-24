package org.black_ixx.playerpoints;

import org.black_ixx.playerpoints.commands.Commander;
import org.black_ixx.playerpoints.config.LocalizeConfig;
import org.black_ixx.playerpoints.config.RootConfig;
import org.black_ixx.playerpoints.listeners.VotifierListener;
import org.black_ixx.playerpoints.storage.StorageHandler;
import org.black_ixx.playerpoints.storage.imports.Importer;
import org.black_ixx.playerpoints.update.Update;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main plugin class for PlayerPoints.
 */
public class PlayerPoints extends JavaPlugin {

   /**
    * Plugin tag.
    */
   public static final String TAG = "[PlayerPoints]";

   /**
    * Root config reference.
    */
   private RootConfig rootConfig;

   /**
    * Storage handler.
    */
   private StorageHandler storage;

   /**
    * API instance.
    */
   private PlayerPointsAPI api;

   @Override
   public void onEnable() {
      // Initialize localization
      LocalizeConfig.init(this);
      // Initialize config
      rootConfig = new RootConfig(this);
      // Do imports
      Importer importer = new Importer(this);
      importer.checkImport();
      // Intialize storage handler
      storage = new StorageHandler(this);
      // Initialize API
      api = new PlayerPointsAPI(this);
      // Initialize updater
      Update.init(this);
      Update.checkUpdate();
      // Register commands
      final Commander commander = new Commander(this);
      getCommand("points").setExecutor(commander);
      getCommand("p").setExecutor(commander);
      // Register votifier listener, if applicable
      if(rootConfig.voteEnabled) {
         final PluginManager pm = getServer().getPluginManager();
         final Plugin votifier = pm.getPlugin("Votifier");
         if(votifier != null) {
            pm.registerEvents(new VotifierListener(this), this);
         } else {
            getLogger().warning("Could not hook into Votifier!");
         }
      }
   }

   /**
    * Get the plugin's API.
    * 
    * @return API instance.
    */
   public PlayerPointsAPI getAPI() {
      return api;
   }

   /**
    * Get the root plugin config.yml handler.
    * 
    * @return Root config handler.
    */
   public RootConfig getRootConfig() {
      return rootConfig;
   }

   /**
    * Get the storage handler for player points data.
    * 
    * @return Storage handler.
    */
   public StorageHandler getStorageHandler() {
      return storage;
   }
}