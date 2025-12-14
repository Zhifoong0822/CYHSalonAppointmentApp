
package com.example.cyhsalonappointment.screens.Admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportViewModel(private val repo: ReportRepository) : ViewModel() {

    private val _report = MutableStateFlow<ReportResult?>(null)
    val report: StateFlow<ReportResult?> = _report

    fun loadReport(start: String, end: String) {
        viewModelScope.launch {
            _report.value = repo.getReport(start, end)
        }
    }

}
