package com.apcoding.budgetbuddy.ui.screens.add

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.apcoding.budgetbuddy.components.TableRow
import com.apcoding.budgetbuddy.components.UnstyledTextField
import com.apcoding.budgetbuddy.models.Recurrence
import com.apcoding.budgetbuddy.ui.theme.BackgroundElevated
import com.apcoding.budgetbuddy.ui.theme.DividerColor
import com.apcoding.budgetbuddy.ui.theme.TopAppBarBackground
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun Add(navController: NavController, vm: AddViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()

    val recurrences = listOf(
        Recurrence.None,
        Recurrence.Daily,
        Recurrence.Weekly,
        Recurrence.Monthly,
        Recurrence.Yearly
    )

    Scaffold(topBar = {
        MediumTopAppBar(
            title = { Text("Add") },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = TopAppBarBackground
            )
        )
    }, content = { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(shapes.large)
                    .background(BackgroundElevated)
                    .fillMaxWidth()
            ) {
                TableRow(label = "Amount", detailContent = {
                    UnstyledTextField(
                        value = state.amount,
                        onValueChange = vm::setAmount,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("0") },
                        arrangement = Arrangement.End,
                        maxLines = 1,
                        textStyle = TextStyle(
                            textAlign = TextAlign.Right,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                        )
                    )
                })
                Divider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
                TableRow(label = "Recurrence", detailContent = {
                    var recurrenceMenuOpened by remember {
                        mutableStateOf(false)
                    }
                    TextButton(
                        onClick = { recurrenceMenuOpened = true }, shape = shapes.large
                    ) {
                        Text(state.recurrence?.name ?: Recurrence.None.name)
                        DropdownMenu(expanded = recurrenceMenuOpened,
                            onDismissRequest = { recurrenceMenuOpened = false }) {
                            recurrences.forEach { recurrence ->
                                DropdownMenuItem(text = { Text(recurrence.name) }, onClick = {
                                    vm.setRecurrence(recurrence)
                                    recurrenceMenuOpened = false
                                })
                            }
                        }
                    }
                })
                Divider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
                var datePickerShowing by remember {
                    mutableStateOf(false)
                }
                TableRow(label = "Date", detailContent = {
                    TextButton(onClick = { datePickerShowing = true }) {
                        Text(state.date.toString())
                    }
                    if (datePickerShowing) {
                        DatePickerDialog(onDismissRequest = { datePickerShowing = false },
                            onDateChange = { it ->
                                vm.setDate(it)
                                datePickerShowing = false
                            },
                            initialDate = state.date,
                            title = { Text("Select date", style = MaterialTheme.typography.titleLarge) })
                    }
                })
                Divider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
                TableRow(label = "Note", detailContent = {
                    UnstyledTextField(
                        value = state.note,
                        placeholder = { Text("Leave some notes") },
                        arrangement = Arrangement.End,
                        onValueChange = vm::setNote,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Right,
                        ),
                    )
                })
                Divider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
                TableRow(label = "Category", detailContent = {
                    var categoriesMenuOpened by remember {
                        mutableStateOf(false)
                    }
                    TextButton(
                        onClick = { categoriesMenuOpened = true }, shape = shapes.large
                    ) {
                        Text(
                            state.category?.name ?: "Select a category first",
                            color = state.category?.color ?: Color.White
                        )
                        DropdownMenu(expanded = categoriesMenuOpened,
                            onDismissRequest = { categoriesMenuOpened = false }) {
                            state.categories?.forEach { category ->
                                DropdownMenuItem(text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            modifier = Modifier.size(10.dp),
                                            shape = CircleShape,
                                            color = category.color
                                        ) {}
                                        Text(
                                            category.name, modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }, onClick = {
                                    vm.setCategory(category)
                                    categoriesMenuOpened = false
                                })
                            }
                        }
                    }
                })
            }
            Button(
                onClick = vm::submitExpense,
                modifier = Modifier.padding(16.dp),
                shape = shapes.large,
                enabled = state.category != null
            ) {
                Text("Submit expense")
            }
        }
    })
}

