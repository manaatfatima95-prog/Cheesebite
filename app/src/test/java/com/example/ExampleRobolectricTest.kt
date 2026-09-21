package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.CheeseBiteRepository
import com.example.viewmodel.CheeseBiteViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
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

  @Test
  fun `verify food search queries and clear search`() {
    val viewModel = CheeseBiteViewModel()

    // Search for burger
    viewModel.onSearchQueryChanged("zinger")
    val zingerResults = viewModel.uiState.value.searchResults
    assertTrue("Search for 'zinger' should return items", zingerResults.isNotEmpty())
    assertTrue("Results should include Zinger", zingerResults.any { it.name.contains("Zinger", ignoreCase = true) })

    // Search for pizza
    viewModel.onSearchQueryChanged("pizza")
    val pizzaResults = viewModel.uiState.value.searchResults
    assertTrue("Search for 'pizza' should return pizzas", pizzaResults.isNotEmpty())

    // Empty search
    viewModel.onSearchQueryChanged("")
    assertTrue("Empty search query should have empty results", viewModel.uiState.value.searchResults.isEmpty())
  }

  @Test
  fun `verify review submission success flow and notification display`() {
    val viewModel = CheeseBiteViewModel()
    assertFalse(viewModel.uiState.value.isReviewSuccessNotificationVisible)
    assertFalse(viewModel.uiState.value.isSubmittingReview)

    var successInvoked = false
    viewModel.submitReview(
      rating = 5.0,
      reviewText = "Delicious crown crust pizza!",
      onSuccess = { successInvoked = true }
    )

    // Immediately while processing, success notification must NOT be visible yet
    assertTrue(viewModel.uiState.value.isSubmittingReview)
    assertFalse(viewModel.uiState.value.isReviewSuccessNotificationVisible)

    // Advance virtual time / let coroutine finish delay
    org.robolectric.shadows.ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

    // After completion, success callback invoked and notification visible
    assertTrue(successInvoked)
    assertFalse(viewModel.uiState.value.isSubmittingReview)
    assertTrue(viewModel.uiState.value.isReviewSuccessNotificationVisible)
    assertNotNull(viewModel.uiState.value.reviewSuccessEventId)
  }

  @Test
  fun `verify review submission validation failure does not show success notification`() {
    val viewModel = CheeseBiteViewModel()
    var errorInvoked = false

    viewModel.submitReview(
      rating = 0.0,
      reviewText = "No star selected",
      onError = { errorInvoked = true }
    )

    org.robolectric.shadows.ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

    // Validation failure: success notification must NOT appear
    assertTrue(errorInvoked)
    assertFalse(viewModel.uiState.value.isReviewSuccessNotificationVisible)
    assertFalse(viewModel.uiState.value.isSubmittingReview)
  }

  @Test
  fun `verify duplicate protection generates distinct event ids and dismiss hides notification`() {
    val viewModel = CheeseBiteViewModel()

    // First submission
    viewModel.submitReview(rating = 4.0, reviewText = "Great burger!")
    org.robolectric.shadows.ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

    val firstEventId = viewModel.uiState.value.reviewSuccessEventId
    assertNotNull(firstEventId)
    assertTrue(viewModel.uiState.value.isReviewSuccessNotificationVisible)

    // Dismiss notification
    viewModel.dismissReviewSuccessNotification()
    assertFalse(viewModel.uiState.value.isReviewSuccessNotificationVisible)

    // Second submission generates distinct event ID (duplicate protection)
    viewModel.submitReview(rating = 5.0, reviewText = "Another great order!")
    org.robolectric.shadows.ShadowLooper.runUiThreadTasksIncludingDelayedTasks()

    val secondEventId = viewModel.uiState.value.reviewSuccessEventId
    assertNotNull(secondEventId)
    assertNotEquals(firstEventId, secondEventId)
    assertTrue(viewModel.uiState.value.isReviewSuccessNotificationVisible)
  }

  @Test
  fun `verify category based filtering returns only requested products`() {
    val viewModel = CheeseBiteViewModel()

    // Pizza selection: returns special and regular pizzas, NO burgers, NO fries, NO deals
    val pizzaItems = viewModel.getItemsForCategory("pizza")
    assertTrue("Pizza items should not be empty", pizzaItems.isNotEmpty())
    assertTrue("All pizza items must be pizza category", pizzaItems.all { it.category == "special_pizza" || it.category == "regular_pizza" })
    assertFalse("Pizza items must not contain burgers", pizzaItems.any { it.category == "burgers" })
    assertFalse("Pizza items must not contain fries", pizzaItems.any { it.category == "fries" })
    assertFalse("Pizza items must not contain deals", pizzaItems.any { it.category == "deals" })

    // Burger selection: returns ONLY burgers
    val burgerItems = viewModel.getItemsForCategory("burgers")
    assertTrue("Burger items should not be empty", burgerItems.isNotEmpty())
    assertTrue("All burger items must be burgers category", burgerItems.all { it.category == "burgers" })
    assertFalse("Burger items must not contain pizza", burgerItems.any { it.category.contains("pizza") })
    assertFalse("Burger items must not contain deals", burgerItems.any { it.category == "deals" })

    // Deals selection: returns ONLY deals
    val dealItems = viewModel.getItemsForCategory("deals")
    assertTrue("Deal items should not be empty", dealItems.isNotEmpty())
    assertTrue("All deal items must have category deals", dealItems.all { it.category == "deals" })
    assertFalse("Deals must not contain standalone regular pizzas", dealItems.any { it.category == "regular_pizza" })
    assertFalse("Deals must not contain standalone burgers", dealItems.any { it.category == "burgers" })

    // Shawarma selection: returns ONLY shawarma
    val shawarmaItems = viewModel.getItemsForCategory("shawarma")
    assertTrue("Shawarma items should not be empty", shawarmaItems.isNotEmpty())
    assertTrue("All shawarma items must be shawarma", shawarmaItems.all { it.category == "shawarma" })

    // Fries selection: returns ONLY fries
    val friesItems = viewModel.getItemsForCategory("fries")
    assertTrue("Fries items should not be empty", friesItems.isNotEmpty())
    assertTrue("All fries items must be fries", friesItems.all { it.category == "fries" })

    // Drinks selection: returns ONLY drinks
    val drinksItems = viewModel.getItemsForCategory("drinks")
    assertTrue("Drinks items should not be empty", drinksItems.isNotEmpty())
    assertTrue("All drinks items must be drinks", drinksItems.all { it.category == "drinks" })
  }

  @Test
  fun `verify focused search results avoid unrelated cross contamination`() {
    val viewModel = CheeseBiteViewModel()

    // Search for fries: should return ONLY fries, not pizzas or burgers
    viewModel.onSearchQueryChanged("fries")
    val friesResults = viewModel.uiState.value.searchResults
    assertTrue("Search for fries should return items", friesResults.isNotEmpty())
    assertTrue("Fries search should only return fries", friesResults.all { it.category == "fries" || it.name.contains("fries", ignoreCase = true) })
    assertFalse("Fries search should not contain pizza", friesResults.any { it.category.contains("pizza") })
    assertFalse("Fries search should not contain burger", friesResults.any { it.category == "burgers" })

    // Search for shawarma: should return ONLY shawarma
    viewModel.onSearchQueryChanged("shawarma")
    val shawarmaResults = viewModel.uiState.value.searchResults
    assertTrue("Search for shawarma should return items", shawarmaResults.isNotEmpty())
    assertTrue("Shawarma search should only return shawarma", shawarmaResults.all { it.category == "shawarma" })

    // Search for zinger: should return zinger burgers or zinger burger deals, NOT pizza or fries
    viewModel.onSearchQueryChanged("zinger")
    val zingerResults = viewModel.uiState.value.searchResults
    assertTrue("Search for zinger should return items", zingerResults.isNotEmpty())
    assertFalse("Zinger search should not contain pizza", zingerResults.any { it.category.contains("pizza") && !it.name.contains("zinger", ignoreCase = true) })
    assertFalse("Zinger search should not contain fries", zingerResults.any { it.category == "fries" })
  }
}
