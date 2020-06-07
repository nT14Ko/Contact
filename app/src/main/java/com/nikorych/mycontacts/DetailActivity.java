package com.nikorych.mycontacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nikorych.mycontacts.data.Contact;
import com.nikorych.mycontacts.data.MainViewModel;
import com.squareup.picasso.Picasso;

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
        Intent intent = getIntent();
        // Получаем id фильма
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);

        } else {
            finish();
        }
        // По этому id получаем фильм из базы данных и заполняем соответсвующие поля
        contact = viewModel.getContactById(id);
        textViewDetailContactEmail.setText(contact.getEmail());
        textViewDetailContactName.setText(contact.getName());
        textViewDetailContactSurname.setText(contact.getSurname());
        // Если у фотографии есть какое-то айди (фотография была добавлена мною вручную(в drawable)), то Пикассо поставит это фото. В ином случае - Пикассо попробует найти фотографию по адресу
        if (contact.getIdPhoto() != 0) {
            Picasso.get().load(contact.getIdPhoto()).placeholder(R.drawable.ic_launcher_foreground).into(imageViewDetailContactPhoto);
        } else {
            Picasso.get().load(contact.getPhoto()).placeholder(R.drawable.ic_launcher_foreground).into(imageViewDetailContactPhoto);
        }
    }

    public void onClickEditContact(View view) {
        Intent intent = new Intent(DetailActivity.this, EditContactActivity.class);
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }
}
