package xyz.xaple.notekeeper

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.hsalf.smilerating.SmileRating
import org.json.JSONObject


/**
 * Created by waseemakram on 13/05/18.
 */

class DownloadListAdapter:RecyclerView.Adapter<DownloadListAdapter.DownloadListViewHolder>(){



    var notes = ArrayList<Note>()

    var context:Context?=null

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DownloadListViewHolder?, position: Int) {
        val note = notes.get(position)
        if(holder != null){
            holder.noteName!!.text = note.name
            holder.noteAuthor!!.text = "By ${note.author}"
            holder.noteRRN!!.text = note.rrn
            holder.noteRating!!.text = note.rating


            when(note.type){

                noteType.pdf -> holder.noteImage!!.setImageResource(R.drawable.pdf)
                noteType.ppt -> holder.noteImage!!.setImageResource(R.drawable.ppt)
                noteType.doc -> holder.noteImage!!.setImageResource(R.drawable.word)

            }

            holder.downloadHolder?.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Do you want to Download this file")
                builder.setPositiveButton("Yes", { dialogInterface, i ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(note.url))
                    startActivity(context,intent,null)
                    //show ratings
                    Handler().postDelayed( {
                        // rate the file
                        val view = LayoutInflater.from(context).inflate(R.layout.smileyrate,null)
                        val builder = AlertDialog.Builder(context)
                        builder.setView(view)
                        builder.setTitle("Please Rate the file")
                        var alert = builder.create()
                        alert.show()
                        val smileRating = view.findViewById<SmileRating>(R.id.smile_rating)
                         smileRating.setOnRatingSelectedListener { level , reselected ->

                                 AndroidNetworking.get(Server.rateNotes(note.id,level.toString())).build()
                                         .getAsJSONObject(object:JSONObjectRequestListener {
                                             override fun onResponse(response: JSONObject?) {
                                                 if(response!!.getBoolean("status")){
                                                     alert.dismiss()
                                                     showAlert(context!!,"","Thanks for Rating the file","ok",{
                                                         it.dismiss()
                                                     },null,null)
                                                 }else{
                                                     showAlert(context!!,"Oops..","Some error occured while rating\nPlease try again later", "ok",{
                                                         it.dismiss()
                                                     },null,null)
                                                 }
                                                 Log.v("Rating",response.toString())
                                             }

                                             override fun onError(anError: ANError?) {
                                                 alert.dismiss()
                                                 showAlert(context!!,"Oops..","Some Error occurred","ok",{
                                                     it.dismiss()
                                                 },null,null)
                                                 Log.v("Rating",anError.toString())
                                             }
                                         })
                         }

                    },2000)

                })


                builder.setNegativeButton("Cancel",{ dialogInterface,i ->
                    dialogInterface.dismiss()
                })

                builder.create().show()

            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DownloadListViewHolder {
        val itemView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.download_list_rows, parent, false)
        context = parent?.context

        return DownloadListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return notes.count()
    }

    class DownloadListViewHolder(v:View):RecyclerView.ViewHolder(v) {

        var noteImage:ImageView? = null
        var noteName:TextView? = null
        var noteAuthor:TextView? = null
        var noteRRN:TextView? = null
        var noteRating:TextView? = null
        var downloadHolder:CardView?=null

        init {
            noteImage = v.findViewById<ImageView>(R.id.noteImage)
            noteName = v.findViewById<TextView>(R.id.noteName)
            noteAuthor = v.findViewById<TextView>(R.id.noteAuthor)
            noteRRN = v.findViewById<TextView>(R.id.noteRRN)
            noteRating = v.findViewById<TextView>(R.id.noteRating)
            downloadHolder = v.findViewById<CardView>(R.id.downloadHolder)
        }




    }

}



