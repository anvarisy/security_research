package tech.anvarisy.research


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Process.SYSTEM_UID
import android.provider.CallLog
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private lateinit var phoneView: TextView
    private lateinit var buildView: TextView
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buildView = findViewById(R.id.tvModel)
        buildView.text = "Model: ${Build.MODEL}\n" +
                "Board:\t ${Build.BOARD}\n" +
                "Manufacture:\t ${Build.MANUFACTURER}\n" +
                "Bootloader:\t ${Build.BOOTLOADER}\n" +
                "User:\t ${Build.USER}\n" +
                "Fingerprint:\t ${Build.FINGERPRINT}\n" +
                "Brand:\t ${Build.BRAND}\n" +
                "Hardware:\t ${Build.HARDWARE}\n" +
                "Device:\t ${Build.DEVICE}"
        val tel =  getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        phoneView = findViewById(R.id.tvPhone)
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_CALL_LOG
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 102)
        }
        phoneView.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_SMS
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_NUMBERS
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            }
            try{
                phoneView.text = tel.simSerialNumber
            }catch (e: java.lang.Exception){
                phoneView.text = e.message
            }
        }
    }
    fun readCallLog() {
        val cursor = contentResolver.query(CallLog.Calls.CONTENT_URI,null, null, null, CallLog.Calls.DATE + " DESC")
        val number = cursor?.getColumnIndex(CallLog.Calls.NUMBER)
        val date = cursor?.getColumnIndex(CallLog.Calls.DATE)
        val type = cursor?.getColumnIndex(CallLog.Calls.TYPE)
        val account_id = cursor?.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID)



        val tmp : MutableList<List<String?>> = mutableListOf()

        while (cursor?.moveToNext() == true ) {
            val call_number = if (number != null) cursor.getString(number) else ""
            val call_date = if(date != null) cursor.getString(date) else ""
            val call_type = if(type != null) cursor.getInt(type).toString() else ""
            val call_account_id = if(account_id != null) cursor.getString(account_id) else ""

            tmp.add( listOf(call_number, call_date, call_type, call_account_id))
        }
        for(i in 0..38){
            if (cursor != null) {
                println(cursor.getColumnName(i))
            }
        }
    }

}