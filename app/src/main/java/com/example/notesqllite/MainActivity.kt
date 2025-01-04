package com.example.notesqllite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.SearchView

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesqllite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Referência para os elementos do XML através do View Binding
    private lateinit var binding: ActivityMainBinding

    // Referência para a base de dados de notas
    private lateinit var db: NoteDataBase

    // Adaptador para a RecyclerView que irá exibir as notas
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar o View Binding para acessar os elementos da interface
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar a base de dados
        db = NoteDataBase(this)

        // Configurar o adaptador com os dados das notas
        noteAdapter = NoteAdapter(db.getALLNOtes(), this)

        // Configurar a RecyclerView com um layout linear e o adaptador
        binding.noteRecycleView.layoutManager = LinearLayoutManager(this)
        binding.noteRecycleView.adapter = noteAdapter
        binding.searchView

        // Configurar o botão de adicionar nota para abrir a atividade de adição de notas
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }

        //Let  o texto que foi escrito na barra de pesquisa
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Caso nao tenha sido pesquisado nada
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Caso tenha pesquisado alguma coisa
                filterNotes(newText)
                return true
            }
        })

    }

    private fun filterNotes(query: String?) {
        if (query.isNullOrEmpty()) {
            // Se estivr vazio vai mostrar todas as notas
            noteAdapter.refreshData(db.getALLNOtes())
        } else {
            // Filtrar as notas que tenham sido pesquisadas
            val filteredNotes = db.getALLNOtes().filter { note ->
                note.titile.contains(query, ignoreCase = true)
            }
            noteAdapter.refreshData(filteredNotes)
        }
    }

    // Método chamado ao retomar a atividade
    override fun onResume() {
        super.onResume()
        // Atualizar os dados exibidos no adaptador ao retomar a aplicação
        noteAdapter.refreshData(db.getALLNOtes())
    }
}
