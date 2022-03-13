package com.exmple.videoplayer_codewithnitish.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.exmple.videoplayer_codewithnitish.R;
import com.exmple.videoplayer_codewithnitish.VideoModel;
import com.exmple.videoplayer_codewithnitish.activity.VideoFolderActivity;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.MyViewHolder> {
    private ArrayList<String> folderNames;
    private ArrayList<VideoModel> viewModels;
    private Context context;

    public FolderAdapter(ArrayList<String> folderNames, ArrayList<VideoModel> viewModels, Context context) {
        this.folderNames = folderNames;
        this.viewModels = viewModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_folder, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int index = folderNames.get(position).lastIndexOf("/");
        String folderName = folderNames.get(position).substring(index + 1);
        holder.name.setText(folderName);

        holder.countVidoes.setText(String.valueOf(getCountVidoe(folderNames.get(position))));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoFolderActivity.class);
                intent.putExtra("folder_name", folderName);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderNames.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, countVidoes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.folderName);
            countVidoes = itemView.findViewById(R.id.countVideo);
        }
    }

    int getCountVidoe(String folders) {
        int count = 0;

        for (VideoModel model : viewModels) {
            if (model.getPath().substring(0, model.getPath().lastIndexOf("/"))
                    .endsWith(folders)) {
                count++;
            }
        }
        return count;
    }
}
