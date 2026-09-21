package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.FoodItem
import com.example.ui.components.AppHeader
import com.example.ui.components.CheeseBiteBottomNav
import com.example.ui.components.ReviewSuccessNotification
import com.example.ui.components.StickyCartBar
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CategoriesScreen
import com.example.ui.screens.ChatbotScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.FavoritesScreen
import com.example.ui.screens.FoodDetailScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.OrderHistoryScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.RestaurantDetailsScreen
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.CheeseBiteTheme
import com.example.viewmodel.CheeseBiteViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Menu : Screen("menu")
    object Cart : Screen("cart")
    object Orders : Screen("orders")
    object Profile : Screen("profile")
    object Search : Screen("search")
    object RestaurantDetails : Screen("restaurant_details")
    object FoodDetail : Screen("food_detail")
    object Checkout : Screen("checkout")
    object OrderTracking : Screen("order_tracking")
    object Favorites : Screen("favorites")
    object Notifications : Screen("notifications")
    object Chatbot : Screen("chatbot")
}

@Composable
fun CheeseBiteApp(
    viewModel: CheeseBiteViewModel = viewModel(),
    initialScreen: Screen = Screen.Splash
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf<Screen>(initialScreen) }
    var selectedFoodItem by remember { mutableStateOf<FoodItem?>(null) }
    var trackingOrderId by remember { mutableStateOf<String?>(null) }
    var justPlacedOrderId by remember { mutableStateOf<String?>(null) }

    // Android system Back button handling:
    // Rule 1: If user is on ANY screen other than Home, navigate to Home and keep app open.
    // Rule 2: If user is on Home, BackHandler is disabled, allowing standard Android app exit.
    BackHandler(enabled = currentScreen != Screen.Home) {
        currentScreen = Screen.Home
    }

    // Listen to snackbars
    LaunchedEffect(Unit) {
        viewModel.snackbarMessages.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    CheeseBiteTheme(darkTheme = uiState.isDarkTheme) {
        val showBottomNav = when (currentScreen) {
            Screen.Home, Screen.Menu, Screen.Cart, Screen.Orders, Screen.Profile -> true
            else -> false
        }

        val showHeader = when (currentScreen) {
            Screen.Home -> true
            else -> false
        }

        val showStickyCart = when (currentScreen) {
            Screen.Home, Screen.Menu, Screen.RestaurantDetails -> uiState.cartItemCount > 0
            else -> false
        }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                if (showHeader) {
                    AppHeader(
                        locationName = uiState.selectedAddress.landmark.ifBlank { uiState.selectedAddress.fullAddress },
                        onLocationClick = { currentScreen = Screen.Checkout },
                        onSearchClick = { currentScreen = Screen.Search },
                        onNotificationsClick = { currentScreen = Screen.Notifications },
                        unreadNotificationCount = uiState.notifications.count { !it.isRead }
                    )
                }
            },
            bottomBar = {
                if (showBottomNav) {
                    CheeseBiteBottomNav(
                        currentRoute = currentScreen.route,
                        onNavigate = { route ->
                            currentScreen = when (route) {
                                "home" -> Screen.Home
                                "menu" -> Screen.Menu
                                "cart" -> Screen.Cart
                                "orders" -> Screen.Orders
                                "profile" -> Screen.Profile
                                else -> Screen.Home
                            }
                        },
                        cartBadgeCount = uiState.cartItemCount,
                        hasActiveOrder = uiState.activeOrder != null
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentScreen) {
                    Screen.Splash -> {
                        SplashScreen(
                            onSplashFinished = { currentScreen = Screen.Onboarding }
                        )
                    }
                    Screen.Onboarding -> {
                        OnboardingScreen(
                            onFinish = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Home -> {
                        HomeScreen(
                            viewModel = viewModel,
                            onNavigateToFoodDetail = { item ->
                                selectedFoodItem = item
                                currentScreen = Screen.FoodDetail
                            },
                            onNavigateToSearch = { currentScreen = Screen.Search },
                            onNavigateToCategories = { currentScreen = Screen.Menu },
                            onNavigateToRestaurant = { currentScreen = Screen.RestaurantDetails },
                            onNavigateToChatbot = { currentScreen = Screen.Chatbot }
                        )
                    }
                    Screen.Menu -> {
                        CategoriesScreen(
                            viewModel = viewModel,
                            onCategorySelected = { categoryId ->
                                // category is selected in viewModel
                            },
                            onNavigateToFoodDetail = { item ->
                                selectedFoodItem = item
                                currentScreen = Screen.FoodDetail
                            },
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Cart -> {
                        CartScreen(
                            viewModel = viewModel,
                            onNavigateToCheckout = { currentScreen = Screen.Checkout },
                            onExploreMenuClick = { currentScreen = Screen.Menu }
                        )
                    }
                    Screen.Orders -> {
                        OrderHistoryScreen(
                            viewModel = viewModel,
                            onTrackOrder = { orderId ->
                                trackingOrderId = orderId
                                currentScreen = Screen.OrderTracking
                            },
                            onExploreMenuClick = { currentScreen = Screen.Menu }
                        )
                    }
                    Screen.Profile -> {
                        ProfileScreen(
                            viewModel = viewModel,
                            onNavigateToFavorites = { currentScreen = Screen.Favorites },
                            onNavigateToOrders = { currentScreen = Screen.Orders },
                            onNavigateToRestaurant = { currentScreen = Screen.RestaurantDetails },
                            onNavigateToChatbot = { currentScreen = Screen.Chatbot }
                        )
                    }
                    Screen.Search -> {
                        SearchScreen(
                            viewModel = viewModel,
                            onBackClick = { currentScreen = Screen.Home },
                            onNavigateToFoodDetail = { item ->
                                selectedFoodItem = item
                                currentScreen = Screen.FoodDetail
                            }
                        )
                    }
                    Screen.RestaurantDetails -> {
                        RestaurantDetailsScreen(
                            viewModel = viewModel,
                            onBackClick = { currentScreen = Screen.Home },
                            onNavigateToFoodDetail = { item ->
                                selectedFoodItem = item
                                currentScreen = Screen.FoodDetail
                            }
                        )
                    }
                    Screen.FoodDetail -> {
                        val item = selectedFoodItem ?: uiState.allFoodItems.first()
                        FoodDetailScreen(
                            foodItem = item,
                            viewModel = viewModel,
                            onBackClick = { currentScreen = Screen.Home },
                            onNavigateToCart = { currentScreen = Screen.Cart }
                        )
                    }
                    Screen.Checkout -> {
                        CheckoutScreen(
                            viewModel = viewModel,
                            onBackClick = { currentScreen = Screen.Cart },
                            onOrderSuccess = { orderId ->
                                trackingOrderId = orderId
                                justPlacedOrderId = orderId
                                currentScreen = Screen.OrderTracking
                            }
                        )
                    }
                    Screen.OrderTracking -> {
                        OrderTrackingScreen(
                            orderId = trackingOrderId,
                            viewModel = viewModel,
                            onBackClick = { currentScreen = Screen.Home },
                            onExploreMenuClick = { currentScreen = Screen.Home },
                            showSuccessConfirmation = (justPlacedOrderId != null && justPlacedOrderId == trackingOrderId)
                        )
                    }
                    Screen.Favorites -> {
                        FavoritesScreen(
                            viewModel = viewModel,
                            onBackClick = { currentScreen = Screen.Profile },
                            onNavigateToFoodDetail = { item ->
                                selectedFoodItem = item
                                currentScreen = Screen.FoodDetail
                            },
                            onExploreMenuClick = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Notifications -> {
                        NotificationsScreen(
                            viewModel = viewModel,
                            onBackClick = { currentScreen = Screen.Home },
                            onExploreMenuClick = { currentScreen = Screen.Home }
                        )
                    }
                    Screen.Chatbot -> {
                        ChatbotScreen(
                            onBackClick = { currentScreen = Screen.Home }
                        )
                    }
                }

                // Sticky Cart Bar (floating above bottom navigation)
                if (showStickyCart) {
                    StickyCartBar(
                        itemCount = uiState.cartItemCount,
                        totalPrice = uiState.finalTotal,
                        onViewCartClick = { currentScreen = Screen.Cart },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Polished Review Success Notification (floating above bottom navigation)
                ReviewSuccessNotification(
                    visible = uiState.isReviewSuccessNotificationVisible,
                    eventId = uiState.reviewSuccessEventId,
                    onDismiss = { viewModel.dismissReviewSuccessNotification() },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = if (showStickyCart) 72.dp else 12.dp)
                )
            }
        }
    }
}
