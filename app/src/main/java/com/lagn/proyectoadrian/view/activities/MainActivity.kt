package com.lagn.proyectoadrian.view.activities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.lagn.proyectoadrian.R
import com.lagn.proyectoadrian.db.DbDatos
import java.io.IOException
import java.util.UUID
import java.util.concurrent.TimeUnit


const val REQUEST_ENABLE_BT = 1

class MainActivity : AppCompatActivity() {

    private var mHandler: Handler? = null
    private var timeInSeconds: Long = 0
    private  var btnID: Int = 0
    private  var verificar: Boolean = false
    private lateinit var btn30s: Button
    private lateinit var btn1min: Button
    private lateinit var btnDetener: Button


    lateinit var mBtAdapter: BluetoothAdapter
    var mAddressDevices: ArrayAdapter<String>? = null
    var mNameDevices: ArrayAdapter<String>? = null

    companion object {

        var m_myUUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private var m_bluetoothSocket: BluetoothSocket? = null

        var m_isConnected: Boolean = false
        lateinit var m_address: String
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        /*val dbHelper = DbHelper(this)
        val db = dbHelper.writableDatabase
        if (db!= null){
            Toast.makeText(this,"La bd fué creadaexitosamente", Toast.LENGTH_LONG).show()
        }else  Toast.makeText(this,"error al crear bd", Toast.LENGTH_LONG).show()*/

        mAddressDevices = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        mNameDevices = ArrayAdapter(this, android.R.layout.simple_list_item_1)

        val btActivar = findViewById<Button>(R.id.activarBt)
        val btApagar = findViewById<Button>(R.id.desconectar_bt)
        val btDispositivos = findViewById<Button>(R.id.selec_bt)
        val btConectar = findViewById<Button>(R.id.conectar_bt)
        btn30s = findViewById(R.id.btn30s)
        btn1min = findViewById(R.id.btn1min)
        btnDetener = findViewById(R.id.btnDetener)
        val spinDisp = findViewById<Spinner>(R.id.listaDvices)
        val status =  findViewById<TextView>(R.id.status)

        inits()



        //--------------------------
        val someActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()

        ) { result ->
            if (result.resultCode == REQUEST_ENABLE_BT) {

                Log.i("MainActivity", "Actividad Registrada")
            }


        }

        //inicializar bt adapter
        mBtAdapter = (getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter

        //encendido o apagado
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth no está disponible", Toast.LENGTH_SHORT).show()
        } else Toast.makeText(this, "Bluetooth está disponible!!!!", Toast.LENGTH_SHORT).show()

