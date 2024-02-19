package com.hustleknight.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class HustleKnightApplication

fun main(args: Array<String>) {
//    exitProcess(SpringApplication.exit(SpringApplication.run(UntitledApplication::class.java, *args)))
    runApplication<HustleKnightApplication>(*args)
}
