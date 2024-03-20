package com.soat220.lanchonete.kitchen.usecase

import com.soat220.lanchonete.common.model.Order
import com.soat220.lanchonete.kitchen.port.RegisterOrderPort
import javax.inject.Named

@Named
class RegisterOrder (
    private val registerOrderPort: RegisterOrderPort
) {

    fun execute(order: Order) {
        registerOrderPort.execute(order)
    }
}