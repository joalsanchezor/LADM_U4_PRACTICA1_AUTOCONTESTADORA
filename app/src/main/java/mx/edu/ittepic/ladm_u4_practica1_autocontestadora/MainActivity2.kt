package mx.edu.ittepic.ladm_u4_practica1_autocontestadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        mostrarContactos()
        button4.setOnClickListener {
            guardarContacto()
        }
    }

    private fun guardarContacto() {
        var contacto: MutableMap<String, String> = HashMap()
        contacto.put("nombre",nombreEd.text.toString())
        contacto.put("telefono",telefonoEd.text.toString())
        contacto.put("contactoDeseado",radioButton3.isChecked.toString())

        db.collection("CONTACTOS").document()
            .set(contacto)
            .addOnSuccessListener {
                Toast.makeText(this, "REGISTRADO",Toast.LENGTH_LONG).show()
                nombreEd.setText("")
                telefonoEd.setText("")
                mostrarContactos()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message,Toast.LENGTH_LONG).show()
            }
    }

    private fun consulta() : ArrayList<String>{
        val resultadoConsulta = ArrayList<String>()

        db.collection("CONTACTOS").addSnapshotListener { value, error ->
            if(error != null){
                return@addSnapshotListener
            }
            resultadoConsulta.clear()
            value!!.documents
            for (i in value){
                val contacto = "NOMBRE: ${i.getString("nombre")}\nTELÉFONO: ${i.getString("telefono")}\nCONTACTO DESEADO: ${i.getString("contactoDeseado")}"
                resultadoConsulta.add(contacto)
            }
            if (resultadoConsulta.isEmpty()){
                val respuesta = "SIN CONTACTOS"
                resultadoConsulta.add(respuesta)
            }
        }

        return resultadoConsulta
    }



    private fun mostrarContactos(){
        val resultado = consulta()
        listaContactos.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, resultado)
    }
}
