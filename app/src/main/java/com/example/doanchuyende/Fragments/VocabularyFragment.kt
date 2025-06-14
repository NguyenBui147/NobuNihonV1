package com.example.doanchuyende.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Adapters.VocabularyAdapter
import com.example.doanchuyende.Models.VocabularyModel
import com.example.doanchuyende.R

class VocabularyFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VocabularyAdapter
    private var vocabularyList: List<VocabularyModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_vocabulary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        recyclerView = view.findViewById(R.id.recyclerViewVocabulary)
        recyclerView.layoutManager = LinearLayoutManager(context)
        
        // Test data
        vocabularyList = listOf(
            VocabularyModel(
                id = 1,
                japanese = "一年生",
                reading = "いちねんせい",
                meaning = "học sinh năm nhất",
                example = "私は一年生です。"
            ),
            VocabularyModel(
                id = 2,
                japanese = "一番",
                reading = "いちばん",
                meaning = "số 1",
                example = "一番好きな食べ物は何ですか？"
            ),
            VocabularyModel(
                id = 3,
                japanese = "一度",
                reading = "いちど",
                meaning = "1 lần",
                example = "一度だけ行ったことがあります。"
            ),
            VocabularyModel(
                id = 4,
                japanese = "一杯",
                reading = "いっぱい",
                meaning = "1 cốc, rất nhiều",
                example = "コーヒーを一杯ください。"
            ),
            VocabularyModel(
                id = 5,
                japanese = "一緒",
                reading = "いっしょ",
                meaning = "cùng nhau",
                example = "一緒に行きましょう。"
            ),
            VocabularyModel(
                id = 6,
                japanese = "一分",
                reading = "いっぷん",
                meaning = "1 phút",
                example = "一分待ってください。"
            ),
            VocabularyModel(
                id = 7,
                japanese = "一枚",
                reading = "いちまい",
                meaning = "1 vật thể",
                example = "写真を一枚撮りましょう。"
            )
        )
        
        adapter = VocabularyAdapter(vocabularyList)
        recyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(): VocabularyFragment {
            return VocabularyFragment()
        }
    }
} 