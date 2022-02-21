package com.codeop.notes.data

import android.content.Context
import android.content.res.Configuration
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codeop.notes.R
import kotlinx.parcelize.Parcelize
import java.util.*

@Entity
@Parcelize
data class Note(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "color") val color: Color,
    @ColumnInfo(name = "pos") val pos: Int = -1,
    @ColumnInfo(name = "isArchived") val archived: Boolean = false
): Parcelable {
    companion object {
        fun createNote(title: String, description: String, color: Color) =
            Note(UUID.randomUUID().toString(), title, description, color)
    }

    enum class Color(val colorLight: Int, val colorDark: Int) {
        RED(R.color.red_200, R.color.red_800),
        BLUE(R.color.blue_200, R.color.blue_800),
        YELLOW(R.color.yellow_200, R.color.yellow_800),
        GREEN(R.color.green_200, R.color.green_800),
        WHITE(R.color.white, R.color.black);

        companion object {
            fun getColorId(context: Context, color: Color): Int {
                return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> color.colorDark
                    else -> color.colorLight
                }
            }

            fun getStrokeColor(context: Context): Int {
                return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_YES -> R.color.white
                    else -> R.color.black
                }
            }
        }
    }
}