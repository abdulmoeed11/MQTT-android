package com.moeed.mqtt_sample

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage


class MqttHandler constructor(context: Context): IMqttActionListener, MqttCallback {
    private var client: MqttAndroidClient? = null
    private val context = context
    fun connect(brokerUrl: String?, clientId: String?, username: String = "moeed", password: String = "Pakistan123") {
        client = MqttAndroidClient(context, brokerUrl, clientId)
        client!!.setCallback(this)
        val options = MqttConnectOptions()
//        options.userName = username
//        options.password = password.toCharArray()
        try {
            // Connect to the broker
            client?.connect(options, null, this)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            client?.disconnect(null, this)
        } catch (e: NullPointerException){
            Toast.makeText(context, "Initialize connection first", Toast.LENGTH_SHORT).show()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String?, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            client?.publish(topic, mqttMessage)
        } catch (e: NullPointerException){
            e.printStackTrace()
            Toast.makeText(context, "Initialize connection first", Toast.LENGTH_SHORT).show()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String?) {
        try {
            client?.subscribe(topic,1, null, this)
        } catch (e: NullPointerException){
            e.printStackTrace()
            Toast.makeText(context, "Initialize connection first", Toast.LENGTH_SHORT).show()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun isConnected(): Boolean{
        try {
            val connection  = client?.isConnected
            connection?.let {
                if (it) return true
            }
            return false
        } catch (e: NullPointerException){
            e.printStackTrace()
            Toast.makeText(context, "Initialize connection first", Toast.LENGTH_SHORT).show()
            return false
        } catch (e:MqttException) {
            e.printStackTrace()
            return false
        }
    }

    override fun onSuccess(asyncActionToken: IMqttToken?) {
        Log.d("MQTTxx", "Connection Success")
        Toast.makeText(context, "MQTT Connection success", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        exception?.printStackTrace()
        Log.d("MQTTxx", "Connection Failed")
        Toast.makeText(context, "MQTT Connection fails: ${exception.toString()}",
            Toast.LENGTH_SHORT).show()
    }

    override fun connectionLost(cause: Throwable?) {
        cause?.printStackTrace()
        Toast.makeText(context, "Connection Lost", Toast.LENGTH_SHORT).show()
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        val msg = "Receive message: ${message.toString()} from topic: $topic"

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun deliveryComplete(token: IMqttDeliveryToken?) {
        Toast.makeText(context, "Delivery Complete", Toast.LENGTH_SHORT).show()
    }
}