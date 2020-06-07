package com.nikorych.mycontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.nikorych.mycontacts.adapters.ContactsAdapter;
import com.nikorych.mycontacts.data.Contact;
import com.nikorych.mycontacts.data.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ContactsAdapter adapter;
    private RecyclerView recyclerViewContacts;
    private List<Contact> contacts = new ArrayList<>();
    private MainViewModel viewModel;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemGetStarted:
                getStarted();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (!preferences.getBoolean("started", false)) {
            getStarted();
            preferences.edit().putBoolean("started", true).apply();
        }

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        adapter = new ContactsAdapter(contacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        registerObserver();
        recyclerViewContacts.setAdapter(adapter);
        adapter.setOnContactIntentClickListener(new ContactsAdapter.OnContactIntentClickListener() {
            @Override
            public void onContactClick(int position) {
                Contact contact = adapter.getContacts().get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", contact.getId());
                startActivity(intent);
            }
        });
        adapter.setOnDeleteContactClickListener(new ContactsAdapter.OnDeleteContactClickListener() {
            @Override
            public void onContactClick(int position) {
                deleteContact(position);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
                deleteContact(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewContacts);
    }

    private void deleteContact(int position) {
        Contact contact = adapter.getContacts().get(position);
        viewModel.deleteContact(contact);
    }

    private void registerObserver() {
        LiveData<List<Contact>> contactsFromDb = viewModel.getContacts();
        contactsFromDb.observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contactsFromLiveData) {
                adapter.setContacts(contactsFromLiveData);
            }
        });
    }

    private void getStarted() {
        viewModel.deleteAllContacts();
        viewModel.insertContact(new Contact(R.drawable.contact1, "Welsh", "Corgi", "welshcorgi@gmail.com"));
        viewModel.insertContact(new Contact(R.drawable.contact_cat, "Egor", "Cat", "coshkaegor@mail.ru"));
        viewModel.insertContact(new Contact(R.drawable.contact_hamster, "John", "Hamster", "homyakdjon@ukr.ner"));
        viewModel.insertContact(new Contact(R.drawable.dog_contact, "Good", "Boy", "goodboy@gmail.com"));
        viewModel.insertContact(new Contact(R.drawable.pugs_contact, "Pugs", "Brothers", "brotherspugs@gmail.com"));
        viewModel.insertContact(new Contact(R.drawable.contact0, "Tsyatka", "Cat", "doroshenko.m23@gail.com"));
    }

    public void onClickAddContact(View view) {
        Intent intent = new Intent(this, EditContactActivity.class);
        startActivity(intent);
    }
}
