package com.leijendary.spring.apigateway.template.core.filter

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.filter.factory.RemoveRequestHeaderGatewayFilterFactory
import org.springframework.core.Ordered
import org.springframework.core.Ordered.HIGHEST_PRECEDENCE
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class RemoveRequestHeaderGlobalFilter : RemoveRequestHeaderGatewayFilterFactory(), GlobalFilter, Ordered {
    private val config = NameConfig().apply {
        name = HEADER_USER_ID
    }

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        return super
            .apply(config)
            .filter(exchange, chain)
    }

    override fun getOrder() = HIGHEST_PRECEDENCE + 2
}
