package com.example.cataracta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cataracta.databinding.ResultBinding
import com.example.cataracta.ui.irisPreview.IrisPreviewFragment
import com.example.cataracta.ui.record.RecordFragment

class ResultFragment: Fragment() {

    private lateinit var binding: ResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ResultBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRecord.setOnClickListener{
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.my_fragment_container, RecordFragment::class.java, null)
                .addToBackStack(IrisPreviewFragment::class.java.name)
                .commit()

        }
    }
}