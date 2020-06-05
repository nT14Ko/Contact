package com.nikorych.mycontacts.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactsDatabase extends RoomDatabase {
    private static ContactsDatabase database;
    private static final String DB_NAME = "contacts3.db";
    private static final Object LOCK = new Object();

    public static ContactsDatabase getInstance(Context context){
        synchronized (LOCK){
            if (database == null){
                database = Room.databaseBuilder(context, ContactsDatabase.class, DB_NAME).build();
            }
            return database;
        }
    }
    public abstract ContactsDao contactsDao();
}
