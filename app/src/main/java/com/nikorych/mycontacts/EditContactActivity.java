package com.nikorych.mycontacts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class EditContactActivity extends AppCompatActivity {



    private ImageView imageViewBigContactPhoto;
    private ImageView imageViewEditContactPhoto;
    private EditText editTextContactName;
    private EditText editTextContactSurname;
    private EditText editTextContactEmail;
    private Contact contact;
    private static final int RC_GET_IMAGE = 1;


    private int id;
    private int photoId;
    private String photo;

    private MainViewModel viewModel;

    @Override
    protected void onResume() {
        super.onResume();
        // Если у фотографии есть какое-то айди (фотография была добавлена мною вручную (в drawable)), то Пикассо поставит это фото. В ином случае - Пикассо попробует найти фотографию по адресу
        if (contact.getIdPhoto() != 0 && contact.getPhoto() == null){
            Picasso.get().load(contact.getIdPhoto()).placeholder(R.drawable.ic_launcher_foreground).into(imageViewBigContactPhoto);
        } else {
            Picasso.get().load(Uri.parse(contact.getPhoto())).placeholder(R.drawable.ic_launcher_foreground).into(imageViewBigContactPhoto);
        }

    }

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
        // Получаем id фильма, ели оно есть, то заполняем соответсвующие  поля
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
    public void onClickSaveContact(View view) {
        String email = editTextContactEmail.getText().toString().trim();
        String name = editTextContactName.getText().toString().trim();
        String surname = editTextContactSurname.getText().toString().trim();
        getPhoto();
        // Если поля заполнены, то сохраняем контакт, если нет - выоводим Тост
        if (isFilled(name, surname, email)){
            // Если у контакта есть id, то мы его перезапишем. В первом случае с айди фото, если этот контакт "системный(в drawable)" и во втором случае с адресом фото
            if (id != 0){
                if (isSystemPhoto()){
                    Contact contact = new Contact(id, photoId, name, surname, email);
                    viewModel.insertContact(contact);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else  {
                    Contact contact = new Contact(id, photo, name, surname, email);
                    viewModel.insertContact(contact);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
                // Если у контакта нет id, то мы его создадим, и если не была указана никакая фотография, то в качестве фото контакта будет фото из drawable
            } else {
                if (photo != null){
                    Contact contact = new Contact(photo, name, surname, email);
                    viewModel.insertContact(contact);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                } else {
                        Contact contact = new Contact(R.drawable.ic_launcher_foreground, name, surname, email);
                        viewModel.insertContact(contact);
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);

                }

            }

        } else {
            Toast.makeText(this, "Все поля должны быть заполенны", Toast.LENGTH_SHORT).show();
        }

    }

    // Проверка заполненности полей
    private boolean isFilled(String name, String surname, String email){
        return !name.isEmpty() && !surname.isEmpty() && !email.isEmpty();
    }

    // ПРоверка на то, содержится ли фотография в drawable
    private boolean isSystemPhoto(){
        if (contact.getIdPhoto() != 0 && contact.getPhoto() == null){
            return true;
        } else {
            return false;
        }
    }
    private void getPhoto(){
        if (isSystemPhoto()){
            photoId = contact.getIdPhoto();
        } else {
            photo = contact.getPhoto();
        }
    }

    //Интент для того, чтобы взять фото из галереи
    public void onclickEditPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(intent,RC_GET_IMAGE);

    }

    //Устанавливаем у нашей фотографии адрес из галереии
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_GET_IMAGE && resultCode == RESULT_OK){
            if (data != null) {
                Uri uri = data.getData();
                assert uri != null;
                contact.setPhoto(uri.toString());
            }
        }
    }

    public void onClickGoBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
