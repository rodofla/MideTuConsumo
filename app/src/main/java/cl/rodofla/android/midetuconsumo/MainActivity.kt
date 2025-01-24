package cl.rodofla.android.midetuconsumo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import cl.rodofla.android.midetuconsumo.ui.screens.AppNavigation
import cl.rodofla.android.midetuconsumo.ui.theme.MideTuConsumoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplicamos el tema de Material 3
            MideTuConsumoTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // Inicializamos el controlador de navegación
                    val navController = rememberNavController()
                    // Llamamos a la función de navegación
                    AppNavigation(navController = navController)
                }
            }
        }
    }
}