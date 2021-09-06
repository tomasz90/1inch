package com.oneinch.repository

import com.oneinch.`object`.Chain
import com.oneinch.`object`.Token
import com.oneinch.`object`.TokenQuote
import com.oneinch.on_chain_api.tx.FakeTransaction
import com.oneinch.repository.dao.FakeTokenQuoteEntity
import com.oneinch.repository.dao.toFakeTokenQuoteEntity
import com.oneinch.repository.dao.toTokenQuote
import org.springframework.stereotype.Component

@Component
open class FakeRepositoryManager(val repository: IRepository, val chain: Chain) : IRepositoryManager {

    init {
        fillWithFakeBalanceIfEmpty()
    }

    fun save(TokenQuoteEntity: FakeTokenQuoteEntity): FakeTokenQuoteEntity {
        return repository.save(TokenQuoteEntity)
    }

    fun saveTransaction(from: TokenQuote, to: TokenQuote, t: FakeTransaction) {
        TODO("Not yet implemented, include date")
    }

    fun getBalance(erc20: Token): TokenQuote? {
        return repository.findByAddress(erc20.address)?.toTokenQuote()
    }

    fun updateBalance(from: TokenQuote, to: TokenQuote) {
        val entity = repository.findByAddress(from.address)!!
        entity.readable -= from.readable // TODO: 05.09.2021 minus origin
        entity.origin -= from.origin // TODO: 05.09.2021 minus origin
        var entity2 = repository.findByAddress(to.address)
        if (entity2 == null) {
            entity2 = createTokenQuoteEntity(to.symbol, to.readable)
        } else {
            entity2.readable += to.readable
            entity2.origin += to.origin
        }
        save(entity)
        save(entity2)
    }

    private fun fillWithFakeBalanceIfEmpty() {
//        if (repository.count() == 0L) {
//            val usdt = createTokenQuoteEntity("USDT", 10000.0)
//            save(usdt)
//        }
    }

    private fun createTokenQuoteEntity(symbol: String, readable: Double): FakeTokenQuoteEntity {
        val token = chain.tokens.first { it.symbol == symbol }
        val tokenQuote = TokenQuote(token.symbol, token.address, readable)
        return tokenQuote.toFakeTokenQuoteEntity()
    }
}