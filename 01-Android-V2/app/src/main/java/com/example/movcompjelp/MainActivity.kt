package com.example.movcompjelp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    val contenidoIntentExplicito = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == RESULT_OK){
            if(result.data != null) {
                val data = result.data
                Log.i("intent-epn", "${data?.getStringExtra("nombreModificado")}")
            }
        }
    }

    val contenidoIntentImplicito = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if(result.resultCode == RESULT_OK) {
            if (result.data != null){
                if(result.data!!.data != null){
                    val uri: Uri = result.data!!.data!!
                    val cursor = contentResolver.query(
                        uri,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                    cursor?.moveToFirst()
                    val indiceTelefono = cursor?.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                    val telefono = cursor?.getString(
                        indiceTelefono!!
                    )
                    cursor?.close()
                    Log.i("intent-epn", "Telefono ${telefono}")
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botonCicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        botonCicloVida.setOnClickListener{
            irActividad(ACicloVida::class.java)
        }

        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonListView.setOnClickListener{
            irActividad(BListView::class.java)
        }

        val botonIntentImplicito = findViewById<Button>(R.id.btn_intent_implicito)
        botonIntentImplicito.setOnClickListener{
            val intentConRespuesta = Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            )
            contenidoIntentImplicito.launch(intentConRespuesta)
        }

        val botonIntent = findViewById<Button>(R.id.btn_intent)
        botonIntent.setOnClickListener{
            abrirActividadConParametros(CIntentExplicitoParametros::class.java)
        }

        val botonSQL = findViewById<Button>(R.id.btn_sqlite)
        botonSQL.setOnClickListener{
            abrirActividadConParametros(ECrudEntrenador::class.java)
        }

        val botonRView = findViewById<Button>(R.id.btn_recycler_view)
        botonRView.setOnClickListener{
            abrirActividadConParametros(GRecyclerView::class.java)
        }

        val botonGmaps = findViewById<Button>(R.id.btn_google_maps)
        botonGmaps.setOnClickListener {
            abrirActividadConParametros(HGoogleMaps::class.java)
        }

        val botonAuth = findViewById<Button>(R.id.btn_intent_firebase_ui)
        botonAuth.setOnClickListener {
            irActividad(IFirebaseUIauth::class.java)
        }
        val botonFirebase = findViewById<Button>(R.id.btn_intent_firestore)
        botonFirebase.setOnClickListener {
            irActividad(JFirebaseFirestore::class.java)
        }
    }

    fun irActividad(clase: Class<*>){
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    fun abrirActividadConParametros(
        clase:Class<*>
    ){
        val intentExplicito = Intent(this, clase)

        intentExplicito.putExtra("nombre", "Adrian")
        intentExplicito.putExtra("apellido", "Eguez")
        intentExplicito.putExtra("edad", 32)
        intentExplicito.putExtra("entrenador", BEntrenador(1, "ash", "pueblo paleta"))
        contenidoIntentExplicito.launch(intentExplicito)

    }


}