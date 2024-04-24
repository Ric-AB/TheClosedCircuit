package com.closedcircuit.closedcircuitapplication.common.presentation.util

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

actual class ImagePicker(
    private val activity: ComponentActivity
) {
    private lateinit var imagePicker: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>

    @Composable
    actual fun registerPicker(onImagePicked: (ByteArray) -> Unit) {
        imagePicker =
            rememberLauncherForActivityResult(
                ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                uri?.let {
                    activity.contentResolver.openInputStream(uri)?.use {
                        onImagePicked(it.readBytes())
                    }
                }
            }
    }

    actual fun pickImage() {
        imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}