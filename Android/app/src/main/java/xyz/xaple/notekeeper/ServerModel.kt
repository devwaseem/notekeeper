package xyz.xaple.notekeeper

/**
 * Created by waseemakram on 17/05/18.
 */


class Server {
    companion object {


         private var coreUrl = "https://c-note-keeper.herokuapp.com"
//            private var coreUrl = "http://192.168.43.145:3000"



        internal fun getMessage(query:String,sessionID:String):String{
            return "$coreUrl/ai/$query/$sessionID"
        }


        internal fun getNotes(noteName:String,dept:String):String{
            return "$coreUrl/db/getNote/$noteName/$dept"
        }


        internal fun saveNotes():String{
            return "$coreUrl/db/saveNote/"
        }

         internal  fun rateNotes(id:String,level:String):String {
             return "$coreUrl/db/rateNote/$id/$level"
         }




    }
}