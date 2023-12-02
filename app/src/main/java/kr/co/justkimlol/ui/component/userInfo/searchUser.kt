package kr.co.justkimlol.ui.component.userInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kr.co.justkimlol.ui.component.startPadding
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.mainfragment.user.viewModel.UserViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun searchUser(userText: String = "소환사 이름", view: UserViewModel = viewModel()) {
    var value by remember { mutableStateOf(userText) }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column (
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .height(80.dp)
    ){
        Row {
            OutlinedTextField(
                value = value,
                onValueChange = {value = it},
                textStyle = MaterialTheme.typography.titleMedium,
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        onClick = {
                            view.setStepUserFind(value)
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "소환사 이름 입력"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search,
                ),
                keyboardActions = KeyboardActions (onSearch = {
                    keyboardController?.hide()
                    view.setStepUserFind(value)
                }),

                shape = MaterialTheme.shapes.extraLarge,
                //colors = MaterialTheme.colorScheme.primary,
                trailingIcon = {
                    IconButton(
                        onClick = {
                            value = ""
                        }) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = "필드 지우기"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = startPadding, end = startPadding)
                    .defaultMinSize(0.dp, 0.dp)
                    .background(MaterialTheme.colorScheme.primary,
                        MaterialTheme.shapes.extraLarge)
            )
        }

    }
}

@Preview
@Composable
fun PreviewSearchUser() {
    LolInfoViewerTheme(true) {
        searchUser("")

    }
}