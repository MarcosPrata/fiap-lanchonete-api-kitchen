package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.AbstractHttpClientService
import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.model.Customer
import com.soat220.lanchonete.common.model.Product
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.kitchen.port.FindCustomerByIdPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class FindCustomerByIdAdapter(
    @Value("\${url.erp}") private val erpHost: String
): FindCustomerByIdPort {

    override fun execute(customerId: Long): Result<Customer?, DomainException> {
        return try {

            val httpClientService = AbstractHttpClientService<Product>()

            Success(httpClientService.get("$erpHost:83/api/erp/customers/$customerId"))
        } catch (e: Exception) {
            Failure(
                DomainException(e, ErrorCode.ENTITY_NOT_FOUND_ERROR)
            )
        }
    }
}