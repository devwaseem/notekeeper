package xyz.xaple.notekeeper

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import java.net.InetAddress

/**
 * Created by waseemakram on 13/05/18.
 */



data class Note(

        var name:String,
        var author:String,
        var rrn:String,
        var rating:String,
        var type: noteType,
        var url:String,
        var id:String

)

enum class noteType {
    pdf,ppt,doc
}

//singleton used for uplaoding
class Uploader private constructor() {

    companion object {
        var content:String = ""

        private var instance:Uploader? = null

        internal fun getInstance():Uploader{
            if(instance == null){
                instance = Uploader()
                return instance!!
            }
            return instance!!
        }

    }

}

//fun isInternetAvailable(): Boolean {
//    try {
//        val ipAddr = InetAddress.getByName("https://google.co.in")
//        //You can replace it with your name
//        return !ipAddr.equals("")
//
//    } catch (e: Exception) {
//        return false
//    }
//
//}

fun isInternetAvailable(): Boolean {
    try{
        val command = "ping -c 1 google.com"
        return Runtime.getRuntime().exec(command).waitFor() == 0
    }catch (e:Exception){
        return false
    }

}


fun showAlert(context: Context, title:String, message: String, positiveText:String?,
              positiveUnit:(((DialogInterface)->Unit)?),
              negativeText:String?,
              negativeUnit:(((DialogInterface)->Unit)?)){

    var builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    if(positiveText != null)
        builder.setPositiveButton(positiveText, DialogInterface.OnClickListener { dialog, i ->
            if(positiveUnit != null) positiveUnit(dialog)
        })
    if(negativeText != null)
        builder.setNegativeButton(negativeText, DialogInterface.OnClickListener { dialog, i ->
            if(negativeUnit != null) negativeUnit(dialog)
        })
    builder.create().show()
}



