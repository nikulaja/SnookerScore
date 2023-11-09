package com.nikulaja.snookerscore

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
class SnookerScore

fun main(args: Array<String>) {
    runApplication<SnookerScore>(*args)
}
