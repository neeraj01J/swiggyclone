package com.example.foodclub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodclub.ui.navigation.NavGraph
import com.example.foodclub.ui.navigation.Screen
import com.example.foodclub.ui.theme.FoodclubTheme
import com.example.foodclub.ui.theme.OrangePrimary
import com.example.foodclub.ui.theme.OrangeLight
import com.example.foodclub.ui.theme.White
import com.example.foodclub.ui.viewmodel.CartViewModel
import com.example.foodclub.ui.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodclubTheme {
                val navController = rememberNavController()
                val cartViewModel: CartViewModel = viewModel()
                val profileViewModel: ProfileViewModel = viewModel()
                
                val items = listOf(
                    Triple(Screen.Home, "Home", Icons.Filled.Home),
                    Triple(Screen.Search, "Search", Icons.Filled.Search),
                    Triple(Screen.Cart, "Cart", Icons.Filled.ShoppingCart),
                    Triple(Screen.Profile, "Account", Icons.Filled.Person),
                )

                Scaffold(
                    topBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        val showTopRibbon = currentDestination?.route != Screen.Login.route

                        if (showTopRibbon) {
                            // Ribbon expanded to topmost with solid color
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(OrangePrimary)
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .statusBarsPadding()
                                        .height(6.dp)
                                )
                            }
                        }
                    },
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        
                        val showBottomBar = currentDestination?.route in listOf(
                            Screen.Home.route,
                            Screen.Search.route,
                            Screen.Cart.route,
                            Screen.Profile.route
                        )

                        if (showBottomBar) {
                            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                                items.forEach { (screen, label, icon) ->
                                    NavigationBarItem(
                                        icon = { Icon(icon, contentDescription = label) },
                                        label = { Text(label) },
                                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = OrangePrimary,
                                            selectedTextColor = OrangePrimary,
                                            indicatorColor = OrangeLight
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        NavGraph(
                            navController = navController, 
                            cartViewModel = cartViewModel,
                            profileViewModel = profileViewModel
                        )
                    }
                }
            }
        }
    }
}