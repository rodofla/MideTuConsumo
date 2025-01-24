package cl.rodofla.android.midetuconsumo.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "readings")
data class ReadingEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val value: Float,
    val date: Long
)
