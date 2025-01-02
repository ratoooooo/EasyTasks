package com.example.notesqllite

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(private var notes: List<Note>, private val context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // Referência para a base de dados para operações de atualização e eliminação
    private val db: NoteDataBase = NoteDataBase(context)

    // Classe interna para representar os elementos de um item da lista
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleView) // Exibe o título da nota
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView) // Exibe o conteúdo da nota
        val updateButton: ImageView = itemView.findViewById(R.id.updateButton) // Botão para atualizar a nota
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton) // Botão para eliminar a nota
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // Infla o layout do item da lista e cria o ViewHolder
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        // Obtém a nota atual com base na posição
        val note = notes[position]

        // Define os valores do título e do conteúdo
        holder.titleTextView.text = note.titile
        holder.contentTextView.text = note.content

        // Configura o botão de atualização para abrir a atividade de edição da nota
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id) // Passa o ID da nota para a atividade de atualização
            }
            holder.itemView.context.startActivity(intent)
        }

        // Configura o botão de eliminação para apagar a nota e atualizar a lista
        holder.deleteButton.setOnClickListener {
            db.deleteNote(note.id) // Elimina a nota da base de dados
            refreshData(db.getALLNOtes()) // Atualiza os dados da lista
            Toast.makeText(holder.itemView.context, "Nota eliminada", Toast.LENGTH_SHORT).show() // Notifica o utilizador
        }
    }

    override fun getItemCount(): Int {
        // Retorna o número total de notas na lista
        return notes.size
    }

    fun refreshData(newNotes: List<Note>) {
        // Atualiza a lista de notas e notifica o RecyclerView para refrescar a exibição
        notes = newNotes
        notifyDataSetChanged()
    }
}
