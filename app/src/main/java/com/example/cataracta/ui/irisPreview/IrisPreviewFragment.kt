package com.example.cataracta.ui.irisPreview

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.example.cataracta.R
import com.example.cataracta.databinding.IrisPreviewBinding
import com.example.cataracta.ResultFragment
import org.json.JSONObject
import java.io.ByteArrayInputStream

class IrisPreviewFragment: Fragment() {

    private lateinit var ivLeftEye: ImageView
    private lateinit var ivRightEye: ImageView
    private lateinit var btnUpload: Button
    private lateinit var binding: IrisPreviewBinding
    private lateinit var tvNoLeftEye: TextView
    private lateinit var tvNoRightEye: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = IrisPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivLeftEye = view.findViewById(R.id.ivLeftEye)
        ivRightEye = view.findViewById(R.id.ivRightEye)
        btnUpload = view.findViewById(R.id.btnUploadIrises)
        tvNoLeftEye = view.findViewById(R.id.tvNoLeftEye)
        tvNoRightEye = view.findViewById(R.id.tvNoRightEye)

        val leftEye = requireArguments().getString("leftEye")
        val rightEye = requireArguments().getString("rightEye")

        if(leftEye != null){
            val leftEyeBitmap = transferImageToBitmap(leftEye)
            ivLeftEye.setImageBitmap(leftEyeBitmap)
            tvNoLeftEye.visibility = View.INVISIBLE
        }

        if(rightEye != null){
            val rightEyeBitmap = transferImageToBitmap(rightEye)
            ivRightEye.setImageBitmap(rightEyeBitmap)
            tvNoRightEye.visibility = View.INVISIBLE
        }

        binding.btnUploadIrises.setOnClickListener{
            //Upload to BE for detecting cataract
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.my_fragment_container, ResultFragment::class.java, null)
                .addToBackStack(IrisPreviewFragment::class.java.name)
                .commit()
        }

    }

    private fun transferImageToBitmap(base64String: String): Bitmap {
        val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
        val inputStream = ByteArrayInputStream(decodedBytes)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        return bitmap
    }
}