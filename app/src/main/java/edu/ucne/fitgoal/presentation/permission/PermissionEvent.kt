package edu.ucne.fitgoal.presentation.permission

import android.content.Context

sealed interface PermissionEvent {
    data class PermissionResult(val permission: String, val isGranted: Boolean): PermissionEvent
    data object DismissDialog: PermissionEvent
    data class CheckPermissions(val permission: String, val  context: Context): PermissionEvent
}