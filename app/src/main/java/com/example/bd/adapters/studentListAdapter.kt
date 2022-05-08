package com.example.bd.adapters

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bd.R
import com.example.bd.models.studentList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class studentListAdapter(
    private val studentL: ArrayList<studentList>
) : RecyclerView.Adapter<StudentListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentListViewHolder {
        return StudentListViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.student_line, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StudentListViewHolder, position: Int) {
        //array com a listagem
        val currentList = studentL[position]

        holder.title.text = currentList.titulo
        holder.address.text = currentList.morada
        holder.rent.text = currentList.preco.toString() + "€/mês"

        //if (currentList.rooms == 1) {
        //    holder.rooms.text = currentList.rooms.toString() + " Quarto"
        //} else {
        //    holder.rooms.text = currentList.rooms.toString() + " Quartos"
        //}

        if (currentList.rAnimais) {
            holder.shared.setImageResource(R.drawable.ic_baseline_people_24)
        } else {
            holder.shared.setImageResource(R.drawable.ic_baseline_person_24)
        }

        if (currentList.rFumadores) {
            holder.smoke.setImageResource(R.drawable.ic_baseline_smoking_rooms_24)
        } else {
            holder.smoke.setImageResource(R.drawable.ic_baseline_smoke_free_24)
        }

        if (currentList.rAcessivel) {
            holder.accessible.setImageResource(R.drawable.ic_baseline_accessible_24)
        } else {
            holder.accessible.setImageResource(R.drawable.ic_baseline_not_accessible_24)
        }

        // Both
        if (currentList.reservado.equals("0")) {
            holder.male.setImageResource(R.drawable.ic_baseline_male_24)
            holder.female.setImageResource(R.drawable.ic_baseline_female_24)
            // Male only
        } else if (currentList.reservado.equals("1")) {
            holder.male.setImageResource(R.drawable.ic_baseline_male_24)
            // Female only
        } else {
            holder.female.setImageResource(R.drawable.ic_baseline_female_24)
        }


        //holder.title.text = currentList.title
        //holder.address.text = currentList.address
        //holder.rent.text = currentList.rent.toString() + "€/mês"
//
        //if (currentList.rooms == 1) {
        //    holder.rooms.text = currentList.rooms.toString() + " Quarto"
        //} else {
        //    holder.rooms.text = currentList.rooms.toString() + " Quartos"
        //}
//
        //if (currentList.shared) {
        //    holder.shared.setImageResource(R.drawable.ic_baseline_people_24)
        //} else {
        //    holder.shared.setImageResource(R.drawable.ic_baseline_person_24)
        //}
//
        //if (currentList.smoke) {
        //        holder.smoke.setImageResource(R.drawable.ic_baseline_smoking_rooms_24)
        //} else {
        //    holder.smoke.setImageResource(R.drawable.ic_baseline_smoke_free_24)
        //}
//
        //if (currentList.accessible) {
        //    holder.accessible.setImageResource(R.drawable.ic_baseline_accessible_24)
        //} else {
        //    holder.accessible.setImageResource(R.drawable.ic_baseline_not_accessible_24)
        //}
//
        //// Both
        //if (currentList.sex == 0) {
        //    holder.male.setImageResource(R.drawable.ic_baseline_male_24)
        //    holder.female.setImageResource(R.drawable.ic_baseline_female_24)
        //    // Male only
        //} else if (currentList.sex == 1) {
        //    holder.male.setImageResource(R.drawable.ic_baseline_male_24)
        //    // Female only
        //} else {
        //    holder.female.setImageResource(R.drawable.ic_baseline_female_24)
        //}

        //atraves do codigo do anuncio vou buscar uma imagem "aleatoria" possivel a BD
        val ref = FirebaseDatabase.getInstance().getReference("ImagensAnuncios")
        ref.child(currentList.codAnuncio!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val imagem = "https://firebasestorage.googleapis.com/v0/b/rentalstudent-47413.appspot.com/o/imagensAnuncios%2Fvazio.jpg?alt=media&token=18b47cd5-7b63-43d2-964f-35f69008848a"
                    if (snapshot.exists()) {
                        val imagem = "${snapshot.child("imagemURL").value}"
                    }

                    Picasso.get().load(imagem).into(holder.imagemA)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }

    override fun getItemCount(): Int {
        return studentL.size
    }

    fun addTodo(sl: studentList) {
        studentL.add(sl)
        notifyDataSetChanged()
    }
}

class StudentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title: TextView = itemView.findViewById(R.id.title)
    val address: TextView = itemView.findViewById(R.id.address)
    val rent: TextView = itemView.findViewById(R.id.price)
    val shared: ImageView = itemView.findViewById(R.id.shared)
    val smoke: ImageView = itemView.findViewById(R.id.smoke)
    val accessible: ImageView = itemView.findViewById(R.id.accessible)
    val male: ImageView = itemView.findViewById(R.id.male)
    val female: ImageView = itemView.findViewById(R.id.female)
    val rooms: TextView = itemView.findViewById(R.id.roooms)
    val imagemA: ImageView = itemView.findViewById(R.id.imagemA)

}