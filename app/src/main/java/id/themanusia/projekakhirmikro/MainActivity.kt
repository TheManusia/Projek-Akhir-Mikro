package id.themanusia.projekakhirmikro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import id.themanusia.projekakhirmikro.ui.theme.ProjekAkhirMikroTheme
import id.themanusia.projekakhirmikro.ui.theme.Purple80
import id.themanusia.projekakhirmikro.ui.theme.PurpleGrey40
import id.themanusia.projekakhirmikro.ui.theme.Typography

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProjekAkhirMikroTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val firebaseDatabase = FirebaseDatabase.getInstance()
                    val databaseReference = firebaseDatabase.getReference("dht22")
                    var num = 0
                    databaseReference.child("random").get().addOnCompleteListener {
                        num = it.result.value.toString().toInt()
                    }
                    Greeting(num)
                }
            }
        }
    }
}

@Composable
fun Greeting(tempnum: Int = -1) {
    val temperature = remember {
        mutableStateOf(0.0f)
    }

    val humidity = remember {
        mutableStateOf(0.0f)
    }

    val heatIndex = remember {
        mutableStateOf(0.0f)
    }

    val random = remember {
        mutableStateOf(0)
    }

    val tempRandom = remember {
        mutableStateOf(tempnum)
    }

    val count = remember {
        mutableStateOf(1000)
    }

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("dht22")

    databaseReference.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val data = snapshot.getValue(Dht22::class.java)!!

            temperature.value = data.temperature
            humidity.value = data.humidity
            heatIndex.value = data.heatIndex
            random.value = data.random
        }

        override fun onCancelled(error: DatabaseError) {

        }
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(modifier = Modifier.padding(16.dp), text = buildAnnotatedString {
            if (random.value == tempRandom.value) {
                count.value = count.value + 1
            } else {
                count.value = 0
                tempRandom.value = random.value
            }

            if (count.value < 1000) {
                append("Device Online")
            } else {
                append("Device Offline")
            }
        })
        TemperatureIndicator(
            currentValue = temperature.value,
            maxValue = 40,
            progressBackgroundColor = Purple80,
            progressIndicatorColor = PurpleGrey40,
            title = buildAnnotatedString {
                val defaultSpan =
                    Typography.titleLarge.toSpanStyle()
                append(AnnotatedString(text = "Temperature", spanStyle = defaultSpan))
            },
            suffix = buildAnnotatedString {
                val defaultSpan =
                    Typography.titleLarge.copy(color = Purple80).toSpanStyle()
                append(AnnotatedString(text = "°C", spanStyle = defaultSpan))
            },
            textStyle = Typography.displayLarge.toSpanStyle()
        )
        Row(modifier = Modifier.padding(16.dp)) {
            TemperatureIndicator(
                currentValue = humidity.value,
                maxValue = 100,
                diameter = 120.dp,
                progressBackgroundColor = Purple80,
                progressIndicatorColor = PurpleGrey40,
                title = buildAnnotatedString {
                    val defaultSpan =
                        Typography.bodyLarge.toSpanStyle()
                    append(AnnotatedString(text = "Humidity", spanStyle = defaultSpan))
                },
                suffix = buildAnnotatedString {
                    val defaultSpan =
                        Typography.titleLarge.copy(color = Purple80).toSpanStyle()
                    append(AnnotatedString(text = "%", spanStyle = defaultSpan))
                },
                textStyle = Typography.headlineLarge.toSpanStyle()
            )
            TemperatureIndicator(
                modifier = Modifier.padding(horizontal = 16.dp),
                currentValue = heatIndex.value,
                maxValue = 100,
                diameter = 120.dp,
                progressBackgroundColor = Purple80,
                progressIndicatorColor = PurpleGrey40,
                title = buildAnnotatedString {
                    val defaultSpan =
                        Typography.bodyLarge.toSpanStyle()
                    append(AnnotatedString(text = "Heat Index", spanStyle = defaultSpan))
                },
                suffix = buildAnnotatedString {
                    val defaultSpan =
                        Typography.titleLarge.copy(color = Purple80).toSpanStyle()
                    append(AnnotatedString(text = "°C", spanStyle = defaultSpan))
                },
                textStyle = Typography.headlineLarge.toSpanStyle()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjekAkhirMikroTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Greeting()
        }
    }
}


