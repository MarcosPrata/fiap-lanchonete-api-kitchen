package com.soat220.lanchonete.common.driven.postgresdb

import com.soat220.lanchonete.common.driven.postgresdb.model.Order
import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.common.model.enums.OrderStatus
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
    fun findAllByStatusAndPaymentStatusOrderByCreatedAtAsc(orderStatus: OrderStatus, paymentStatus: PaymentStatus): List<Order>
}