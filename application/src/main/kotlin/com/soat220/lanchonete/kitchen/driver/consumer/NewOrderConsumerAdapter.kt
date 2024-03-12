package com.soat220.lanchonete.kitchen.driver.consumer

import com.google.gson.GsonBuilder
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.config.LocalDateTimeTypeAdapter
import com.soat220.lanchonete.kitchen.port.NewOrderConsumerPort
import com.soat220.lanchonete.kitchen.usecase.RegisterNewOrder
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NewOrderConsumerAdapter(

    private val registerNewOrder: RegisterNewOrder

): NewOrderConsumerPort {

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .create()

    @RabbitListener(queues = ["\${queue.pedidos.registrados}"])
    override fun receive(newOrderMessage: String) {

        val order = gson.fromJson(newOrderMessage, Order::class.java);
        order.paymentStatus = PaymentStatus.PENDING

        registerNewOrder.execute(order)
    }

}