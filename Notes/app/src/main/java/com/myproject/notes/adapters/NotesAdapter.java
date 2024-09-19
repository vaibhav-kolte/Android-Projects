package com.myproject.notes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myproject.notes.Note;
import com.myproject.notes.databinding.NotesRvBinding;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    private static final String TAG = "NotesAdapter";
    private final List<Note> noteList;

    public NotesAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NotesRvBinding binding = NotesRvBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.binding.tvTitle.setText(note.getTitle());
        holder.binding.tvContent.setText(note.getNote());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public NotesRvBinding binding;

        public ViewHolder(@NonNull NotesRvBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
