package com.leijendary.spring.apigateway.template.core.extension

import com.leijendary.spring.apigateway.template.core.config.properties.RequestProperties
import com.leijendary.spring.apigateway.template.core.config.properties.RetryProperties
import com.leijendary.spring.apigateway.template.core.filter.AuthenticatedGatewayFilterFactory
import com.leijendary.spring.apigateway.template.core.resolver.RemoteAddressKeyResolver
import com.leijendary.spring.apigateway.template.core.util.SpringContext.Companion.getBean
import org.springframework.cloud.gateway.filter.factory.RetryGatewayFilterFactory.BackoffConfig
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec
import org.springframework.http.HttpStatus.BAD_GATEWAY
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import java.time.Duration.ofMillis

private val authenticatedGatewayFilterFactory = getBean(AuthenticatedGatewayFilterFactory::class.java)
private val redisRateLimiter = getBean(RedisRateLimiter::class.java)
private val remoteAddressKeyResolver = getBean(RemoteAddressKeyResolver::class.java)
private val requestProperties = getBean(RequestProperties::class.java)
private val retryProperties = getBean(RetryProperties::class.java)
private val backoff = BackoffConfig(
    ofMillis(retryProperties.backoff.firstBackoff),
    ofMillis(retryProperties.backoff.maxBackoff),
    retryProperties.backoff.factor,
    true
)

fun GatewayFilterSpec.defaultFilters(filterSpecs: () -> GatewayFilterSpec) {
    setRequestSize(requestProperties.maxSize)
    requestRateLimiter {
        it.keyResolver = remoteAddressKeyResolver
        it.rateLimiter = redisRateLimiter
    }
    filterSpecs()
    retry {
        it.retries = retryProperties.retries
        it.backoff = backoff
        it.setStatuses(BAD_GATEWAY, SERVICE_UNAVAILABLE)
    }
}

fun GatewayFilterSpec.authenticated(vararg scopes: String): GatewayFilterSpec {
    return filter(authenticatedGatewayFilterFactory.apply { it.scopes = scopes.toSet() })
}