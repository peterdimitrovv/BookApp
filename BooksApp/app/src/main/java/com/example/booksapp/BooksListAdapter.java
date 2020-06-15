package com.example.booksapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksListAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<BookModel> booksList;

    public BooksListAdapter(Context context, int layout, ArrayList<BookModel> booksList) {
        this.context = context;
        this.layout = layout;
        this.booksList = booksList;
    }

    @Override
    public int getCount() {
        return booksList.size();
    }

    @Override
    public Object getItem(int position) {
        return booksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtName, txtGenre, txtAuthor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.txtName = row.findViewById(R.id.txtName);
            holder.txtGenre = row.findViewById(R.id.txtGenre);
            holder.txtAuthor = row.findViewById(R.id.txtAuthor);
            holder.imageView = row.findViewById(R.id.imgIcon);
            row.setTag(holder);
        }
        else  {
            holder = (ViewHolder)row.getTag();
        }

        BookModel bookModel = booksList.get(position);
        holder.txtName.setText(bookModel.getName());
        holder.txtGenre.setText(bookModel.getGenre());
        holder.txtAuthor.setText(bookModel.getAuthor());

        byte[] bookImage = bookModel.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bookImage, 0, bookImage.length);
        holder.imageView.setImageBitmap(bitmap);
        return row;
    }
}
