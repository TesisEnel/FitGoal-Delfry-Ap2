package edu.ucne.fitgoal.presentation.permission

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class NotificationsTextProvider: PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if(isPermanentlyDeclined) {
            "Este permiso ha sido rechazado permanentemente. " +
             "Puedes ir a la configuración de la app para activarlo."
        } else {
            "Esta app necesita acceso a tus notificaciones" +
            "Para poder mostrarte a que hora debes tomar tu agua."
        }
    }
}