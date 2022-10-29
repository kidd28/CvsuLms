package com.example.cvsulms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.HolderAdapter>  {
    Context context;
    ArrayList<TaskModel> tasklist;

    public TaskAdapter(Context context , ArrayList<TaskModel> tasklist){
        this.context= context;
        this.tasklist=tasklist;
    }

    @NonNull
    @Override
    public TaskAdapter.HolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_list, parent, false);
        return new HolderAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.HolderAdapter holder, int position) {
        TaskModel model = tasklist.get(position);
        String subjCode = model.getSubjCode();
        String Title = model.getTitle();

        holder.SubjCode.setText(subjCode);
        holder.Title.setText(Title);

    }
    @Override
    public int getItemCount() {
        return tasklist.size();
    }
    public class HolderAdapter extends RecyclerView.ViewHolder {
       private TextView SubjCode, Title;
        public HolderAdapter(@NonNull View itemView) {
            super(itemView);
            SubjCode = itemView.findViewById(R.id.SubjectCode);
            Title = itemView.findViewById(R.id.Title);

        }
    }
}
