package com.woozy.untitled

import org.hibernate.annotations.processing.SQL
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class UntitledApplication

fun main(args: Array<String>) {
//    exitProcess(SpringApplication.exit(SpringApplication.run(UntitledApplication::class.java, *args)))
    runApplication<UntitledApplication>(*args)
}
