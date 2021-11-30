package mx.edu.ittepic.ladm_u4_practica1_autocontestadora

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    var listaLlamadasPerdidas = ArrayList<String>()
    var desactivadaContestadora = true
    var activadaAutocontestadora = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), 369)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, Array(1) { Manifest.permission.READ_CALL_LOG }, 101)
        }

        cargarLlamadasPerdidas()
        button.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            if(desactivadaContestadora == true){
                desactivadaContestadora = false
                activadaAutocontestadora = true
                button2.setText("AUTOCONTESTADORA ACTIVADA")
                Toast.makeText(this, "SE HA ACTIVADO LA AUTOCONTESTADORA", Toast.LENGTH_SHORT).show()
            }else{
                desactivadaContestadora = true
                activadaAutocontestadora = false
                button2.setText("ACTIVAR AUTOCONTESTADORA")
                Toast.makeText(this, "SE HA DESACTIVADO LA AUTOCONTESTADORA", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarLlamadasPerdidas() {
        listaLlamadasPerdidas.clear()
        db.collection("LISTALLAMADAS").addSnapshotListener { value, error ->
            if(error != null){
                return@addSnapshotListener
            }
            value!!.documents
            for (i in value){
                val llamada = "NÚMERO: ${i.getString("numero")}\nTIPO DE CONTACTO: ${i.getString("contactoDeseado")}"
                listaLlamadasPerdidas.add(llamada.toString())
            }
            if (listaLlamadasPerdidas.isEmpty()){
                val respuesta = "SIN LLAMADAS PERDIDAS"
                listaLlamadasPerdidas.add(respuesta)
            }

            listViewLlamadasPerdidas.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaLlamadasPerdidas)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

        }
    }
}