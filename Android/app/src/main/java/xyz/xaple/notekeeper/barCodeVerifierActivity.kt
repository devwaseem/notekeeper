package xyz.xaple.notekeeper

import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import me.dm7.barcodescanner.zbar.Result
import me.dm7.barcodescanner.zbar.ZBarScannerView

class barCodeVerifierActivity : AppCompatActivity(),ZBarScannerView.ResultHandler {


    var mScannerView: ZBarScannerView? = null
    val TAG = "barCodeVerifierActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScannerView =  ZBarScannerView(this)
        setContentView(mScannerView)

        mScannerView?.setAutoFocus(true)
        mScannerView?.flash = true

    }


    override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this)
        mScannerView?.startCamera()
    }

    override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()
    }

    override fun handleResult(res: Result?) {
        if(res == null) return
        Log.v(TAG, res.contents)
        Log.v(TAG, res.barcodeFormat.name)

        var code = res.barcodeFormat.name
        var content = res.contents
        if(code == "CODE39"){
            Uploader.content = content
            showAlert(this,"","Verification successful","Ok",{ alert ->
                alert.dismiss()
                finish()
            },null,null)
        }else{
            Uploader.content = ""
            showAlert(this,"Verification Error",
                    "Please scan the Barcode that is in the back your ID card",
                    "OK",{ alert ->
                alert.dismiss()
                mScannerView?.resumeCameraPreview(this)
            }, "Cancel",{ alert->
                    alert.dismiss()
                    finish()
            })
        }



    }
}


