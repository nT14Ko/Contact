package com.nikorych.mycontacts.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {

    private static ContactsDatabase database;

    private LiveData<List<Contact>> contacts;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = ContactsDatabase.getInstance(getApplication());
        contacts = database.contactsDao().getAllContacts();
    }


    public LiveData<List<Contact>> getContacts() {
        return contacts;
    }


    public void insertContact(Contact contact) {
        new InsertTask().execute(contact);
    }

    private static class InsertTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {
            if (contacts != null && contacts.length > 0) {
                database.contactsDao().insertContact(contacts[0]);
            }
            return null;
        }
    }

    public void deleteContact(Contact contact) {
        new DeleteTask().execute(contact);
    }

    private static class DeleteTask extends AsyncTask<Contact, Void, Void> {
        @Override
        protected Void doInBackground(Contact... contacts) {
            if (contacts != null && contacts.length > 0) {
                database.contactsDao().deleteContact(contacts[0]);
            }
            return null;
        }
    }

    public Contact getContactById(int id) {
        try {
            return new GetContactTask().execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class GetContactTask extends AsyncTask<Integer, Void, Contact> {
        @Override
        protected Contact doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.contactsDao().getContactById(integers[0]);
            }
            return null;
        }
    }

    public void deleteAllContacts() {
        new DeleteAllContactsTask().execute();
    }

    private static class DeleteAllContactsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... contacts) {
            database.contactsDao().deleteAllContacts();
            return null;
        }
    }
}
