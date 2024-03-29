package com.soat220.lanchonete.kitchen.port

import com.soat220.lanchonete.common.model.Order

interface RegisterOrderPort {

    fun execute(order: Order)
}