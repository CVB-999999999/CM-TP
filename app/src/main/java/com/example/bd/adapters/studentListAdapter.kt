package com.example.bd.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bd.R
import com.example.bd.models.studentList

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
        val currentList = studentL[position]

        holder.title.text = currentList.title
        holder.address.text = currentList.address
        holder.rent.text = currentList.rent.toString() + "€/mês"

        if (currentList.shared) {
            holder.shared.setImageResource(R.drawable.ic_baseline_people_24)
        } else {
            holder.shared.setImageResource(R.drawable.ic_baseline_person_24)
        }

        if (currentList.smoke) {
            holder.smoke.setImageResource(R.drawable.ic_baseline_smoking_rooms_24)
        } else {
            holder.smoke.setImageResource(R.drawable.ic_baseline_smoke_free_24)
        }

        if (currentList.accessible) {
            holder.accessible.setImageResource(R.drawable.ic_baseline_accessible_24)
        } else {
            holder.accessible.setImageResource(R.drawable.ic_baseline_not_accessible_24)
        }

        // Both
        if (currentList.sex == 0) {
            holder.male.setImageResource(R.drawable.ic_baseline_male_24)
            holder.female.setImageResource(R.drawable.ic_baseline_female_24)
            // Male only
        } else if (currentList.sex == 1) {
            holder.male.setImageResource(R.drawable.ic_baseline_male_24)
            // Female only
        } else {
            holder.female.setImageResource(R.drawable.ic_baseline_female_24)
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

class StudentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title: TextView = itemView.findViewById(R.id.title)
    val address: TextView = itemView.findViewById(R.id.address)
    val rent: TextView = itemView.findViewById(R.id.price)
    val shared: ImageView = itemView.findViewById(R.id.shared)
    val smoke: ImageView = itemView.findViewById(R.id.smoke)
    val accessible: ImageView = itemView.findViewById(R.id.accessible)
    val male: ImageView = itemView.findViewById(R.id.male)
    val female: ImageView = itemView.findViewById(R.id.female)

}