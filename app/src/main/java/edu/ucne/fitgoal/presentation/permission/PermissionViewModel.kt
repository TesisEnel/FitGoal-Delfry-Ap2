package edu.ucne.fitgoal.presentation.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class PersmissionViewModel: ViewModel() {

    private val visiblePermissionDialogQueue = mutableStateListOf<String>()
    val dialogQueue = visiblePermissionDialogQueue

    fun onEvent(event: PermissionEvent){
        when(event){
            is PermissionEvent.CheckPermissions -> checkPermission(event.permission,event.context)
            PermissionEvent.DismissDialog -> dismissDialog()
            is PermissionEvent.PermissionResult -> onPermissionResult(event.permission, event.isGranted)
        }
    }

    private fun dismissDialog() {
        visiblePermissionDialogQueue.removeAt(0)
    }

    private fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if(!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    private fun checkPermission(permission: String, context: Context) {
        val isGranted = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        if (isGranted) {
            visiblePermissionDialogQueue.remove(permission)
        } else if (!visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }
}