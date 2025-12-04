package com.example.cyhsalonappointment

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cyhsalonappointment.local.AppDatabase
import com.example.cyhsalonappointment.local.datastore.UserSessionManager
import com.example.cyhsalonappointment.screens.Admin.AdminRepository
import com.example.cyhsalonappointment.screens.Admin.AdminViewModel
import com.example.cyhsalonappointment.screens.Admin.AdminViewModelFactory
import com.example.cyhsalonappointment.screens.AdminLogin.AdminLoginScreen
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryScreen
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryViewModel
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryViewModelFactory
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
import com.example.cyhsalonappointment.screens.StylistSelection.StylistSelectionScreen
import com.example.cyhsalonappointment.screens.StylistSelection.StylistSelectionViewModel
import com.example.cyhsalonappointment.screens.StylistSelection.StylistSelectionViewModelFactory
import com.example.cyhsalonappointment.screens.TempPaymentScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            val navController = rememberNavController()
            val customerDao = AppDatabase.getDatabase(this).customerDao()
            val adminDao = AppDatabase.getDatabase(this).adminDao()
            val repository = CustomerRepository(customerDao)
            val adminRepo = AdminRepository(adminDao)
            val session = UserSessionManager(this)
            val customerViewModel: CustomerViewModel =
                viewModel(factory = CustomerViewModelFactory(repository, session))
            val adminViewModel: AdminViewModel = viewModel(
                factory = AdminViewModelFactory(adminRepo)
            )
            val stylistDao = AppDatabase.getDatabase(this).stylistDao()
            val stylistVM: StylistSelectionViewModel = viewModel(
                factory = StylistSelectionViewModelFactory(stylistDao)
            )


            NavHost(
                navController = navController,
                startDestination = "logo"
            ) {
                composable("logo"){
                    LogoScreen(onLoginButtonClicked = { navController.navigate("login") },
                        onSignUpButtonClicked = { navController.navigate("sign_up") },
                        onAdminLoginButtonClicked = { navController.navigate("admin_login") })
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

                composable("admin_login"){
                    AdminLoginScreen(viewModel = adminViewModel,
                        onBackButtonClicked = { navController.popBackStack() },
                        onSuccess = { navController.navigate("services") })  //navigate to ys staff
                }

                composable("forgot_password"){
                    ForgotPasswordScreen(viewModel = customerViewModel,
                        onBackButtonClicked = { navController.popBackStack() })
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
                    EditProfileScreen(viewModel = customerViewModel,
                        onBackButtonClicked = { navController.popBackStack() },
                        onSaveSuccess = { navController.popBackStack() })
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
                    route = "reschedule/{appointmentId}/{serviceName}/{date}/{timeSlotId}",
                    arguments = listOf(
                        navArgument("appointmentId") { type = NavType.StringType },
                        navArgument("serviceName") { type = NavType.StringType },
                        navArgument("date") { type = NavType.StringType },
                        navArgument("timeSlotId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
                    val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
                    val oldDate = backStackEntry.arguments?.getString("date") ?: ""
                    val oldTime = backStackEntry.arguments?.getString("timeSlotId") ?: ""

                    RescheduleScreen(
                        navController = navController,
                        appointmentId = appointmentId,
                        serviceName = serviceName,
                        oldDate = oldDate,
                        oldTime = oldTime
                    )
                }
                composable(
                    "selectStylist/{serviceName}/{selectedDate}/{selectedTimeSlot}",
                    arguments = listOf(
                        navArgument("serviceName") { type = NavType.StringType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedTimeSlot") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
                    val date = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val slot = backStackEntry.arguments?.getString("selectedTimeSlot") ?: ""

                    StylistSelectionScreen(
                        navController = navController,
                        stylistVM = stylistVM,
                        serviceName = serviceName,
                        selectedDate = date,
                        selectedTimeSlot = slot
                    )
                }

                composable(
                    "tempPayment/{serviceName}/{selectedDate}/{selectedTimeSlot}/{stylistId}",
                    arguments = listOf(
                        navArgument("serviceName") { type = NavType.StringType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedTimeSlot") { type = NavType.StringType },
                        navArgument("stylistId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
                    val date = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val slot = backStackEntry.arguments?.getString("selectedTimeSlot") ?: ""
                    val stylistId = backStackEntry.arguments?.getString("stylistId") ?: ""

                    TempPaymentScreen(
                        navController = navController,
                        serviceName = serviceName,
                        selectedDate = date,
                        selectedTimeSlot = slot,
                        stylistId = stylistId
                    )
                }



            }
        }
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "appointment_channel",
                "Appointment Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifies before appointment time"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

}

