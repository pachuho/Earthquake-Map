package com.pachuho.earthquakemap.ui.screen.map.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pachuho.earthquakemap.R
import com.pachuho.earthquakemap.ui.util.toLocalDateTime
import com.pachuho.earthquakemap.ui.util.toMillis
import java.util.*

@Composable
fun DateRangePicker(
    minDateTime: Long,
    startDateTime: Long,
    endDateTime: Long,
    onDateSelected: (DateSelect) -> Unit
) {
    var showStartDateDialog by remember { mutableStateOf(false) }
    var showEndDateDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable { showStartDateDialog = true }
        ) {
            Text(
                text = "${startDateTime.toLocalDateTime().year}${stringResource(R.string.year)}",
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = "${startDateTime.toLocalDateTime().monthValue}${stringResource(R.string.month)}",
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        Text(
            text = "~",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )

        Row(
            modifier = Modifier.clickable { showStartDateDialog = true }
        ) {

            Text(
                text = "${endDateTime.toLocalDateTime().year}${stringResource(R.string.year)}",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "${endDateTime.toLocalDateTime().monthValue}${stringResource(R.string.month)}",
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }


    if (showStartDateDialog) {
        DatePickerDialog(
            startYear = minDateTime.toLocalDateTime().year,
            currentDate = startDateTime,
            onClickConfirm = {
                showStartDateDialog = false
                onDateSelected(DateSelect.StartDateTime(it))
            },
            onDisMiss = {
                showStartDateDialog = false
            }
        )
    }

    if (showEndDateDialog) {
        DatePickerDialog(
            startYear = minDateTime.toLocalDateTime().year,
            currentDate = endDateTime,
            onClickConfirm = {
                showEndDateDialog = false
                onDateSelected(DateSelect.EndDateTime(it))
            },
            onDisMiss = {
                showEndDateDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    startYear: Int,
    currentDate: Long,
    onClickConfirm: (dataTime: Long) -> Unit,
    onDisMiss: () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = { onDisMiss() },
        confirmButton = {},
        colors = DatePickerDefaults.colors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(6.dp)
    ) {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val datePickerState = rememberDatePickerState(
            yearRange = startYear..currentYear,
            initialDisplayMode = DisplayMode.Picker,
            initialSelectedDateMillis = currentDate.toMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis < System.currentTimeMillis()
                }
            })

        DatePicker(
            state = datePickerState,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {
                onDisMiss()
            }) {
                Text(text = stringResource(R.string.cancel))
            }

            Spacer(modifier = Modifier.width(5.dp))

            Button(onClick = {
                datePickerState.selectedDateMillis?.let { selectedDateMillis ->
                    onClickConfirm(selectedDateMillis)
                }
            }) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    }
}


sealed class DateSelect {
    data class StartDateTime(val dateTime: Long) : DateSelect()
    data class EndDateTime(val dateTime: Long) : DateSelect()
}

@Composable
@Preview(showBackground = true)
fun StartDatePickerDialogPreview() {
    DatePickerDialog(
        startYear = 2016,
        currentDate = 201602250145,
        onClickConfirm = {},
        onDisMiss = {}
    )
}

@Composable
@Preview(showBackground = true)
fun EndDatePickerDialogPreview() {
    DatePickerDialog(
        startYear = 2016,
        currentDate = 202408230145,
        onClickConfirm = {},
        onDisMiss = {}
    )
}

@Composable
@Preview(showBackground = true)
fun MarkerDatePickerPreview() {
    val minDateTime by remember { mutableLongStateOf(201602230145) }
    var startDateTime by remember { mutableLongStateOf(201602230145) }
    var endDateTime by remember { mutableLongStateOf(202408230145) }

    DateRangePicker(minDateTime, startDateTime, endDateTime) { dateSelect ->
        when (dateSelect) {
            is DateSelect.StartDateTime -> {
                startDateTime = dateSelect.dateTime
            }

            is DateSelect.EndDateTime -> {
                endDateTime = dateSelect.dateTime
            }
        }
    }
}