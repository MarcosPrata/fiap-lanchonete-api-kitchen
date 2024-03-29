package com.soat220.lanchonete.kitchen.port

import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.model.Customer
import com.soat220.lanchonete.common.result.Result

interface FindCustomerByIdPort {

    fun execute(customerId: Long): Result<Customer?, DomainException>
}