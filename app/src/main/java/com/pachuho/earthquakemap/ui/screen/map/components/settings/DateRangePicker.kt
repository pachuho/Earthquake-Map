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
import com.pachuho.earthquakemap.ui.util.toLocalDateTimeAsLong
import com.pachuho.earthquakemap.ui.util.toLong
import timber.log.Timber
import java.util.*

@Composable
fun DateRangePicker(
    minDateTime: Long,
    startDateTime: Long,
    endDateTime: Long,
    onDisMiss: () -> Unit,
    onDateSelected: (Long, Long) -> Unit
) {
    var showStartDateDialog by remember { mutableStateOf(false) }
    var showEndDateDialog by remember { mutableStateOf(false) }

    var currentStartDateTime by remember { mutableLongStateOf(startDateTime) }
    var currentEndDateTime by remember { mutableLongStateOf(endDateTime) }

    Timber.e("currentStartDateTime: $currentStartDateTime")
    Timber.e("currentEndDateTime: $currentEndDateTime")

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = "조회 기간 설정",
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { showStartDateDialog = true }
            ) {
                DateText("${currentStartDateTime.toLocalDateTime().year}${stringResource(R.string.year)}")
                DateText("${currentStartDateTime.toLocalDateTime().monthValue}${stringResource(R.string.month)}")
                DateText("${currentStartDateTime.toLocalDateTime().dayOfMonth}${stringResource(R.string.day)}")
            }

            DateText("~")

            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable { showEndDateDialog = true }
            ) {
                DateText("${currentEndDateTime.toLocalDateTime().year}${stringResource(R.string.year)}")
                DateText("${currentEndDateTime.toLocalDateTime().monthValue}${stringResource(R.string.month)}")
                DateText("${currentEndDateTime.toLocalDateTime().dayOfMonth}${stringResource(R.string.day)}")
            }
        }

        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(onClick = {
                onDisMiss()
            }) {
                Text(text = stringResource(R.string.cancel))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(onClick = {
                onDateSelected(currentStartDateTime, currentEndDateTime)
            }) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    }


    if (showStartDateDialog) {
        DatePickerDialog(
            startYear = minDateTime.toLocalDateTime().year,
            currentDate = currentStartDateTime,
            onClickConfirm = {
                showStartDateDialog = false

                diffStartAndEnd(true, it, currentEndDateTime) { newStart, newEnd ->
                    currentStartDateTime = newStart
                    currentEndDateTime = newEnd
                }
                Timber.e("onClickConfirm, currentStartDateTime: $currentStartDateTime, currentEndDateTime: $currentEndDateTime")
            },
            onDisMiss = {
                showStartDateDialog = false
            }
        )
    }

    if (showEndDateDialog) {
        DatePickerDialog(
            startYear = currentStartDateTime.toLocalDateTime().year,
            currentDate = currentEndDateTime,
            onClickConfirm = {
                showEndDateDialog = false

                diffStartAndEnd(false, currentStartDateTime, it) { newStart, newEnd ->
                    currentStartDateTime = newStart
                    currentEndDateTime = newEnd
                }
                Timber.e("onClickConfirm, currentStartDateTime: $currentStartDateTime, currentEndDateTime: $currentEndDateTime")
            },
            onDisMiss = {
                showEndDateDialog = false
            }
        )
    }
}

private fun diffStartAndEnd(
    isStart: Boolean,
    start: Long,
    end: Long,
    onResult: (newStart: Long, newEnd: Long) -> Unit
) {
    var newStartDateTime = start.toLocalDateTime()
    var newEndDateTime = end.toLocalDateTime()

    if (isStart) {
        if (!newStartDateTime.isBefore(newEndDateTime)) {
            newEndDateTime = newStartDateTime.plusDays(1)
        }
    } else {
        if (!newEndDateTime.isAfter(newStartDateTime)) {
            newStartDateTime = newEndDateTime.minusDays(1)
        }
    }

    val newStartMillis = newStartDateTime.toLong()
    val newEndMillis = newEndDateTime.toLong()

    return onResult(newStartMillis, newEndMillis)
}

@Composable
private fun DateText(
    text: String
) {
    Text(
        modifier = Modifier.padding(horizontal = 4.dp),
        text = text,
        style = MaterialTheme.typography.titleSmall,
    )
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
                    onClickConfirm(selectedDateMillis.toLocalDateTimeAsLong())
                }
            }) {
                Text(text = stringResource(R.string.confirm))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun MarkerDatePickerPreview() {
    val minDateTime by remember { mutableLongStateOf(201602230145) }
    var startDateTime by remember { mutableLongStateOf(201602230145) }
    var endDateTime by remember { mutableLongStateOf(202408230145) }

    Timber.e("startDateTime: $startDateTime")
    Timber.e("endDateTime: $endDateTime")
    DateRangePicker(
        minDateTime = minDateTime,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
        onDisMiss = {
            Timber.e("Dismiss")
        },
        onDateSelected = { start, end ->
            Timber.e("onDateSelected, startDateTime: $startDateTime")
            Timber.e("onDateSelected, endDateTime: $endDateTime")
            startDateTime = start
            endDateTime = end
        }
    )
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