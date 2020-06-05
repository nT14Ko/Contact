package com.nikorych.mycontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.insertContact(new Contact(R.drawable.contact1, "Timothy", "Petrov", "email"));
        adapter = new ContactsAdapter(contacts);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        getData();
        recyclerViewContacts.setAdapter(adapter);
        adapter.setOnContactIntentClickListener(new ContactsAdapter.OnContactIntentClickListener() {
            @Override
            public void onContactClick(int position) {
                Contact contact = adapter.getContacts().get(position);
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                intent.putExtra("id", contact.getId());
//                startActivity(intent);
                Toast.makeText(MainActivity.this, contact.getPhoto(), Toast.LENGTH_SHORT).show();
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

    public void onClickAddContact(View view) {
        Intent intent = new Intent(this, EditContactActivity.class);
        startActivity(intent);
    }
}
