package com.soat220.lanchonete.postgres.model

import com.soat220.lanchonete.customer.model.Customer
import javax.persistence.*

@Entity
data class CustomerEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column
    var name: String,
    @Column(unique = true)
    var cpf: String
) {
    fun toDomain() = Customer(
        id = id,
        name = name,
        cpf = cpf
    )

    companion object {
        fun fromDomain(customer: Customer) = CustomerEntity(
            customer.id,
            customer.name,
            customer.cpf
        )
    }
}