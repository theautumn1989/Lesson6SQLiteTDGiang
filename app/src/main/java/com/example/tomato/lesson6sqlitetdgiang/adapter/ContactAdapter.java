package com.example.tomato.lesson6sqlitetdgiang.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomato.lesson6sqlitetdgiang.R;
import com.example.tomato.lesson6sqlitetdgiang.model.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHodelr> {

    OnCallBack onCallBack;

    ArrayList<Contact> arrContact;
    Context context;

    public ContactAdapter(ArrayList<Contact> arrContact, Context context, OnCallBack onCallBack) {
        this.arrContact = arrContact;
        this.context = context;
        this.onCallBack = onCallBack;
    }

    @NonNull
    @Override
    public ViewHodelr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        if (itemView != null) {
            return new ViewHodelr(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodelr holder, int position) {

        holder.tvName.setText(arrContact.get(position).getName());
        holder.tvNumberPhone.setText(arrContact.get(position).getNumberPhone());
    }

    @Override
    public int getItemCount() {
        return arrContact.size();
    }

    public class ViewHodelr extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvNumberPhone;

        public ViewHodelr(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvNumberPhone = itemView.findViewById(R.id.tv_number_phone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCallBack.onItemClicked(getAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
        }
    }

    public interface OnCallBack {
        void onItemClicked(int position);
    }
}

