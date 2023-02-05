package com.lagn.proyectoadrian.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.lagn.proyectoadrian.R
import com.lagn.proyectoadrian.databinding.ListElementRvBinding
import com.lagn.proyectoadrian.model.Regis
import com.lagn.proyectoadrian.view.activities.MainActivity
import com.lagn.proyectoadrian.view.activities.Registros

class RVAdapter(private val context: Context, val datos: ArrayList<Regis>): RecyclerView.Adapter<RVAdapter.MyViewHolder>(){

    private val layoutInflater = LayoutInflater.from(context)

    class MyViewHolder(view: ListElementRvBinding): RecyclerView.ViewHolder(view.root){
        val tvTiempo = view.tvTiempo
        val tvObservaciones= view.tvObservaciones
        val tvFecha = view.tvFecha

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListElementRvBinding.inflate(layoutInflater)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvTiempo.text = datos[position].tiempo
        holder.tvObservaciones.text = datos[position].observaciones
        holder.tvFecha.text = datos[position].fecha


        //Para los clicks de cada elemento viewholder

        holder.itemView.setOnClickListener {
            //Manejar el click
            if(context is Registros) context.selectedDato(datos[position])
        }

    }

    override fun getItemCount(): Int {
        return datos.size
    }
}