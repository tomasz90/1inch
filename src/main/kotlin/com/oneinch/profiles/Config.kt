package com.oneinch.profiles

import com.oneinch.common.Chain
import com.oneinch.config.PropertiesLoader
import com.oneinch.config.SettingsLoader
import com.oneinch.oneinch_api.api.ApiProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.reflect.full.declaredMemberProperties

@Configuration
open class Config {
    @Autowired
    lateinit var propertiesLoader: PropertiesLoader

    @Autowired
    lateinit var settingsLoader: SettingsLoader

    @Bean
    open fun properties() = propertiesLoader.load()

    @Bean
    open fun settings() = settingsLoader.load()

    @Bean
    open fun chain(): Chain {
        val instance = properties()
        return instance.javaClass.kotlin
            .declaredMemberProperties
            .filter { it.name == settings().chain }
            .map { it.get(instance) }
            .first() as Chain
    }

    @Bean
    open fun oneInch() = ApiProvider(properties()).create()
}