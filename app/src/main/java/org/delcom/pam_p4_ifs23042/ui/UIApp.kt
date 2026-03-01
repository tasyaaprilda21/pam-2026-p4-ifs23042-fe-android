package org.delcom.pam_p4_ifs23042.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23042.helper.ConstHelper
import org.delcom.pam_p4_ifs23042.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23042.ui.screens.BagsAddScreen
import org.delcom.pam_p4_ifs23042.ui.screens.BagsDetailScreen
import org.delcom.pam_p4_ifs23042.ui.screens.BagsEditScreen
import org.delcom.pam_p4_ifs23042.ui.screens.BagsScreen
import org.delcom.pam_p4_ifs23042.ui.screens.HomeScreen
import org.delcom.pam_p4_ifs23042.ui.screens.PlantsAddScreen
import org.delcom.pam_p4_ifs23042.ui.screens.PlantsDetailScreen
import org.delcom.pam_p4_ifs23042.ui.screens.PlantsEditScreen
import org.delcom.pam_p4_ifs23042.ui.screens.PlantsScreen
import org.delcom.pam_p4_ifs23042.ui.screens.ProfileScreen
import org.delcom.pam_p4_ifs23042.ui.viewmodels.BagViewModel
import org.delcom.pam_p4_ifs23042.ui.viewmodels.PlantViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController: NavHostController = rememberNavController(),
    plantViewModel: PlantViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // BagViewModel diinisialisasi via Hilt
    val bagViewModel: BagViewModel = hiltViewModel()

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                CustomSnackbar(snackbarData, onDismiss = {
                    snackbarHostState.currentSnackbarData?.dismiss()
                })
            }
        },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
        ) {
            // Home
            composable(route = ConstHelper.RouteNames.Home.path) {
                HomeScreen(navController = navController)
            }

            // Profile
            composable(route = ConstHelper.RouteNames.Profile.path) {
                ProfileScreen(
                    navController = navController,
                    plantViewModel = plantViewModel
                )
            }

            // Plants
            composable(route = ConstHelper.RouteNames.Plants.path) {
                PlantsScreen(
                    navController = navController,
                    plantViewModel = plantViewModel
                )
            }

            // Plants Add
            composable(route = ConstHelper.RouteNames.PlantsAdd.path) {
                PlantsAddScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel
                )
            }

            // Plants Detail
            composable(
                route = ConstHelper.RouteNames.PlantsDetail.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
                PlantsDetailScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId = plantId
                )
            }

            // Plants Edit
            composable(
                route = ConstHelper.RouteNames.PlantsEdit.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
                PlantsEditScreen(
                    navController = navController,
                    snackbarHost = snackbarHostState,
                    plantViewModel = plantViewModel,
                    plantId = plantId
                )
            }

            // =====================
            // BAGS ROUTES - BARU
            // =====================

            // Bags List
            composable(route = ConstHelper.RouteNames.Bags.path) {
                BagsScreen(
                    navController = navController,
                    bagViewModel = bagViewModel
                )
            }

            // Bags Add
            composable(route = ConstHelper.RouteNames.BagsAdd.path) {
                BagsAddScreen(
                    navController = navController,
                    bagViewModel = bagViewModel
                )
            }

            // Bags Detail
            composable(
                route = ConstHelper.RouteNames.BagsDetail.path,
                arguments = listOf(navArgument("bagId") { type = NavType.StringType })
            ) { backStackEntry ->
                val bagId = backStackEntry.arguments?.getString("bagId") ?: ""
                BagsDetailScreen(
                    navController = navController,
                    bagViewModel = bagViewModel,
                    bagId = bagId
                )
            }

            // Bags Edit
            composable(
                route = ConstHelper.RouteNames.BagsEdit.path,
                arguments = listOf(navArgument("bagId") { type = NavType.StringType })
            ) { backStackEntry ->
                val bagId = backStackEntry.arguments?.getString("bagId") ?: ""
                BagsEditScreen(
                    navController = navController,
                    bagViewModel = bagViewModel,
                    bagId = bagId
                )
            }
        }
    }
}