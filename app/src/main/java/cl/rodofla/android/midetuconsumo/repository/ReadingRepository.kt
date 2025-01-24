package cl.rodofla.android.midetuconsumo.repository

import cl.rodofla.android.midetuconsumo.data.local.ReadingDao
import cl.rodofla.android.midetuconsumo.data.model.ReadingEntity
import kotlinx.coroutines.flow.Flow

class ReadingRepository(private val readingDao: ReadingDao) {

    fun getAllReadings(): Flow<List<ReadingEntity>> {
        return readingDao.getAllReadings()
    }

    suspend fun insertReading(reading: ReadingEntity) {
        readingDao.insertReading(reading)
    }
}