package id.themanusia.projekakhirmikro

import com.google.firebase.database.PropertyName

data class Dht22(
    @PropertyName("temperature") val temperature: Double = 0.0,
    @PropertyName("humidity") val humidity: Double = 0.0,
    @PropertyName("heatindex") val heatIndex: Double = 0.0,
    @PropertyName("random") val random: Int = 0,
    ) {
    constructor() : this(0.0,0.0,0.0, 0)

    override fun toString(): String {
        return "peler gede: $temperature, $humidity, $heatIndex, $random"
    }
}