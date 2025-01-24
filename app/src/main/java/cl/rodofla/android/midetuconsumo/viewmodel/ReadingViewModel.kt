package cl.rodofla.android.midetuconsumo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import cl.rodofla.android.midetuconsumo.data.local.AppDatabase
import cl.rodofla.android.midetuconsumo.data.model.ReadingEntity
import cl.rodofla.android.midetuconsumo.repository.ReadingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReadingViewModel(application: Application) : AndroidViewModel(application) {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "readings_db"
    ).build()

    private val repository = ReadingRepository(db.readingDao())
    private val _forceUpdate = MutableStateFlow(Unit)

    val allReadings: StateFlow<List<ReadingEntity>> =
        repository.getAllReadings()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun addReading(type: String, value: Float, date: Long) {
        viewModelScope.launch {
            repository.insertReading(
                ReadingEntity(
                    type = type,
                    value = value,
                    date = date
                )
            )
        }
    }

    fun refreshReadings() {
        viewModelScope.launch {
            _forceUpdate.value = Unit
        }
    }

}