package com.rroot.pediatricdose.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object Routes {
    const val HOME = "home"
    const val CALCULATOR = "calculator"
    const val DRUGS = "drugs"
    const val DRUG_DETAIL = "drug/{name}"
    const val DISCLAIMER = "disclaimer"

    fun drugDetail(name: String): String = "drug/${java.net.URLEncoder.encode(name, "UTF-8")}"
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onCalculator = { navController.navigate(Routes.CALCULATOR) },
                onDrugs = { navController.navigate(Routes.DRUGS) },
                onDisclaimer = { navController.navigate(Routes.DISCLAIMER) },
            )
        }
        composable(Routes.CALCULATOR) {
            CalculatorScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.DRUGS) {
            DrugListScreen(
                onBack = { navController.popBackStack() },
                onSelect = { drug -> navController.navigate(Routes.drugDetail(drug.name)) },
            )
        }
        composable(Routes.DRUG_DETAIL) { backStack ->
            val encoded = backStack.arguments?.getString("name").orEmpty()
            val name = java.net.URLDecoder.decode(encoded, "UTF-8")
            DrugDetailScreen(name = name, onBack = { navController.popBackStack() })
        }
        composable(Routes.DISCLAIMER) {
            DisclaimerScreen(onBack = { navController.popBackStack() })
        }
    }
}
