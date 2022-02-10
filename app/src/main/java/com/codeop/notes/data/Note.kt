package com.codeop.notes.data

import android.content.Context
import android.content.res.Configuration
import android.os.Parcelable
import com.codeop.notes.R
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note(
    val uid: String,
    val title: String,
    val text: String,
    val color: Color,
    val pos: Int = -1,
    val archived: Boolean = false
): Parcelable {
    companion object {
        private const val DELIMITER = "|"

        fun createNote(title: String, description: String, color: Color) =
            Note(UUID.randomUUID().toString(), title, description, color)

        fun fromString(value: String): Note {
            val split = value.split(DELIMITER)
            return Note(
                uid = split[0],
                title = split[1],
                text = split[2],
                color = Color.values()[split[3].toInt()],
                pos = split[4].toInt(),
                archived = split[5].toBoolean()
            )
        }
    }

    override fun toString(): String {
        return "$uid$DELIMITER" +
                "$title$DELIMITER" +
                "$text$DELIMITER" +
                "${Color.values().indexOf(color)}$DELIMITER" +
                "$pos$DELIMITER" +
                "$archived"
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