package edu.ucne.fitgoal.presentation.permission

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun PermissionScreen(
    viewModel: PersmissionViewModel = viewModel<PersmissionViewModel>()
) {
    val context = LocalContext.current as Activity
    val lifecycleOwner = LocalLifecycleOwner.current
    val onEvent = viewModel::onEvent

    val notificationsPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            onEvent(PermissionEvent.PermissionResult(
                permission = Manifest.permission.POST_NOTIFICATIONS,
                isGranted = isGranted
            ))
        }
    )

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                onEvent(PermissionEvent.CheckPermissions(
                    permission = Manifest.permission.POST_NOTIFICATIONS,
                    context = context
                ))
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        notificationsPermissionResultLauncher
            .launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    viewModel.dialogQueue.reversed().forEach { permission ->
        PermissionDialog(
            permissionTextProvider = when (permission) {
                Manifest.permission.POST_NOTIFICATIONS -> NotificationsTextProvider()
                else -> return@forEach
            },
            isPermanentlyDeclined = !shouldShowRequestPermissionRationale(
                context, permission
            ),
            onOkClick = {
                onEvent(PermissionEvent.DismissDialog)
                notificationsPermissionResultLauncher.launch(permission)
            }
        )
    }
}