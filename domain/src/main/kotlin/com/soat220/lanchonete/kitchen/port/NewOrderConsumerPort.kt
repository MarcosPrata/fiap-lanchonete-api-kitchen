package com.soat220.lanchonete.kitchen.port

interface NewOrderConsumerPort {

    fun receive(newOrderMessage: String)
}