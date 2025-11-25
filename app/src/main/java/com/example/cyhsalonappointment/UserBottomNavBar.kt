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
    val items = listOf("services", "bookingHistory", "account")

    NavigationBar {
        items.forEach { screen ->
            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

            NavigationBarItem(
                selected = currentRoute == screen,
                onClick = { navController.navigate(screen) },
                icon = {
                    when (screen) {
                        "services" -> Icon(Icons.Default.Home, "Services")
                        "bookingHistory" -> Icon(Icons.Default.DateRange, "Bookings")
                        "account" -> Icon(Icons.Default.AccountCircle, "Account")
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
