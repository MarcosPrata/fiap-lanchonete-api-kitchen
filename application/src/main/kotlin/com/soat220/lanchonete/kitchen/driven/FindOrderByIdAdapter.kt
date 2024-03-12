package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.postgresdb.OrderRepository
import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.kitchen.port.FindOrderByIdPort
import org.springframework.stereotype.Service

@Service
class FindOrderByIdAdapter(
    private val orderRepository: OrderRepository
): FindOrderByIdPort {
    override fun execute(orderId: Long): Result<Order, DomainException> {
        return try {
            Success(orderRepository.findById(orderId).orElseThrow().toDomain())
        } catch (e: Exception) {
            Failure(DomainException(e, ErrorCode.DATABASE_ERROR))
        }
    }
}