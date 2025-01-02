package com.example.notesqllite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesqllite.databinding.ActivityAddNoteBinding

class AddNote : AppCompatActivity() {

    // Referência para os elementos do XML através do View Binding
    private lateinit var binding: ActivityAddNoteBinding

    // Referência para a base de dados de notas
    private lateinit var db: NoteDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar o View Binding para acessar os elementos da interface
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar a base de dados
        db = NoteDataBase(this)

        // Configurar o botão de salvar nota
        binding.saveButton.setOnClickListener {
            // Obter o título e conteúdo da nota introduzidos pelo utilizador
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Criar um objeto Note com os dados inseridos
            val note = Note(0, title, content)

            // Inserir a nota na base de dados
            db.insertNote(note)

            // Encerrar a atividade atual após salvar a nota
            finish()

            // Mostrar uma mensagem ao utilizador confirmando o salvamento da nota
            Toast.makeText(this, "Nota guardada", Toast.LENGTH_LONG).show()
        }
    }
}
