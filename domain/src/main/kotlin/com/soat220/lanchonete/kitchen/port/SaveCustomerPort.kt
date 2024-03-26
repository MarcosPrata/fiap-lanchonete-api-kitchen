package com.soat220.lanchonete.kitchen.port

import com.soat220.lanchonete.common.model.Customer

interface SaveCustomerPort {

    fun execute(customer: Customer)
}