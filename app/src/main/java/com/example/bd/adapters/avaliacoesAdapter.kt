package com.example.bd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.R
import com.example.bd.app.MyApplication.Companion.formatTimeStamp
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

        val data = formatTimeStamp(currentList.dataPublicacao!!)

        holder.comentario.text = currentList.comentario.toString()
        holder.dataPub.text = "Publicado em: " + data

        when(currentList.geral){
            5.0 -> {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_24)
            }
            4.5 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_half_24)
            }
            4.0 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            3.5 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            3.0 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            2.5 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            2.0 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            1.5 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            1.0 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            0.5 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            0.0 ->  {
                holder.avG1.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avG5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
        }

        when(currentList.proprietario){
            5.0 -> {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_24)
            }
            4.5 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_half_24)
            }
            4.0 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            3.5 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            3.0 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            2.5 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            2.0 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            1.5 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            1.0 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            0.5 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            0.0 ->  {
                holder.avP1.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avP5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
        }

        when(currentList.espaco){
            5.0 -> {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_24)
            }
            4.5 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_half_24)
            }
            4.0 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            3.5 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            3.0 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            2.5 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            2.0 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            1.5 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            1.0 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            0.5 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_half_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
            0.0 ->  {
                holder.avE1.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE2.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE3.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE4.setImageResource(R.drawable.ic_baseline_star_border_24)
                holder.avE5.setImageResource(R.drawable.ic_baseline_star_border_24)
            }
        }

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

    val avG1 : ImageView = itemView.findViewById(R.id.avG1)
    val avG2 : ImageView = itemView.findViewById(R.id.avG2)
    val avG3 : ImageView = itemView.findViewById(R.id.avG3)
    val avG4 : ImageView = itemView.findViewById(R.id.avG4)
    val avG5 : ImageView = itemView.findViewById(R.id.avG5)

    val avP1 : ImageView = itemView.findViewById(R.id.avP1)
    val avP2 : ImageView = itemView.findViewById(R.id.avP2)
    val avP3 : ImageView = itemView.findViewById(R.id.avP3)
    val avP4 : ImageView = itemView.findViewById(R.id.avP4)
    val avP5 : ImageView = itemView.findViewById(R.id.avP5)

    val avE1 : ImageView = itemView.findViewById(R.id.avE1)
    val avE2 : ImageView = itemView.findViewById(R.id.avE2)
    val avE3 : ImageView = itemView.findViewById(R.id.avE3)
    val avE4 : ImageView = itemView.findViewById(R.id.avE4)
    val avE5 : ImageView = itemView.findViewById(R.id.avE5)

}