package com.example.cyhsalonappointment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cyhsalonappointment.screens.Account.AccountScreen
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryScreen
import com.example.cyhsalonappointment.screens.ServiceDescription.ServiceDescriptionScreen
import com.example.cyhsalonappointment.screens.ServiceMainScreen.ServicesMainScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = "services"
            ) {

                composable("services") {
                    ServicesMainScreen(navController)
                }

                composable("bookingHistory") {
                    BookingHistoryScreen(navController)
                }

                composable("account") {
                    AccountScreen(navController)
                }

                composable(
                    route = "serviceDetail/{name}/{desc}/{price}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType },
                        navArgument("desc") { type = NavType.StringType },
                        navArgument("price") { type = NavType.StringType }
                    )
                ) { backStackEntry ->

                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val desc = backStackEntry.arguments?.getString("desc") ?: ""
                    val price = backStackEntry.arguments?.getString("price") ?: ""

                    ServiceDescriptionScreen(
                        navController = navController,
                        serviceName = name,
                        serviceDescription = desc,
                        servicePrice = price
                    )
                }




            }
        }
    }
}

