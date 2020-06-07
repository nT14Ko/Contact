package com.nikorych.mycontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikorych.mycontacts.data.Contact;
import com.nikorych.mycontacts.data.MainViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageViewDetailContactPhoto;
    private TextView textViewDetailContactName;
    private TextView textViewDetailContactSurname;
    private TextView textViewDetailContactEmail;
    private ImageView imageViewEditContact;


    private int id;
    private Contact contact;


    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageViewDetailContactPhoto = findViewById(R.id.imageViewDetailContactPhoto);
        textViewDetailContactEmail = findViewById(R.id.textViewDetailContactEmail);
        textViewDetailContactName = findViewById(R.id.textViewDetailContactName);
        textViewDetailContactSurname = findViewById(R.id.textViewDetailContactSurname);
        imageViewEditContact = findViewById(R.id.imageViewEditContact);
        imageViewDetailContactPhoto = findViewById(R.id.imageViewDetailContactPhoto);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContact();
        setPhoto();
    }

    private void getContact() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);

        } else {
            finish();
        }
        contact = viewModel.getContactById(id);
        textViewDetailContactEmail.setText(contact.getEmail());
        textViewDetailContactName.setText(contact.getName());
        textViewDetailContactSurname.setText(contact.getSurname());
    }

    private void setPhoto() {
        if (contact.getIdPhoto() != 0 && contact.getPhoto() == null) {
            Picasso.get().load(contact.getIdPhoto()).placeholder(R.drawable.ic_launcher_foreground).into(imageViewDetailContactPhoto);
        } else {
            Picasso.get().load(Uri.parse(contact.getPhoto())).placeholder(R.drawable.ic_launcher_foreground).into(imageViewDetailContactPhoto);
        }
    }

    public void onClickEditContact(View view) {
        Intent intent = new Intent(DetailActivity.this, EditContactActivity.class);
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }
}
