package com.soat220.lanchonete.kitchen.usecase

import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.model.enums.OrderStatus
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.kitchen.port.FindOrdersByStatusPort
import com.soat220.lanchonete.kitchen.port.FindProductByIdPort
import javax.inject.Named

@Named
class FindReceivedOrders(
    private val findOrdersByStatusPort: FindOrdersByStatusPort,
    private val findProductByIdPort: FindProductByIdPort
) {
    fun execute(): Result<List<Order>, DomainException> {
        return findOrdersByStatusPort.execute(OrderStatus.RECEIVED)
    }
}