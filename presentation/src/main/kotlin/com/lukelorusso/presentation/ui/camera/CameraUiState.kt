package com.lukelorusso.presentation.ui.camera

import com.lukelorusso.domain.model.Color
import com.lukelorusso.presentation.ui.base.ContentState
import io.fotoapparat.capability.Capabilities

data class CameraUiState(
    val contentState: ContentState = ContentState.CONTENT,
    val cameraCapabilities: Capabilities? = null,
    val lastLensPosition: Int? = null,
    val lastZoomValue: Int? = null,
    val pixelNeighbourhood: Int = -1,
    val color: Color? = null
)
