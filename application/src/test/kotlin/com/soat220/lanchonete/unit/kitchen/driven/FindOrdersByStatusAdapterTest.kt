package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.postgresdb.OrderRepository
import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.common.model.Product
import com.soat220.lanchonete.common.model.enums.Category
import com.soat220.lanchonete.common.model.enums.OrderStatus
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.common.result.getOrNull
import com.soat220.lanchonete.kitchen.port.FindCustomerByIdPort
import com.soat220.lanchonete.kitchen.port.FindOrdersByStatusPort
import com.soat220.lanchonete.kitchen.port.FindProductByIdPort
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FindOrdersByStatusAdapterTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var findProductByIdPort: FindProductByIdPort
    private lateinit var findCustomerByIdPort: FindCustomerByIdPort
    private lateinit var findOrdersByStatusPort: FindOrdersByStatusPort

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        findProductByIdPort = mockk()
        findCustomerByIdPort = mockk()
        findOrdersByStatusPort = FindOrdersByStatusAdapter(orderRepository, findProductByIdPort, findCustomerByIdPort)
    }

    @Test
    fun `test execute success`() {
        // Arrange
        val order1 = Order(
            orderStatus = OrderStatus.COMPLETED,
            notes = "",
            orderItems = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            paymentStatus = PaymentStatus.APPROVED
        )

        val orders = listOf(
            com.soat220.lanchonete.common.driven.postgresdb.model.Order.fromDomain(order1)
        )

        val product = Product();

        every { orderRepository.findAllByStatusAndPaymentStatusOrderByCreatedAtAsc(OrderStatus.COMPLETED, PaymentStatus.APPROVED) } returns orders
        every { findProductByIdPort.execute(any()) } returns Success(product)


        // Act
        val result: Result<List<Order>, DomainException> = findOrdersByStatusPort.execute(OrderStatus.COMPLETED)

        // Assert
        assertEquals(Success::class.java, result.javaClass)
        assertEquals(orders.last().id, result.getOrNull()!!.last().id)
    }

    @Test
    fun `test execute failure`() {
        // Arrange
        every { orderRepository.findAllByStatusAndPaymentStatusOrderByCreatedAtAsc(OrderStatus.COMPLETED, PaymentStatus.APPROVED) } throws RuntimeException()

        // Act
        val result: Result<List<Order>, DomainException> = findOrdersByStatusPort.execute(OrderStatus.COMPLETED)

        // Assert
        assertEquals(Failure::class.java, result.javaClass)
        assertEquals(ErrorCode.DATABASE_ERROR, (result as Failure).reason.errorCode)
    }
}
