package com.jetkollage.feat.home.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetKollageTopBar(
    title: String,
    onLicence: () -> Unit = {},
    onAbout: () -> Unit = {}
) = CenterAlignedTopAppBar(
    modifier = Modifier.clip(
        MaterialTheme.shapes.extraLarge.copy(
            topStart = ZeroCornerSize, topEnd = ZeroCornerSize,
        )
    ),
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ),
    title = { Text(title) },
    actions = {
        var expanded by remember { mutableStateOf(false) }
        Box {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
            DropdownMenu(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    enabled = false,
                    text = { Text("Licence") },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                    onClick = onLicence
                )
                DropdownMenuItem(
                    enabled = false,
                    text = { Text("About") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    onClick = onAbout
                )
            }
        }
    }
)