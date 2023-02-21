package com.leijendary.spring.apigateway.template.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.util.unit.DataSize

@ConfigurationProperties(prefix = "request")
class RequestProperties {
    var maxSize: DataSize = DataSize.ofMegabytes(100)
}
