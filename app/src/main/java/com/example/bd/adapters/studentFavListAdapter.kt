package com.example.bd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.R
import com.example.bd.app.OnStudentClickCodListener
import com.example.bd.app.OnStudentClickListener
import com.example.bd.models.studentList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class studentFavListAdapter(
    private val studentL: ArrayList<studentList>, private val onStudentClickCodListener: OnStudentClickCodListener
) :
    RecyclerView.Adapter<StudentFavListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentFavListViewHolder {
        return StudentFavListViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.student_line, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StudentFavListViewHolder, position: Int) {
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

        //atraves do codigo do anuncio vou buscar uma imagem a BD
        val ref = FirebaseDatabase.getInstance().getReference("ImagensAnuncios")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    var conta = 0
                    for (anuncioSnap in snapshot.children) {
                        val codA = "${anuncioSnap.child("codAnuncio").value}"
                        if (codA.equals(currentList.codAnuncio)){
                            val imagem = "${anuncioSnap.child("imagemURL").value}"
                            Picasso.get().load(imagem).into(holder.imagemA)
                            conta=conta+1
                        }
                    }
                    if (conta==0){
                        val imagem = "https://firebasestorage.googleapis.com/v0/b/rentalstudent-47413.appspot.com/o/imagensAnuncios%2F0c31a1fea169f62723961552742988b3a97e8e12.jpg?alt=media&token=ee27cce2-f4be-4801-aa1a-b7ce98516852"
                        Picasso.get().load(imagem).into(holder.imagemA)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        holder.itemView.setOnClickListener {
            onStudentClickCodListener.onStudentClickCodItem(currentList.codAnuncio!!)
        }
    }

    override fun getItemCount(): Int {
        return studentL.size
    }

    fun addTodo(sl: studentList) {
        studentL.add(sl)
        notifyDataSetChanged()
    }
}

class StudentFavListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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