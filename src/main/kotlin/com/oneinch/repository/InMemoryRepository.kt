package com.oneinch.repository

import com.oneinch.`object`.TokenQuote
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
open class InMemoryRepository {

    private val allBalance = mutableListOf<TokenQuote>()

    fun save(tokenQuote: TokenQuote) {
        allBalance.add(tokenQuote)
    }

    fun findByAddress(address: String): TokenQuote? {
        return allBalance.firstOrNull { it.address == address }
    }

    fun update(tokenQuote: TokenQuote) {
        val index = allBalance.indexOfFirst { it.address == tokenQuote.address }
        if (index > -1) {
            allBalance.removeAt(index)
        }
        allBalance.add(tokenQuote)
    }
}