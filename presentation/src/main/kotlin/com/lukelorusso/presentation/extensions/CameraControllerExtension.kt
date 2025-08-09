package com.lukelorusso.presentation.extensions

import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController

fun CameraController.canSwitchToFront() =
    this.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA && this.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)

fun CameraController.canSwitchToBack() =
    this.cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA && this.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)