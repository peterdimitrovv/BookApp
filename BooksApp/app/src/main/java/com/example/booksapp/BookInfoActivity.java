package com.example.booksapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BookInfoActivity extends AppCompatActivity {

    int position = 0;
    ArrayList<BookModel> list;
    ImageView imgInfo;
    TextView nameInfo, authorInfo, genreInfo, yearInfo, ratingInfo, descriptionInfo;
    Button btnShare;
    String name, author, genre, year, rating, description;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_info_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Book Info");

        Intent intent = getIntent();
        position = intent.getExtras().getInt("Position");
        list = new ArrayList<>();

        init();

        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM BOOKS");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String genre = cursor.getString(2);
            String author = cursor.getString(3);
            int year = cursor.getInt(4);
            double rating = cursor.getDouble(5);
            String description = cursor.getString(6);
            byte[] image = cursor.getBlob(7);

            list.add(new BookModel(id, name, genre, author, year, rating, description, image));
            if (list.size() == 0) {
                Toast.makeText(this, "No Books Records Found...", Toast.LENGTH_SHORT).show();
            }
        }

        BookModel bookModel = list.get(position);
        name = bookModel.getName();
        author = bookModel.getAuthor();
        genre = bookModel.getGenre();
        description = bookModel.getDescription();
        rating = String.valueOf(bookModel.getRating());
        year = String.valueOf(bookModel.getYear());
        byte[] bookImage = bookModel.getImage();
        final Bitmap bitmap = BitmapFactory.decodeByteArray(bookImage, 0, bookImage.length);
        imgInfo.setImageBitmap(bitmap);
        nameInfo.setText(name);
        authorInfo.setText(author);
        genreInfo.setText(genre);
        yearInfo.setText(year);
        ratingInfo.setText(rating);
        descriptionInfo.setText(description);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Book name:" + name + "\n" + "Author:" + author + "\n" +
                        "Genre:" + genre + "\n" + "Year:" + year + "\n" + "Rating:" +  rating + "\n" +
                        "Description:" + description;
                String shareSub = "Your subject here";
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share using"));
            }
        });
    }

    private void init() {
        imgInfo = findViewById(R.id.imgInfo);
        nameInfo = findViewById(R.id.nameInfo);
        authorInfo = findViewById(R.id.authorInfo);
        genreInfo = findViewById(R.id.genreInfo);
        yearInfo = findViewById(R.id.yearInfo);
        ratingInfo = findViewById(R.id.ratingInfo);
        descriptionInfo = findViewById(R.id.descriptionInfo);
        btnShare = findViewById(R.id.btnShare);
    }
}
