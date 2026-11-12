package com.ibaiqzzaaziza.rutex.component

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.ibaiqzzaaziza.rutex.BuildConfig
import com.ibaiqzzaaziza.rutex.model.TitikItinerary
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

/**
 * Peta rute memakai OpenStreetMap (osmdroid), sehingga tidak butuh API key.
 * Setiap titik diberi penanda berurutan dan dihubungkan dengan garis rute.
 */
@Composable
fun PetaRute(titik: List<TitikItinerary>, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    val mapView = remember {
        // Server OpenStreetMap mewajibkan user agent yang jelas
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            controller.setZoom(13.0)
        }
    }

    // MapView perlu tahu kapan layar aktif/tidak aktif agar tidak boros baterai
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
            mapView.onDetach()
        }
    }

    AndroidView(
        factory = { mapView },
        modifier = modifier,
        update = { peta ->
            peta.overlays.clear()
            val titikPeta = titik.mapNotNull { t ->
                if (t.lat != null && t.lng != null) t to GeoPoint(t.lat, t.lng) else null
            }
            if (titikPeta.isEmpty()) return@AndroidView

            val garis = Polyline(peta).apply {
                setPoints(titikPeta.map { it.second })
                outlinePaint.color = Color.rgb(255, 122, 0)
                outlinePaint.strokeWidth = 8f
            }
            peta.overlays.add(garis)

            titikPeta.forEachIndexed { index, (data, posisi) ->
                val marker = Marker(peta).apply {
                    position = posisi
                    title = "${'A' + index}. ${data.nama}"
                    snippet = listOfNotNull(data.tiba, data.berangkat).joinToString(" – ")
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                }
                peta.overlays.add(marker)
            }

            // Kamera diarahkan agar semua titik terlihat
            if (titikPeta.size == 1) {
                peta.controller.setCenter(titikPeta.first().second)
            } else {
                val kotak = BoundingBox.fromGeoPoints(titikPeta.map { it.second })
                peta.post { peta.zoomToBoundingBox(kotak.increaseByScale(1.3f), false) }
            }
            peta.invalidate()
        }
    )
}
