package com.leijendary.spring.template.apigateway.core.model

data class ErrorModel(val source: List<Any>, val code: String, val message: String? = null)
