package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class AddNoteActivity extends AppCompatActivity {

    private EditText addNoteTitle, addNoteDescription;
    private Spinner spinnerDaysOfWeek;
    private RadioGroup radioGroupPriority;
    private Button buttonSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initElementsForAddNoteActivity();
        buttonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = addNoteTitle.getText().toString().trim();
                String description = addNoteDescription.getText().toString().trim();
                String dayOfWeek = spinnerDaysOfWeek.getSelectedItem().toString();
                int radioButtonId = radioGroupPriority.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(radioButtonId);
                int priority = Integer.parseInt(radioButton.getText().toString());
                Note note = new Note(title, description, dayOfWeek, priority);
                MainActivity.notes.add(note);
                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initElementsForAddNoteActivity() {
        addNoteTitle = findViewById(R.id.editTextTitle);
        addNoteDescription = findViewById(R.id.editTextMultiLineDescription);
        spinnerDaysOfWeek = findViewById(R.id.spinnerDaysOfWeek);
        radioGroupPriority = findViewById(R.id.radioGroupPriority);
        buttonSaveNote = findViewById(R.id.buttonSaveNote);
    }
}