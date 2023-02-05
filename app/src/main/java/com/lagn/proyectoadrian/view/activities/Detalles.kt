package com.lagn.proyectoadrian.view.activities

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lagn.proyectoadrian.R
import com.lagn.proyectoadrian.databinding.ActivityDetallesBinding
import com.lagn.proyectoadrian.databinding.ListElementRvBinding
import com.lagn.proyectoadrian.db.DbDatos
import com.lagn.proyectoadrian.model.Regis

class Detalles : AppCompatActivity() {
    private lateinit var binding: ActivityDetallesBinding

    private lateinit var dbRegis: DbDatos
    var dato: Regis? = null
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetallesBinding.inflate(layoutInflater)
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
                tvFechaD.setText(it.fecha)
                tvTiempoD.setText(it.tiempo)
                tvObservacionesD.setText(it.observaciones)



            }
        }


    }



    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun click(view: View) {

        when(view.id){
            R.id.btnEdit ->{
                val intent = Intent(this,Edit::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
                finish()

            }

            R.id.btnDelete ->{

                AlertDialog.Builder(this)
                    .setTitle("Confirmación")
                    .setMessage("Realmente quieres eliminar el registro ${dato?.tiempo}??")
                    .setPositiveButton("ACEPTAR",DialogInterface.OnClickListener { dialog, which ->

                        if (dbRegis.delete(id)){
                            Toast.makeText(this,"BORRADO EXITOSAMENTE", Toast.LENGTH_SHORT).show()

                            startActivity(Intent(this,Registros::class.java))
                            finish()
                        }else Toast.makeText(this,"REGISTRO NO BORRADO!!", Toast.LENGTH_SHORT).show()


                    })
                    .setNegativeButton("CANCELAR", DialogInterface.OnClickListener { dialog, which ->
                        dialog.dismiss()

                    })
                    .show()


            }
        }


    }
}