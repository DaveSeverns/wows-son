package com.sevdotdev.wowson.ui.common.core.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.sevdotdev.wowson.ui.common.core.icons.previews.IconContainer

val VectorIcons.WowIcon: ImageVector
    get() {
        if (_wowIcon != null) {
            return _wowIcon!!
        }
        _wowIcon = materialIcon(name = "Filled.WowIcon") {
            materialPath {
                moveTo(7.0f, 8.0f)
                lineTo(9f, 16f)
                horizontalLineToRelative(2f)
                lineTo(12f, 12f)
                lineTo(13f, 16f)
                horizontalLineToRelative(2f)
                lineTo(17f,8f)
                horizontalLineToRelative(-1f)
                lineTo(14f, 14f)
                lineTo(12f, 11f)
                lineTo(10f, 14f)
                lineTo(8f, 8f)
                horizontalLineToRelative(-1f)
                close()
            }
        }
        return _wowIcon!!
    }

private var _wowIcon: ImageVector? = null

@Preview
@Composable
fun WowIconPreview() {
    IconContainer(vectorIcon = VectorIcons.WowIcon)
}