package com.example.cvsulms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TeacherSectionAdapter extends RecyclerView.Adapter<TeacherSectionAdapter.HolderTSAdapter> {
     Context context;
     ArrayList<TeacherSectionModel> seclist;

    public TeacherSectionAdapter(Context context , ArrayList<TeacherSectionModel> seclist){
        this.context= context;
        this.seclist=seclist;
    }
    @NonNull
    @Override
    public TeacherSectionAdapter.HolderTSAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_list, parent, false);
        return new HolderTSAdapter(v);
    }
    @Override
    public void onBindViewHolder(@NonNull TeacherSectionAdapter.HolderTSAdapter holder, int position) {
        TeacherSectionModel model = seclist.get(position);
        String secCode = model.getSecCode();
        String subjCode = model.getSubjCode();
        String subj = model.getSubject();
        String teacherUid = model.getTeacherUid();

        holder.section.setText(secCode);
        holder.subject.setText(subj);

        holder.section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, TeacherSectionUi.class);
                intent.putExtra("secCode",secCode );
                intent.putExtra("subjCode",subjCode );
                intent.putExtra("subj",subj );
                intent.putExtra("teacherUid",teacherUid );
                context.startActivity(intent);
            }
        });
        holder.subject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, TeacherSectionUi.class);
                intent.putExtra("secCode",secCode );
                intent.putExtra("subjCode",subjCode );
                intent.putExtra("subj",subj );
                intent.putExtra("teacherUid",teacherUid );
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return seclist.size();
    }
   class HolderTSAdapter extends RecyclerView.ViewHolder {
        private TextView section, subject;
        public HolderTSAdapter(@NonNull View itemView) {
            super(itemView);

            section =itemView.findViewById(R.id.Section);
            subject = itemView.findViewById(R.id.Subject);

        }
    }
}
