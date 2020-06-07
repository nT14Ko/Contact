package com.nikorych.mycontacts.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nikorych.mycontacts.R;
import com.nikorych.mycontacts.data.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<Contact> contacts;
    private OnDeleteContactClickListener onContactClickListener;
    private OnContactIntentClickListener onContactIntentClickListener;

    public interface OnDeleteContactClickListener {
        void onContactClick(int position);
    }

    public interface OnContactIntentClickListener {
        void onContactClick(int position);
    }

    public void setOnDeleteContactClickListener(OnDeleteContactClickListener onContactClickListener) {
        this.onContactClickListener = onContactClickListener;
    }

    public void setOnContactIntentClickListener(OnContactIntentClickListener onContactIntentClickListener) {
        this.onContactIntentClickListener = onContactIntentClickListener;
    }

    public ContactsAdapter(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        if (contact.getIdPhoto() != 0 && contact.getPhoto() == null) {
            Picasso.get().load(contact.getIdPhoto()).placeholder(R.drawable.ic_launcher_foreground).into(holder.imageViewContactPhoto);
        } else {
            Picasso.get().load(Uri.parse(contact.getPhoto())).placeholder(R.drawable.ic_launcher_foreground).into(holder.imageViewContactPhoto);
        }
        holder.textViewContactName.setText(contact.getName());
        holder.textViewContactSurname.setText(contact.getSurname());
        holder.textViewContactEmail.setText(contact.getEmail());
    }

    private static Bitmap base64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }


    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewContactPhoto;
        private TextView textViewContactName;
        private TextView textViewContactSurname;
        private TextView textViewContactEmail;
        private ImageView imageViewDeleteContact;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewContactPhoto = itemView.findViewById(R.id.imageViewContactPhoto);
            textViewContactEmail = itemView.findViewById(R.id.textViewContactEmail);
            textViewContactName = itemView.findViewById(R.id.textViewContactName);
            textViewContactSurname = itemView.findViewById(R.id.textViewContactSurname);
            imageViewDeleteContact = itemView.findViewById(R.id.imageViewDeleteContact);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onContactIntentClickListener != null) {
                        onContactIntentClickListener.onContactClick(getAdapterPosition());
                    }
                }
            });
            imageViewDeleteContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onContactClickListener != null) {
                        onContactClickListener.onContactClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
