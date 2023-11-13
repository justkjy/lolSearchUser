package kr.co.lol.ui.component.button

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kr.co.lol.R

data class LeadingIconData(
    @DrawableRes val iconDrawable: Int,
    @StringRes val iconContentDescription: Int? = R.string.blank_string
)
