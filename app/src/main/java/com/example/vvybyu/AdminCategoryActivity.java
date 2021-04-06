package com.example.vvybyu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView animeIcon, gundamIcon, funGoodIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        animeIcon = (ImageView) findViewById(R.id.animeIcon);
        gundamIcon = (ImageView) findViewById(R.id.gundamIcon);
        funGoodIcon = (ImageView) findViewById(R.id.funGoodIcon);

        animeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "anime");
                startActivity(intent);
            }
        });

        gundamIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "gundam");
                startActivity(intent);
            }
        });

        funGoodIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "funGood");
                startActivity(intent);
            }
        });
    }
}