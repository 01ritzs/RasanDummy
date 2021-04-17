package com.du.de.rasandummy.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.du.de.rasandummy.R;
import com.du.de.rasandummy.RoomDatabase.Product;
import com.du.de.rasandummy.RoomDatabase.RasanDatabase;

import java.util.List;

public class RoomActivity extends AppCompatActivity {

    EditText etProductName, etProductQuantity, etProductRate, etPdtName, etPdtId, etId;
    RasanDatabase rasanDatabase;
    Button btnRead, btnInsert, btnUpdate, btnDelete;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        etId = findViewById(R.id.etId);
        etPdtName = findViewById(R.id.etPdtName);
        etProductName = findViewById(R.id.etProductName);
        etProductRate = findViewById(R.id.etProductRate);
        etProductQuantity = findViewById(R.id.etProductQuantity);
        btnInsert = findViewById(R.id.btnInsert);
        btnRead = findViewById(R.id.btnRead);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toInsertDatabase();
            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDB();
                getProductDB();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDB();
                toUpdate();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDB();
                toDeleteDatabase();
            }
        });
    }

    private void toDeleteDatabase() {
        rasanDatabase.dao().productDeletion(Integer.parseInt(etId.getText().toString()));
        Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show();
    }

    private void toUpdate() {
        rasanDatabase.dao().updateProduct(etPdtName.getText().toString(), Integer.parseInt(etPdtId.getText().toString()));
        Toast.makeText(this, "Data updated", Toast.LENGTH_SHORT).show();
    }

    private void toInsertDatabase() {

        int quantity = Integer.parseInt(etProductQuantity.getText().toString());
        int rate = Integer.parseInt(etProductRate.getText().toString());
        Product product = new Product(etProductName.getText().toString(), quantity, rate);
        setUpDB();

        rasanDatabase.dao().productInsertion(product);
        Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT).show();
    }

    private void setUpDB() {
        rasanDatabase = Room.databaseBuilder(
                RoomActivity.this, RasanDatabase.class, "RasanDatabase")
                .allowMainThreadQueries().build();
    }

    private void getProductDB() {
        List<Product> productData = rasanDatabase.dao().getProduct();

        for (int i = 0; i < productData.size(); i++) {
            Log.d("Product Data", String.valueOf(productData.get(i).getId() + ": " + productData.get(i).getName()
                    + ": " + productData.get(i).getRate() + ": " + productData.get(i).getQuantity()));
        }
    }
}
