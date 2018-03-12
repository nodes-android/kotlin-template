package dk.eboks.app.domain.models

import java.io.Serializable

/**
 * Created by bison on 24-06-2017.
 */
data class Sender(
    var id : Long,
    var name : String = "",
    var unreadCount : Int = 0,
    var logo : Image? = null
) : Serializable