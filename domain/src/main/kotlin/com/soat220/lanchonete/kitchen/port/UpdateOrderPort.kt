package com.soat220.lanchonete.kitchen.port

import com.soat220.lanchonete.common.model.Order

interface UpdateOrderPort {

    fun execute(order: Order)
}