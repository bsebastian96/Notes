package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.notes.model.Note;
import com.example.notes.persistence.NoteRepository;
import com.example.notes.util.Utility;

public class NoteActivity extends AppCompatActivity implements View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher {

    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    public static final int EDIT_MODE_DISABLED = 0;

    // UI components
    // Preceding all global variables with char 'g' to indicate global
    private LinedEditText gLinedEditText;
    private EditText gEditTitle;
    private TextView gViewTitle;
    private RelativeLayout gCheckContainer, gBackArrowContainer;
    private ImageButton gCheck, gBackArrow;

    // Variables
    private boolean gIsNewNote;
    private Note gInitialNote;
    private GestureDetector gGestureDetector;
    // Keeps track of the state
    private int gMode;
    private NoteRepository gNoteRepository;
    private Note gFinalNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        gLinedEditText = findViewById(R.id.note_text);
        gEditTitle = findViewById(R.id.note_edit_title);
        gViewTitle = findViewById(R.id.note_text_title);
        gCheckContainer = findViewById(R.id.check_container);
        gBackArrowContainer = findViewById(R.id.back_arrow_container);
        gCheck = findViewById(R.id.toolbar_check);
        gBackArrow = findViewById(R.id.toolbar_back_arrow);

        gNoteRepository = new NoteRepository(this);

        // Checks if incoming intent is for a new note or an already existing note
        if (getIncomingIntent()) {
            // this is a new note, (Edit mode)
            setNewNoteProperties();
            enableEditMode();
        } else {
            // this is NOT a new note (View mode)
            setNoteProperties();
            disableContentInteraction();
        }

        //
        setListeners();
    }

    /*
        If the incoming intent is sending a bundle with a Note, then that means it is an existing
        note so we go to View mode.  If the incoming intent is not sending a Note, that means
        the user clicks on creating a new Note, so we go right to Edit mode for the user to
        create a new Note.
     */
    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_note")) {
            gInitialNote = getIntent().getParcelableExtra("selected_note");

            gFinalNote = new Note();
            gFinalNote.setTitle(gInitialNote.getTitle());
            gFinalNote.setContent(gInitialNote.getContent());
            gFinalNote.setTimestamp(gInitialNote.getTimestamp());
            gFinalNote.setId(gInitialNote.getId());

            gMode = EDIT_MODE_DISABLED;
            gIsNewNote = false;
            return false;
        }
        gMode = EDIT_MODE_ENABLED;
        gIsNewNote = true;
        return true;
    }

    // Sets properties for a new Note, not implemented yet
    private void setNewNoteProperties() {
        gViewTitle.setText("Note Title");
        gEditTitle.setText("Note Title");

        gInitialNote = new Note();
        gFinalNote = new Note();
        gInitialNote.setTitle("Note Title");
        gFinalNote.setTitle("Note Title");
    }

    // Sets properties for viewing note that already existed
    private void setNoteProperties() {
        gViewTitle.setText(gInitialNote.getTitle());
        gEditTitle.setText(gInitialNote.getTitle());
        gLinedEditText.setText(gInitialNote.getContent());
    }

    private void enableEditMode() {
        gBackArrowContainer.setVisibility(View.GONE);
        gCheckContainer.setVisibility(View.VISIBLE);

        gViewTitle.setVisibility(View.GONE);
        gEditTitle.setVisibility(View.VISIBLE);

        gMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    private void disableEditMode() {
        gBackArrowContainer.setVisibility(View.VISIBLE);
        gCheckContainer.setVisibility(View.GONE);

        gViewTitle.setVisibility(View.VISIBLE);
        gEditTitle.setVisibility(View.GONE);

        gMode = EDIT_MODE_DISABLED;

        disableContentInteraction();

        // Saving note if the title or note content has changed
        String temp = gLinedEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if (temp.length() > 0) {
            gFinalNote.setTitle(gEditTitle.getText().toString());
            gFinalNote.setContent(gLinedEditText.getText().toString());
            String timestamp = Utility.getCurrentTimestamp();
            gFinalNote.setTimestamp(timestamp);

            if (!gFinalNote.getContent().equals(gInitialNote.getContent()) ||
                    !gFinalNote.getTitle().equals(gInitialNote.getTitle())) {
                saveChanges();
            }
        }
    }

    /*
        Unless LinedEditText is in edit mode, we want to disable interactions with it to prevent
        single clicks opening edit mode for the content
     */
    private void disableContentInteraction() {
        gLinedEditText.setKeyListener(null);
        gLinedEditText.setFocusable(false);
        gLinedEditText.setFocusableInTouchMode(false);
        gLinedEditText.setCursorVisible(false);
        gLinedEditText.clearFocus();
    }

    /*
        If LinedEditText is in edit mode, we want interactions with it available.  We also have to
        reassign the key listener to the default since the disable method sets it to null
     */
    private void enableContentInteraction() {
        gLinedEditText.setKeyListener(new EditText(this).getKeyListener());
        gLinedEditText.setFocusable(true);
        gLinedEditText.setFocusableInTouchMode(true);
        gLinedEditText.setCursorVisible(true);
        gLinedEditText.requestFocus();
    }

    // Fixing keyboard not disappearing when leaving edit mode, android keyboard = soft keyboard
    private void hideSoftKeyboard() {
        InputMethodManager i = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }

        i.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*
        Initializes the touch listener and gesture detector. Once a touch is detected, onTouch() is
        called, which passes the event to the gesture detector and runs a following method
        corresponding to the type of touch that occurred
     */
    private void setListeners() {
        gLinedEditText.setOnTouchListener(this);
        gGestureDetector = new GestureDetector(this, this);
        gViewTitle.setOnClickListener(this);
        gCheck.setOnClickListener(this);
        gBackArrow.setOnClickListener(this);
        gEditTitle.addTextChangedListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        enableEditMode();
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {

            case R.id.toolbar_check: {
                hideSoftKeyboard();
                disableEditMode();
                break;
            }

            case R.id.note_text_title: {
                enableEditMode();
                // When clicking the title, we want the cursor focus to be at the end of the string
                gEditTitle.requestFocus();
                gEditTitle.setSelection(gEditTitle.length());
                break;
            }

            case R.id.toolbar_back_arrow: {
                // Destroys activity, can only be called in an activity, not a fragment
                finish();
                break;
            }
        }
    }

    /*
        When we hit back when we are in Edit mode, we want to go back to view mode instead of
        going back to the note list activity, so we have to intercept the back click by
        overriding the following method and registering a click to the check mark which
        goes back to view mode.
     */
    @Override
    public void onBackPressed() {
        if (gMode == EDIT_MODE_ENABLED) {
            onClick(gCheck);
        } else {
            super.onBackPressed();
        }
    }

    /*
        Called when activity is paused (onPause in lifecycle).
        This is so we can save properties when activity is going to be destroyed.
        Changing screen configuration causes the activity to be killed, then recreated so to
        prevent changes in mode, we need to save the mode property.
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("mode", gMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        gMode = savedInstanceState.getInt("mode");
        if (gMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }

    // Saving note by checking if it is a new note and calling saveNewNote
    private void saveChanges() {
        if (gIsNewNote) {
            saveNewNote();
        } else {
            updateNote();
        }
    }

    private void updateNote() {
        gNoteRepository.updateNote(gFinalNote);
    }

    private void saveNewNote() {
        gNoteRepository.insertNoteTask(gFinalNote);
    }

    /*
        Methods for extending TextWatcher to save the title name in the view mode after changing
        the title in the edit mode and clicking the check mark
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        gViewTitle.setText(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
