package mx.edu.ittepic.ladm_u4_tarea2_contentproviders_registrollamadas

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.CallLog
import android.provider.CallLog.Calls
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {
    val siLecturaLlamadas = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siLecturaLlamadas)
        }

        button.setOnClickListener {
            RegistroLlamadas()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == siLecturaLlamadas){
            Toast.makeText(this,"Permisos otorgados",Toast.LENGTH_LONG)
                .show()
        }

    }

    private fun RegistroLlamadas() {

        var resultado = ""

        val cursorLlamadas =
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED ) {
                contentResolver.query( Calls.CONTENT_URI,null,null,null,null)
            } else { ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siLecturaLlamadas)

                return
            }

        val numero : Int = cursorLlamadas!!.getColumnIndex(Calls.NUMBER)
        val tipo : Int = cursorLlamadas.getColumnIndex(Calls.TYPE)

        while (cursorLlamadas.moveToNext()) {
            var numeroTelefono = cursorLlamadas.getString(numero)
            var tipoLlamada = cursorLlamadas.getString(tipo)
            var tipoCodigo = tipoLlamada.toInt()
            var tipoLlamadaTexto = ""
            when(tipoCodigo){
                CallLog.Calls.OUTGOING_TYPE-> tipoLlamadaTexto = "Llamada saliente"
                CallLog.Calls.INCOMING_TYPE-> tipoLlamadaTexto = "Llamada entrante"
                CallLog.Calls.MISSED_TYPE-> tipoLlamadaTexto = "Llamada perdida"
                CallLog.Calls.REJECTED_TYPE-> tipoLlamadaTexto = "Llamada rechazada"
            }
            resultado += "Numero celular : "+numeroTelefono +"\n"+
                    "Tipo de llamada: " + tipoLlamadaTexto +"\n"+
                    "-----------------------------------------------------\n"
        }
        salida.setText("Registro de Llamadas:\n"+resultado)
    }
}

