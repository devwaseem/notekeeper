package xyz.xaple.notekeeper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.*
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import kotlinx.android.synthetic.main.activity_upload_note.*
import org.json.JSONObject

class UploadNoteActivity : AppCompatActivity() {

    private val TAG = "UploadNoteActivity"
    private val context = this
    private var name = ""
    private var author = ""
    private var dept = ""
    private var tag = ""
    private var link = ""
    private var type = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_note)

        val categories = ArrayList<String>()
        categories.add("PDF")
        categories.add("DOC/DOCX")
        categories.add("PPT")


        typeField.adapter = ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,categories)
        typeField.prompt = "Select the type of the note"

        uploadButton.setOnClickListener {

            name = nameField.text.toString()
            author = authorNameField.text.toString()
            dept = deptField.text.toString()
            tag = tagField.text.toString()
            link = urlField.text.toString()

            if(!checkEmpty(name,author,dept,tag,link,type)){ //not empty
                var linkRegex = "https?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)".toRegex()
                if(!linkRegex.matches(link)){
                    showAlert(this,"","The link you entered is incorrect..","OK",{it.dismiss()},null,null)
                    return@setOnClickListener
                }


                if(Uploader.content != ""){
                    //start uploading to server
                    uploadButton.isEnabled = false
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Uploading your note...")
                    val alert = builder.create()

                    AndroidNetworking.post(Server.saveNotes())
                            .addBodyParameter("name",name)
                            .addBodyParameter("authorName",author)
                            .addBodyParameter("department",dept)
                            .addBodyParameter("url",link)
                            .addBodyParameter("tags",tag)
                            .addBodyParameter("type",type)
                            .addBodyParameter("authorID",Uploader.content)
                            .setContentType("x-www-form-urlencoded")
                            .build()
                            .getAsJSONObject(object :JSONObjectRequestListener {

                                override fun onResponse(response: JSONObject?) {
                                    if(response!!.getBoolean("status")){
                                        alert.dismiss()
                                        showAlert(context,"Upload Successful",
                                                "Your notes have been uploaded successfully\nThank you for sharing your notes, you can search this note using its tags or the name of the file itself",
                                                "ok",{
                                            finish()
                                        },null,null)
                                    }else{
                                        showAlert(context,"OOPS..",response.getString("message"),"ok",{
                                            it.dismiss()
                                        },null,null)
                                    }
                                    Log.v(TAG,response.toString())
                                }

                                override fun onError(anError: ANError?) {
                                    alert.dismiss()
                                    showAlert(context,"Oops..","Some Error occurred","ok",{
                                        it.dismiss()
                                    },null,null)
                                    Log.v(TAG,anError.toString())
                                }
                            })
                }else{
                    startActivity(Intent(context,barCodeVerifierActivity::class.java))
                }

            }else{
                showAlert(this,"Missing fields","Please fill all the fields","Ok",{it.dismiss()},null,null)
                return@setOnClickListener
            }


        }

        typeField.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
                type = ""
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, i: Int, p3: Long) {
                type = categories.get(i)
            }
        }

    }


    private fun checkEmpty(vararg fields:String):Boolean{
        return fields.any { it.trim() == "" }
    }


    override fun onResume() {
        super.onResume()
        if(Uploader.content != "") {
            verifyText.text = "Upload your note"
        }
    }

}
