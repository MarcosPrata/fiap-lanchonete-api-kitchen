package com.soat220.lanchonete.unit.kitchen.usecase

import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.common.model.enums.OrderStatus
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.kitchen.port.FindOrdersByStatusPort
import com.soat220.lanchonete.kitchen.usecase.FindPreparingOrders
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FindPreparingOrdersTest {

    private lateinit var findOrdersByStatusPort: FindOrdersByStatusPort
    private lateinit var findPreparingOrders: FindPreparingOrders

    @BeforeEach
    fun setUp() {
        findOrdersByStatusPort = mockk()
        findPreparingOrders = FindPreparingOrders(findOrdersByStatusPort)
    }

    @Test
    fun `test execute success`() {
        // Arrange
        val order = Order(
            orderStatus = OrderStatus.COMPLETED,
            notes = "",
            orderItems = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            paymentStatus = PaymentStatus.APPROVED
        )

        val orders = listOf(order)
        val expectedResult: Result<List<Order>, DomainException> = Success(orders)

        every { findOrdersByStatusPort.execute(OrderStatus.IN_PREPARATION) } returns expectedResult

        // Act
        val result: Result<List<Order>, DomainException> = findPreparingOrders.execute()

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `test execute failure`() {
        // Arrange
        val expectedFailure: Result<List<Order>, DomainException> = Failure(DomainException("Error", ErrorCode.DATABASE_ERROR))

        every { findOrdersByStatusPort.execute(OrderStatus.IN_PREPARATION) } returns expectedFailure

        // Act
        val result: Result<List<Order>, DomainException> = findPreparingOrders.execute()

        // Assert
        assertEquals(expectedFailure, result)
    }
}
