package com.soat220.lanchonete.kitchen.driven

import com.soat220.lanchonete.common.driven.postgresdb.CustomerRepository
import com.soat220.lanchonete.common.model.Customer
import com.soat220.lanchonete.kitchen.port.SaveCustomerPort
import org.springframework.stereotype.Service
import com.soat220.lanchonete.common.driven.postgresdb.model.Customer as CustomerEntity

@Service
class SaveCustomerAdapter(
    private val customerRepository: CustomerRepository
): SaveCustomerPort {

    override fun execute(customer: Customer) {
        val customerEntity = CustomerEntity.fromDomain(customer)

        val optionalCustomer = customerRepository.findByCpf(customer.cpf)
        if (optionalCustomer.isPresent) {
            customerEntity.email = optionalCustomer.get().email
            customerEntity.name = optionalCustomer.get().name
        }

        customerRepository.save(customerEntity)
    }
}