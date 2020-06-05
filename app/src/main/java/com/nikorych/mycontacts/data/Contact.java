package com.nikorych.mycontacts.data;

import android.widget.ImageView;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contact {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int idPhoto;
    private String photo;
    private String name;
    private String surname;
    private String email;


    public Contact(int id, String photo, String name, String surname, String email) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
    @Ignore
    public Contact(int id, int IdPhoto, String name, String surname, String email) {
        this.id = id;
        this.idPhoto = IdPhoto;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
    @Ignore
    public Contact(int IdPhoto, String name, String surname, String email) {
        this.idPhoto = IdPhoto;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
    @Ignore
    public Contact(String photo, String name, String surname, String email) {
        this.photo = photo;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }
    @Ignore
    public Contact(){

    }

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(int idPhoto) {
        this.idPhoto = idPhoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
