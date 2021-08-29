
const val BASE_URL = "https://api.1inch.exchange/"
const val DECIMALS = 18

val BSC_DAI = Token("BSC_DAI", "0x1af3f329e8be154074d8769d1ffa4ee058b1dbc3")
val BSC_UST = Token("BSC_UST", "0x23396cf899ca06c4472205fc903bdb4de249d6fc")
val BSC_USDC = Token("BSC_USDC", "0x8ac76a51cc950d9822d68b83fe1ad97b32cd580d")
val BSC_USDT= Token("BSC_USDT", "0x55d398326f99059ff775485246999027b3197955")
val BSC_TUSD = Token("BSC_TUSD", "0x14016e85a25aeb13065688cafb43044c2ef86784")

val MATIC_DAI = Token("MATIC_DAI", "0x8f3cf7ad23cd3cadbd9735aff958023239c6a063")
val MATIC_UST = Token("MATIC_UST", "0x692597b009d13c4049a947cab2239b7d6517875f")
val MATIC_USDC = Token("MATIC_USDC", "0x2791bca1f2de4661ed88a30c99a7a9449aa84174")
val MATIC_USDT= Token("MATIC_USDT", "0xc2132d05d31c914a87c6611c10748aeb04b58e8f")

val WAIT_MESSAGE = "\n---------------- WAIT ----------------"

val AMOUNT_TO_SELL = "50000"

class Chain(val id: Int, val tokens: List<Token>)

val BSC = Chain(56, listOf(BSC_DAI, BSC_UST, BSC_USDC, BSC_USDT, BSC_TUSD))
val MATIC = Chain(137, listOf(MATIC_DAI, MATIC_UST, MATIC_USDC, MATIC_USDT))


