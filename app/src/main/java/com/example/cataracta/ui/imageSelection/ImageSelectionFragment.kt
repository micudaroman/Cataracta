package com.example.cataracta.ui.imageSelection

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cataracta.R

class ImageSelectionFragment : Fragment() {

    private lateinit var rvPhotos: RecyclerView
    private lateinit var photos: ArrayList<Photo>
    private lateinit var btnUploadSelectedImages: Button
    private lateinit var photoAdapter: PhotoAdapter
    lateinit var photoPaths: Array<Int>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPhotos = view.findViewById(R.id.recyclerView)
        rvPhotos.layoutManager = GridLayoutManager(activity, 2)
        rvPhotos.setHasFixedSize(true)

        photoAdapter = PhotoAdapter(mutableListOf())
        rvPhotos.adapter = photoAdapter

        btnUploadSelectedImages = view.findViewById(R.id.btnUploadSelectedImages)
        btnUploadSelectedImages.setOnClickListener{
            val selectedImages = photoAdapter.getCheckedPhotos()
            val stringBuilder = StringBuilder()
            selectedImages.map { selectedImage -> stringBuilder.append(selectedImage.source, ' ') }
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(activity, stringBuilder.toString(), duration)
            toast.show()
        }

        photos = arrayListOf<Photo>()
        getUserData()
    }

    private fun getUserData() {
        photoPaths = arrayOf(
//            TODO Add images from BE

//            TEMP Files only for preview
            R.drawable.a,
            R.drawable.a,
            R.drawable.a,
            R.drawable.a,
            R.drawable.a,
            R.drawable.a,
            R.drawable.a,
            R.drawable.a,
        )

        for(i in photoPaths.indices){
            val video = Photo(photoPaths[i])
            photos.add(video)
        }

        photoAdapter.setPhotos(photos)
    }


}