package com.example.cyhsalonappointment.screens.Admin.Reports

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.cyhsalonappointment.local.DAO.CustomerReport
import com.example.cyhsalonappointment.local.DAO.ServiceSalesReport

@Composable
fun ServiceSalesPieChart(
    data: List<ServiceSalesReport>
) {
    if (data.isEmpty()) return

    val totalSales = data.sumOf { it.total }

    // 🔥 Generate unlimited distinct colors
    val colors = remember(data.size) {
        List(data.size) { index ->
            Color.hsv(
                hue = (index * 360f / data.size),
                saturation = 0.7f,
                value = 0.9f
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 🥧 PIE CHART
        Canvas(
            modifier = Modifier
                .size(220.dp)
                .padding(8.dp)
        ) {
            var startAngle = -90f

            data.forEachIndexed { index, item ->
                val sweepAngle =
                    (item.total / totalSales * 360f).toFloat()

                drawArc(
                    color = colors[index],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true
                )

                startAngle += sweepAngle
            }
        }

        Spacer(Modifier.width(16.dp))

        // 📌 LEGEND WITH PERCENTAGE
        Column {
            data.forEachIndexed { index, item ->
                val percentage = (item.total / totalSales * 100)

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        Modifier
                            .size(12.dp)
                            .background(colors[index])
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "${item.serviceName} " +
                                "${String.format("%.1f", percentage)}%"
                    )
                }
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}


@Composable
fun TopCustomerBarChart(
    customers: List<CustomerReport>
) {
    val top5 = customers.take(5)
    val maxSpent = top5.maxOfOrNull { it.totalSpent } ?: 1.0

    Column {
        top5.forEach { customer ->
            Column {
                Text(customer.customerId)
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(Color.LightGray)
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(
                                (customer.totalSpent / maxSpent).toFloat()
                            )
                            .height(16.dp)
                            .background(Color(0xFF6A5ACD))
                    )
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

