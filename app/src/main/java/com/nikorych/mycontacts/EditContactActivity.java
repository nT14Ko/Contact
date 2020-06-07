package com.nikorych.mycontacts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nikorych.mycontacts.data.Contact;
import com.nikorych.mycontacts.data.MainViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class EditContactActivity extends AppCompatActivity {


    private ImageView imageViewBigContactPhoto;
    private ImageView imageViewEditContactPhoto;
    private EditText editTextContactName;
    private EditText editTextContactSurname;
    private EditText editTextContactEmail;
    private Contact contact;
    private static final int RC_GET_IMAGE = 1;


    private int index;
    private int id;
    private int photoId;
    private String photo;

    private MainViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        imageViewEditContactPhoto = findViewById(R.id.imageViewEditContactPhoto);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        editTextContactEmail = findViewById(R.id.editTextContactEmail);
        editTextContactName = findViewById(R.id.editTextContactName);
        editTextContactSurname = findViewById(R.id.editTextContactSurname);
        imageViewBigContactPhoto = findViewById(R.id.imageViewBigContactPhoto);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("id")) {
            id = intent.getIntExtra("id", -1);
            imageViewEditContactPhoto.setVisibility(View.GONE);
            contact = viewModel.getContactById(id);
            editTextContactSurname.setText(contact.getSurname());
            editTextContactName.setText(contact.getName());
            editTextContactEmail.setText(contact.getEmail());
        } else {
            contact = new Contact();
            contact.setIdPhoto(R.drawable.ic_launcher_foreground);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (contact.getIdPhoto() != 0 && contact.getPhoto() == null) {
            Picasso.get().load(contact.getIdPhoto()).placeholder(R.drawable.ic_launcher_foreground).into(imageViewBigContactPhoto);
        } else {
            Picasso.get().load(Uri.parse(contact.getPhoto())).placeholder(R.drawable.ic_launcher_foreground).into(imageViewBigContactPhoto);
        }
    }

    public void onClickSaveContact(View view) {
        String email = editTextContactEmail.getText().toString().trim();
        String name = editTextContactName.getText().toString().trim();
        String surname = editTextContactSurname.getText().toString().trim();
        getPhoto();
        if (isFilled(name, surname, email)) {
            if (id != 0) {
                if (isSystemPhoto()) {
                    Contact contact = new Contact(id, photoId, name, surname, email);
                    viewModel.insertContact(contact);
                    finish();
                } else {
                    Contact contact = new Contact(id, photo, name, surname, email);
                    viewModel.insertContact(contact);
                    finish();
                }
            } else {
                if (photo != null) {
                    Contact contact = new Contact(photo, name, surname, email);
                    viewModel.insertContact(contact);
                    finish();
                } else {
                    Contact contact = new Contact(R.drawable.ic_launcher_foreground, name, surname, email);
                    viewModel.insertContact(contact);
                    finish();
                }
            }
        } else {
            Toast.makeText(this, "Все поля должны быть заполенны", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFilled(String name, String surname, String email) {
        return !name.isEmpty() && !surname.isEmpty() && !email.isEmpty();
    }

    private boolean isSystemPhoto() {
        if (contact.getIdPhoto() != 0 && contact.getPhoto() == null) {
            return true;
        } else {
            return false;
        }
    }

    private void getPhoto() {
        if (isSystemPhoto()) {
            photoId = contact.getIdPhoto();
        } else {
            photo = contact.getPhoto();
        }
    }

    public void onclickEditPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent, RC_GET_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                assert uri != null;
                contact.setPhoto(uri.toString());
            }
        }
    }

    public void onClickGoBack(View view) {
        finish();
    }
}
