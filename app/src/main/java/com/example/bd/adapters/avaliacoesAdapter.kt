package com.example.bd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.R
import com.example.bd.app.OnStudentClickListener
import com.example.bd.models.avaliacoesModel
import com.example.bd.models.studentList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class avaliacoesAdapter(
private val avaliacoesL: ArrayList<avaliacoesModel>
) :
RecyclerView.Adapter<AvaliacoesListViewHoder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvaliacoesListViewHoder {
        return AvaliacoesListViewHoder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.avaliacoes_line, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AvaliacoesListViewHoder, position: Int) {
        //array com a listagem
        val currentList = avaliacoesL[position]

        holder.avGeral.text = currentList.geral.toString()
        holder.avProp.text = currentList.proprietario.toString()
        holder.avEspaco.text = currentList.espaco.toString()
        holder.comentario.text = currentList.comentario.toString()
        holder.dataPub.text = currentList.dataPublicacao.toString()

    }

    override fun getItemCount(): Int {
        return avaliacoesL.size
    }

    fun addTodo(sl: avaliacoesModel) {
        avaliacoesL.add(sl)
        notifyDataSetChanged()
    }

    fun rmAll() {
        avaliacoesL.clear()
        notifyDataSetChanged()
    }
}

class AvaliacoesListViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val avGeral: TextView = itemView.findViewById(R.id.avGeral)
    val avProp: TextView = itemView.findViewById(R.id.avProp)
    val avEspaco: TextView = itemView.findViewById(R.id.avEsp)
    val comentario: TextView = itemView.findViewById(R.id.comentario)
    val dataPub: TextView = itemView.findViewById(R.id.data)

}