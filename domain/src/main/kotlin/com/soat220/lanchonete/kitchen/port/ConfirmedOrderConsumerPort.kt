package com.soat220.lanchonete.kitchen.port

interface ConfirmedOrderConsumerPort {

    fun receive(orderId: String)
}