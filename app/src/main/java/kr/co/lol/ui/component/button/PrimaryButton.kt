package kr.co.lol.ui.component.button

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.co.lol.R
import kr.co.lol.ui.theme.LolInfoViewerTheme
import kr.co.lol.ui.theme.Paddings


val LEADING_ICON_SIZE = 24.dp

@Composable
fun PrimaryButton(
    @StringRes id: Int? = null,
    text: String = "",
    leadingIconData: LeadingIconData? = null,
    clickAble: Boolean = true,
    onClick: () -> Unit = { }
) {


    Button(
        onClick =  onClick ,
        enabled = clickAble,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            leadingIconData?.let {
                Icon(
                    modifier = Modifier.size(LEADING_ICON_SIZE),
                    painter = painterResource(id = leadingIconData.iconDrawable),
                    contentDescription = leadingIconData.iconContentDescription?.let { desc ->
                        stringResource(id = desc)
                    }
                )
                Spacer(modifier = Modifier.width(Paddings.small))
            }

            Text(
                text = id?.let { stringResource(id = id) } ?: text,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(Paddings.medium)
            )
        }
    }
}

@Preview
@Composable
fun PrimaryButtonPreview() {
    LolInfoViewerTheme(true) {
        val leadingIconData = LeadingIconData(iconDrawable = R.drawable.earth, iconContentDescription = R.string.primaryButtonText)
        PrimaryButton(leadingIconData = leadingIconData, id = R.string.primaryButtonDesc)
    }
}