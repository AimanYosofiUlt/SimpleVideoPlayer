package com.exmple.videoplayer_codewithnitish.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.exmple.videoplayer_codewithnitish.R;
import com.exmple.videoplayer_codewithnitish.VideoModel;
import com.exmple.videoplayer_codewithnitish.activity.VideoPlayerAvtivity;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.MyViewHolder> {
    public static ArrayList<VideoModel> videoFolders;
    private Context context;

    public VideoAdapter(ArrayList<VideoModel> viewModels, Context context) {
        this.videoFolders = viewModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_file, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(videoFolders.get(position).getTitle());

        Glide.with(context)
                .load(videoFolders.get(position).getPath())
                .into(holder.thumbnail);
        StringBuilder stringBuilder = new StringBuilder();
        holder.info.setText(
                stringBuilder.append(videoFolders.get(position).getDuration()).append("\n")
                        .append(videoFolders.get(position).getResolution()).append("   ")
                        .append(videoFolders.get(position).getSize()).toString()

        );

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerAvtivity.class);
                intent.putExtra("p", holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return videoFolders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title, info;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.folderName);
            info = itemView.findViewById(R.id.info);
            thumbnail = itemView.findViewById(R.id.thumbnail);

        }
    }

}
