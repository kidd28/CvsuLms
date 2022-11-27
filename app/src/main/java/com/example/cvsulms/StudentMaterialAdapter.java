package com.example.cvsulms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentMaterialAdapter extends  RecyclerView.Adapter<StudentMaterialAdapter.HolderAdapter>{
    Context context;
    ArrayList<MaterialModel> materiallist;

    public StudentMaterialAdapter(Context context , ArrayList<MaterialModel> materiallist){
        this.context= context;
        this.materiallist=materiallist;
    }

    @NonNull
    @Override
    public StudentMaterialAdapter.HolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_list, parent, false);
        return new StudentMaterialAdapter.HolderAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentMaterialAdapter.HolderAdapter holder, int position) {
        MaterialModel model = materiallist.get(position);
        String subjCode = model.getSubjCode();
        String Title = model.getTitle();

        holder.SubjCode.setText(subjCode);
        holder.Title.setText(Title);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (context, TeacherMaterialUi.class);
                intent.putExtra("Title",model.getTitle());
                intent.putExtra("Description",model.getDescription());
                intent.putExtra("SecCode",model.getSecCode());
                intent.putExtra("SubjCode",model.getSubjCode());
                intent.putExtra("Subject",model.getSubject());
                intent.putExtra("TeacherUid",model.getTeacherUid());
                intent.putExtra("FileId",model.getFileId());
                intent.putExtra("FileName",model.getFileName());
                intent.putExtra("Filelink",model.getFilelink());
                intent.putExtra("MaterialId",model.getMaterialId());
                context.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return materiallist.size();
    }
    public class HolderAdapter extends RecyclerView.ViewHolder {
        private TextView SubjCode, Title;
        private RelativeLayout layout;

        public HolderAdapter(@NonNull View itemView) {
            super(itemView);
            layout =  itemView.findViewById(R.id.layout);
            SubjCode = itemView.findViewById(R.id.SubjectCode);
            Title = itemView.findViewById(R.id.Title);

        }
    }
}
