package com.example.notesqllite

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notesqllite.databinding.ActivityUpdateNoteBinding

class UpdateNoteActivity : AppCompatActivity() {
    // Inicialização do binding para ligação com os elementos do layout
    private lateinit var binding: ActivityUpdateNoteBinding

    // Instância da base de dados
    private lateinit var db: NoteDataBase

    // Variável para armazenar o ID da nota a ser atualizada
    private var noteID: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialização da base de dados
        db = NoteDataBase(this)

        // Obter o ID da nota passado pela intent
        noteID = intent.getIntExtra("note_id", -1)

        // Se o ID for inválido, fechar a atividade
        if (noteID == -1) {
            finish()
            return
        }

        // Obter os dados da nota a partir do ID e preencher os campos do layout
        val note = db.getNoteByID(noteID)
        binding.titleUpdateText.setText(note.titile) // Preencher o campo do título
        binding.contentEditText.setText(note.content) // Preencher o campo do conteúdo

        // Configurar o botão de atualização
        binding.updatebutton.setOnClickListener {
            // Obter os novos valores dos campos de texto
            val newText = binding.titleUpdateText.text.toString()
            val newContent = binding.contentEditText.text.toString()

            if (newText.isEmpty() || newContent.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            // Criar um objeto Note atualizado
            val updateNote = Note(noteID, newText, newContent)

            // Atualizar a nota na base de dados
            db.updateNote(updateNote)

            // Finalizar a atividade e mostrar uma mensagem de sucesso
            finish()
            Toast.makeText(this, "Alterações guardadas", Toast.LENGTH_SHORT).show()
        }
    }
}
