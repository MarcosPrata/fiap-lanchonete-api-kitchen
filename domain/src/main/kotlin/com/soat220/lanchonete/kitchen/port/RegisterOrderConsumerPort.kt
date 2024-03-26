package com.soat220.lanchonete.kitchen.port

interface RegisterOrderConsumerPort {

    fun receive(orderMessage: String)
}