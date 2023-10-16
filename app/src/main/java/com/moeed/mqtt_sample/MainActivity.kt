package com.moeed.mqtt_sample

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.moeed.mqtt_sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mqtt: MqttHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        initListeners()
    }

    fun initListeners(){
        binding.button.setOnClickListener {
            val brokerURL = binding.editText.text.toString()
            val clientId = binding.editTextText.text.toString()
            val username = binding.editTextText3.toString()
            val password = binding.editTextText4.toString()
            if (!brokerURL.isEmpty() && !clientId.isEmpty()){
                mqtt = MqttHandler(this@MainActivity, brokerURL, clientId)
                mqtt!!.connect(username,password)
            }else{
                Toast.makeText(this@MainActivity, "Enter Info", Toast.LENGTH_LONG).show()
            }
        }
        binding.button3.setOnClickListener {
            if (mqtt != null){
                if (mqtt!!.isConnected()){
                    Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this@MainActivity, "Not connected", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this@MainActivity, "Not connected", Toast.LENGTH_SHORT).show()
            }
        }
        binding.button2.setOnClickListener {
            val messageL = binding.editTextText2.text.toString()
            val topicL = binding.editTextText6.text.toString()
            Log.d("TOPIC", topicL)
            if (messageL.isNotEmpty() && topicL.isNotEmpty()){
                mqtt?.publish(topicL, messageL)
            }else{
                Toast.makeText(this@MainActivity, "Enter topic and message", Toast.LENGTH_SHORT).show()
            }
        }
        binding.button4.setOnClickListener {
            if(mqtt!=null){
                mqtt?.disconnect()
            }else{
                Toast.makeText(this@MainActivity, "Connect first", Toast.LENGTH_SHORT).show()
            }
        }
        binding.button6.setOnClickListener {
            val topicL = binding.editTextText6.text.toString()
            if (topicL.isNotEmpty()){
                mqtt?.subscribe(topicL)
            }else{
                Toast.makeText(this@MainActivity, "Enter topic", Toast.LENGTH_SHORT).show()
            }
        }
    }
}