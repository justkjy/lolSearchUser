package kr.co.justkimlol.ui.component.circleprocess

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import coil.compose.AsyncImage
import kr.co.justkimlol.R
import kr.co.justkimlol.mainfragment.home.internet.champTilesUrl
import kr.co.justkimlol.ui.component.championInfo.TierProfile
import kr.co.justkimlol.ui.theme.LolInfoViewerTheme
import kr.co.justkimlol.ui.theme.Paddings
import kotlin.math.floor

@Composable
fun CustomCircularProccess(
    modifier : Modifier = Modifier,
    primaryColor : Color,
    secondaryColor : Color,
    minValue : Int = 0,
    maxValue : Int = 100,
    championEngName : String = "Aatrox",
    circleRadius:Float,

    winValue : Int = 0,
    loseValue : Int = 0,
    onPositionChange: (Int)->Unit
) {
    val winPercent = Float.let {
        var percent = 0f
        if (winValue > 0) {
            percent = (winValue.toFloat() / (winValue + loseValue).toFloat() * 100.0).toFloat()
        }
        percent
    }

    var circleCenter by remember {
        mutableStateOf(Offset.Zero)
    }

    Column(modifier = modifier) {

        ConstraintLayout(
            Modifier.fillMaxSize()
        ) {
            Box(
                modifier = modifier
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(true) {
                            detectDragGestures(
                                onDragStart = {},
                                onDrag = { _, _ ->
                                },
                                onDragEnd = {
                                }
                            )
                        }
                ) {
                    val width = size.width
                    val height = size.height
                    val circleThickness = width / 15f
                    circleCenter = Offset(x = width / 2f, y = height / 2f)

                    drawCircle(
                        style = Stroke(
                            width = circleThickness
                        ),
                        color = secondaryColor,
                        radius = circleRadius,
                        center = circleCenter
                    )

                    drawArc(
                        color = primaryColor,
                        startAngle = 90f,
                        sweepAngle = (360f / maxValue) * winPercent,
                        style = Stroke(
                            width = circleThickness,
                            cap = StrokeCap.Round
                        ),
                        useCenter = false,
                        size = Size(
                            width = circleRadius * 2f,
                            height = circleRadius * 2f
                        ),
                        topLeft = Offset(
                            (width - circleRadius * 2f) / 2f,
                            (height - circleRadius * 2f) / 2f
                        )
                    )
                }
            }
            val (champImg, text1, text2) = createRefs()
            AsyncImage(
                model = champTilesUrl(championEngName),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.warrior_helmet),
                contentDescription = "",
                alignment = Alignment.Center,

                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(0.5f)
                    .clip(CircleShape)
                    .constrainAs(champImg) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = "Win:${winValue}",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .alpha(0.5f)
                    .constrainAs(text1) {
                        top.linkTo(parent.top, 50.dp)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = "${floor(winPercent)} %",
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .alpha(0.5f)
                    .constrainAs(text2) {
                        top.linkTo(text1.bottom, 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
        }
    }
}

@Composable
fun PreviewLineChart (
    pointsData : List<Point> = listOf(
        Point(0f, 0f),
        Point(1f, 16f),
        Point(2f, 8f),
        Point(3f, 10f, "Win"),
        Point(4f, 1f, "Win"),
        Point(5f, 4f),
        Point(6f, 16f),
        Point(7f, 8f),
        Point(8f, 10f, "Win"),
        Point(9f, 1f, "Win"),
        Point(10f, 4f),
        Point(11f, 16f),
        Point(12f, 8f),
        Point(13f, 10f, "Win"),
        Point(14f, 1f, "Win"),
        Point(15f, 4f),
        Point(16f, 16f),
        Point(17f, 8f),
        Point(18f, 10f, "Win"),
        Point(19f, 1f, "Win"),
        Point(20f, 4f),
        Point(21f, 16f),
        Point(22f, 8f),
        Point(23f, 10f, "Win"),
        Point(24f, 1f, "Win"),
        Point(25f, 4f),
        Point(26f, 16f),
        Point(27f, 8f),
        Point(28f, 10f, "Win"),
        Point(29f, 1f, "Win"),
        Point(30f, 4f),
        Point(31f, 16f),
        Point(32f, 8f),
        Point(33f, 10f, "Win"),
        Point(34f, 1f, "Win"),
        Point(35f, 4f),
        Point(36f, 16f),
        Point(37f, 8f),
        Point(38f, 10f, "Win"),
        Point(39f, 1f, "Win"),
        Point(40f, 4f),
        Point(41f, 16f),
        Point(42f, 8f),
        Point(43f, 10f, "Win"),
        Point(44f, 1f, "Win"),
        Point(45f, 4f),
        Point(46f, 16f),
        Point(47f, 8f),
        Point(48f, 10f, "Win"),
        Point(49f, 1f, "Win"),
        Point(50f, 4f),
        Point(51f, 16f),
        Point(52f, 8f),
        Point(53f, 10f, "Win"),
        Point(54f, 1f, "Win"),
        Point(55f, 4f),
        Point(56f, 16f),
        Point(57f, 8f),
        Point(58f, 10f, "Win"),
        Point(59f, 1f, "Win"),
        Point(60f, 4f),
        Point(61f, 16f),
        Point(62f, 8f),
        Point(63f, 10f,"Win"),
        Point(64f, 1f, "Win"),
        Point(65f, 4f),
        Point(66f, 16f),
        Point(67f, 8f),
        Point(68f, 10f, "Win"),
        Point(69f, 1f, "Win"),
        Point(70f, 4f),
        Point(71f, 16f),
        Point(72f, 8f),
        Point(73f, 10f, "Win"),
        Point(74f, 1f, "Win"),
        Point(75f, 4f),
        Point(76f, 16f),
        Point(77f, 8f),
        Point(78f, 10f, "Win"),
        Point(79f, 1f, "Win"),
        Point(80f, 4f),
        Point(81f, 16f),
        Point(82f, 8f),
        Point(83f, 10f, "Win"),
        Point(84f, 1f, "Win"),
        Point(85f, 4f),
        Point(86f, 16f),
        Point(87f, 8f),
        Point(88f, 10f, "Win"),
        Point(89f, 1f, "Win"),
        Point(90f, 4f),
        Point(91f, 16f),
        Point(92f, 8f),
        Point(93f, 10f, "Win"),
        Point(94f, 1f, "Win"),
        Point(95f, 4f),
        Point(96f, 16f),
        Point(97f, 8f),
        Point(98f, 10f, "Win"),
        Point(99f, 1f, "Win"),
        Point(100f, 10f)
    )
) : LineChartData {

    val pink = Color(0xFFFF4AAA)
    val steps = 16

    val mostYStep = pointsData.maxOf { it.y.toInt() }

    val xStepSize = when(pointsData.size) {
        in 0..5 -> 50.dp
        else -> 20.dp
    }

    val xAxisData = AxisData.Builder()
        .axisStepSize(xStepSize)
        .topPadding(105.dp)
        .steps(pointsData.size - 1)
        .labelData { i ->
            var valuePoint = pointsData[i].x.toString()
            if(pointsData.size - 1 >= 10) {
                if ((i % 5) == 0) {
                    valuePoint = pointsData[i].x.toInt().toString()
                } else valuePoint = ""
            } else {
                valuePoint = pointsData[i].x.toInt().toString()
            }

            valuePoint
        }
        .labelAndAxisLinePadding(10.dp)
        .axisLabelColor(Color.White)
        .axisLineColor(Color.White)
        .indicatorLineWidth(5.dp)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(mostYStep)
        .axisStepSize(1.dp)
        .labelAndAxisLinePadding(30.dp)
        .backgroundColor(Color.Transparent)
        .labelData { i ->
            var valueData = ""
            if(mostYStep >= 5) {
                if( i % 5 == 0) {
                    valueData = i.toString()
                } else {
                    valueData = ""
                }
            } else {
                valueData = i.toString()
            }
            valueData
        }
        .axisLineColor(Color.White)
        .axisLabelColor(Color.White)
        .build()

    return LineChartData (
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    LineStyle(
                        color = Color.White,
                        width = 4.0f,
                        lineType = LineType.SmoothCurve(isDotted = false)
                    ),
                    IntersectionPoint(
                        color = Color.Blue,
                        radius = 2.dp
                    ),
                    SelectionHighlightPoint(color = Color.Yellow),

                    ShadowUnderLine(
                        alpha = 0.8f,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                pink,
                                Color.Transparent
                            )
                        )
                    ),
                    SelectionHighlightPopUp(
                        backgroundColor = Color.Blue,
                        labelColor = Color.White,
                        labelSize = 20.sp,

                        labelAlignment = Paint.Align.CENTER,
                        paddingBetweenPopUpAndPoint = 10.dp,
                        popUpLabel = { x, y ->
                            "Kill${y.toInt()}/${x.toInt()}th"
                        }
                    )
                )
            )
        ),
        backgroundColor = MaterialTheme.colorScheme.background,
        gridLines = GridLines(
            color = Color.Gray,
            alpha = 0.5f,
            lineWidth = 0.4.dp
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData
    )
}


