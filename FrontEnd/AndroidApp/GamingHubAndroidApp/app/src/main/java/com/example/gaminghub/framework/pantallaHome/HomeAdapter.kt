package com.example.gaminghub.framework.pantallaHome

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gaminghub.domain.modelo.PartidaCard
import com.example.gaminghub.R

class HomeAdapter(
    val context: Context,
) : ListAdapter<PartidaCard, HomeAdapter.ItemViewholder>(DiffCallback()) {

    // ViewHolder para manejar la vista de cada tarjeta
    class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id : TextView = itemView.findViewById(R.id.tvId)
        val title: TextView = itemView.findViewById(R.id.tvGameTitle)
        val status : TextView = itemView.findViewById(R.id.tvStatus)
        val creationDate : TextView = itemView.findViewById(R.id.tvCreationDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_partidas, parent, false)
        )
    }

    // Vincula los datos del objeto PartidaCard a la vista
    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        val partidaCard = getItem(position)
        holder.id.text = partidaCard.id
        holder.title.text = partidaCard.juegoNombre
        holder.status.text = partidaCard.estado
        holder.creationDate.text = partidaCard.fechaCreacion

        // Ejemplo de acción al hacer clic en un elemento
        holder.itemView.setOnClickListener {
             Toast.makeText(context, "Diste a ${partidaCard.juegoNombre}", Toast.LENGTH_SHORT).show()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PartidaCard>() {
        override fun areItemsTheSame(oldItem: PartidaCard, newItem: PartidaCard): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PartidaCard, newItem: PartidaCard): Boolean {
            return oldItem == newItem
        }
    }
}