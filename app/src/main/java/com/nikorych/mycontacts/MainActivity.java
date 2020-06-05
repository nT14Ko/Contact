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
import android.net.Uri;
import android.os.Bundle;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nikorych.mycontacts.adapters.ContactsAdapter;
import com.nikorych.mycontacts.data.Contact;
import com.nikorych.mycontacts.data.MainViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

public class MainActivity extends AppCompatActivity {
    private ContactsAdapter adapter;
    private RecyclerView recyclerViewContacts;
    private List<Contact> contacts = new ArrayList<>();

    private MainViewModel viewModel;

    // Меню
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
        // Если приложение открывается в первый раз, то вызывается метод getStarted()
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        if (preferences.getInt("started", 0) == 0){
            int count = 0;
            getStarted();
            count++;
            preferences.edit().putInt("started", count).apply();
        }
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        adapter = new ContactsAdapter(contacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        // Получение данных из базы данных
        getData();
        recyclerViewContacts.setAdapter(adapter);
        // Активность детализации
        adapter.setOnContactIntentClickListener(new ContactsAdapter.OnContactIntentClickListener() {
            @Override
            public void onContactClick(int position) {
                Contact contact = adapter.getContacts().get(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("id", contact.getId());
                startActivity(intent);
            }
        });
        // Удаление контакта
        adapter.setOnDeleteContactClickListener(new ContactsAdapter.OnDeleteContactClickListener() {
            @Override
            public void onContactClick(int position) {
                deleteContact(position);
            }
        });
        // Удаление по свайпу
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped( RecyclerView.ViewHolder viewHolder, int i) {
                deleteContact(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewContacts);
    }


    private void deleteContact(int position){
        Contact contact = adapter.getContacts().get(position);
        viewModel.deleteContact(contact);
    }

    private void getData(){
        LiveData<List<Contact>> contactsFromDb = viewModel.getContacts();
        contactsFromDb.observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contactsFromLiveData) {
                adapter.setContacts(contactsFromLiveData);
            }
        });
    }
    private void getStarted(){
        viewModel.deleteAllContacts();
        viewModel.insertContact(new Contact(R.drawable.contact1, "Welsh", "Corgi", "welshcorgi@gmail.com"));
        viewModel.insertContact(new Contact(R.drawable.contact_cat, "Egor", "Cat", "coshkaegor@mail.ru"));
        viewModel.insertContact(new Contact(R.drawable.contact_hamster, "John", "Hamster", "homyakdjon@ukr.ner"));
        viewModel.insertContact(new Contact(R.drawable.dog_contact, "Good", "Boy", "goodboy@gmail.com"));
        viewModel.insertContact(new Contact(R.drawable.pugs_contact, "Pugs", "Brothers", "brotherspugs@gmail.com"));
    }

    public void onClickAddContact(View view) {
        Intent intent = new Intent(this, EditContactActivity.class);
        startActivity(intent);
    }
}
