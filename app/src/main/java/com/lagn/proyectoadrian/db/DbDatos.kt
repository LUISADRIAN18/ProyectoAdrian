package com.lagn.proyectoadrian.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.lagn.proyectoadrian.model.Regis

class DbDatos(private val context: Context):DbHelper(context) {

    fun insertDato(tiempo: String, observaciones: String?): Long{
        val dbHelper = DbHelper(context)
        val db = dbHelper.writableDatabase

        var id: Long = 0

        try {
            val values = ContentValues()
            values.put("tiempo", tiempo)
            values.put("observaciones", observaciones)

            id = db.insert("bt",null, values)

        }catch (e: java.lang.Exception){


        }finally {
            db.close()
        }

        return id
    }


    fun getDatos():ArrayList<Regis>{
        val dbHelper = DbHelper(context)
        val db = dbHelper.writableDatabase

        var listDatos = ArrayList<Regis>()
        var datosTmp: Regis? = null

        var cursorDatos: Cursor? = null

        cursorDatos= db.rawQuery("SELECT * FROM bt", null)

        if (cursorDatos.moveToFirst()){
            do {
                datosTmp = Regis(cursorDatos.getInt(0),cursorDatos.getString(1),cursorDatos.getString(2),cursorDatos.getString(3))
                listDatos.add(datosTmp)

            }while (cursorDatos.moveToNext())

        }

        cursorDatos.close()



        return listDatos

    }

    fun getUnDato(id: Int): Regis?{

        val dbHelper = DbHelper(context)
        val db = dbHelper.writableDatabase

        var datoId : Regis? = null

        var cursorDato: Cursor? = null

        cursorDato = db.rawQuery("SELECT * FROM bt WHERE id = $id LIMIT 1", null)

        if (cursorDato.moveToFirst()){
            datoId = Regis(cursorDato.getInt(0),cursorDato.getString(1),cursorDato.getString(2),cursorDato.getString(3))
        }

        cursorDato.close()

        return datoId


    }


    fun updateDato(id: Int,  observaciones: String):Boolean {
        var bandera = false

        val dbHelper = DbHelper(context)
        val db = dbHelper.writableDatabase

        try {

            db.execSQL("UPDATE bt SET observaciones = '$observaciones' WHERE id = $id ")
            bandera = true

        }catch (e: java.lang.Exception){

        }finally {
            db.close()
        }

        return bandera
    }

    fun delete(id: Int): Boolean{
        var bandera = false

        val dbHelper = DbHelper(context)
        val db = dbHelper.writableDatabase

        try {

            db.execSQL("DELETE FROM BT WHERE id = $id")
            bandera = true
        }catch (e: Exception){

        }finally {
            db.close()
        }


        return bandera
    }
}