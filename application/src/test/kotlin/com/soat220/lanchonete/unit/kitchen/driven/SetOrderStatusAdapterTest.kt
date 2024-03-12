package com.soat220.lanchonete.unit.kitchen.driven

import com.soat220.lanchonete.common.driven.postgresdb.OrderRepository
import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.common.model.enums.OrderStatus
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.common.result.getOrNull
import com.soat220.lanchonete.kitchen.driven.SetOrderStatusAdapter
import com.soat220.lanchonete.kitchen.port.SetOrderStatusPort
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import java.util.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class SetOrderStatusAdapterTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var setOrderStatusPort: SetOrderStatusPort

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        setOrderStatusPort = SetOrderStatusAdapter(orderRepository)
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

        val entityOrder = com.soat220.lanchonete.common.driven.postgresdb.model.Order.fromDomain(order)

        every { orderRepository.findById(any()) } returns Optional.of(entityOrder)
        every { orderRepository.save(any()) } answers {
            val updated = it.invocation.args[0] as com.soat220.lanchonete.common.driven.postgresdb.model.Order
            updated
        }

        // Act
        val result: Result<Order, DomainException> = setOrderStatusPort.execute(1L, OrderStatus.COMPLETED)

        // Assert
        assertEquals(Success::class.java, result.javaClass)
        assertEquals(order.id, result.getOrNull()!!.id)
    }

    @Test
    fun `test execute failure`() {
        // Arrange
        val orderId = 1L
        val orderStatus = OrderStatus.IN_PREPARATION

        every { orderRepository.findById(orderId) } throws RuntimeException()

        // Act
        val result: Result<Order, DomainException> = setOrderStatusPort.execute(orderId, orderStatus)

        // Assert
        assertEquals(Failure::class.java, result.javaClass)
        assertEquals(ErrorCode.DATABASE_ERROR, (result as Failure).reason.errorCode)
    }
}
