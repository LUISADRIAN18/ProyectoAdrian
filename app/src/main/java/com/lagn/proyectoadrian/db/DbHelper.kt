package com.lagn.proyectoadrian.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

open class DbHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Registros.db"
        private const val TABLE_GAMES = "bt"


    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_GAMES(id INTEGER PRIMARY KEY AUTOINCREMENT, tiempo TEXT NOT NULL, observaciones TEXT, fecha DEFAULT CURRENT_TIMESTAMP)")

        //="CREATE TABLE CURSOS(CODIGO TEXT PRIMARY KEY, CURSO TEXT, CARRERA TEXT, DATE DEFAULT CURRENT_TIMESTAMP)";

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

        db?.execSQL("DROP TABLE $TABLE_GAMES")
        onCreate(db)


    }


}