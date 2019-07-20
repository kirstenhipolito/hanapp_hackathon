package com.hanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import java.io.File;

public class InputFormActivity extends AppCompatActivity {

    private TextView barcodeValue;
    private EditText itemName;
    private EditText manufacturer;
    private EditText itemPrice;
    private EditText locationName;
    private Button submitInfo;
    private ImageView itemPicture;
    Barcode barcode = null;
    Dialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_form);

        barcodeValue = (TextView) findViewById(R.id.barcode_edit);
        itemName = (EditText) findViewById(R.id.item_name);
        manufacturer = (EditText) findViewById(R.id.manufacturer_name);
        itemPrice = (EditText) findViewById(R.id.price_item);
        locationName = (EditText) findViewById(R.id.location_edit);
        submitInfo = (Button) findViewById(R.id.submit_detail);
        itemPicture = (ImageView) findViewById(R.id.item_pic);

        Intent data = getIntent();
        if (data != null) {
            barcode = data.getParcelableExtra(CameraActivity.BarcodeObject);
            barcodeValue.setText(barcode.displayValue);
        }

        String path = "/sdcard/CSV_Files/" ;
        String fileName = "items.csv";
        CsvFileInOut csvFile = new CsvFileInOut(path,fileName);
        String[] string_value = csvFile.read(barcode.displayValue);

        if (string_value != null) {
            itemName.setText(string_value[1]);
            manufacturer.setText(string_value[2]);
            itemPrice.setHint(string_value[3]);
            itemPrice.requestFocus();

            File imageFile = new File(string_value[4]);
            if (imageFile.exists()) {
                itemPicture.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            }
        }

        promptDialog = new Dialog(this);
        submitInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPrompt(false);
            }
        });
    }

    @Override
    public void onBackPressed(){
        showPrompt(true);
    }

    public void showPrompt(Boolean prompt_exit) {
        Button cancel;
        Button submit;
        TextView prompt;

        promptDialog.setContentView(R.layout.prompt);
        prompt = (TextView) promptDialog.findViewById(R.id.prompt_text);
        cancel = (Button) promptDialog.findViewById(R.id.submit_no);
        submit = (Button) promptDialog.findViewById(R.id.submit_yes);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                promptDialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                promptDialog.dismiss();
                Intent input_to_home_intent = new Intent(InputFormActivity.this,HomeActivity.class);
                startActivity(input_to_home_intent);
                finish();
            }
        });

        if(prompt_exit) prompt.setText("Are you sure you want to exit?");
        promptDialog.show();
    }
}

