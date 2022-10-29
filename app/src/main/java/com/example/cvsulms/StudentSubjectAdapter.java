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

public class StudentSubjectAdapter extends RecyclerView.Adapter<StudentSubjectAdapter.HolderAdapter>  {
    Context context;
    ArrayList<StudentSubjectModel> sublist;

    public StudentSubjectAdapter(Context context , ArrayList<StudentSubjectModel> sublist){
        this.context= context;
        this.sublist=sublist;
    }
    @NonNull
    @Override
    public StudentSubjectAdapter.HolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list, parent, false);
        return new StudentSubjectAdapter.HolderAdapter(v);
    }
    @Override
    public void onBindViewHolder(@NonNull StudentSubjectAdapter.HolderAdapter holder, int position) {
        StudentSubjectModel model = sublist.get(position);
        String secCode = model.getSecCode();
        String subjCode = model.getSubjCode();
        String subj = model.getSubject();
        String teacherUid = model.getTeacherUid();

        holder.subjcode.setText(subjCode);
        holder.subject.setText(subj);

        holder.subjcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, StudentSubjectUi.class);
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
                Intent intent = new Intent (context, StudentSubjectUi.class);
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
        return sublist.size();
    }
    public class HolderAdapter extends RecyclerView.ViewHolder {
        private TextView subjcode, subject;
        public HolderAdapter(@NonNull View itemView) {
            super(itemView);

            subjcode =itemView.findViewById(R.id.SubCode);
            subject = itemView.findViewById(R.id.Subject);
        }
    }
}
