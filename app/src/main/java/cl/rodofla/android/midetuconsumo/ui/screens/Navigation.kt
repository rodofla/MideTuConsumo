package cl.rodofla.android.midetuconsumo.ui.screens

import FormScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "listScreen",
        modifier = modifier
    ) {
        composable("listScreen") {
            ListScreen(
                onNavigateToForm = {
                    navController.navigate("formScreen")
                }
            )
        }
        composable("formScreen") {
            FormScreen(
                onFormDone = {
                    navController.popBackStack()
                }
            )
        }
    }
}