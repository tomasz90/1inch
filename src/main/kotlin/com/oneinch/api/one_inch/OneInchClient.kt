package com.oneinch.api.one_inch

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Coin
import com.oneinch.`object`.CoinQuote
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.api.one_inch.api.OneInchApi
import com.oneinch.api.one_inch.api.data.QuoteDto
import com.oneinch.api.one_inch.api.data.SwapDto
import com.oneinch.api.one_inch.api.data.toDto
import com.oneinch.loader.Settings
import com.oneinch.util.getLogger
import org.springframework.stereotype.Component
import retrofit2.Response

@Component
class OneInchClient(val myAddress: String, val oneInch: OneInchApi, val settings: Settings, val chain: Chain) {

    fun quote(from: TokenQuote, to: Token): QuoteDto? {
        val response = oneInch.quote(chain.id, from.token.address, to.address, from.origin).execute()
        return if (response.isSuccessful) {
            response.body()!!.toDto()
        } else {
            response.logErrorMessage("Error getting quotes.")
            null
        }
    }

    fun swap(from: CoinQuote, to: Coin, allowPartialFill: Boolean, protocols: String, slippage: Double): SwapDto? {
        val response =
            oneInch.swap(
                chain.id,
                from.token.address,
                to.address,
                from.origin,
                myAddress,
                slippage,
                allowPartialFill,
                protocols
            ).execute()
        return if (response.isSuccessful) {
            response.body()!!.toDto()
        } else {
            response.logErrorMessage("Error getting quotes.")
            null
        }
    }
}

fun <T> Response<T>.logErrorMessage(info: String) {
    val text = this.errorBody()!!.charStream().readText()
    getLogger().error("$info Response status: ${this.code()} $text")
}