        btActivar.setOnClickListener {
            if (mBtAdapter.isEnabled) {
                Toast.makeText(this, "Bluetooth ya se encuantra activado", Toast.LENGTH_SHORT)
                    .show()
            } else {
                //Encender Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this, android.Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED

                ) {
                    Log.i("MainActivity", "ActivityCompat#requestPermissions")
                }
                someActivityResultLauncher.launch(enableBtIntent)
            }
        }
        //Apagar bluetooth
        btApagar.setOnClickListener {
            if (!mBtAdapter.isEnabled){
                Toast.makeText(this, "Bluetooth ya se encuentra desactivado", Toast.LENGTH_SHORT).show()

            }else{
                mBtAdapter.disable()
                Toast.makeText(this, "Bluetooth se ha desactivado", Toast.LENGTH_SHORT).show()
                status.text = "DESCONECTADO"

            }


        }

        //Dispositivos emparejados
        btDispositivos.setOnClickListener {
            if (mBtAdapter.isEnabled) {

                val pairedDevices: Set<BluetoothDevice>? = mBtAdapter?.bondedDevices
                mAddressDevices!!.clear()
                mNameDevices!!.clear()

                pairedDevices?.forEach { device ->
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    mAddressDevices!!.add(deviceHardwareAddress)
                    //........... EN ESTE PUNTO GUARDO LOS NOMBRE A MOSTRARSE EN EL COMBO BOX
                    mNameDevices!!.add(deviceName)
                }

                //ACTUALIZO LOS DISPOSITIVOS
                spinDisp.adapter = mNameDevices
            } else {
                val noDevices = "Ningun dispositivo pudo ser emparejado"
                mAddressDevices!!.add(noDevices)
                mNameDevices!!.add(noDevices)
                Toast.makeText(this, "Primero vincule un dispositivo bluetooth", Toast.LENGTH_LONG).show()
            }


        }

        //Conectar
        btConectar.setOnClickListener {
            try {
                if (m_bluetoothSocket== null || !m_isConnected){
                    val IntValSpin = spinDisp.selectedItemPosition
                    m_address = mAddressDevices!!.getItem(IntValSpin).toString()
                    Toast.makeText(this, m_address, Toast.LENGTH_SHORT).show()
                    //cancel
                    mBtAdapter?.cancelDiscovery()
                    val device: BluetoothDevice = mBtAdapter.getRemoteDevice(m_address)
                    m_bluetoothSocket = device.createInsecureRfcommSocketToServiceRecord(m_myUUID)
                    m_bluetoothSocket!!.connect()
                    ac1min()
                    ac30s()




                }
                Toast.makeText(this,"CONEXION EXITOSA", Toast.LENGTH_SHORT).show()
                status.text = "CONECTADO"
                status.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorA))

                Log.i("MainActivity","conexion exitosa")
            }catch (e: IOException){
                e.printStackTrace()
                Toast.makeText(this,"ERROR DE CONEXION ", Toast.LENGTH_SHORT).show()
                status.text = "NO SE PUDO CONECTAR REVISE DISPOSITIVOS"
                status.setTextColor(Color.RED)
                Log.i("MainActivity", "ERROR de conexion")
            }
        }

        initToolBar()

        btn30s.setOnClickListener {
            verificar = true
            val dbDatos= DbDatos(this)
            val id = dbDatos.insertDato("30s",null)
            if (id>0){
                Toast.makeText(this,"REGISTRO GUARDADO EXITOSAMENTE", Toast.LENGTH_SHORT).show()

            }else Toast.makeText(this,"Error en el registro", Toast.LENGTH_SHORT).show()

            acDetener()
            inAc1min()
            inAc30s()
            sendCommand("1")
            btnID = 1
            startChronometer()



        }

        btn1min.setOnClickListener {
            verificar = true
            val dbDatos= DbDatos(this)
            val id = dbDatos.insertDato("1min",null)
            if (id>0){
                Toast.makeText(this,"REGISTRO GUARDADO EXITOSAMENTE", Toast.LENGTH_SHORT).show()

            }else Toast.makeText(this,"Error en el registro", Toast.LENGTH_SHORT).show()


            inAc30s()
            inAc1min()
            acDetener()
            sendCommand("1")
            btnID = 2
            startChronometer()

        }
        btnDetener.setOnClickListener {
            var tvTimeDate = findViewById<TextView>(R.id.tvTimer)
            tvTimeDate.setTextColor(ContextCompat.getColor(this, R.color.black))
            verificar = false
            sendCommand("0")
            resetTime()
            ac1min()
            ac30s()
            inAcDetener()
        }



    }

    private fun sendCommand(input:String){
        if( m_bluetoothSocket != null){
            try {
                m_bluetoothSocket!!.outputStream.write(input.toByteArray())
            }catch (e: IOException){
                e.printStackTrace()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        var id = item.itemId


        if (id == R.id.menu_ayuda) {
            startActivity(Intent(this,Ayuda::class.java))

        }
        if (id == R.id.menu_registros) {
            startActivity(Intent(this,Registros::class.java))

        }

        return super.onOptionsItemSelected(item)
    }

    private fun initToolBar() {

        var toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)


    }

    private fun resetTime(){
        mHandler?.removeCallbacks(chronometer)
        timeInSeconds = 0

        var tvTimeDate = findViewById<TextView>(R.id.tvTimer)
        tvTimeDate.text = "00:00:00"



    }
    private fun startTime(){
        mHandler = Handler(Looper.getMainLooper())
        chronometer.run()
        var tvTimeDate = findViewById<TextView>(R.id.tvTimer)
        tvTimeDate.setTextColor(ContextCompat.getColor(this, R.color.colorStart))


    }

    private var chronometer: Runnable = object: Runnable{
        override fun run() {
            try {

                if( verificar){
                timeInSeconds++
                updateWathc(timeInSeconds)
                when  {
                    btnID == 1  ->  {
                        if (timeInSeconds == 30L) {
                            resetTime()
                            verificar = false
                            sendCommand("0")
                        }

                    }
                    btnID == 2  ->  {
                        if (timeInSeconds == 60L) {
                            resetTime()
                            verificar = false
                            sendCommand("0")
                        }

                    }

                }
                }

            }finally {
                mHandler!!.postDelayed(this,1000L)

            }
        }

    }

    private fun updateWathc(timeInSeconds: Long){
        val timeTex = formattedWatch((timeInSeconds*1000))
        var tvTimeDate = findViewById<TextView>(R.id.tvTimer)
        tvTimeDate.text = timeTex

    }

    private fun formattedWatch(ms: Long): String{
        var miliSeconds = ms
        val minutes = TimeUnit.MILLISECONDS.toMinutes(miliSeconds)
        miliSeconds -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(miliSeconds)

        return "${if (minutes <10) "0" else ""}$minutes: " +
                "${if (seconds <10)  "0" else ""}$seconds"

    }



    private fun startChronometer(){
        resetTime()
        startTime()

    }

    private fun inits(){
        inAc30s()
        inAc1min()
        inAcDetener()








    }

    private fun inAc30s(){
        btn30s.isEnabled = false
        btn30s.setBackgroundResource(R.drawable.inactive)

    } private fun ac30s(){

        btn30s.isEnabled = true
        btn30s.setBackgroundResource(R.drawable.gradient)

    }
    private fun inAc1min(){
        btn1min.isEnabled = false
        btn1min.setBackgroundResource(R.drawable.inactive)


    }
    private fun ac1min(){
        btn1min.isEnabled = true
        btn1min.setBackgroundResource(R.drawable.gradient)


    }

    private fun inAcDetener(){
        btnDetener.isEnabled = false
        btnDetener.isVisible = false


    }
    private fun acDetener(){
        btnDetener.isEnabled = true
        btnDetener.isVisible = true


    }









}