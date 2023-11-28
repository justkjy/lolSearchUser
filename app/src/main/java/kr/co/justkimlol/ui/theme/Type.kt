package kr.co.justkimlol.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kr.co.justkimlol.R

// Set of Material typography styles to start with
private val lineSeedBold = FontFamily(
    Font(R.font.line_seed_kr_bd, FontWeight.Bold)
)
private val lineSeedRegular = FontFamily(
    Font(R.font.line_seed_kr_rg, FontWeight.Normal)
)
private val lineSeedThin = FontFamily(
    Font(R.font.line_seed_kr_th, FontWeight.Thin)
)


val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = lineSeedBold,
        fontSize = 57.sp,
    ),

    displaySmall = TextStyle(
        fontFamily = lineSeedBold,
        fontSize = 36.sp,
    ),

    titleLarge = TextStyle(
        fontFamily = lineSeedBold,
        fontSize = 22.sp
    ),

    titleMedium = TextStyle(
        fontFamily = lineSeedBold,
        fontSize = 20.sp
    ),

    titleSmall = TextStyle(
        fontFamily = lineSeedBold,
        fontSize = 16.sp
    ),

    bodyLarge = TextStyle(
        fontFamily = lineSeedRegular,
        fontSize = 16.sp
    ),

    bodyMedium = TextStyle(
        fontFamily = lineSeedRegular,
        fontSize = 12.sp
    ),

    labelMedium = TextStyle(
        fontFamily = lineSeedRegular,
        fontSize = 12.sp
    ),

    // other Brush
    headlineSmall = TextStyle(
        fontFamily = lineSeedBold,
        fontSize = 24.sp,
        brush = Brush.verticalGradient(listOf(
                Color.Blue,
                Color.Red,
                Color.White
            )
        )
    )
)