package com.soat220.lanchonete.kitchen.driven

import com.fasterxml.jackson.databind.ObjectMapper
import com.soat220.lanchonete.common.driven.AbstractHttpClientService
import com.soat220.lanchonete.common.exception.DomainException
import com.soat220.lanchonete.common.exception.ErrorCode
import com.soat220.lanchonete.common.exception.NotFoundException
import com.soat220.lanchonete.common.model.Product
import com.soat220.lanchonete.common.result.Failure
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.kitchen.port.FindProductByIdPort
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class FindProductByIdAdapter(
    @Value("\${url.erp}") private val erpHost: String
) : FindProductByIdPort {

    override fun execute(productId: Long): Result<Product?, DomainException> {
        return try {

            val httpClientService = AbstractHttpClientService<Product>()

            Success(httpClientService.get("$erpHost:83/api/erp/products/$productId"))
        } catch (e: Exception) {
            Failure(
                DomainException(e, ErrorCode.ENTITY_NOT_FOUND_ERROR)
            )
        }
    }
}