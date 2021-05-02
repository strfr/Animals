package com.hsyn.animals

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ContentAdapter(val animals: ArrayList<Animals>, clickListener: IClickListener) :
    RecyclerView.Adapter<ContentAdapter.Holder>() {

    var clickListener: IClickListener? = clickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.select_answer, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bindItems(animals[position], position, clickListener)
    }

    override fun getItemCount(): Int {
        return animals.size
    }

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItems(
            animals: Animals,
            index: Int,
            clickListener: IClickListener?
        ) {
            val cardText = itemView.findViewById(R.id.cardText) as TextView
            val cardImage = itemView.findViewById(R.id.cardImage) as ImageView

            cardText.text = animals.name
            cardImage.setImageResource(animals.image)

            itemView.setOnClickListener {
                clickListener?.listener(index)
            }
        }
    }
}