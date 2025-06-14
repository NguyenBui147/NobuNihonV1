package com.example.doanchuyende.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doanchuyende.Activities.QuizQuestionActivity
import com.example.doanchuyende.Models.QuizModel
import com.example.doanchuyende.R

class QuizListAdapter(private val quizList: List<QuizModel>) : 
    RecyclerView.Adapter<QuizListAdapter.QuizViewHolder>() {

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.quizTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.quizDescription)
        val durationTextView: TextView = itemView.findViewById(R.id.quizDuration)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(itemView.context, QuizQuestionActivity::class.java)
                    intent.putExtra("QUIZ_ID", quizList[position].id)
                    intent.putExtra("QUIZ_TITLE", quizList[position].title)
                    itemView.context.startActivity(intent)
                }
            }
        }

        fun bind(quiz: QuizModel) {
            titleTextView.text = quiz.title
            descriptionTextView.text = quiz.description
            durationTextView.text = quiz.duration
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_quiz, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(quizList[position])
    }

    override fun getItemCount() = quizList.size
} 