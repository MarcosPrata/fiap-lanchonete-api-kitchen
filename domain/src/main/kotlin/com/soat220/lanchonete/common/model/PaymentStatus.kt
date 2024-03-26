package com.soat220.lanchonete.common.model

enum class PaymentStatus(code: Int) {

    APPROVED(0),
    PENDING(1),
    DECLINED(2),
    ERROR(3)

}