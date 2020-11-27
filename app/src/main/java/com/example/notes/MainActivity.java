package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewNotes;
    private FloatingActionButton btnAddNote;
    private NotesAdapter adapter;
    private NotesDBHelper dbHelper;

    public static final ArrayList<Note> notes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initElementsForMainActivity();

        dbHelper = new NotesDBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

//        notesAdd();
//
//        for (Note note : notes) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(NotesContract.NotesEntry.COLUMN_TITLE, note.getTitle());
//            contentValues.put(NotesContract.NotesEntry.COLUMN_DESCRIPTION, note.getDescription());
//            contentValues.put(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK, note.getDayOfWeek());
//            contentValues.put(NotesContract.NotesEntry.COLUMN_PRIORITY, note.getPriority());
//            database.insert(NotesContract.NotesEntry.TABLE_NAME, null, contentValues);
//        }

        ArrayList<Note> notesFromDB = new ArrayList<>();
        Cursor cursor = database.query(NotesContract.NotesEntry.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String title = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DESCRIPTION));
            String day_of_week = cursor.getString(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DAY_OF_WEEK));
            int priority = cursor.getInt(cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_PRIORITY));
            Note note = new Note(title, description, day_of_week, priority);
            notesFromDB.add(note);
        }

        cursor.close();

//        adapter = new NotesAdapter(notes);
        adapter = new NotesAdapter(notesFromDB);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this)); // Внешний вид RecyclerView вертикальный список
//        recyclerViewNotes.setLayoutManager(new GridLayoutManager(this, 3)); // Внешний вид RecyclerView сетка 3 столбца
        recyclerViewNotes.setAdapter(adapter);

        adapter.setOnNoteClickListener(new NotesAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(int position) {
                Toast.makeText(MainActivity.this, "Удерживайте, чтобы удалить заметку с номером позиции: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongNoteClick(int position) {
                removeNote(position);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeNote(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerViewNotes);
    }

    private void removeNote(int position) {
        notes.remove(position);
        adapter.notifyDataSetChanged();
    }

//    private void notesAdd() {
//        if (notes.isEmpty()) {
//            notes.add(new Note("Парикмахер", "Сделать прическу", "Понедельник", 2));
//            notes.add(new Note("Баскедбол", "Игра со школьной командой", "Вторник", 3));
//            notes.add(new Note("Магазин", "Купить новые джинсы", "Понедельник", 3));
//            notes.add(new Note("Стамотолог", "Вылечить зубы", "Понедельник", 2));
//            notes.add(new Note("Парикмахер", "Сделать прическу к выпускному", "Среда", 1));
//            notes.add(new Note("Баскедбол", "Игра со школьной командой", "Вторник", 3));
//            notes.add(new Note("Магазин", "Купить кроссовки", "Понедельник", 3));
//        }
//    }

    private void initElementsForMainActivity() {
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        btnAddNote = findViewById(R.id.btnAddNote);
    }
}