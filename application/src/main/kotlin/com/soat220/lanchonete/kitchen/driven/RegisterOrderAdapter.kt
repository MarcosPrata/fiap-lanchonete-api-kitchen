package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.postgresdb.OrderItemRepository
import com.soat220.lanchonete.common.driven.postgresdb.OrderRepository
import com.soat220.lanchonete.common.driven.postgresdb.model.OrderItem
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.result.orThrow
import com.soat220.lanchonete.kitchen.port.RegisterOrderPort
import com.soat220.lanchonete.kitchen.port.SaveCustomerPort
import org.springframework.stereotype.Service
import java.util.Objects.nonNull
import com.soat220.lanchonete.common.driven.postgresdb.model.Order as OrderEntity

@Service
class RegisterOrderAdapter(
    private val saveCustomerPort: SaveCustomerPort,
    private val orderRepository: OrderRepository,
    private val orderItemRepository: OrderItemRepository
) : RegisterOrderPort {

    override fun execute(order: Order) {

        if (nonNull(order.customer)) {
            saveCustomerPort.execute(order.customer!!)
        }

        val orderItensTosave = order.orderItems.map { OrderItem.fromDomain(it) }

        order.orderItems = mutableListOf()

        val savedOrder = orderRepository.saveAndFlush(OrderEntity.fromDomain(order))

        val savedOrderItens = orderItemRepository.saveAllAndFlush(
            orderItensTosave.map {
                it.order = savedOrder
                it
            }
        )

        savedOrder.orderItems = savedOrderItens

        orderRepository.save(savedOrder)
    }

}