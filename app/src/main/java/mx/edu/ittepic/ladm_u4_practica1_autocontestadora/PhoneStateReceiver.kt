package mx.edu.ittepic.ladm_u4_practica1_autocontestadora

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main2.*

private var telephonyState = false

class PhoneStateReceiver : BroadcastReceiver() {
    val db = FirebaseFirestore.getInstance()
    var listaBlanca = ArrayList<String>()
    var listaNegra = ArrayList<String>()

    override fun onReceive(context: Context?, intent: Intent?) {

            if(intent!!.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_RINGING){
                telephonyState = true
            }

            if(intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_OFFHOOK){
                telephonyState = false
            }

            if(telephonyState && intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE){
                val numero = intent.extras?.getString("incoming_number")
                db.collection("CONTACTOS").addSnapshotListener { value, error ->
                    if(error !=null)
                    {
                        return@addSnapshotListener
                    }
                    listaNegra.clear()
                    listaBlanca.clear()

                    for (i in value!!){
                        val num = i.get("telefono")
                        if(i.getString("contactoDeseado")!!.equals("true")){
                            listaBlanca.add(num.toString())
                        }else{
                            listaNegra.add(num.toString())
                        }
                    }


                    if(listaBlanca.contains(numero.toString())){
                        val SMS = SmsManager.getDefault()
                        Toast.makeText(context, "ENVIANDO MENSAJE...",Toast.LENGTH_LONG).show()
                        SMS.sendTextMessage(numero,null,"DISCULPA, TE LLAMO EN UN RATO.",null,null)
                        Toast.makeText(context, "¡MENSAJE ENVIADO!",Toast.LENGTH_LONG).show()
                        registrarLlamada(context!!, numero.toString(), "DISCULPA, TE LLAMO EN UN RATO.","CONTACTO DESEADO")
                    }

                    if(listaNegra.contains(numero.toString())){
                        val SMS = SmsManager.getDefault()
                        SMS.sendTextMessage(numero,null,"NO DEVOLVERE TU LLAMADA, POR FAVOR NO INSISTAS",null,null)
                        //Toast.makeText(context, "ENTRANDO: ${numero}",Toast.LENGTH_LONG).show()
                        registrarLlamada(context!!, numero.toString(), "NO DEVOLVERE TU LLAMADA, POR FAVOR NO INSISTAS","CONTACTO NO DESEADO")
                    }
                }
            }

    }

    private fun registrarLlamada(cont: Context, numero: String, mensaje: String, contactoDeseado: String){
        var llamada: MutableMap<String, String> = HashMap()
        llamada.put("numero",numero)
        llamada.put("mensaje",mensaje)
        llamada.put("contactoDeseado", contactoDeseado)

        db.collection("LISTALLAMADAS").document()
            .set(llamada)
            .addOnSuccessListener {
                Toast.makeText(cont, "LLAMADA REGISTRADA",Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(cont, it.message,Toast.LENGTH_LONG).show()
            }
    }

}