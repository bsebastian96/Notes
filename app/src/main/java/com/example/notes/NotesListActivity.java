package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.notes.adapters.NotesRecyclerAdapter;
import com.example.notes.model.Note;
import com.example.notes.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;

// AppCompat refers to compatibility with more versions of Android
public class NotesListActivity extends AppCompatActivity implements NotesRecyclerAdapter.OnNoteListener {

    private static final String TAG = "NotesListActivity";

    // UI Components
    private RecyclerView mRecyclerView;

    // Variables
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNoteRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        // Activity classes have an implicit view reference, so we can implicitly call findViewByID
        mRecyclerView = findViewById(R.id.recyclerView);

        // Initialize empty RecyclerView
        initRecyclerView();
        insertFakeNotes();

        setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));
        setTitle("Notes");
    }

    private void insertFakeNotes() {
        for (int i = 0; i < 1000; i++) {
            Note note = new Note();
            note.setTitle("title#" + i);
            note.setTimestamp("Jan 2019");
            note.setContent("content#" + i);
            mNotes.add(note);
        }
        // Important call that notifies system that data set has changed and list can be updated
        mNoteRecyclerAdapter.notifyDataSetChanged();
    }

    /*
       Initializing the recyclerView component with a layout manager and adapter.
       Adding an item decorator to the recyclerView to add spacing
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);

        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNoteRecyclerAdapter = new NotesRecyclerAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
        mRecyclerView.addItemDecoration(itemDecorator);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: clicked " + position);

        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
    }
}
