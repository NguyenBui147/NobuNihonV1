package com.example.doanchuyende.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Activities.KanjiDetailActivity
import com.example.doanchuyende.Models.KanjiModel
import com.example.doanchuyende.R

class KanjiAdapter(private val kanjiList: MutableList<KanjiModel>) :
    RecyclerView.Adapter<KanjiAdapter.KanjiViewHolder>() {

    inner class KanjiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val kanjiTextView: TextView = itemView.findViewById(R.id.kanjiTextView)
        val meaningTextView: TextView = itemView.findViewById(R.id.meaningTextView)
        val readingTextView: TextView = itemView.findViewById(R.id.readingTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(itemView.context, KanjiDetailActivity::class.java)
                    intent.putExtra("KANJI_ID", kanjiList[position].id)
                    intent.putExtra("KANJI", kanjiList[position].kanji)
                    intent.putExtra("MEANING", kanjiList[position].meaning)
                    intent.putExtra("ONYOMI", kanjiList[position].onyomi)
                    intent.putExtra("KUNYOMI", kanjiList[position].kunyomi)
                    intent.putStringArrayListExtra("EXAMPLES", ArrayList(kanjiList[position].examples))
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(kanji: KanjiModel) {
            kanjiTextView.text = kanji.kanji
            meaningTextView.text = kanji.meaning
            readingTextView.text = "${kanji.onyomi} / ${kanji.kunyomi}"
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KanjiViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_kanji, parent, false)
        return KanjiViewHolder(view)
    }

    override fun onBindViewHolder(holder: KanjiViewHolder, position: Int) {
        holder.bind(kanjiList[position])
    }

    override fun getItemCount() = kanjiList.size
    fun updateList(newList: List<KanjiModel>) {
        kanjiList.clear()
        kanjiList.addAll(newList)
        notifyDataSetChanged()
    }
}