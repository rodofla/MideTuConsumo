package cl.rodofla.android.midetuconsumo.ui.screens

import FormScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import cl.rodofla.android.midetuconsumo.viewmodel.ReadingViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val viewModel: ReadingViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "listScreen",
        modifier = modifier
    ) {
        composable("listScreen") {
            ListScreen(
                onNavigateToForm = {
                    navController.navigate("formScreen")
                },
                viewModel = viewModel
            )
        }
        composable("formScreen") {
            FormScreen(
                onFormDone = {
                    navController.popBackStack()
                    viewModel.refreshReadings()
                },
                readingViewModel = viewModel
            )
        }
    }
}
