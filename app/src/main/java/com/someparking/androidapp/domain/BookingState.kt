package com.someparking.androidapp.domain

enum class BookingState(val value: String) {
    APPROVED("подтверждено"),
    REJECTED("отклонено"),
    PENDING("на рассмотрении"),
    UNDER_CONSIDERATION("на рассмотрении у администратора арендатора/владельца"),
    CHECK_IN("заезд"),
    CHECK_OUT_WAIT("выезд с ожиданием"),
    CHECK_OUT("выезд"),
    CANCELED("отменено"),
    COMPLETED("завершено"),
    UNKNOWN(""),
}