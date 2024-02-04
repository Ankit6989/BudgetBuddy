package com.apcoding.budgetbuddy.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.apcoding.budgetbuddy.components.charts.MonthlyChart
import com.apcoding.budgetbuddy.components.charts.WeeklyChart
import com.apcoding.budgetbuddy.components.charts.YearlyChart
import com.apcoding.budgetbuddy.components.expenseList.ExpenseList
import com.apcoding.budgetbuddy.models.Recurrence
import com.apcoding.budgetbuddy.ui.screens.reports.ReportPageViewModel
import com.apcoding.budgetbuddy.ui.theme.LabelSecondary
import com.apcoding.budgetbuddy.utils.formatDayForRange
import com.apcoding.budgetbuddy.viewModelFactory
import java.text.DecimalFormat
import java.time.LocalDate



@Composable
fun ReportPage(
    innerPadding: PaddingValues,
    page: Int,
    recurrence: Recurrence,
    vm: ReportPageViewModel = viewModel(
        key = "$page-${recurrence.name}",
        factory = viewModelFactory {
            ReportPageViewModel(page, recurrence)
        })
) {
    val uiState = vm.uiState.collectAsState().value

    Column(
        modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    "${
                        uiState.dateStart.formatDayForRange()
                    } - ${uiState.dateEnd.formatDayForRange()}",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        "INR",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabelSecondary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(DecimalFormat("0.#").format(uiState.totalInRange), style = MaterialTheme.typography.headlineMedium)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Avg/day", style = MaterialTheme.typography.titleSmall)
                Row(modifier = Modifier.padding(top = 4.dp)) {
                    Text(
                        "INR",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabelSecondary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(DecimalFormat("0.#").format(uiState.avgPerDay), style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        Box(
            modifier = Modifier
                .height(180.dp)
                .padding(vertical = 16.dp)
        ) {
            when (recurrence) {
                Recurrence.Weekly -> WeeklyChart(expenses = uiState.expenses)
                Recurrence.Monthly -> MonthlyChart(
                    expenses = uiState.expenses,
                    LocalDate.now()
                )
                Recurrence.Yearly -> YearlyChart(expenses = uiState.expenses)
                else -> Unit
            }
        }

        ExpenseList(
            expenses = uiState.expenses, modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    rememberScrollState()
                )
        )
    }
}


@Composable
@Preview
fun Report() {
    ReportPage(
        innerPadding = PaddingValues(),
        page = 0,
        recurrence = Recurrence.Weekly
    )
}
