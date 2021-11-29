package mx.edu.ittepic.ladm_u4_practica1_autocontestadora

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MensajesBD(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE MENSAJES(IDMENSAJE INTEGER PRIMARY KEY AUTOINCREMENT, LISTABLANCAMSJ VARCHAR(200), LISTANEGRAMSJ VARCHAR(200))")
        val datos = ContentValues()
        datos.put("LISTABLANCAMSJ","DISCULPA, TE LLAMO EN UN RATO.")
        datos.put("LISTANEGRAMSJ", "NO DEVOLVERE TU LLAMADA, POR FAVOR NO INSISTAS")

        db.insert("MENSAJES",null,datos)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}