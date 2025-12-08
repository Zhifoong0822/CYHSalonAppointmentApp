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
import com.example.cyhsalonappointment.App.Companion.db
import com.example.cyhsalonappointment.ServiceDetails.ServiceDetailViewModel
import com.example.cyhsalonappointment.ServiceDetails.ServiceDetailViewModelFactory
import com.example.cyhsalonappointment.ServiceDetails.ServiceDetailsScreen
import com.example.cyhsalonappointment.data.ServiceRepository
import com.example.cyhsalonappointment.local.AppDatabase
import com.example.cyhsalonappointment.local.datastore.UserSessionManager
import com.example.cyhsalonappointment.local.entity.SalonService
import com.example.cyhsalonappointment.screens.Admin.AddServiceScreen
import com.example.cyhsalonappointment.screens.Admin.AdminHomeScreen
import com.example.cyhsalonappointment.screens.Admin.AdminRepository
import com.example.cyhsalonappointment.screens.Admin.AdminViewModel
import com.example.cyhsalonappointment.screens.Admin.AdminViewModelFactory
import com.example.cyhsalonappointment.screens.Admin.EditServiceScreen
import com.example.cyhsalonappointment.screens.Admin.ServiceListScreen
import com.example.cyhsalonappointment.screens.Admin.ServiceViewModel
import com.example.cyhsalonappointment.screens.AdminLogin.AdminLoginScreen
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryScreen
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryViewModel
import com.example.cyhsalonappointment.screens.BookingHistory.BookingHistoryViewModelFactory
import com.example.cyhsalonappointment.screens.Customer.CustomerRepository
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModel
import com.example.cyhsalonappointment.screens.Customer.CustomerViewModelFactory
import com.example.cyhsalonappointment.screens.EditProfile.EditProfileScreen
import com.example.cyhsalonappointment.screens.ForgotPassword.ForgotPasswordScreen
import com.example.cyhsalonappointment.screens.Login.LoginScreen
import com.example.cyhsalonappointment.screens.Logo.LogoScreen
import com.example.cyhsalonappointment.screens.Profile.ProfileScreen
import com.example.cyhsalonappointment.screens.Reschedule.RescheduleScreen
import com.example.cyhsalonappointment.screens.ServiceDescription.ServiceDescriptionScreen
import com.example.cyhsalonappointment.screens.ServiceMainScreen.ServicesMainScreen
import com.example.cyhsalonappointment.screens.SignUp.SignUpScreen
import com.example.cyhsalonappointment.screens.StylistSelection.StylistSelectionScreen
import com.example.cyhsalonappointment.screens.StylistSelection.StylistSelectionViewModel
import com.example.cyhsalonappointment.screens.StylistSelection.StylistSelectionViewModelFactory
import com.example.cyhsalonappointment.screens.TempPaymentScreen
import com.example.cyhsalonappointment.screens.Admin.ReportRepository
import com.example.cyhsalonappointment.screens.Admin.ReportViewModel
import com.example.cyhsalonappointment.screens.Admin.ReportViewModelFactory
import com.example.cyhsalonappointment.screens.Admin.ServiceViewModelFactory
import com.example.cyhsalonappointmentscreens.BookingScreen.BookingScreen
import com.example.cyhsalonappointment.screens.Admin.Reports.DailyReportScreen
import com.example.cyhsalonappointment.screens.Admin.Reports.WeeklyReportScreen
import com.example.cyhsalonappointment.screens.Admin.Reports.MonthlyReportScreen
import com.example.cyhsalonappointment.screens.Admin.Reports.CustomerReportScreen


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel()
        setContent {
            val navController = rememberNavController()
            val serviceDao = AppDatabase.getDatabase(this).serviceDao()
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

            val serviceRepo = ServiceRepository(serviceDao)
            val stylistVM: StylistSelectionViewModel = viewModel(
                factory = StylistSelectionViewModelFactory(stylistDao,serviceRepo)
            )
            val serviceVM: ServiceViewModel = viewModel(
                factory = ServiceViewModelFactory(serviceRepo)
            )
            val serviceDetailsViewModel: ServiceDetailViewModel = viewModel(
                factory = ServiceDetailViewModelFactory(serviceRepo)
            )
            val reportDao = db.reportDao()
            val reportRepo = ReportRepository(reportDao)
            val reportVM: ReportViewModel = viewModel(factory = ReportViewModelFactory(reportRepo))



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
                        onSuccess = { navController.navigate("admin_home") }
                    )  //navigate to ys staff
                }

                // ----------------------- ADMIN NAVIGATION -----------------------
                composable("admin_home") {
                    AdminHomeScreen(
                        // go to the *new* service list
                        onManageServices = { navController.navigate("admin_services") },
                        onGenerateDailyReport = { navController.navigate("daily_report") },
                        onGenerateWeeklyReport = { navController.navigate("weekly_report") },
                        onGenerateMonthlyReport = { navController.navigate("monthly_report") },
                        onGenerateCustomerReport = { navController.navigate("customer_report") },
                        onLogOutButtonClicked = { navController.navigate("logo") }
                    )
                }

