package com.example.personallibrary.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.personallibrary.R;
import com.example.personallibrary.data.Book;
import com.example.personallibrary.data.BookDatabaseHelper;
import com.example.personallibrary.utils.ImageUtils;

import java.io.File;

public class AddBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etDescription, etLiked, etDisliked;
    private ImageView ivCover;
    private Button btnSave, btnSelectImage;
    private BookDatabaseHelper dbHelper;
    private String selectedImagePath = null;

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    String savedPath = ImageUtils.saveImageToInternalStorage(this, uri);
                    if (savedPath != null) {
                        selectedImagePath = savedPath;
                        Glide.with(this).load(new File(savedPath)).placeholder(R.drawable.ic_placeholder).into(ivCover);
                    } else {
                        Toast.makeText(this, "Не удалось сохранить изображение", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_form);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Добавить книгу");
        }

        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etDescription = findViewById(R.id.et_description);
        etLiked = findViewById(R.id.et_liked);
        etDisliked = findViewById(R.id.et_disliked);
        ivCover = findViewById(R.id.iv_cover);
        btnSave = findViewById(R.id.btn_save);
        btnSelectImage = findViewById(R.id.btn_select_image);

        dbHelper = new BookDatabaseHelper(this);

        btnSelectImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        btnSave.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            etTitle.setError("Введите название");
            return;
        }
        String author = etAuthor.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String liked = etLiked.getText().toString().trim();
        String disliked = etDisliked.getText().toString().trim();

        Book book = new Book(0, title, author, description, liked, disliked, selectedImagePath);
        long id = dbHelper.addBook(book);
        if (id != -1) {
            Toast.makeText(this, "Книга добавлена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка при сохранении", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}