package com.moeed.mqtt_sample

import android.content.Context
import android.widget.Toast
import info.mqtt.android.service.Ack
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage


class MqttHandler constructor(contexts: Context, brokerUrl: String, clientId: String): IMqttActionListener, MqttCallback {
    private var context: Context
    private var client: MqttAndroidClient
    init {
        context = contexts
        client = MqttAndroidClient(context,brokerUrl,clientId, Ack.AUTO_ACK)
    }


    fun connect(username: String, password: String) {
        client.setCallback(this)
        val options = MqttConnectOptions()
        if (username.isNotEmpty()){
            options.userName = username
        }
        if (password.isNotEmpty()){
            options.password = password.toCharArray()
        }
        options.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
        try {
            client.connect(options, null, this)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            client.disconnect(null, this)
        } catch (e: NullPointerException){
            Toast.makeText(context, "Initialize connection first", Toast.LENGTH_SHORT).show()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            client.publish(topic, mqttMessage,null, this)
        } catch (e: NullPointerException){
            e.printStackTrace()
            Toast.makeText(context, "Initialize connection first", Toast.LENGTH_SHORT).show()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun subscribe(topic: String) {
        try {
//            client.subscribe(topic,1)
            client.subscribe(topic,1, null, this)
        } catch (e: NullPointerException){
            e.printStackTrace()
            Toast.makeText(context, "Initialize connection first", Toast.LENGTH_SHORT).show()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun isConnected(): Boolean{
        try {
            val connection  = client.isConnected
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
        Toast.makeText(context, "MQTT Connection success", Toast.LENGTH_SHORT).show()
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        exception?.printStackTrace()
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