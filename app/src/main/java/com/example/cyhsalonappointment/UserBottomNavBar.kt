package com.example.cyhsalonappointment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf("services", "bookingHistory", "accountSelection")

    NavigationBar {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

        items.forEach { screen ->
            val isSelected = when(screen) {
                "services" -> currentRoute == "services"
                "bookingHistory" -> currentRoute?.startsWith("bookingHistory") == true
                "profile" -> currentRoute == "profile"
                else -> false
            }

            val onClickAction = when(screen) {
                "services" -> { { navController.navigate("services") } }
                "bookingHistory" -> { { navController.navigate("bookingHistory?isAdmin=false&status=") } }
                "profile" -> { { navController.navigate("profile") } }
                else -> { {} }
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = onClickAction,
                icon = {
                    when (screen) {
                        "services" -> Icon(Icons.Default.Home, "Services")
                        "bookingHistory" -> Icon(Icons.Default.DateRange, "Bookings")
                        "accountSelection" -> Icon(Icons.Default.AccountCircle, "Account")
                    }
                },
                label = {
                    Text(
                        when (screen) {
                            "services" -> "Services"
                            "bookingHistory" -> "Bookings"
                            else -> "Account"
                        }
                    )
                }
            )
        }
    }
}

