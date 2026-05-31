package com.example.personallibrary.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class BookDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "personal_library.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_BOOKS = "books";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_LIKED = "liked";
    private static final String COLUMN_DISLIKED = "disliked";
    private static final String COLUMN_IMAGE_PATH = "image_path";

    public BookDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_BOOKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE + " TEXT NOT NULL, "
                + COLUMN_AUTHOR + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_LIKED + " TEXT, "
                + COLUMN_DISLIKED + " TEXT, "
                + COLUMN_IMAGE_PATH + " TEXT"
                + ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        onCreate(db);
    }

    public long addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_DESCRIPTION, book.getDescription());
        values.put(COLUMN_LIKED, book.getLiked());
        values.put(COLUMN_DISLIKED, book.getDisliked());
        values.put(COLUMN_IMAGE_PATH, book.getImagePath());
        long id = db.insert(TABLE_BOOKS, null, values);
        db.close();
        return id;
    }

    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, null, null, null, null, COLUMN_TITLE + " ASC");
        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LIKED)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISLIKED)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))
                );
                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return bookList;
    }

    public Book getBookById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Book book = new Book(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LIKED)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DISLIKED)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_PATH))
            );
            cursor.close();
            db.close();
            return book;
        } else {
            if (cursor != null) cursor.close();
            db.close();
            return null;
        }
    }

    public int updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, book.getTitle());
        values.put(COLUMN_AUTHOR, book.getAuthor());
        values.put(COLUMN_DESCRIPTION, book.getDescription());
        values.put(COLUMN_LIKED, book.getLiked());
        values.put(COLUMN_DISLIKED, book.getDisliked());
        values.put(COLUMN_IMAGE_PATH, book.getImagePath());
        int rowsAffected = db.update(TABLE_BOOKS, values, COLUMN_ID + "=?", new String[]{String.valueOf(book.getId())});
        db.close();
        return rowsAffected;
    }

    public void deleteBook(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOOKS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}