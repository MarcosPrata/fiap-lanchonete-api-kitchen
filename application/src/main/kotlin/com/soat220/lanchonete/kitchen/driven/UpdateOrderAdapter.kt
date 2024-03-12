package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.postgresdb.OrderRepository
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.kitchen.port.UpdateOrderPort
import org.springframework.stereotype.Service
import com.soat220.lanchonete.common.driven.postgresdb.model.Order as OrderEntity

@Service
class UpdateOrderAdapter (
    private val orderRepository: OrderRepository
): UpdateOrderPort {

    override fun execute(order: Order){
        val orderToSave = OrderEntity.fromDomain(order)
        orderToSave.orderItems.map { it.order = orderToSave }
        orderRepository.save(orderToSave)
    }
}