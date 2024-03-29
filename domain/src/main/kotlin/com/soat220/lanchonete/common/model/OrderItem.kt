package com.soat220.lanchonete.common.model

class OrderItem(
    val id: Long? = null,
    var product: Product?,
    var productId: Long?,
    val amount: Int = 1,
)