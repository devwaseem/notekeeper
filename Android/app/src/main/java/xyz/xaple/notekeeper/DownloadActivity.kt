package xyz.xaple.notekeeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_download.*
import org.json.JSONArray
import org.json.JSONObject

class DownloadActivity : AppCompatActivity() {

    val adapter = DownloadListAdapter()

    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        downloadList.layoutManager = LinearLayoutManager(applicationContext)

        downloadList.adapter = adapter
        val bundle = intent.extras
        getNotes(bundle.getString("NOTE"),bundle.getString("DEPT"))
//        adapter.notes.add(Note("Maths and science and institute 1","Waseem Akram","160071601153","1","3.5",noteType.doc))
//        adapter.notes.add(Note("Maths 2","Waseem Akram","160071601153","2","4.5",noteType.pdf))
//        adapter.notes.add(Note("Maths 3","Waseem Akram","160071601153","3","5.0",noteType.ppt))




    }


    fun getNotes(note:String,dept:String){
        var alertBuilder = AlertDialog.Builder(this)
        alertBuilder.setMessage("Loading...")
        alertBuilder.setCancelable(false)
        var alert = alertBuilder.create()
        alert.show()
        val notes = ArrayList<Note>()
        AndroidNetworking.get( Server.getNotes(note,dept))
                .build()
                .getAsJSONArray(object: JSONArrayRequestListener{
                    override fun onResponse(response: JSONArray?) {
                        Log.v("DOWNLOAD","$note,$dept,${response!!}")
                        for(i in 0 until response!!.length()){
                            val jsonObject = response.getJSONObject(i)
                            val authorObject = jsonObject.getJSONObject("author")
                            val authorName = authorObject.getString("name")
                            val rrn = authorObject.getString("id")
                            val rating = jsonObject.getJSONObject("rating").getString("actual").toFloat()
                            val noteId = jsonObject.getString("_id")
                            val name = jsonObject.getString("name")
                            val dept = jsonObject.getString("department")
                            val url = jsonObject.getString("url")
                            var type = noteType.pdf
                            when ( jsonObject.getString("type") ){
                                "PDF" -> type = noteType.pdf
                                "PPT" -> type = noteType.ppt
                                "DOC/DOCX" -> type = noteType.doc
                            }
                            val note = Note(name,authorName,rrn,"%.1f".format(rating),type,url,noteId)
                            notes.add(note)
                        }
                        adapter.notes = notes
                        adapter.notifyDataSetChanged()
                        alert.dismiss()
                        if(notes.isEmpty()){
                            showAlert(context,"Notes not found","Please search the note again using different name (ex: m4, maths 4....)",
                                    "OK",{
                                it.dismiss()
                                finish()
                            },null,null)
                        }else{

                        }
                    }

                    override fun onError(anError: ANError?) {
                        alert.dismiss()
                        showAlert(context,"Oops..","Some error occured please try again later",
                                "OK",{
                            it.dismiss()
                            finish()
                        },null,null)
                        Log.e("ERROR",anError.toString())
                    }
                })
    }

}
