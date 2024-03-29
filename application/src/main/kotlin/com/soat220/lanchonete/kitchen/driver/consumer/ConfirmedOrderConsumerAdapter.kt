package com.soat220.lanchonete.kitchen.driver.consumer

import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.common.result.orThrow
import com.soat220.lanchonete.kitchen.port.ConfirmedOrderConsumerPort
import com.soat220.lanchonete.kitchen.port.FindOrderByIdPort
import com.soat220.lanchonete.kitchen.port.UpdateOrderPort
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class ConfirmedOrderConsumerAdapter(
    private val findOrderByIdPort: FindOrderByIdPort,
    private val updateOrderPort: UpdateOrderPort
): ConfirmedOrderConsumerPort {

    @RabbitListener(queues = ["\${queue.pedidos.confirmados}"])
    override fun receive(orderId: String) {

        val order = findOrderByIdPort.execute(orderId.toLong()).orThrow()

        order.paymentStatus = PaymentStatus.APPROVED

        updateOrderPort.execute(order)

    }
}