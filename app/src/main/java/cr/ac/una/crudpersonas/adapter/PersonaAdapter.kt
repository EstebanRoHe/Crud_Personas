package cr.ac.una.crudpersonas.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cr.ac.una.crudpersonas.R
import cr.ac.una.crudpersonas.entity.Persona

class PersonaAdapter(var personas: ArrayList<Persona>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_HEADER = 0
    private val VIEW_TYPE_ITEM = 1



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_header, parent, false)
            HeaderViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            ViewHolder(view)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind()
        } else if (holder is ViewHolder) {
            val personasItem = personas[position - 1] // Restar 1 para compensar el encabezado
            holder.bind(personasItem)
        }
    }
    override fun getItemCount(): Int {
        return personas.size + 1
    }


    fun updateData(newData: ArrayList<Persona>) {
        personas.clear()
        personas.addAll(newData)
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       fun bind(){
            val idTextView = itemView.findViewById<TextView>(R.id.id)
            val nombreTextView = itemView.findViewById<TextView>(R.id.nombre)
            val apellidoTextView = itemView.findViewById<TextView>(R.id.apellido)

            idTextView.text = "id"
            nombreTextView.text = "Nombre"
            apellidoTextView.text = "Apellido"

        }

    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idtextView = itemView.findViewById<TextView>(R.id.id)
        val nombreTextView = itemView.findViewById<TextView>(R.id.nombre)
        val apellidoTextView = itemView.findViewById<TextView>(R.id.apellido)

        val negro = Color.rgb(47, 48, 48)
        fun bind(persona: Persona) {

           itemView.setBackgroundColor (negro)
            idtextView.text = persona.id.toString()
            nombreTextView.text = persona.nombre.toString()
            apellidoTextView.text = persona.apellido.toString()

        }
    }
}

