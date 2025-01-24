package cl.rodofla.android.midetuconsumo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cl.rodofla.android.midetuconsumo.data.model.ReadingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadingDao {

    @Query("SELECT * FROM readings ORDER BY date DESC")
    fun getAllReadings(): Flow<List<ReadingEntity>>

    @Insert
    suspend fun insertReading(reading: ReadingEntity)
}