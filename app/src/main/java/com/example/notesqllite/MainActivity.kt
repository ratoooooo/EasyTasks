package com.example.notesqllite

import android.content.Intent
import android.os.Bundle
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

        // Configurar o botão de adicionar nota para abrir a atividade de adição de notas
        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddNote::class.java)
            startActivity(intent)
        }
    }

    // Método chamado ao retomar a atividade
    override fun onResume() {
        super.onResume()
        // Atualizar os dados exibidos no adaptador ao retomar a aplicação
        noteAdapter.refreshData(db.getALLNOtes())
    }
}
