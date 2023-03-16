package com.example.cataracta.ui.imageSelection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cataracta.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class PhotoAdapter(private val videosList: ArrayList<Photo>):RecyclerView.Adapter<PhotoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.photo_card,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val videoPath = videosList[position].source;
        Picasso.get().load(videoPath).into(holder.videoCard);
    }

    override fun getItemCount(): Int {
        return videosList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val videoCard : ShapeableImageView = itemView.findViewById(R.id.photoPreview)
    }
}