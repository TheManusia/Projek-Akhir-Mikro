package id.themanusia.projekakhirmikro

import com.google.firebase.database.PropertyName

data class Dht22(
    @PropertyName("temperature") val temperature: Float = 0.0f,
    @PropertyName("humidity") val humidity: Float = 0.0f,
    @PropertyName("heatindex") val heatIndex: Float = 0.0f,
    @PropertyName("random") val random: Int = 0,
    ) {
    constructor() : this(0.0f,0.0f,0.0f, 0)

    override fun toString(): String {
        return "peler gede: $temperature, $humidity, $heatIndex, $random"
    }
}