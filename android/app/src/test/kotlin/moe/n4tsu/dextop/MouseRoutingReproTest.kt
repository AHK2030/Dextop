package moe.n4tsu.dextop

import java.io.File
import org.junit.Assert.assertFalse
import org.junit.Test

/**
 * Automatic reproduction for: physical mouse neither moves nor clicks inside the
 * Dextop virtual display on Android 16 (non-Samsung devices).
 *
 * MirrorService.onServiceConnected() forces the physical mouse/keyboard routing
 * flags to false, so PhysicalInputRouter is never asked to associate devices with
 * the Dextop display. These tests fail while that override exists.
 */
class MouseRoutingReproTest {

    private val mirrorSource: String by lazy {
        val candidates = listOf(
            File("src/main/kotlin/moe/n4tsu/dextop/MirrorService.kt"),
            File("android/app/src/main/kotlin/moe/n4tsu/dextop/MirrorService.kt")
        )
        candidates.first { it.exists() }.readText()
    }

    @Test
    fun physicalMouseRoutingIsNotForcedOff() {
        val forcedOff = Regex("""routePhysicalMouseToDextop\s*=\s*false""").containsMatchIn(mirrorSource)
        assertFalse(
            "Physical mouse routing is forced off in MirrorService.onServiceConnected(); " +
                "the mouse is never associated with the Dextop display on Android 16.",
            forcedOff
        )
    }

    @Test
    fun physicalKeyboardRoutingIsNotForcedOff() {
        val forcedOff = Regex("""routePhysicalKeyboardToDextop\s*=\s*false""").containsMatchIn(mirrorSource)
        assertFalse(
            "Physical keyboard routing is forced off in MirrorService.onServiceConnected().",
            forcedOff
        )
    }
}
