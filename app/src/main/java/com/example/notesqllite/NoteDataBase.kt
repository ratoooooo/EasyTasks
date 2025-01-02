package com.example.notesqllite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDataBase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "noteapp.db" // Nome da base de dados
        private const val DATABASE_VERSION = 1 // Versão da base de dados
        private const val TABLE_NAME = "allnotes" // Nome da tabela
        private const val COLUMN_ID = "id" // Nome da coluna para o ID
        private const val COLUMN_TITLE = "title" // Nome da coluna para o título
        private const val COLUMN_CONTENT = "content" // Nome da coluna para o conteúdo
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Criação da tabela na base de dados
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Atualização da base de dados: remove a tabela antiga e cria uma nova
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTableQuery)
        onCreate(db)
    }

    fun insertNote(note: Note) {
        // Insere uma nova nota na base de dados
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.titile)
            put(COLUMN_CONTENT, note.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getALLNOtes(): List<Note> {
        // Obtém todas as notas armazenadas na base de dados
        val noteList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            val note = Note(id, text, content)
            noteList.add(note)
        }
        cursor.close()
        db.close()
        return noteList
    }

    fun updateNote(note: Note) {
        // Atualiza uma nota existente com base no ID
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.titile)
            put(COLUMN_CONTENT, note.content)
        }
        val whereClauses = "$COLUMN_ID = ?"
        val whereArrays = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClauses, whereArrays)
        db.close()
    }

    fun getNoteByID(noteid: Int): Note {
        // Obtém uma nota específica com base no ID
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteid"
        val cursor = db.rawQuery(query, null)
        cursor.moveToNext()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val text = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return Note(id, text, content)
    }

    fun deleteNote(noteID: Int) {
        // Remove uma nota da base de dados com base no ID
        val db = writableDatabase
        val whereClauses = "$COLUMN_ID = ?"
        val whereArrays = arrayOf(noteID.toString())
        db.delete(TABLE_NAME, whereClauses, whereArrays)
        db.close()
    }
}
