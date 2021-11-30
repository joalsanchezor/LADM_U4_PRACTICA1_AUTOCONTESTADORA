package mx.edu.ittepic.ladm_u4_practica1_autocontestadora

import android.Manifest
import android.content.ContentValues
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
    public var listaLlamadasPerdidas = ArrayList<String>()

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

        //cargarLlamadasPerdidas()
        button.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }

        button2.setOnClickListener {
            cargarLlamadasPerdidas()

        /*if(consulta().equals("DESACTIVADO")){
                actualizar("ACTIVADO")
                button2.setText("AUTOCONTESTADORA ACTIVADA")
                Toast.makeText(this, "AUTOCONTESTADORA ACTIVADA", Toast.LENGTH_SHORT).show()
            }else{
                actualizar("DESACTIVADO")
                button2.setText("ACTIVAR AUTOCONTESTADORA")
                Toast.makeText(this, "AUTOCONTESTADORA DESACTIVADA", Toast.LENGTH_SHORT).show()
            }*/
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

    fun actualizar(estado : String){
        var configuracion = Configuracion(this, "CONFIGURACION",null, 1).writableDatabase
        val dato = ContentValues()

        dato.put("estado",estado)

        val resultado = configuracion.update("ESTADOAUTOCONTESTADORA", dato, "idEstado=?", arrayOf("1"))
    }

    public fun consulta() : String{
        var configuracion = Configuracion(this, "CONFIGURACION",null, 1).readableDatabase
        val cursor = configuracion.query("ESTADOAUTOCONTESTADORA", arrayOf("*"), "idEstado=?", arrayOf("1"), null, null, null)

        var resultado = ""

        if(cursor.moveToFirst()){
            resultado = cursor.getString(1)
        }
        return resultado
    }

}