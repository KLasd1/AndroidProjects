package com.example.personallibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.personallibrary.R;
import com.example.personallibrary.data.Book;
import com.example.personallibrary.data.BookDatabaseHelper;

import java.io.File;

public class BookDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvAuthor, tvDescription, tvLiked, tvDisliked;
    private ImageView ivCover;
    private Button btnEdit, btnDelete;
    private BookDatabaseHelper dbHelper;
    private int bookId;
    private Book currentBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Детали книги");
        }

        tvTitle = findViewById(R.id.tv_detail_title);
        tvAuthor = findViewById(R.id.tv_detail_author);
        tvDescription = findViewById(R.id.tv_detail_description);
        tvLiked = findViewById(R.id.tv_detail_liked);
        tvDisliked = findViewById(R.id.tv_detail_disliked);
        ivCover = findViewById(R.id.iv_detail_cover);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);

        dbHelper = new BookDatabaseHelper(this);
        bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId == -1) {
            Toast.makeText(this, "Ошибка: книга не найдена", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadBookDetails();

        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(BookDetailActivity.this, EditBookActivity.class);
            intent.putExtra("book_id", bookId);
            startActivity(intent);
        });

        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void loadBookDetails() {
        currentBook = dbHelper.getBookById(bookId);
        if (currentBook != null) {
            tvTitle.setText(currentBook.getTitle());
            tvAuthor.setText(currentBook.getAuthor() != null && !currentBook.getAuthor().isEmpty() ? currentBook.getAuthor() : "Автор не указан");
            tvDescription.setText(currentBook.getDescription() != null && !currentBook.getDescription().isEmpty() ? currentBook.getDescription() : "Нет описания");
            tvLiked.setText(currentBook.getLiked() != null && !currentBook.getLiked().isEmpty() ? currentBook.getLiked() : "Не указано");
            tvDisliked.setText(currentBook.getDisliked() != null && !currentBook.getDisliked().isEmpty() ? currentBook.getDisliked() : "Не указано");

            String imagePath = currentBook.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    Glide.with(this).load(imageFile).placeholder(R.drawable.ic_placeholder).into(ivCover);
                } else {
                    Glide.with(this).load(R.drawable.ic_placeholder).into(ivCover);
                }
            } else {
                Glide.with(this).load(R.drawable.ic_placeholder).into(ivCover);
            }
        } else {
            Toast.makeText(this, "Книга не найдена", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Удалить книгу")
                .setMessage("Вы уверены, что хотите удалить \"" + currentBook.getTitle() + "\"?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    if (currentBook.getImagePath() != null) {
                        File file = new File(currentBook.getImagePath());
                        if (file.exists()) file.delete();
                    }
                    dbHelper.deleteBook(bookId);
                    Toast.makeText(this, "Книга удалена", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBookDetails();
    }
}