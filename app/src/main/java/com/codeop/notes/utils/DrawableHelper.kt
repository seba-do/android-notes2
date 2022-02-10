package com.codeop.notes.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.codeop.notes.R
import com.codeop.notes.data.Note

object DrawableHelper {
    fun getTintedDrawable(context: Context, color: Note.Color): Drawable? {
        val drawable = AppCompatResources.getDrawable(
            context,
            R.drawable.bg_btn_border_default
        )?.mutate()

        (drawable as? GradientDrawable)?.let { drw ->
            drw.setColor(ContextCompat.getColor(context, Note.Color.getColorId(context, color)))
            drw.setStroke(3, ContextCompat.getColor(context, Note.Color.getStrokeColor(context)))
        }

        return drawable
    }
}