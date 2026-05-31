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

public class EditBookActivity extends AppCompatActivity {

    private EditText etTitle, etAuthor, etDescription, etLiked, etDisliked;
    private ImageView ivCover;
    private Button btnUpdate, btnSelectImage;
    private BookDatabaseHelper dbHelper;
    private Book book;
    private int bookId;
    private String currentImagePath;
    private String newImagePath = null;

    private final ActivityResultLauncher<String> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    String savedPath = ImageUtils.saveImageToInternalStorage(this, uri);
                    if (savedPath != null) {
                        newImagePath = savedPath;
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
            getSupportActionBar().setTitle("Редактировать книгу");
        }

        etTitle = findViewById(R.id.et_title);
        etAuthor = findViewById(R.id.et_author);
        etDescription = findViewById(R.id.et_description);
        etLiked = findViewById(R.id.et_liked);
        etDisliked = findViewById(R.id.et_disliked);
        ivCover = findViewById(R.id.iv_cover);
        btnUpdate = findViewById(R.id.btn_save);
        btnSelectImage = findViewById(R.id.btn_select_image);

        btnUpdate.setText("Обновить");

        dbHelper = new BookDatabaseHelper(this);
        bookId = getIntent().getIntExtra("book_id", -1);
        if (bookId == -1) {
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        book = dbHelper.getBookById(bookId);
        if (book == null) {
            Toast.makeText(this, "Книга не найдена", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        fillFields();

        btnSelectImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));
        btnUpdate.setOnClickListener(v -> updateBook());
    }

    private void fillFields() {
        etTitle.setText(book.getTitle());
        etAuthor.setText(book.getAuthor());
        etDescription.setText(book.getDescription());
        etLiked.setText(book.getLiked());
        etDisliked.setText(book.getDisliked());
        currentImagePath = book.getImagePath();

        if (currentImagePath != null && !currentImagePath.isEmpty()) {
            File imageFile = new File(currentImagePath);
            if (imageFile.exists()) {
                Glide.with(this).load(imageFile).placeholder(R.drawable.ic_placeholder).into(ivCover);
            } else {
                Glide.with(this).load(R.drawable.ic_placeholder).into(ivCover);
            }
        } else {
            Glide.with(this).load(R.drawable.ic_placeholder).into(ivCover);
        }
    }

    private void updateBook() {
        String title = etTitle.getText().toString().trim();
        if (title.isEmpty()) {
            etTitle.setError("Введите название");
            return;
        }

        if (newImagePath != null && currentImagePath != null && !currentImagePath.equals(newImagePath)) {
            File oldFile = new File(currentImagePath);
            if (oldFile.exists()) oldFile.delete();
        }
        String finalImagePath = (newImagePath != null) ? newImagePath : currentImagePath;

        book.setTitle(title);
        book.setAuthor(etAuthor.getText().toString().trim());
        book.setDescription(etDescription.getText().toString().trim());
        book.setLiked(etLiked.getText().toString().trim());
        book.setDisliked(etDisliked.getText().toString().trim());
        book.setImagePath(finalImagePath);

        int rows = dbHelper.updateBook(book);
        if (rows > 0) {
            Toast.makeText(this, "Книга обновлена", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Ошибка обновления", Toast.LENGTH_SHORT).show();
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