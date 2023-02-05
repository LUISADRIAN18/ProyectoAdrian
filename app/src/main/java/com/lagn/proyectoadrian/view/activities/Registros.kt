package com.lagn.proyectoadrian.view.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.lagn.proyectoadrian.R
import com.lagn.proyectoadrian.databinding.ActivityRegistrosBinding
import com.lagn.proyectoadrian.db.DbDatos
import com.lagn.proyectoadrian.model.Regis
import com.lagn.proyectoadrian.view.adapters.RVAdapter

class Registros : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrosBinding

    private lateinit var listRegis: ArrayList<Regis>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val dbDatos =  DbDatos(this)
        listRegis = dbDatos.getDatos()


        val adapter = RVAdapter(this, listRegis)
        binding.rvRegistros.layoutManager = LinearLayoutManager(this)
        binding.rvRegistros.adapter = adapter



    }

    fun selectedDato(dato:Regis){

        val intent = Intent(this,Detalles::class.java)
        intent.putExtra("id",dato.id)
        startActivity(intent)
        finish()


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}