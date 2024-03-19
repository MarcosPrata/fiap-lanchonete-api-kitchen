package com.soat220.lanchonete.kitchen.driven

import com.fasterxml.jackson.databind.ObjectMapper
import com.soat220.lanchonete.common.exception.NotFoundException
import com.soat220.lanchonete.common.model.Product
import com.soat220.lanchonete.common.result.Result
import com.soat220.lanchonete.common.result.Success
import com.soat220.lanchonete.kitchen.port.FindProductByIdPort
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class FindProductByIdAdapter: FindProductByIdPort {

    override fun execute(productId: Long): Result<Product, NotFoundException> {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://erp:83/api/erp/products/$productId")) // TODO set url by value
            .build();

        val response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return Success(ObjectMapper().readValue(response.body(), Product::class.java))
    }
}