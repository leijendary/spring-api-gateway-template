package com.leijendary.spring.boot.apigateway.core.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger

inline fun <reified T> T.logger(): Logger {
    return getLogger(T::class.java)
}