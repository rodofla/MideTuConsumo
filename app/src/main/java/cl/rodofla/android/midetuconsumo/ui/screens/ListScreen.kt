package cl.rodofla.android.midetuconsumo.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.rodofla.android.midetuconsumo.data.model.ReadingEntity
import cl.rodofla.android.midetuconsumo.viewmodel.ReadingViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.Modifier
import cl.rodofla.android.midetuconsumo.R
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListScreen(
    onNavigateToForm: () -> Unit,
    viewModel: ReadingViewModel = viewModel()
) {
    val readings by viewModel.allReadings.collectAsState()


    val luzReadings = readings.filter { it.type.lowercase() == "luz" }
    val aguaReadings = readings.filter { it.type.lowercase() == "agua" }
    val gasReadings = readings.filter { it.type.lowercase() == "gas" }


    val luzMedia: Double? = luzReadings.map { it.value }.average().takeIf { it.isFinite() }
    val aguaMedia: Double? = aguaReadings.map { it.value }.average().takeIf { it.isFinite() }
    val gasMedia: Double? = gasReadings.map { it.value }.average().takeIf { it.isFinite() }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Lista de Gastos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) {
                Icon(
                    painter = painterResource(android.R.drawable.ic_input_add),
                    contentDescription = "Agregar",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Mostrar las medias anuales
            MediaDashboard(
                luzMedia = luzMedia,
                aguaMedia = aguaMedia,
                gasMedia = gasMedia
            )

            // Mostrar registros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    stickyHeader {
                        Header("Luz", drawableRes = R.drawable.ic_luz)
                    }
                    items(luzReadings) { reading ->
                        ReadingItem(reading, luzMedia)
                    }
                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    stickyHeader {
                        Header("Agua", drawableRes = R.drawable.ic_agua)
                    }
                    items(aguaReadings) { reading ->
                        ReadingItem(reading, aguaMedia)
                    }
                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    stickyHeader {
                        Header("Gas", drawableRes = R.drawable.ic_gas)
                    }
                    items(gasReadings) { reading ->
                        ReadingItem(reading, gasMedia)
                    }
                }
            }
        }
    }
}

@Composable
fun Header(title: String, drawableRes: Int) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = drawableRes),
                contentDescription = title,
                modifier = Modifier
                    .size(48.dp)
                    .padding(bottom = 8.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun MediaDashboard(luzMedia: Double?, aguaMedia: Double?, gasMedia: Double?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Media Luz", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${luzMedia?.roundToInt() ?: 0}", style = MaterialTheme.typography.bodyLarge)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Media Agua", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${aguaMedia?.roundToInt() ?: 0}", style = MaterialTheme.typography.bodyLarge)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Media Gas", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${gasMedia?.roundToInt() ?: 0}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
fun ReadingItem(reading: ReadingEntity?, media: Double?) {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateString = reading?.date?.let { formatter.format(Date(it)) } ?: "Sin fecha"


    val isAboveThreshold = (reading?.value?.toDouble() ?: 0.0) > ((media ?: 0.0) * 1.2)
    val backgroundColor = if (isAboveThreshold) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Valor: ${reading?.value?.toInt() ?: 0}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = dateString,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}






