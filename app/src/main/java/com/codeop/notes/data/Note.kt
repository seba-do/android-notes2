package com.codeop.notes.data

import com.codeop.notes.R
import java.util.*

data class Note(
    val uid: String,
    val title: String,
    val text: String,
    val color: Color
) {
    companion object {
        private const val DELIMITER = "|"

        fun createNote(title: String, description: String, color: Color) =
            Note(UUID.randomUUID().toString(), title, description, color)

        fun fromString(value: String): Note {
            val split = value.split(DELIMITER)
            return Note(split[0], split[1], split[2], Color.values()[split[3].toInt()])
        }
    }

    override fun toString(): String {
        return "$uid$DELIMITER$title$DELIMITER$text$DELIMITER${Color.values().indexOf(color)}"
    }

    enum class Color(val colorId: Int) {
        RED(R.color.red_200),
        BLUE(R.color.blue_200),
        YELLOW(R.color.yellow_200),
        GREEN(R.color.green_200),
        WHITE(R.color.white)
    }
}