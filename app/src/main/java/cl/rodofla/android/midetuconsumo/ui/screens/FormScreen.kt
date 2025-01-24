
import android.widget.Toast
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.rodofla.android.midetuconsumo.viewmodel.ReadingViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onFormDone: () -> Unit,
    readingViewModel: ReadingViewModel = viewModel()
) {
    val context = LocalContext.current

    // Estados del formulario
    var value by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    var type by remember { mutableStateOf("agua") }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Registro De Gastos") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Registrar Gastosr",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center
            )

            // Campo del valor del medidor
            OutlinedTextField(
                value = value,
                onValueChange = {
                    if (it.all { char -> char.isDigit() }) {
                        value = it
                    }
                },
                label = { Text("Valor de Consumo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Campo de fecha con DatePicker modal
            OutlinedTextField(
                value = date,
                onValueChange = { },
                label = { Text("Fecha (yyyy-MM-dd)") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) {
                                showDatePicker = true
                            }
                        }
                    }
            )

            if (showDatePicker) {
                DatePickerModal(
                    onDateSelected = { millis ->
                        if (millis != null) {
                            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                        }
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false }
                )
            }

            // Selector de tipo (RadioButton)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Gasto de:", style = MaterialTheme.typography.bodyLarge)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = type == "agua",
                        onClick = { type = "agua" }
                    )
                    Text("Agua", modifier = Modifier.padding(start = 8.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = type == "luz",
                        onClick = { type = "luz" }
                    )
                    Text("Luz", modifier = Modifier.padding(start = 8.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = type == "gas",
                        onClick = { type = "gas" }
                    )
                    Text("Gas", modifier = Modifier.padding(start = 8.dp))
                }
            }

            // Botón de guardar
            Button(
                onClick = {
                    // Validaciones
                    val isDateValid = try {
                        LocalDate.parse(date)
                        true
                    } catch (e: Exception) {
                        false
                    }

                    if (value.isNotEmpty() && isDateValid) {
                        readingViewModel.addReading(
                            type = type,
                            value = value.toFloatOrNull() ?: 0f,
                            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(date)?.time
                                ?: System.currentTimeMillis()
                        )
                        Toast.makeText(context, "Gastos registrados con éxito", Toast.LENGTH_SHORT).show()
                        onFormDone()
                    } else {
                        Toast.makeText(context, "Error al guardar: Verifica los datos", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Registrar Gastos")
            }
        }
    }
}

// Componente DatePickerModal
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val selectedMillis = datePickerState.selectedDateMillis
                if (selectedMillis != null) {
                    val localDateMillis = adjustToLocalTime(selectedMillis)
                    onDateSelected(localDateMillis)
                } else {
                    onDateSelected(null)
                }
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}


fun adjustToLocalTime(timeInMillis: Long): Long {

    val utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    utcCalendar.timeInMillis = timeInMillis

    val localCalendar = Calendar.getInstance(TimeZone.getDefault())
    localCalendar.set(
        utcCalendar.get(Calendar.YEAR),
        utcCalendar.get(Calendar.MONTH),
        utcCalendar.get(Calendar.DAY_OF_MONTH),
        utcCalendar.get(Calendar.HOUR_OF_DAY),
        utcCalendar.get(Calendar.MINUTE)
    )

    return localCalendar.timeInMillis
}





