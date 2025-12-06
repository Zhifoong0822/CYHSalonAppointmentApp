package com.example.cyhsalonappointment

data class Service(
    val name: String,
    val description: String
)

object ServiceData {
    val services = listOf(
        Service(
            "Haircut",
            "Professional haircut tailored to your preferred style."
        ),
        Service(
            "Hair Wash",
            "Refreshing hair wash with scalp massage and cleansing."
        ),
        Service(
            "Hair Coloring",
            "Full hair coloring service using high-quality, long-lasting dyes."
        ),
        Service(
            "Hair Perm",
            "Long-lasting perm treatment to create curls or add volume."
        )
    )
}
