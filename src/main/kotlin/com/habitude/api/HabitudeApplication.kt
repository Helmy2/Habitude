package com.habitude.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HabitudeApplication

fun main(args: Array<String>) {
    runApplication<HabitudeApplication>(*args)
}
