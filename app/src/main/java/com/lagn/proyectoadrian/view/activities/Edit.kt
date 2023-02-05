package com.lagn.proyectoadrian.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.lagn.proyectoadrian.R
import com.lagn.proyectoadrian.databinding.ActivityDetallesBinding
import com.lagn.proyectoadrian.databinding.ActivityEditBinding
import com.lagn.proyectoadrian.db.DbDatos
import com.lagn.proyectoadrian.model.Regis

class Edit : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    private lateinit var dbRegis: DbDatos
    var dato: Regis? = null
    var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val bundle = intent.extras
        if (bundle!=null){
            id = bundle.getInt("id",0)
        }

        dbRegis = DbDatos(this)
        dato =dbRegis.getUnDato(id)

        dato?.let {

            with(binding){
                tvFechaE.setText(it.fecha)
                tvTiempoE.setText(it.tiempo)
                tvObservacionesE.setText(it.observaciones)



            }
        }


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun click(view: View) {

        if (dbRegis.updateDato(id,binding.tvObservacionesE.text.toString())){
            Toast.makeText(this@Edit, "REGISTRO ACTUALIZADO EXITOSAMENTE", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@Edit,Detalles::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
            finish()


        }else Toast.makeText(this, "ERROR AL ACTUALIZAR", Toast.LENGTH_SHORT).show()

    }
}