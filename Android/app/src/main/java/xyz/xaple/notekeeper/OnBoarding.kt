package xyz.xaple.notekeeper

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_on_boarding.*


class OnBoarding : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        supportActionBar?.hide()
        Handler().postDelayed({
            if(isInternetAvailable()){
                finish()
                startActivity(Intent(applicationContext,MessageListActivity::class.java))
            }else{
                showAlert(this,"Network Problem",
                        "Network seems to be offline, Please check your connection and try again",
                        "ok",{
                    it.dismiss()
                    finish()
                    System.exit(0)
                },null,null)
            }
        },2700)
    }
}
