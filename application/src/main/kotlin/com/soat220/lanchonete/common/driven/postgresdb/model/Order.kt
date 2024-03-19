package com.soat220.lanchonete.common.driven.postgresdb.model

import com.soat220.lanchonete.common.model.PaymentStatus
import com.soat220.lanchonete.common.model.enums.OrderStatus
import java.time.LocalDateTime
import javax.persistence.*
import com.soat220.lanchonete.common.model.Order as DomainOrder

@Entity
@Table(name = "order_table")
class Order(
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    val customer: Customer?,

    @Column(name = "status", nullable = false)
    var status: OrderStatus,

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    var paymentStatus: PaymentStatus,

    @Column(name = "notes", length = 100, nullable = false)
    var notes: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var orderItems: List<OrderItem> = mutableListOf()
) {

    fun toDomain() = DomainOrder(
        id = id,
        customer = customer?.toDomain(),
        orderItems = orderItems.map { it.toDomain() }.toMutableList(),
        orderStatus = status,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt,
        paymentStatus = paymentStatus
    )

    companion object {
        fun fromDomain(order: DomainOrder) = Order(
            id = order.id,
            customer = Customer.fromDomain(order.customer),
            orderItems = order.orderItems.map { OrderItem.fromDomain(it) }.toMutableList(),
            status = order.orderStatus,
            createdAt = order.createdAt,
            updatedAt = order.updatedAt,
            notes = order.notes,
            paymentStatus = order.paymentStatus
        )
    }
}