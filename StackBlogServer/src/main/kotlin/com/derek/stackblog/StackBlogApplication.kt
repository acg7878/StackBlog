package com.derek.stackblog

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class StackBlogApplication

fun main(args: Array<String>) {
    runApplication<StackBlogApplication>(*args)
}
