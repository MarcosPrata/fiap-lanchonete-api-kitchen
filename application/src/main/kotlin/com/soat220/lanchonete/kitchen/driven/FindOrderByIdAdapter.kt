package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.postgresdb.OrderRepository
import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.model.Customer
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.common.result.getOrNull
import com.soat220.lanchonete.kitchen.port.FindCustomerByIdPort
import com.soat220.lanchonete.kitchen.port.FindOrderByIdPort
import org.springframework.stereotype.Service

@Service
class FindOrderByIdAdapter(
    private val orderRepository: OrderRepository,
    private val findCustomerByIdPort: FindCustomerByIdPort
): FindOrderByIdPort {
    override fun execute(orderId: Long): Result<Order, DomainException> {
        return try {
            val orderEntity = orderRepository.findById(orderId).orElseThrow()
            var customer: Customer? = null

            if (orderEntity.customer != null) {
                customer = findCustomerByIdPort.execute(orderEntity.customer).getOrNull()
            }
            Success(orderEntity.toDomain(customer))
        } catch (e: Exception) {
            Failure(DomainException(e, ErrorCode.DATABASE_ERROR))
        }
    }
}