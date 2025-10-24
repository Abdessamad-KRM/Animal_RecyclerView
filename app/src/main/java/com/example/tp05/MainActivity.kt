package com.example.tp05

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import android.widget.RadioGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

data class Animal(
    val nom: String,
    val espece: String,
    val image: Int
)

class AdapterAnimaux(
    private var animaux: MutableList<Animal>,
    private val layoutType: Int
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

        fun bind(animal: Animal, position: Int) {
            // Afficher les données de l'animal
            imageView.setImageResource(animal.image)
            nomTextView.text = animal.nom
            especeTextView.text = animal.espece

            // Bouton Détails (Vert avec icône)
            btnDetails.setOnClickListener {
                val message = "Nom: ${animal.nom}\nEspèce: ${animal.espece}"
                Toast.makeText(
                    itemView.context,
                    message,
                    Toast.LENGTH_LONG
                ).show()
            }

            // Bouton Supprimer (Rouge avec icône)
            btnSupprimer.setOnClickListener {
                // Retirer l'animal de la liste
                animaux.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, animaux.size)

                Toast.makeText(
                    itemView.context,
                    "${animal.nom} a été supprimé",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    // Méthode pour mettre à jour la liste
    fun updateList(newList: MutableList<Animal>) {
        animaux = newList
        notifyDataSetChanged()
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterAnimaux
    private lateinit var radioGroup: RadioGroup
    private val animaux = mutableListOf<Animal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser les vues
        recyclerView = findViewById(R.id.recyclerView)
        radioGroup = findViewById(R.id.radioGroup)

        // Remplir la liste d'animaux avec des données
        initAnimaux()

        // Configuration initiale en mode linéaire
        setupRecyclerView(AdapterAnimaux.LAYOUT_LINEAR)

        // Gérer le changement de mode d'affichage
        setupRadioGroup()
    }

    private fun initAnimaux() {
        animaux.addAll(
            listOf(
                Animal("Lion", "Mammifère", R.drawable.lion),
                Animal("Tigre", "Mammifère", R.drawable.tiger),
                Animal("Éléphant", "Mammifère", R.drawable.elephant),
                Animal("Aigle", "Oiseau", R.drawable.eagle),
                Animal("Perroquet", "Oiseau", R.drawable.parrot),
                Animal("Hibou", "Oiseau", R.drawable.owl),
                Animal("Serpent", "Reptile", R.drawable.snake),
                Animal("Crocodile", "Reptile", R.drawable.crocodile),
                Animal("Tortue", "Reptile", R.drawable.turtle),
                Animal("Chat", "Mammifère", R.drawable.cat),
                Animal("Chien", "Mammifère", R.drawable.dog),
                Animal("Dauphin", "Mammifère", R.drawable.dolphin)
            )
        )
    }

    private fun setupRecyclerView(layoutType: Int) {
        adapter = AdapterAnimaux(animaux, layoutType)
        recyclerView.adapter = adapter

        // Configurer le LayoutManager selon le type
        recyclerView.layoutManager = if (layoutType == AdapterAnimaux.LAYOUT_LINEAR) {
            LinearLayoutManager(this)
        } else {
            GridLayoutManager(this, 2) // 2 colonnes pour la grille
        }
    }

    private fun setupRadioGroup() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioLineaire -> {
                    setupRecyclerView(AdapterAnimaux.LAYOUT_LINEAR)
                }
                R.id.radioGrille -> {
                    setupRecyclerView(AdapterAnimaux.LAYOUT_GRID)
                }
            }
        }
    }
}