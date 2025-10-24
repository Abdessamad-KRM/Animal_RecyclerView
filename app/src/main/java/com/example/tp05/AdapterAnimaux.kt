package com.example.tp05

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class AdapterAnimaux(
    private var animaux: MutableList<Animal>,
    private val layoutType: Int,
    private val onSelectionChanged: () -> Unit
) : RecyclerView.Adapter<AdapterAnimaux.AnimalViewHolder>() {

    companion object {
        const val LAYOUT_LINEAR = 0
        const val LAYOUT_GRID = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val layout = if (layoutType == LAYOUT_LINEAR) {
            R.layout.animal_item
        } else {
            R.layout.animal_item_grid
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return AnimalViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animaux[position]
        holder.bind(animal, position)
    }

    override fun getItemCount(): Int = animaux.size

    inner class AnimalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageAnimal)
        private val nomTextView: TextView = itemView.findViewById(R.id.textNom)
        private val especeTextView: TextView = itemView.findViewById(R.id.textEspece)
        private val btnDetails: Button = itemView.findViewById(R.id.btnDetails)
        private val btnSupprimer: Button = itemView.findViewById(R.id.btnSupprimer)
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxAnimal)

        fun bind(animal: Animal, position: Int) {
            // Afficher les données de l'animal
            imageView.setImageResource(animal.image)
            nomTextView.text = animal.nom
            especeTextView.text = animal.espece
            checkBox.isChecked = animal.isSelected

            // Gérer la sélection via checkbox
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                animaux[adapterPosition].isSelected = isChecked
                onSelectionChanged()
            }

            // Bouton Détails (Vert avec icône)
            btnDetails.setOnClickListener {
                // Create a dialog showing details
                AlertDialog.Builder(itemView.context)
                    .setTitle("Détails de l'animal")
                    .setMessage("Nom : ${animal.nom}\nEspèce : ${animal.espece}")
                    .setIcon(R.drawable.info)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }

            // Bouton Supprimer (Rouge avec icône)
            btnSupprimer.setOnClickListener {
                // Retirer l'animal de la liste
                AlertDialog.Builder(itemView.context)
                    .setTitle("Supprimer ${animal.nom} ?")
                    .setMessage("Voulez-vous vraiment supprimer cet animal ?")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton("Oui") { dialog, _ ->
                        // User confirmed -> remove the item
                        animaux.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, animaux.size)
                        onSelectionChanged()

                        Toast.makeText(
                            itemView.context,
                            "${animal.nom} a été supprimé",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
                    .setNegativeButton("Non") { dialog, _ ->
                        dialog.dismiss() // Cancel deletion
                    }.show()
            }
        }
    }

    // Méthode pour mettre à jour la liste
    fun updateList(newList: MutableList<Animal>) {
        animaux = newList
        notifyDataSetChanged()
    }

    // Récupérer les positions des éléments sélectionnés
    fun getSelectedPositions(): List<Int> {
        return animaux.mapIndexedNotNull { index, animal ->
            if (animal.isSelected) index else null
        }
    }

    // Supprimer tous les éléments sélectionnés
    fun removeSelectedItems() {
        animaux.removeAll { it.isSelected }
        notifyDataSetChanged()
    }
}