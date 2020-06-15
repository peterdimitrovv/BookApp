package com.example.booksapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class BooksListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<BookModel> list;
    BooksListAdapter adapter = null;
    ImageView imageIcon;
    final int REQUEST_CODE_GALLERY = 999;
    Button btnUpdate;
    EditText edtNameUpdate, edtGenreUpdate, edtAuthorUpdate, edtYearUpdate, edtRatingUpdate, edtDescriptionUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books_list_activity);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Books Records List");

        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new BooksListAdapter(this, R.layout.books_list_row, list);
        listView.setAdapter(adapter);

       updateBooksList();

        if (list.size() == 0) {
            Toast.makeText(this, "No Books Records Found...", Toast.LENGTH_SHORT).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                CharSequence[] items = {"Delete", "Update", "Show full info"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(BooksListActivity.this);
                dialog.setTitle("Choose an action");

                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int option) {
                        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT id FROM BOOKS");
                        ArrayList<Integer> arrayId = new ArrayList<Integer>();
                        switch (option) {
                            case 0:
                                while (cursor.moveToNext()) {
                                    arrayId.add(cursor.getInt(0));
                                }
                                showDialogDelete(arrayId.get(position));
                                break;
                            case 1:
                                while (cursor.moveToNext()) {
                                    arrayId.add(cursor.getInt(0));
                                }
                                showDialogUpdate(BooksListActivity.this, arrayId.get(position));
                                break;
                            case 2:
                                while (cursor.moveToNext()) {
                                    arrayId.add(cursor.getInt(0));
                                }
                                Intent intent = new Intent(getApplicationContext(), BookInfoActivity.class);
                                intent.putExtra("Position", position);
                                startActivity(intent);

                                break;
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    private void showDialogDelete(final Integer idBookRecord) {
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(BooksListActivity.this);
        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure you want to delete this book record?");
        dialogDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    MainActivity.sqLiteHelper.deleteData(idBookRecord);
                    Toast.makeText(BooksListActivity.this, "Delete Successfully!!!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    Log.e("Error!!!", e.getMessage());
                }
                updateBooksList();
            }
        });

        dialogDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void showDialogUpdate(final Activity activity, final int position) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_dialog);
        dialog.setTitle("Update");

        imageIcon = dialog.findViewById(R.id.imageViewUpdate);
        edtNameUpdate = dialog.findViewById(R.id.edtNameUpdate);
        edtGenreUpdate = dialog.findViewById(R.id.edtGenreUpdate);
        edtAuthorUpdate = dialog.findViewById(R.id.edtAuthorUpdate);
        edtYearUpdate = dialog.findViewById(R.id.edtYearUpdate);
        edtRatingUpdate = dialog.findViewById(R.id.edtRatingUpdate);
        edtDescriptionUpdate = dialog.findViewById(R.id.edtDescriptionUpdate);
        btnUpdate = dialog.findViewById(R.id.btnUpdate);

        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.95);
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        BooksListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MainActivity.sqLiteHelper.updateData(edtNameUpdate.getText().toString().trim(),
                            edtGenreUpdate.getText().toString().trim(),
                            edtAuthorUpdate.getText().toString().trim(),
                            Integer.parseInt(edtYearUpdate.getText().toString().trim()),
                            Double.parseDouble(edtRatingUpdate.getText().toString().trim()),
                            edtDescriptionUpdate.getText().toString().trim(),
                            imageViewToByte(imageIcon),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(activity, "Successfully Update!!!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    Log.e("Update error", e.getMessage());
                }
                updateBooksList();
            }
        });
    }

    private void updateBooksList() {
        Cursor cursor = MainActivity.sqLiteHelper.getData("SELECT * FROM BOOKS");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String genre = cursor.getString(2);
            String author = cursor.getString(3);
            int year = Integer.parseInt(cursor.getString(4));
            double rating = Double.parseDouble(cursor.getString(5));
            String description = cursor.getString(6);
            byte[] image = cursor.getBlob(7);

            list.add(new BookModel(id, name, genre, author, year, rating, description, image));
        }
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(this, "Don't have permission to access file location!!!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                imageIcon.setImageURI(resultUri);
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
