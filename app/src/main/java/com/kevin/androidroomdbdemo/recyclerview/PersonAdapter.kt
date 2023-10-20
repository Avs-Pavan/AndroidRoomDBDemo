package com.kevin.androidroomdbdemo.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kevin.androidroomdbdemo.databinding.PersonRowBinding
import com.kevin.androidroomdbdemo.room.entity.Person


class PersonAdapter(
    private val personList: List<Person>,
    private var isActionMode: Boolean,
    private val listener: RecyclerListener
) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {

    // Inner class to hold views
    inner class PersonViewHolder(val binding: PersonRowBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val binding = PersonRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PersonViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return personList.size
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {

        // Get person at current position
        val person = personList[position]

        // Bind data with view
        holder.binding.name.text = person.name
        holder.binding.age.text = person.age.toString()
        holder.binding.profession.text = person.profession
        holder.binding.address.text = person.address

        // Set visibility of checkbox based on action mode
        if (isActionMode) {
            holder.binding.linearLayoutCheckBox.visibility = ViewGroup.VISIBLE
        } else{
            holder.binding.checkBox.isChecked = false
            holder.binding.linearLayoutCheckBox.visibility = ViewGroup.GONE
        }

        // Set long click listener on card view
        holder.binding.cardView.setOnLongClickListener {
            listener.onLongPress(position)
            true
        }

        // Set click listener on card view
        holder.binding.cardView.setOnClickListener {
            with(holder.binding.checkBox) {
                isChecked = !isChecked
            }
            listener.onClick(person.id, position)
            true
        }

    }

    override fun getItemId(position: Int): Long {
        return personList[position].id.toLong()
    }

    // Function to set action mode
    fun setActionMode(boolean: Boolean) {
        this.isActionMode = boolean
        notifyDataSetChanged()
    }
}