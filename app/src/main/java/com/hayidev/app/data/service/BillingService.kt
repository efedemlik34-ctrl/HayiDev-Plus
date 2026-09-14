package com.hayidev.app.data.service

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.hayidev.app.data.model.PremiumPlan
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class BillingService @Inject constructor(
    @ApplicationContext private val context: Context
) : PurchasesUpdatedListener {

    private var billingClient: BillingClient
    private var onPurchaseResult: ((PurchaseResult) -> Unit)? = null

    data class PurchaseResult(
        val success: Boolean,
        val purchase: Purchase? = null,
        val error: String? = null
    )

    init {
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()
    }

    fun connect(onReady: (() -> Unit)? = null) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    onReady?.invoke()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Retry connection
            }
        })
    }

    suspend fun queryProducts(productIds: List<String>): List<ProductInfo> {
        return suspendCancellableCoroutine { cont ->
            val productList = productIds.map { productId ->
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            }

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            billingClient.queryProductDetailsAsync(params) { result, details ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    val products = details.map { detail ->
                        ProductInfo(
                            productId = detail.productId,
                            title = detail.title,
                            description = detail.description,
                            price = detail.subscriptionOfferDetails?.firstOrNull()
                                ?.pricingPricingPhases?.pricingPhaseList?.firstOrNull()
                                ?.formattedPrice ?: "",
                            priceAmount = detail.subscriptionOfferDetails?.firstOrNull()
                                ?.pricingPricingPhases?.pricingPhaseList?.firstOrNull()
                                ?.priceAmountMicros?.div(1000000.0) ?: 0.0,
                            offerToken = detail.subscriptionOfferDetails?.firstOrNull()
                                ?.offerToken ?: ""
                        )
                    }
                    cont.resume(products)
                } else {
                    cont.resume(emptyList())
                }
            }
        }
    }

    fun launchPurchaseFlow(
        activity: Activity,
        productId: String,
        offerToken: String,
        onResult: (PurchaseResult) -> Unit
    ) {
        onPurchaseResult = onResult

        val productList = listOf(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(productId)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, details ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && details.isNotEmpty()) {
                val productDetails = details[0]
                val offerToken = productDetails.subscriptionOfferDetails?.firstOrNull()?.offerToken ?: ""

                val flowParams = BillingFlowParams.newBuilder()
                    .setProductDetailsParamsList(
                        listOf(
                            BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .setOfferToken(offerToken)
                                .build()
                        )
                    )
                    .build()

                billingClient.launchBillingFlow(activity, flowParams)
            } else {
                onPurchaseResult?.invoke(PurchaseResult(false, error = "Product not found"))
            }
        }
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { purchase ->
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        acknowledgePurchase(purchase)
                        onPurchaseResult?.invoke(PurchaseResult(true, purchase))
                    }
                }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                onPurchaseResult?.invoke(PurchaseResult(false, error = "Purchase cancelled"))
            }
            else -> {
                onPurchaseResult?.invoke(PurchaseResult(false, error = result.debugMessage))
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient.acknowledgePurchase(params) { result ->
            if (result.responseCode != BillingClient.BillingResponseCode.OK) {
                // Handle error
            }
        }
    }

    suspend fun queryActivePurchases(): List<Purchase> {
        return suspendCancellableCoroutine { cont ->
            billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder()
                    .setProductType(BillingClient.ProductType.SUBS)
                    .build()
            ) { result, purchases ->
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    cont.resume(purchases.filter {
                        it.purchaseState == Purchase.PurchaseState.PURCHASED
                    })
                } else {
                    cont.resume(emptyList())
                }
            }
        }
    }

    fun disconnect() {
        billingClient.endConnection()
    }

    data class ProductInfo(
        val productId: String,
        val title: String,
        val description: String,
        val price: String,
        val priceAmount: Double,
        val offerToken: String
    )
}
