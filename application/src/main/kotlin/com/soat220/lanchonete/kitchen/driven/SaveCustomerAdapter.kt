package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.AbstractHttpClientService
import com.soat220.lanchonete.common.model.Customer
import com.soat220.lanchonete.kitchen.port.SaveCustomerPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class SaveCustomerAdapter(
    @Value("\${url.erp}") private val erpHost: String
): SaveCustomerPort {

    override fun execute(customer: Customer) {
        val httpClientService = AbstractHttpClientService<Customer>()

        httpClientService.post<Customer>("$erpHost:83/api/erp/customers", customer);
    }
}