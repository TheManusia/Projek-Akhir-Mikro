package id.themanusia.projekakhirmikro

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import id.themanusia.projekakhirmikro.ui.theme.ProjekAkhirMikroTheme
import id.themanusia.projekakhirmikro.ui.theme.*

@Composable
fun TemperatureIndicator(
    modifier: Modifier = Modifier,
    currentValue: Int,
    maxValue: Int,
    progressBackgroundColor: Color,
    progressIndicatorColor: Color,
    diameter: Dp = 200.dp,
    title: AnnotatedString,
    suffix: AnnotatedString,
    textStyle: SpanStyle,
) {

    val stroke = with(LocalDensity.current) {
        Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    }

    Column(modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text(modifier = Modifier.padding(16.dp),
            text = title)
        Box(contentAlignment = Alignment.Center) {
            ProgressStatus(
                currentValue = currentValue,
                progressIndicatorColor = progressIndicatorColor,
                suffix = suffix,
                textStyle = textStyle
            )

            val animateFloat = remember { Animatable(0f) }
            LaunchedEffect(animateFloat) {
                animateFloat.animateTo(
                    targetValue = currentValue / maxValue.toFloat(),
                    animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                )
            }
            val sweep: Float by animateFloatAsState(currentValue / maxValue.toFloat() * 360f)

            Canvas(
                Modifier
                    .progressSemantics(currentValue / maxValue.toFloat())
                    .size(diameter)
            ) {
                // Start at 12 O'clock
                val startAngle = 270f
                val diameterOffset = stroke.width / 2

                drawCircle(
                    color = progressBackgroundColor,
                    style = stroke,
                    radius = size.minDimension / 2.0f - diameterOffset
                )
                drawCircularProgressIndicator(startAngle, sweep, progressIndicatorColor, stroke)
            }
        }
    }
}

@Composable
private fun ProgressStatus(
    currentValue: Int,
    progressIndicatorColor: Color,
    modifier: Modifier = Modifier,
    suffix: AnnotatedString,
    textStyle: SpanStyle
) {
    Text(modifier = modifier, text = buildAnnotatedString {
        val emphasisSpan = textStyle.copy(color = progressIndicatorColor)
        append(AnnotatedString("$currentValue", spanStyle = emphasisSpan))
        append(suffix)
    }
    )
}

private fun DrawScope.drawCircularProgressIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

@Preview(showBackground = true)
@Composable
fun CircularIndicatorPreview() {
    ProjekAkhirMikroTheme {
        TemperatureIndicator(
            currentValue = 32,
            maxValue = 100,
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
                append(AnnotatedString(text = " °C", spanStyle = defaultSpan))
            },
            textStyle = Typography.displayLarge.toSpanStyle()
        )
    }
}