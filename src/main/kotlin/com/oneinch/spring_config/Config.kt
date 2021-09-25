package com.oneinch.spring_config

import com.oneinch.`object`.Chain
import com.oneinch.config.Properties
import com.oneinch.config.Protocols
import com.oneinch.config.Settings
import com.oneinch.provider.ApiProvider
import com.oneinch.util.RateLimiter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.atomic.AtomicBoolean

@Configuration
open class Config {

    @Autowired
    lateinit var properties: Properties

    @Autowired
    lateinit var settings: Settings

    @Autowired
    lateinit var allProtocols: Protocols

    @Bean
    open fun apiProvider() = ApiProvider(properties, settings)

    @Bean
    open fun chain(): Chain {
        val name = settings.chain
        return properties.chains.first { it.name == name }
    }

    @Bean
    open fun protocols(): String {
        val name = settings.chain
        return allProtocols.protocols.first { it.chain == name }.asString()
    }

    @Bean
    open fun oneInch() = apiProvider().createOneInch()

    @Bean
    open fun isSwapping() = AtomicBoolean()

    @Bean
    open fun setNotSwapping() {
        isSwapping().set(false) // set for startup only
    }

    @Bean
    open fun limiter() = RateLimiter(settings.maxRps)
}