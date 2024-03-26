package com.soat220.lanchonete.common.driven.postgresdb

import com.soat220.lanchonete.common.driven.postgresdb.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CustomerRepository  : JpaRepository<Customer, Long> {
    fun findByCpf(cpf: String?): Optional<Customer>
}