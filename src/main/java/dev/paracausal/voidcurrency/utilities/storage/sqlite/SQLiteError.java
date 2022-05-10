package dev.paracausal.voidcurrency.utilities.storage.sqlite;

import dev.paracausal.voidcurrency.Core;

import java.util.logging.Level;

public class SQLiteError {

    public static void execute(Core core, Exception ex){
        core.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(Core core, Exception ex){
        core.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }

}
