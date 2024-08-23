package com.example.papergemoetry.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.papergemoetry.R
import com.example.papergemoetry.models.Character
import com.squareup.picasso.Picasso

class CharacterAdapter(
    private val characters: List<Character>,
    private val onCharacterClick: (Character) -> Unit
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.nameTextView.text = character.nombre
        Picasso.get().load(character.foto).into(holder.imageView)

        holder.itemView.setOnClickListener {
            onCharacterClick(character)
        }
    }

    override fun getItemCount(): Int = characters.size

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_character)
        val nameTextView: TextView = itemView.findViewById(R.id.text_name)
        val addToCartButton: Button = itemView.findViewById(R.id.button_add_to_cart)
    }
}
