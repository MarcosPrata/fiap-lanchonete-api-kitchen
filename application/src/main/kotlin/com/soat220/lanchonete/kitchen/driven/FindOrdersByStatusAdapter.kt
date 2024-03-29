package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.postgresdb.OrderRepository
import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.model.Customer
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.common.model.enums.OrderStatus
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.common.result.getOrNull
import com.soat220.lanchonete.kitchen.port.FindCustomerByIdPort
import com.soat220.lanchonete.kitchen.port.FindOrdersByStatusPort
import com.soat220.lanchonete.kitchen.port.FindProductByIdPort
import org.springframework.stereotype.Service

@Service
class FindOrdersByStatusAdapter(
    private val orderRepository: OrderRepository,
    private val findProductByIdPort: FindProductByIdPort,
    private val findCustomerByIdPort: FindCustomerByIdPort
) : FindOrdersByStatusPort {
    override fun execute(orderStatus: OrderStatus): Result<List<Order>, DomainException> {
        return try {

            val order = orderRepository.findAllByStatusAndPaymentStatusOrderByCreatedAtAsc(orderStatus, PaymentStatus.APPROVED)
                .map {

                    var customer: Customer? = null

                    if (it.customer != null) {
                        customer = findCustomerByIdPort.execute(it.customer).getOrNull()
                    }
                    it.toDomain(customer)

                }.toMutableList()

            order.forEach { model ->
                model.orderItems.forEach {
                    it.product = findProductByIdPort.execute(it.productId!!).getOrNull()
                }
            }

            Success(order)
        } catch (e: Exception) {
            return Failure(
                DomainException(e, ErrorCode.DATABASE_ERROR)
            )
        }
    }
}