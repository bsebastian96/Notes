package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.notes.adapters.NotesRecyclerAdapter;
import com.example.notes.model.Note;
import com.example.notes.persistence.NoteRepository;
import com.example.notes.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

// AppCompat refers to compatibility with more versions of Android
public class NotesListActivity extends AppCompatActivity implements
        NotesRecyclerAdapter.OnNoteListener,
        View.OnClickListener {

    private static final String TAG = "NotesListActivity";

    // UI Components
    private RecyclerView gRecyclerView;

    // Variables
    private ArrayList<Note> gNotes = new ArrayList<>();
    private NotesRecyclerAdapter gNoteRecyclerAdapter;
    private NoteRepository gNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        // Activity classes have an implicit view reference, so we can implicitly call findViewByID
        gRecyclerView = findViewById(R.id.recyclerView);

        gNoteRepository = new NoteRepository(this);

        // Initialize empty RecyclerView
        initRecyclerView();
        retrieveNotes();
        // insertFakeNotes();

        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        toolbar.setTitle("Notes");
        toolbar.setTitleTextAppearance(this, R.style.CustomText);
        setSupportActionBar(toolbar);
        //setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));
        //setTitle("Notes");

        // Attaching onClick listener interface to floating action button
        findViewById(R.id.fab).setOnClickListener(this);
    }

    /*
        Every time there is a change to the DB, this method is called and the list of notes is
        re-queried into mNotes.   Any DB calls using LiveData is asynchronous, or working in a
        background thread, which is good because you can't do DB transactions on the main thread
        in Room.
     */
    private void retrieveNotes() {
        gNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                if (gNotes.size() > 0) {
                    gNotes.clear();
                }
                if (notes != null) {
                    gNotes.addAll(notes);
                }
                gNoteRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertFakeNotes() {
        for (int i = 0; i < 1000; i++) {
            Note note = new Note();
            note.setTitle("title#" + i);
            note.setTimestamp("Jan 2019");
            note.setContent("content#" + i);
            gNotes.add(note);
        }
        // Important call that notifies system that data set has changed and list can be updated
        gNoteRecyclerAdapter.notifyDataSetChanged();
    }

    /*
       Initializing the recyclerView component with a layout manager and adapter.
       Adding an item decorator to the recyclerView to add spacing
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);

        gRecyclerView.setLayoutManager(linearLayoutManager);
        gNoteRecyclerAdapter = new NotesRecyclerAdapter(gNotes, this);
        gRecyclerView.setAdapter(gNoteRecyclerAdapter);
        gRecyclerView.addItemDecoration(itemDecorator);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(gRecyclerView);
    }

    @Override
    public void onNoteClick(int position) {
        Log.d(TAG, "onNoteClick: clicked " + position);

        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", gNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    // Implementing a way to delete an item entry by swiping it to the right or left
    private void deleteNote(Note note) {
        gNotes.remove(note);
        gNoteRecyclerAdapter.notifyDataSetChanged();

        gNoteRepository.deleteNote(note);
    }

    // Designed to work with RecyclerViews.  SimpleCallback is a simpler version of Callback
    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            deleteNote(gNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
