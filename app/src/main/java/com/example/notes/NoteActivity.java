package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.notes.model.Note;

public class NoteActivity extends AppCompatActivity {

    private static final String TAG = "NoteActivity";

    // UI components
    private LinedEditText mLinedEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;

    // Variables
    private boolean mIsNewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mLinedEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);



    }
}
