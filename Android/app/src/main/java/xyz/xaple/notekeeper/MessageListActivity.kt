package xyz.xaple.notekeeper

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.stfalcon.chatkit.commons.models.IUser
import com.stfalcon.chatkit.messages.MessageInput
import com.stfalcon.chatkit.messages.MessagesListAdapter
import kotlinx.android.synthetic.main.activity_message_list.*
import java.util.*
import com.androidnetworking.error.ANError
import org.json.JSONArray
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject


class MessageListActivity : AppCompatActivity() {

    var adapter:MessagesListAdapter<Message>?=null
    val botId= "1"
    val userId= "0"
    var sessionID = Random().nextInt()
    val context = this


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_list)
        AndroidNetworking.initialize(getApplicationContext());
        adapter = MessagesListAdapter<Message>("0", null)
        messagesList.setAdapter(adapter)
        getMessageFromBot("hello")
//        startActivity(Intent(applicationContext,DownloadActivity::class.java))
        messageInputField.setInputListener {
            adapter?.addToStart(userMessage(it.toString()),true)
            getMessageFromBot(it.toString())
            true
        }
    }

//    "smalltalk.agent.boss"
    fun getMessageFromBot(query:String){
    if(!isInternetAvailable()){
        adapter?.addToStart(botMessage("Network seems to be offline \n Please check your connection and try again"),true)
        return
    }
        AndroidNetworking.get(Server.getMessage(query,sessionID.toString()))
                .build()
                .getAsJSONObject(object: JSONObjectRequestListener{
                    override fun onResponse(response: JSONObject?) {
                        Log.v("DATA",response.toString())
                        if(response != null){
                            adapter?.addToStart(botMessage(response.getString("message")),true)
                            var action = response.getString("action")
                            Handler().postDelayed({
                                when(action) {
                                    "smalltalk.agent.boss" -> {
                                        val url = "https://www.instagram.com/passionately_curious_me/?hl=en"
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        ContextCompat.startActivity(applicationContext, intent, null)
                                    }

                                    "smalltalk.greetings.bye" -> {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            finishAndRemoveTask()
                                        }
                                    }

                                    "output.getNote" -> {

                                        if(!response.getBoolean("actionIncomplete")){
                                            val downloadIntent = Intent(applicationContext,DownloadActivity::class.java)
                                            val bundle = Bundle()
                                            bundle.putString("NOTE",response.getString("note"))
                                            bundle.putString("DEPT",response.getString("department"))
                                            downloadIntent.putExtras(bundle)
                                            startActivity(downloadIntent)

                                        }
                                    }//case getNote
                                    "input.saveNotes" ->{
                                        startActivity(Intent(applicationContext,UploadNoteActivity::class.java))
                                    }
                                }
                            },2700)

                        }

                    }

                    override fun onError(anError: ANError?) {
                        Log.e("ERROR",anError.toString())
                    }
                })
    }


    fun botMessage(text:String):Message{
        return Message("${adapter?.itemCount}", Date(),Author("","Bot",botId),text)
    }


    fun userMessage(text:String):Message{
        return Message("${adapter?.itemCount}", Date(),Author("","User",userId),text)
    }
}


