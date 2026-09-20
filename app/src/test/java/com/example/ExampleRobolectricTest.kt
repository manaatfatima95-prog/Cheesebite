package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CheeseBiteRepository
import com.example.viewmodel.CheeseBiteViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Cheese Bite", appName)
  }

  @Test
  fun `verify food menu loaded with categories and deals`() {
    assertTrue(CheeseBiteRepository.foodItems.isNotEmpty())
    assertTrue(CheeseBiteRepository.categories.isNotEmpty())

    val deals = CheeseBiteRepository.foodItems.filter { it.category == "deals" }
    assertTrue(deals.isNotEmpty())

    val specialPizzas = CheeseBiteRepository.foodItems.filter { it.category == "special_pizza" }
    assertTrue(specialPizzas.isNotEmpty())
  }

  @Test
  fun `verify cart additions and promo code application`() {
    val viewModel = CheeseBiteViewModel()
    val item = CheeseBiteRepository.foodItems.first { it.id == "bur_zinger" }

    viewModel.addToCart(foodItem = item, quantity = 2)

    val state = viewModel.uiState.value
    assertTrue(state.cartItems.any { it.foodItem.id == "bur_zinger" })

    val applied = viewModel.applyPromoCode("CHEESE10")
    assertTrue(applied)
    assertEquals(10.0, viewModel.uiState.value.promoDiscountPercent, 0.01)
  }
}
