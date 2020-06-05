package com.nikorych.mycontacts.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactsDao {

    @Query("SELECT * FROM contacts ORDER BY name")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM contacts WHERE id == :contactId")
    Contact getContactById(int contactId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);

    @Query("DELETE FROM contacts")
    void deleteAllContacts();
}
