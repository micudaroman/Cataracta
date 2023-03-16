package com.example.cataracta.ui.imageSelection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cataracta.R

private lateinit var newRecyclerView: RecyclerView
private lateinit var newArrayList: ArrayList<Photo>
private lateinit var photoPaths: Array<Int>

class ImageSelectionFragment : Fragment() {
    // Fragment code goes here

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        newRecyclerView = view.findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<Photo>()
        getUserData()
    }

    private fun getUserData() {
        for(i in photoPaths.indices){
            val video = Photo(photoPaths[i])
            newArrayList.add(video)
        }

        newRecyclerView.adapter = PhotoAdapter(newArrayList)
    }
}