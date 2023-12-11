package com.akshitha.contact;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//This line declares the AppDatabase class as an abstract class that extends RoomDatabase.
//It also includes annotations that specify the entities (tables) in the database,
// the version of the database, and disables schema export.
@Database(entities = {ContactEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //Define constants for the number of threads and create an ExecutorService

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //Declare a private static instance of the database and initialize it

    private static AppDatabase instance;

    public abstract ContactDao contactDao();

    //Implement a singleton pattern to get a single instance of the database

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "contact_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}