import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface OneInchService {

    @GET("v3.0/{id}/quote")
    fun getQuoteOnBSC(
        @Path("id") chainId: Int,
        @Query("fromTokenAddress") from: String,
        @Query("toTokenAddress") to: String,
        @Query("amount") amount: String
    ): Call<OneInchResponse>
}

class Token(val name: String, val address: String)

class OneInchClient() {
    private val mapper: ObjectMapper = ObjectMapper()

    init {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    private val jacksonConverter = JacksonConverterFactory.create(mapper)
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()
    private val oneInchService: OneInchService = Retrofit.Builder()
        .client(httpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(jacksonConverter)
        .build()
        .create(OneInchService::class.java)

    fun getQuote(chainId: Int, from: Token, to: Token, quote: String) {
        val response = oneInchService.getQuoteOnBSC(chainId, from.address, to.address, quote.addDecimals(DECIMALS)).execute()
        if (response.isSuccessful) {
            val finalQuote = response.body()?.amountReceived?.removeDecimals(DECIMALS).toString()
            val advantage = calculateAdvantage(quote, finalQuote)
            var isOpportunity = ""
            if (advantage > 0.5) {
               isOpportunity = "Opportunity !!!"
            }
            getLogger()
                .info("${from.name}: $quote, ${to.name}: $finalQuote," +
                        "   advantage: ${String.format("%.2f", advantage)}%    $isOpportunity")
        } else {
            getLogger().info("Error, response status: ${response.code()}")
        }
    }
}


