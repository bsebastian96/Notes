package com.example.notes.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.R;
import com.example.notes.model.Note;
import com.example.notes.util.Utility;

import java.util.ArrayList;

/*
  Whenever you use a RecyclerView, you need an adapter class to adapt the list of objects
  to the RecyclerView.  Extends the RecyclerView adapter class with the ViewHolder type that
  we created below.  When extending the adapter class, we need to implement required methods
  from it.
 */
public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {

    private static final String TAG = "NotesRecyclerAdapter";
    // Using ArrayList to dynamically add list items without a fixed size
    private ArrayList<Note> mNotes = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public NotesRecyclerAdapter(ArrayList<Note> notes, OnNoteListener onNoteListener) {
        this.mNotes = notes;
        this.mOnNoteListener = onNoteListener;
    }

    // Creates ViewHolder object
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_note_list_item,
                parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    // Called for every single entry in the list, sets the attributes
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            String month = mNotes.get(position).getTimestamp().substring(0,2);
            month = Utility.getMonthFromNumber(month);
            String year = mNotes.get(position).getTimestamp().substring(3);
            String timestamp = month + " " + year;
            holder.timestamp.setText(timestamp);
            holder.title.setText(mNotes.get(position).getTitle());
        } catch (NullPointerException e) {
            Log.d(TAG, "onBindViewHolder: NullPointerException " + e.getMessage());
        }
    }

    // Number of entries in the list
    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    /*
      When using a RecyclerView, we need a view holder class to hold the view of each
      individual list item so each item has a view holder.  We attach an OnClickLister
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title, timestamp;
        OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            title = itemView.findViewById(R.id.note_title);
            timestamp = itemView.findViewById(R.id.note_timestamp);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    /*
       Interface to detect and interpret a click on an item
     */
    public interface OnNoteListener {
        void onNoteClick(int position);
    }
}
