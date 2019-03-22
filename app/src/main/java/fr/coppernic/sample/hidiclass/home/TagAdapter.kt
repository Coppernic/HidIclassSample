package fr.coppernic.sample.hidiclass.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.coppernic.sample.hidiclass.R
import fr.coppernic.sample.hidiclass.model.Tag
import fr.coppernic.sdk.utils.core.CpcBytes
import kotlinx.android.synthetic.main.item_tag.view.*
import timber.log.Timber


class TagAdapter(private val tags: MutableList<Tag>) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tag, parent, false))
    }


    override fun getItemCount(): Int {
        return tags.size
    }

    fun updateItem(tag: Tag){
        Timber.d("current tag ${CpcBytes.byteArrayToString(tag.card.cardSerialNumber)}")
        val position = tags.indexOf(tag)
        if (position < 0) { //new tag
            Timber.d("Not in the list")
            tags.add(0, tag)
            // Notify the adapter that an item was inserted at position 0
            notifyItemInserted(0)
        } else {
            tags[position].count++
            // Notify the adapter that an item was changed
            notifyItemChanged(position)
        }
    }

    fun clear(){
        tags.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(tags[position].card.cardNumber > 0) {//LF
            holder.tvTag.text = tags[position].card.cardNumber.toString()
        } else {// HF
            holder.tvTag.text = CpcBytes.byteArrayToString(tags[position].card.cardSerialNumber)
        }
        holder.tvCount.text = tags[position].count.toString()
    }
}

class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvTag : TextView = view.tvTag
    val tvCount: TextView = view.tvCount
}