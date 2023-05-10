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
import com.example.cataracta.R
import com.example.cataracta.databinding.IrisPreviewBinding
import com.example.cataracta.ResultFragment
import org.json.JSONObject
import java.io.ByteArrayInputStream

class IrisPreviewFragment: Fragment() {

    private lateinit var ivLeftEye: ImageView
    private lateinit var ivRightEye: ImageView
    private lateinit var btnUpload: Button
    private val args = Bundle()
    private lateinit var binding: IrisPreviewBinding
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

        val args = this.arguments
//        val data = args?.getString("images").toString()

        val data = "{\"left_eye\":\"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\",\"right_eye\":\"/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAIBAQEBAQIBAQECAgICAgQDAgICAgUEBAMEBgUGBgYFBgYGBwkIBgcJBwYGCAsICQoKCgoKBggLDAsKDAkKCgr/2wBDAQICAgICAgUDAwUKBwYHCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgoKCgr/wAARCAAwADADASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD8lbv4o63eTGWS6myT2apYfiBqssOwXU5JGMBzWfceGoLOZoUkLbWxgitzwh4MguJhczRcEZFfOuvOGlj9SyfJq+b1oxobsNO13xCkiyvf3aDPOBn/ANmFbB8beI7ELNbX87Hs5kZCPyNbsugW9vaPci2HlQpulc9FGCcn8AaNO8Lp4ls4FsoEJuUzB84Bf5tvGevzelJ4uaV2fZVeDcdTp8rkmafhX9qnx54VjWGbU4ZFXlTKxZwf97eD+teq+A/+CgXijW0GkeIJIHj+4FKMJCD/ALZaYd/QV83+JPhVfWFyYtSR4XxkKw5xVfQ9Eh0e6V7eUs6tTjNVXpufEYrDTwlRwqaWM7U7e4/tGQA4AlIP5mvQ/CenRzaTELcjIQ1yeu6fLHfXBZMAyt83vk1t+BdfNq0dojcBcNx71GJa1R9bwJWpYbFx5nZWsdLazwWUEkeuae93ZyXMHnIgI2Kj/OeOuVbGDx+dftr/AMFAP+Cb37F95/wTxPx/+GugWWjato+g2us2GsQ3eFmLQxs0W3djJwvHXI6V+J9zeQTo1pIZSk6FHMbgAA8c967HxB+1F8e9V+Gdp8G9Y+LetXfha1j2nQzcDZKUcmPGcAYBAPsBXFUa9lY/Rs/yvEZriMLPDYn2apzvLf3l2sv6RxXxDvptSun1C+uUlmlhTdIhGDhABkdjgAY9RXEaRCW1XzDCSPMTDZ4OTjH4Z/WtDxNqfnq0VvwScYqzomli3S2jIJCAvM2Mnjn8earDTnTacVqfl/GcaNTGSktNTc+NPgE+G/E8tnbToy3MhaNR2HI/DpXntjDqFjqM1tDlHhk2yB14zx0NfVvib4a6L4vuGe/mctHIZI3HUVjJ+zV4b8VykSTzwuxzvQ8tXbiKKdVs8SlKODaavoeDReI4rf5JYJnb1ETY/PGKW51N9Vg8u2jeNsctnNfR2m/sp6SNPadIUlgSTy28933E/hmt3Rv2TdCEa+To1tFvHAIZ81ksNfZHq/60Y23LC/zPlTR9Hvr6cSyxFkQ8nb3r0T4Q/D6+8TeNLONxthFwglDjHyk/yr6As/2X9GsY2jaBFwCxWNMcetdh4G/Zq8nSzr3hu2kldHwQi88fj7Gt6GGcZ3sebjKksyalWP/Z\"}"
        val json = JSONObject(data)
        val leftEye = json.getString("left_eye")
        val rightEye = json.getString("right_eye")

        if(leftEye != null){
            val leftEyeBitmap = transferImageToBitmap(leftEye)
            ivLeftEye.setImageBitmap(leftEyeBitmap)
        }

        if(rightEye != null){
            val rightEyeBitmap = transferImageToBitmap(rightEye)
            ivRightEye.setImageBitmap(rightEyeBitmap)
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