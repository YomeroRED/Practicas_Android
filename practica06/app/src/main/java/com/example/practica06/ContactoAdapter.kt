package com.example.practica06

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactoAdapter(
    private val contactos: List<Contacto>,
    private val onItemClick: (Contacto) -> Unit
) : RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>() {

    // ViewHolder: guarda las referencias a las vistas de cada celda
    class ContactoViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)
    }

    // Infla el layout del ítem (solo se llama para crear celdas nuevas)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contacto, parent, false)
        return ContactoViewHolder(view)
    }

    // Vincula los datos con la celda (se reutiliza al hacer scroll)
    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = contactos[position]
        holder.tvNombre.text = contacto.nombre
        holder.tvTelefono.text = "Tel: ${contacto.telefono}"
        holder.itemView.setOnClickListener { onItemClick(contacto) }
    }

    override fun getItemCount() = contactos.size
}