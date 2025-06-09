package com.example.gaminghub.framework.pantallaSocial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gaminghub.R
import com.example.gaminghub.domain.modelo.FriendCard

class SocialAdapter(
    val context: Context,
) : ListAdapter<FriendCard, SocialAdapter.ItemViewholder>(DiffCallback()) {

    // ViewHolder para manejar la vista de cada tarjeta
    class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val friendName : TextView = itemView.findViewById(R.id.tvFriendName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_amigos, parent, false)
        )
    }

    // Vincula los datos del objeto FriendCard a la vista
    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        val friendCard = getItem(position)
        holder.friendName.text = friendCard.friendName

        // Ejemplo de acción al hacer clic en un elemento
        holder.itemView.setOnClickListener {
            Toast.makeText(context, "Diste a ${friendCard.friendName}", Toast.LENGTH_SHORT).show()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FriendCard>() {
        override fun areItemsTheSame(oldItem: FriendCard, newItem: FriendCard): Boolean {
            return oldItem.friendName == newItem.friendName
        }

        override fun areContentsTheSame(oldItem: FriendCard, newItem: FriendCard): Boolean {
            return oldItem == newItem
        }
    }
}