package com.example.papergemoetry

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.papergemoetry.adapters.CharacterAdapter
import com.example.papergemoetry.network.CharacterResponse
import com.example.papergemoetry.network.RickAndMortyApi
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.papergemoetry.models.Character


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    //Variables para contenido Catalogo

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://papergeometry.online")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(RickAndMortyApi::class.java) //fin para llamada de api

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val searchInput: EditText = findViewById(R.id.search_input)
        val searchIcon: ImageView = findViewById(R.id.search_icon)

        searchIcon.setOnClickListener {
            filterCharacters(searchInput.text.toString())
        }
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterCharacters(s.toString())
            }
        })

        fetchCharactersBySpecies("Mario")

    }



    private fun filterCharacters(query: String) {
        val filteredList = characters.filter {
            it.nombre.contains(query, ignoreCase = true)
        }
        setupRecyclerView(filteredList)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.item_one -> {
                findViewById<LinearLayout>(R.id.search_container).visibility = View.GONE
                fetchCharactersBySpecies("Mario")

            }
            R.id.item_two -> {
                findViewById<LinearLayout>(R.id.search_container).visibility = View.VISIBLE
                fetchCharacters()
            }
            R.id.item_three -> {
                findViewById<LinearLayout>(R.id.search_container).visibility = View.GONE
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun fetchCharactersBySpecies(categoria: String) {
        service.getCharacters().enqueue(object : retrofit2.Callback<List<Character>> {
            override fun onResponse(call: Call<List<Character>>, response: retrofit2.Response<List<Character>>) {
                if (response.isSuccessful) {
                    val allCharacters = response.body() ?: emptyList()
                    val filteredCharacters = allCharacters.filter { it.categoria == categoria }
                    setupRecyclerView(filteredCharacters)
                }
            }

            override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private lateinit var characters: List<Character>

    private fun fetchCharacters() {
        service.getCharacters().enqueue(object : retrofit2.Callback<List<Character>> {
            override fun onResponse(call: Call<List<Character>>, response: retrofit2.Response<List<Character>>) {
                if (response.isSuccessful) {
                    characters = response.body() ?: emptyList()
                    setupRecyclerView(characters)
                }
            }

            override fun onFailure(call: Call<List<Character>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupRecyclerView(characters: List<Character>) {
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val gridLayoutManager = GridLayoutManager(this, 2) // Número de columnas en la cuadrícula
        recyclerView.layoutManager = gridLayoutManager

        recyclerView.adapter = CharacterAdapter(characters) { character ->
            // Aquí se maneja el clic en un personaje
            val intent = Intent(this, CharacterDetailActivity::class.java)
            intent.putExtra("character", character)
            startActivity(intent)
        }
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }
}