package com.example.tp05

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.widget.RadioGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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