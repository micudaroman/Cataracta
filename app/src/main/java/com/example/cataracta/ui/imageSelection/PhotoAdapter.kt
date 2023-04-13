package com.example.cataracta.ui.imageSelection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.cataracta.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class PhotoAdapter(private var photoList: MutableList<Photo>):RecyclerView.Adapter<PhotoAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.photo_card,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currPhoto = photoList[position]
        val photoPath = currPhoto.source

        Picasso.get().load(photoPath).into(holder.photoCard);

        holder.photoCard.setOnClickListener{
            currPhoto.isSelected = !currPhoto.isSelected
            holder.cbSelectImage.isChecked = !currPhoto.isSelected
        }

        holder.cbSelectImage.setOnCheckedChangeListener { _, isChecked ->
            currPhoto.isSelected = isChecked
        }
    }

    fun getCheckedPhotos(): List<Photo> {
        return photoList.filter { photo -> photo.isSelected }
    }

    fun setPhotos(photos: ArrayList<Photo>){
        photoList = photos
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val photoCard : ShapeableImageView = itemView.findViewById(R.id.photoPreview)
        val cbSelectImage: CheckBox = itemView.findViewById(R.id.cbSelectImage)
    }
}