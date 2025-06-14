package com.example.doanchuyende.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Activities.VocabularyDetailActivity
    import com.example.doanchuyende.Models.VocabularyModel
import com.example.doanchuyende.R

class VocabularyAdapter(private val vocabularyList: List<VocabularyModel>) : 
    RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder>() {

    inner class VocabularyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val japaneseTextView: TextView = itemView.findViewById(R.id.japaneseTextView)
        val readingTextView: TextView = itemView.findViewById(R.id.readingTextView)
        val meaningTextView: TextView = itemView.findViewById(R.id.meaningTextView)
        val exampleTextView: TextView = itemView.findViewById(R.id.exampleTextView)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    try {
                        val vocabulary = vocabularyList[position]
                        val intent = Intent(itemView.context, VocabularyDetailActivity::class.java).apply {
                            putExtra("VOCABULARY_ID", vocabulary.id)
                            putExtra("JAPANESE", vocabulary.japanese)
                            putExtra("READING", vocabulary.reading)
                            putExtra("MEANING", vocabulary.meaning)
                            putExtra("EXAMPLE", vocabulary.example)
                        }
                        itemView.context.startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("VocabularyAdapter", "Error starting VocabularyDetailActivity", e)
                    }
                }
            }
        }

        fun bind(vocabulary: VocabularyModel) {
            japaneseTextView.text = vocabulary.japanese
            readingTextView.text = vocabulary.reading
            meaningTextView.text = vocabulary.meaning
            exampleTextView.text = vocabulary.example
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocabularyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vocabulary, parent, false)
        return VocabularyViewHolder(view)
    }

    override fun onBindViewHolder(holder: VocabularyViewHolder, position: Int) {
        holder.bind(vocabularyList[position])
    }

    override fun getItemCount() = vocabularyList.size
} 