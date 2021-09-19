package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.TokenQuote
import com.oneinch.repository.dao.Passed
import com.oneinch.repository.dao.RealTxEntity
import com.oneinch.repository.dao.TokenEntity
import com.oneinch.util.getLogger
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.util.*

@Component
class RealRepositoryManager(
    val iTokenEntityRepository: ITokenEntityRepository,
    val iRealTxRepository: IRealTxRepository,
    val chain: Chain
) {

    init {
        if (iTokenEntityRepository.findByChainId(chain.id).isEmpty()) {
            chain.tokens.forEach {
                val tokenEntity = TokenEntity(it.symbol, it.address, chain.id)
                iTokenEntityRepository.save(tokenEntity)
            }
        }
    }

    fun saveTransaction(
        from: TokenQuote,
        to: TokenQuote,
        gasPrice: BigInteger,
        txHash: String?,
        maxSlippage: Double,
        advantage: Double,
        requestTimeStamp: Date,
        passed: Passed
    ) {
        if (txHash == null) {
            getLogger().error("TxHash is null, did not receive proper response")
            return
        }
        val tx = RealTxEntity(
            requestTimeStamp = requestTimeStamp,
            chainId = chain.id,
            hash = txHash,
            fromSymbol = from.symbol,
            fromAddress = from.address,
            fromReadable = from.calcReadable(chain).round(),
            fromAmount = from.origin.toString(),
            toSymbol = to.symbol,
            toAddress = to.address,
            toReadable = to.calcReadable(chain).round(),
            toAmount = to.origin.toString(),
            gasPrice = gasPrice,
            maxSlippage = maxSlippage,
            advantage = advantage,
            passed = passed
        )
        iRealTxRepository.save(tx)
    }
}

fun Double.round() = Math.round(this * 100.0) / 100.0