// ------------- REPORT SCREENS -------------
                composable("daily_report") {
                    DailyReportScreen(reportVM, onBack = { navController.popBackStack() })
                }
                composable("weekly_report") {
                    WeeklyReportScreen(reportVM, onBack = { navController.popBackStack() })
                }
                composable("monthly_report") {
                    MonthlyReportScreen(reportVM, onBack = { navController.popBackStack() })
                }
                composable("customer_report") {
                    CustomerReportScreen(reportVM, onBack = { navController.popBackStack() })
                }

// ------------- SERVICE LIST (single source of truth) -------------
                composable("admin_services") {
                    ServiceListScreen(
                        viewModel = serviceVM,
                        onAddClick = { navController.navigate("admin_add_service") },
                        onEditClick = { id ->
                            navController.navigate("admin_edit_service/$id")
                        }
                    )
                }

// ------------- ADD SERVICE -------------
                composable("admin_add_service") {
                    AddServiceScreen(
                        viewModel = serviceVM,
                        onBack = { navController.popBackStack() }
                    )
                }

// ------------- EDIT SERVICE (by ID) -------------
                composable(
                    route = "admin_edit_service/{serviceId}",
                    arguments = listOf(navArgument("serviceId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val id = backStackEntry.arguments?.getInt("serviceId") ?: -1
                    EditServiceScreen(
                        serviceId = id,
                        viewModel = serviceVM,
                        onBack = { navController.popBackStack() }
                    )
                }

// ---------------------------------------------------------------


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
                    route = "serviceDetail/{name}/{desc}",
                    arguments = listOf(
                        navArgument("name") { type = NavType.StringType },
                        navArgument("desc") { type = NavType.StringType }
                    )
                ) { backStackEntry ->

                    val name = backStackEntry.arguments?.getString("name") ?: ""
                    val desc = backStackEntry.arguments?.getString("desc") ?: ""

                    ServiceDescriptionScreen(
                        navController = navController,
                        serviceName = name,
                        serviceDescription = desc
                    )
                }

                composable(
                    "serviceDetails/{categoryId}/{selectedDate}/{selectedTimeSlot}",
                    arguments = listOf(
                        navArgument("categoryId") { type = NavType.IntType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedTimeSlot") { type = NavType.StringType }
                    )
                ) { backStackEntry ->

                    val categoryId = backStackEntry.arguments!!.getInt("categoryId")
                    val date = backStackEntry.arguments!!.getString("selectedDate")!!
                    val slot = backStackEntry.arguments!!.getString("selectedTimeSlot")!!

                    val services = serviceDetailsViewModel
                        .getServicesByCategory(categoryId)
                        .collectAsState(initial = emptyList())

                    ServiceDetailsScreen(
                        navController = navController,
                        services = services.value,
                        selectedDate = date,
                        selectedTimeSlot = slot
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
                    "selectStylist/{serviceId}/{selectedDate}/{selectedTimeSlot}",
                    arguments = listOf(
                        navArgument("serviceId") { type = NavType.IntType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedTimeSlot") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val serviceId = backStackEntry.arguments?.getInt("serviceId")!!
                    val date = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val slot = backStackEntry.arguments?.getString("selectedTimeSlot") ?: ""

                    StylistSelectionScreen(
                        navController = navController,
                        stylistVM = stylistVM,
                        serviceId = serviceId,
                        selectedDate = date,
                        selectedTimeSlot = slot
                    )
                }


                composable(
                    "tempPayment/{serviceName}/{selectedDate}/{selectedTimeSlot}/{stylistId}/{hairLength}"
                        ,
                    arguments = listOf(
                        navArgument("serviceName") { type = NavType.StringType },
                        navArgument("selectedDate") { type = NavType.StringType },
                        navArgument("selectedTimeSlot") { type = NavType.StringType },
                        navArgument("stylistId") { type = NavType.StringType },
                        navArgument("hairLength") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val serviceName = backStackEntry.arguments?.getString("serviceName") ?: ""
                    val date = backStackEntry.arguments?.getString("selectedDate") ?: ""
                    val slot = backStackEntry.arguments?.getString("selectedTimeSlot") ?: ""
                    val stylistId = backStackEntry.arguments?.getString("stylistId") ?: ""
                    val hairLength = backStackEntry.arguments?.getString("hairLength") ?: ""

                    TempPaymentScreen(
                        navController = navController,
                        serviceName = serviceName,
                        selectedDate = date,
                        selectedTimeSlot = slot,
                        stylistId = stylistId,
                        hairLength = hairLength
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