@Composable
@Preview
fun PreviewCustomCircular() {
    LolInfoViewerTheme(true) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
        ) {
            Row {
                TierProfile(
                    modifier = Modifier.size(100.dp),
                    profileId = 5986,
                    userName = "justKim",
                    userTier = "GOLD",
                    skillNum = 500
                )
                Spacer(modifier = Modifier.padding(Paddings.small))
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.height(100.dp)
                ) {
                    Spacer(modifier = Modifier.padding(Paddings.medium))
                    Row {
                        Text(text = "COUNT",
                            color = Color.Gray,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(Paddings.small)
                        )

                        Text(text = "60",
                            color = Color.White,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(Paddings.small)
                        )
                    }

                    Row {
                        Text(text = "WIN", color = Color.Gray,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(Paddings.small)
                        )

                        Text(text = "30", color = Color.Blue,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(Paddings.small)
                        )
                    }
                    Row {
                        Text(text = "LOSE", color = Color.Gray,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(Paddings.small)
                        )

                        Text(text = "30", color = Color.Red,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(Paddings.small)
                        )
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(0.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                LineChart(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    lineChartData = PreviewLineChart()
                )
            }

            Row {
                CustomCircularProccess(
                    modifier = Modifier
                        .size(130.dp)
                        .background(MaterialTheme.colorScheme.background),
                    primaryColor = Color.Blue,
                    secondaryColor = Color.Red,
                    circleRadius = 100f,
                    onPositionChange = { }
                )
                CustomCircularProccess(
                    modifier = Modifier
                        .size(130.dp)
                        .background(MaterialTheme.colorScheme.background)
                    ,
                    primaryColor = Color.Blue,
                    secondaryColor = Color.Red,
                    circleRadius = 100f,
                    onPositionChange = {
                    }
                )

                CustomCircularProccess(
                    modifier = Modifier
                        .size(130.dp)
                        .background(MaterialTheme.colorScheme.background),
                    primaryColor = Color.Blue,
                    secondaryColor = Color.Red,
                    circleRadius = 100f,
                    onPositionChange = { }
                )
            }
        }
    }
}