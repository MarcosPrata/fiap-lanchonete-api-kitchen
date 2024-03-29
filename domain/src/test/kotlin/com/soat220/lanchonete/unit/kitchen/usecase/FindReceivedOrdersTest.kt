package com.soat220.lanchonete.unit.kitchen.usecase

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
import com.soat220.lanchonete.kitchen.port.FindOrdersByStatusPort
import com.soat220.lanchonete.kitchen.port.FindProductByIdPort
import com.soat220.lanchonete.kitchen.usecase.FindReceivedOrders
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FindReceivedOrdersTest {

    private lateinit var findOrdersByStatusPort: FindOrdersByStatusPort
    private lateinit var findProductByIdPort: FindProductByIdPort
    private lateinit var findReceivedOrders: FindReceivedOrders

    @BeforeEach
    fun setUp() {
        findOrdersByStatusPort = mockk()
        findProductByIdPort = mockk()
        findReceivedOrders = FindReceivedOrders(findOrdersByStatusPort, findProductByIdPort)
    }

    @Test
    fun `test execute success`() {
        val order = Order(
            orderStatus = OrderStatus.COMPLETED,
            notes = "",
            orderItems = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            paymentStatus = PaymentStatus.APPROVED
        )

        // Arrange
        val orders = listOf(order)
        val expectedResult: Result<List<Order>, DomainException> = Success(orders)
        val product = Product();

        every { findOrdersByStatusPort.execute(OrderStatus.RECEIVED) } returns expectedResult
        every { findProductByIdPort.execute(any()) } returns Success(product)

        // Act
        val result: Result<List<Order>, DomainException> = findReceivedOrders.execute()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `test execute failure`() {
        // Arrange
        val expectedFailure: Result<List<Order>, DomainException> = Failure(DomainException("Error", ErrorCode.DATABASE_ERROR))

        every { findOrdersByStatusPort.execute(OrderStatus.RECEIVED) } returns expectedFailure

        // Act
        val result: Result<List<Order>, DomainException> = findReceivedOrders.execute()

        // Assert
        assertEquals(expectedFailure, result)
    }
}
