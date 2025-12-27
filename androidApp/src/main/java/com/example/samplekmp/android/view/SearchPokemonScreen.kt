package com.example.samplekmp.android.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.samplekmp.android.viewModel.PokemonListViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchPokemonScreen() {
    val viewModel = remember { PokemonListViewModel() }
    val pokemon by viewModel.pokemon.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                keyboardController?.hide()
                val id = text.toIntOrNull()
                if (id != null) {
                    viewModel.onClickSearchButton(id)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("検索")
        }

        Spacer(modifier = Modifier.height(16.dp))

        pokemon?.let {
            AsyncImage(
                model = it.sprities.normal,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = it.name,
                    fontSize = 36.sp,
                )
            }
        }
    }
}
