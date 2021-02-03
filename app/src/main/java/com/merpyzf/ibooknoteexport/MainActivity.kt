package com.merpyzf.ibooknoteexport

import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import java.io.File

class MainActivity : AppCompatActivity() {
    val NS_TIME_INTERVAL_SINCE_1970 = 978307200
    val noteDBPath = File(Environment.getExternalStorageDirectory().path, "ibook.nosync/AEAnnotation/AEAnnotation_v10312011_1727_local.sqlite").path
    val bookDBPath = File(Environment.getExternalStorageDirectory().path, "ibook.nosync/BKLibrary/BKLibrary-1-091020131601.sqlite").path
    val bookTableName = "ZBKLIBRARYASSET"
    val noteTableName = "ZAEANNOTATION"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bookDB = openSQLiteDB(bookDBPath)
        val noteDB = openSQLiteDB(noteDBPath)
        val bookCursor = bookDB.query(bookTableName, arrayOf("ZSORTTITLE", "ZSORTAUTHOR", "ZASSETID"), "", arrayOf(), null, null, null)
        while (bookCursor.moveToNext()) {
            val title = bookCursor.getString(0)
            val author = bookCursor.getString(1)
            val id = bookCursor.getString(2)
            Log.i("wk", "id: $id, title: $title, author: $author")
            val noteCursor = noteDB.query(noteTableName,
                    arrayOf("ZANNOTATIONLOCATION", "ZANNOTATIONSELECTEDTEXT", "ZANNOTATIONNOTE",
                            "ZANNOTATIONREPRESENTATIVETEXT", "ZFUTUREPROOFING5", "ZANNOTATIONSTYLE",
                            "ZANNOTATIONMODIFICATIONDATE"),
                    "ZANNOTATIONASSETID=?",
                    arrayOf(id),
                    null,
                    null,
                    null
            )
            while (noteCursor.moveToNext()) {
                val location = noteCursor.getString(0)
                val selectedText = noteCursor.getString(1)
                val notationNote = noteCursor.getString(2)
                val notationRepresentText = noteCursor.getString(3)
                val chapter = noteCursor.getString(4)
                val style = noteCursor.getInt(5)
                val modificationDate = noteCursor.getLong(6)
                Log.i("wk", "====")
                Log.i("wk", "location: $location")
                Log.i("wk", "selectedText：$selectedText")
                Log.i("wk", "notationNote: $notationNote") // 笔记内容
                Log.i("wk", "notationRepresentText: $notationRepresentText") // 摘录内容
                Log.i("wk", "chapter: $chapter")
                Log.i("wk", "style: $style")
                Log.i("wk", "modificationDate: ${NS_TIME_INTERVAL_SINCE_1970 + modificationDate}") // 修改时间
                Log.i("wk", "====")
            }
            noteCursor.close()
        }
        bookCursor.close()
        bookDB.close()
        noteDB.close()
    }

    fun openSQLiteDB(path: String): SQLiteDatabase {
        return SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);
    }
}

