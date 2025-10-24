package com.example.tp05

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdapterAnimaux
    private lateinit var radioGroup: RadioGroup
    private lateinit var textSelectionInfo: TextView
    private lateinit var btnSupprimerSelection: Button
    private val animaux = mutableListOf<Animal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser les vues
        recyclerView = findViewById(R.id.recyclerView)
        radioGroup = findViewById(R.id.radioGroup)
        textSelectionInfo = findViewById(R.id.textSelectionInfo)
        btnSupprimerSelection = findViewById(R.id.btnSupprimerSelection)

        // Remplir la liste d'animaux avec des données
        initAnimaux()

        // Configuration initiale en mode linéaire
        setupRecyclerView(AdapterAnimaux.LAYOUT_LINEAR)

        // Gérer le changement de mode d'affichage
        setupRadioGroup()

        // Bouton pour supprimer les éléments sélectionnés
        btnSupprimerSelection.setOnClickListener {
            val selectedCount = adapter.getSelectedPositions().size
            if (selectedCount > 0) {
                AlertDialog.Builder(this)
                    .setTitle("Confirmation")
                    .setMessage("Voulez-vous supprimer $selectedCount élément(s) sélectionné(s) ?")
                    .setIcon(R.drawable.warning)
                    .setPositiveButton("OK") { _, _ ->
                        adapter.removeSelectedItems()
                        updateSelectionInfo()
                        Toast.makeText(this, "$selectedCount élément(s) supprimé(s)", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Annuler", null)
                    .show()
            }
        }

        updateSelectionInfo()
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
        adapter = AdapterAnimaux(animaux, layoutType) {
            updateSelectionInfo()
        }
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

    private fun updateSelectionInfo() {
        val selectedPositions = adapter.getSelectedPositions()
        if (selectedPositions.isEmpty()) {
            textSelectionInfo.visibility = View.GONE
            btnSupprimerSelection.visibility = View.GONE
        } else {
            textSelectionInfo.visibility = View.VISIBLE
            btnSupprimerSelection.visibility = View.VISIBLE
            textSelectionInfo.text = "Vous avez sélectionné l'élément(s) de position : ${selectedPositions.joinToString(", ")}"
        }
    }
}