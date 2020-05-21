package mx.edu.ittepic.ladm_u4_tarea2_edgarramirez

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var contenido : Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_SMS),1)
        }

        btnRegistroLlamadas.setOnClickListener {
            registroLlamadas()
        }

        btnMensajesRecibidos.setOnClickListener {
            recibidoSMS()
        }

    }//onCreate

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 1){

            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG), 2)
                contenido = CallLog.Calls.CONTENT_URI
            }
        }
    }//onRequestPermissionsResult

    /*----------------------------------------------------REGISTRO LLAMADAS---------------------------------------------------------------------*/
    private fun registroLlamadas() {
        contenido = CallLog.Calls.CONTENT_URI
        var cursor = contentResolver.query(contenido!!,null,null,null,null)

        var llamadas = ""

        if(cursor!!.moveToLast()){

            llamadas += "\nNOMBRE DE CONTACTO: "+cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))+
                    "\nDURACION DE LLAMADA: "+cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))+
            "\n-----------------------------------------------------------------------------------------\n"

            while(cursor.moveToPrevious()){

                //DURACION EN SEGUNDOS
                llamadas += "\nNOMBRE DE CONTACTO: "+cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))+
                        "\nDURACION DE LLAMADA: "+cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION))+
                "\n-----------------------------------------------------------------------------------------\n"
            }
        }
        else{
            llamadas = "NO SE ENCONTRO UN REGISTRO DE LLAMADAS"
        }
        txtContenido.setText(llamadas)

    }//registroLlamadas

    /*------------------------------------------------------RECIBIDOS SMS ----------------------------------------------------------------------*/
    private fun recibidoSMS() {
        /*sent - mensajes enviados
          inbox - mensajes recibidos
          draft - borradores
        */
        var cursor = contentResolver.query(Uri.parse("content://sms/inbox"),null,null,null,null)

        var mensajes = ""

        if(cursor!!.moveToLast()){
            do{
                mensajes += "\nMENSAJE: "+cursor.getString(cursor.getColumnIndex("body")) +
                        "\n-----------------------------------------------------------------------------------------\n"
            }while(cursor.moveToPrevious())

        }
        else{
            mensajes = "NO EXITEN MENSAJES RECIBIDOS"
        }
        txtContenido.setText(mensajes)
    }//recibidoSMS


}//class
