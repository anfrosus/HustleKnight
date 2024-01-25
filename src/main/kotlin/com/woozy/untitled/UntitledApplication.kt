package com.woozy.untitled

import org.hibernate.annotations.processing.SQL
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UntitledApplication

fun main(args: Array<String>) {
    runApplication<UntitledApplication>(*args)
}
