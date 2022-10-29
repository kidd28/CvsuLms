package com.example.cvsulms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MaterialAdapter extends  RecyclerView.Adapter<MaterialAdapter.HolderAdapter>  {
    Context context;
    ArrayList<MaterialModel> materiallist;

    public MaterialAdapter(Context context , ArrayList<MaterialModel> materiallist){
        this.context= context;
        this.materiallist=materiallist;
    }

    @NonNull
    @Override
    public MaterialAdapter.HolderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.material_list, parent, false);
        return new MaterialAdapter.HolderAdapter(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialAdapter.HolderAdapter holder, int position) {
        MaterialModel model = materiallist.get(position);
        String subjCode = model.getSubjCode();
        String Title = model.getTitle();

        holder.SubjCode.setText(subjCode);
        holder.Title.setText(Title);

    }
    @Override
    public int getItemCount() {
        return materiallist.size();
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
