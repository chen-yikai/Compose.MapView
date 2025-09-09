package dev.eliaschen.composemapview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import dev.eliaschen.composemapview.ui.theme.ComposeMapViewTheme
import kotlinx.coroutines.delay
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.modules.OfflineTileProvider
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.tileprovider.util.SimpleRegisterReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.io.File
import java.io.FileWriter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMapViewTheme {
                MapView()
            }
        }
    }
}

@Composable
fun MapView() {
    AndroidView(factory = {
        MapView(it).apply {
            setUseDataConnection(false)
            val deviceFile = File(context.filesDir.absolutePath + "/osmdroid/", "map.sqlite")
            context.assets.open("map.sqlite").copyTo(deviceFile.outputStream())
            tileProvider = OfflineTileProvider(
                SimpleRegisterReceiver(context),
                arrayOf(deviceFile)
            )
            setMultiTouchControls(true)
            controller.setCenter(GeoPoint(23.0168, 115.4628))
            controller.setZoom(8)
            setTileSource(
                XYTileSource(
                    "Google Maps HD",
                    7,
                    12,
                    256,
                    ".png",
                    arrayOf("")
                )
            )
            controller.animateTo(GeoPoint(25.0168, 121.4628), 9.0, 1000L)
        }
    })
}
