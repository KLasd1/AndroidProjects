package com.example.personallibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personallibrary.R;
import com.example.personallibrary.adapter.BookAdapter;
import com.example.personallibrary.data.Book;
import com.example.personallibrary.data.BookDatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private BookDatabaseHelper dbHelper;
    private List<Book> bookList;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view_books);
        fabAdd = findViewById(R.id.fab_add);
        dbHelper = new BookDatabaseHelper(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        bookAdapter = new BookAdapter(this, bookList, book -> {
            Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
            intent.putExtra("book_id", book.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(bookAdapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBookActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    private void loadBooks() {
        bookList = dbHelper.getAllBooks();
        if (bookList.isEmpty()) {
            Toast.makeText(this, "Библиотека пуста. Добавьте первую книгу!", Toast.LENGTH_SHORT).show();
        }
        if (bookAdapter != null) {
            bookAdapter.updateList(bookList);
        }
    }
}