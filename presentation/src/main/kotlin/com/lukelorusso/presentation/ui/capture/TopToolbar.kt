package com.lukelorusso.presentation.ui.capture

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.lukelorusso.presentation.R


@Composable
internal fun TopToolbar(
    isNextCameraAvailable: Boolean,
    isNextCameraFront: Boolean,
    isFlashAvailable: Boolean,
    isFlashOn: Boolean,
    onNextCameraSelected: () -> Unit,
    onFlashSelected: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxWidth()
                .background(colorResource(id = R.color.black_50)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isNextCameraAvailable) {
                IconButton(
                    modifier = Modifier
                        .size(ICON_SIZE)
                        .padding(ICON_PADDING),
                    onClick = onNextCameraSelected
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isNextCameraFront) R.drawable.camera_front_white
                            else R.drawable.camera_rear_white
                        ),
                        contentDescription = null
                    )
                }
            }

            if (isFlashAvailable) {
                IconButton(
                    modifier = Modifier
                        .size(ICON_SIZE)
                        .padding(ICON_PADDING),
                    onClick = onFlashSelected
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (isFlashOn) R.drawable.flash_off_white
                            else R.drawable.flash_on_white
                        ),
                        contentDescription = null
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
