package com.soat220.lanchonete.unit.kitchen.usecase

import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.model.enums.OrderStatus
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.kitchen.port.SetOrderStatusPort
import com.soat220.lanchonete.kitchen.usecase.FinishOrder
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class FinishOrderTest {

    private lateinit var setOrderStatusPort: SetOrderStatusPort
    private lateinit var finishOrder: FinishOrder

    @BeforeEach
    fun setUp() {
        setOrderStatusPort = mockk()
        finishOrder = FinishOrder(setOrderStatusPort)
    }

    @Test
    fun `test execute success`() {
        // Arrange
        val order = Order(
            orderStatus = OrderStatus.COMPLETED,
            notes = "",
            orderItems = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        val orderId = 1L
        val expectedResult: Result<Order, DomainException> = Success(order)

        every { setOrderStatusPort.execute(orderId, OrderStatus.READY) } returns expectedResult

        // Act
        val result: Result<Order, DomainException> = finishOrder.execute(orderId)

        // Assert
        assertEquals(expectedResult, result)
    }

    @Test
    fun `test execute failure`() {
        // Arrange
        val orderId = 1L
        val expectedFailure: Result<Order, DomainException> = Failure(DomainException( "Error", ErrorCode.DATABASE_ERROR))

        every { setOrderStatusPort.execute(orderId, OrderStatus.READY) } returns expectedFailure

        // Act
        val result: Result<Order, DomainException> = finishOrder.execute(orderId)

        // Assert
        assertEquals(expectedFailure, result)
    }
}
