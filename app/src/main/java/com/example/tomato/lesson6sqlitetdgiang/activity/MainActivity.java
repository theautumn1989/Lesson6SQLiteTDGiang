package com.example.tomato.lesson6sqlitetdgiang.activity;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.tomato.lesson6sqlitetdgiang.R;
import com.example.tomato.lesson6sqlitetdgiang.adapter.ContactAdapter;
import com.example.tomato.lesson6sqlitetdgiang.database.DBManager;
import com.example.tomato.lesson6sqlitetdgiang.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        ContactAdapter.OnCallBack, View.OnClickListener {

    RecyclerView recyclerView;
    SwitchCompat switchStatus;
    LinearLayoutManager layoutManager;
    ArrayList<Contact> arrayList = null;
    ImageButton ibtnAdd;
    ContactAdapter adapter;

    public static DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DBManager(this);

        initView();
        initEvent();
        initData();
        updateData();

    }


    private void initData() {
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        optimizationInterface();
        arrayList = new ArrayList<>();

        adapter = new ContactAdapter(arrayList, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
    }

    private void initEvent() {
        switchStatus.setOnCheckedChangeListener(this);
        ibtnAdd.setOnClickListener(this);
    }

    private void initView() {
        switchStatus = findViewById(R.id.switch_status);
        recyclerView = findViewById(R.id.recyclerview);
        ibtnAdd = findViewById(R.id.imgbtn_add);
    }

    private void changeInterface() {
        if (switchStatus.isChecked()) {
            layoutManager = new GridLayoutManager(this, 2);
            optimizationInterface();
            recyclerView.setLayoutManager(layoutManager);
        } else {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            optimizationInterface();
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    private void optimizationInterface() {
        recyclerView.setHasFixedSize(true);
        // tối ưu hóa tự động
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void addContact() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add);
        dialog.setCanceledOnTouchOutside(false);

        final EditText edtName = dialog.findViewById(R.id.edt_name);
        final EditText edtPhoneNumber = dialog.findViewById(R.id.edt_phone_number);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnClose = dialog.findViewById(R.id.btn_close);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtName.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtPhoneNumber.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, R.string.notification, Toast.LENGTH_SHORT).show();
                } else {
                    Contact contact = new Contact(edtName.getText().toString().trim(), edtPhoneNumber.getText().toString().trim());
                    dbManager.insertData(contact);
                    updateData();
                    Toast.makeText(MainActivity.this, "insert successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn_add:
                addContact();
                break;
            default:
                break;
        }
    }

    private void editContact(final int position) {
        final Contact contact = arrayList.get(position);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update);
        //  dialog.setCanceledOnTouchOutside(false);

        final EditText edtName = dialog.findViewById(R.id.edt_name);
        final EditText edtPhoneNumber = dialog.findViewById(R.id.edt_phone_number);
        Button btnSave = dialog.findViewById(R.id.btn_save);
        Button btnDelete = dialog.findViewById(R.id.btn_delete);

        edtName.setText(contact.getName());
        edtPhoneNumber.setText(contact.getNumberPhone());

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbManager.deleteData(contact);
                updateData();
                // adapter.notifyItemRemoved(position);

                Toast.makeText(MainActivity.this, "delete successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtName.getText().toString().trim()) ||
                        TextUtils.isEmpty(edtPhoneNumber.getText().toString().trim())) {
                    Toast.makeText(MainActivity.this, R.string.notification, Toast.LENGTH_SHORT).show();
                } else {
                    contact.setName(edtName.getText().toString().trim());
                    contact.setNumberPhone(edtPhoneNumber.getText().toString().trim());
                    dbManager.updateData(contact);
                    updateData();
                    Toast.makeText(MainActivity.this, "update successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {
            case R.id.switch_status:
                changeInterface();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClicked(int position) {
        editContact(position);
    }

    public void updateData() {
        arrayList.clear();
        arrayList = (ArrayList<Contact>) dbManager.getAllData();
        adapter = new ContactAdapter(arrayList, getApplicationContext(), this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

