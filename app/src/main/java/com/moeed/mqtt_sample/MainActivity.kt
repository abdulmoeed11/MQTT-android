package com.moeed.mqtt_sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.moeed.mqtt_sample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mqtt: MqttHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        mqtt = MqttHandler(this@MainActivity)
        initListeners()
    }

    fun initListeners(){
        binding.button.setOnClickListener {
            val brokerURL = binding.editText.text.toString()
            val clientId = binding.editTextText.text.toString()
            val username = binding.editTextText3.toString()
            val password = binding.editTextText4.toString()
            if (!brokerURL.isEmpty() && !clientId.isEmpty()){
                mqtt.connect(brokerURL,clientId,username,password)
            }else{
                Toast.makeText(this@MainActivity, "Enter Info", Toast.LENGTH_LONG).show()
            }
        }
        binding.button3.setOnClickListener {
            if (mqtt.isConnected()){
                Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this@MainActivity, "Not connected", Toast.LENGTH_SHORT).show()
            }
        }
        binding.button2.setOnClickListener {
                val messageL = binding.editTextText2.text.toString()
                mqtt.publish("/moeedLocation", messageL)
        }
    }
}