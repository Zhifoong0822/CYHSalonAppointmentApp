package com.example.cyhsalonappointment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cyhsalonappointment.local.datastore.UserSessionManager
import com.example.cyhsalonappointment.screens.Account.AccountScreen
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryScreen
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryViewModel
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryViewModelFactory
import com.example.cyhsalonappointment.screens.Customer.CustomerDatabase
import com.example.cyhsalonappointment.screens.Customer.CustomerRepository
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModel
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModelFactory
import com.example.cyhsalonappointment.screens.EditProfile.EditProfileScreen
import com.example.cyhsalonappointment.screens.ForgotPassword.ForgotPasswordScreen
import com.example.cyhsalonappointment.screens.Reschedule.RescheduleScreen
import com.example.cyhsalonappointment.screens.Login.LoginScreen
import com.example.cyhsalonappointment.screens.Logo.LogoScreen
import com.example.cyhsalonappointment.screens.Profile.ProfileScreen
import com.example.cyhsalonappointment.screens.ServiceDescription.ServiceDescriptionScreen
import com.example.cyhsalonappointment.screens.ServiceMainScreen.ServicesMainScreen
import com.example.cyhsalonappointmentscreens.BookingScreen.BookingScreen

import com.example.cyhsalonappointment.screens.SignUp.SignUpScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val customerDao = CustomerDatabase.getDatabase(this).customerDao()
            val repository = CustomerRepository(customerDao)
            val session = UserSessionManager(this)
            val customerViewModel: CustomerViewModel =
                viewModel(factory = CustomerViewModelFactory(repository, session))

            NavHost(
                navController = navController,
                startDestination = "logo"
            ) {
                composable("logo"){
                    LogoScreen(onLoginButtonClicked = { navController.navigate("login") },
                        onSignUpButtonClicked = { navController.navigate("sign_up") })
                }

                composable("login"){
                    LoginScreen(viewModel = customerViewModel,
                        onSuccess = { navController.navigate("services") },
                        onBackButtonClicked = { navController.navigate("logo") },
                        onForgotPasswordClicked = { navController.navigate("forgot_password") })
                }

                composable("sign_up"){
                    SignUpScreen(viewModel = customerViewModel,
                        onBackButtonClicked = { navController.navigate("logo") },
                        onSuccess = { navController.navigate("logo") })
                }

                composable("forgot_password"){
                    ForgotPasswordScreen()
                }

                composable("profile") {
                    val userEmail by session.getUserEmail().collectAsState(initial = "")
                    if (userEmail.isNotEmpty()) {
                        ProfileScreen(
                            customerEmail = userEmail,
                            viewModel = customerViewModel,
                            onBackButtonClicked = { navController.popBackStack() },
                            onEditProfileClicked = { navController.navigate("edit_profile") },
                            onLogoutClicked = {
                                navController.navigate("logo") {
                                    popUpTo("services") { inclusive = true }
                                }
                            }
                        )
                    } else {
                        // Show a simple loading spinner while userId is being loaded
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }
                }

                composable("edit_profile"){
                    EditProfileScreen()
                }

                composable("services") {
                    ServicesMainScreen(navController)
                }

                composable("bookingHistory") {
                    val dao = App.db.appointmentDao()
                    val bhVM: BookingHistoryViewModel = viewModel(
                        factory = BookingHistoryViewModelFactory(dao)
                    )

                    BookingHistoryScreen(
                        navController = navController,
                        viewModel = bhVM,
                        onRescheduleClick = { appt ->
                            navController.navigate("reschedule/ServiceName/${appt.date}/${appt.timeSlotId}")
                        }
                    )
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
                composable(
                    route = "booking/{serviceName}",
                    arguments = listOf(
                        navArgument("serviceName") { type = NavType.StringType }
                    )
                ) { backStackEntry ->

                    val name = backStackEntry.arguments?.getString("serviceName") ?: ""

                    BookingScreen(
                        navController = navController,
                        serviceName = name
                    )
                }

                composable(
                    "reschedule/{name}/{date}/{time}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType },
                        navArgument("date") { type = NavType.StringType },
                        navArgument("time") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val date = backStackEntry.arguments?.getString("date") ?: ""
                    val time = backStackEntry.arguments?.getString("time") ?: ""

                    RescheduleScreen(
                        navController = navController,
                        serviceName = name,
                        oldDate = date,
                        oldTime = time
                    )
                }







            }
        }
    }
}

