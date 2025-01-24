package cl.rodofla.android.midetuconsumo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import cl.rodofla.android.midetuconsumo.data.model.ReadingEntity

@Database(entities = [ReadingEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readingDao(): ReadingDao
}