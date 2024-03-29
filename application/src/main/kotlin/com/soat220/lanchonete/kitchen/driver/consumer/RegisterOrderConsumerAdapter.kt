package com.soat220.lanchonete.kitchen.driver.consumer

import com.google.gson.GsonBuilder
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.config.LocalDateTimeTypeAdapter
import com.soat220.lanchonete.kitchen.port.RegisterOrderConsumerPort
import com.soat220.lanchonete.kitchen.usecase.RegisterOrder
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class RegisterOrderConsumerAdapter(

    private val registerOrder: RegisterOrder

): RegisterOrderConsumerPort {

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeTypeAdapter())
        .create()

    @RabbitListener(queues = ["\${queue.pedidos.registrados}"])
    override fun receive(orderMessage: String) {

        val order = gson.fromJson(orderMessage, Order::class.java);
        order.paymentStatus = PaymentStatus.PENDING

        registerOrder.execute(order)
    }

}