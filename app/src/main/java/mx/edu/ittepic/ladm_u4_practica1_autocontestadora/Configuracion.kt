package mx.edu.ittepic.ladm_u4_practica1_autocontestadora

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Configuracion(
    context: Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
) : SQLiteOpenHelper(context, name, factory, version) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE ESTADOAUTOCONTESTADORA(idEstado INTEGER PRIMARY KEY AUTOINCREMENT, estado VARCHAR(200))")
        val datos = ContentValues()
        datos.put("estado","DESACTIVADO")

        db.insert("ESTADOAUTOCONTESTADORA",null,datos)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }
}