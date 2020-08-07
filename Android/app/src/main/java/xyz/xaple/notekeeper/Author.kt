package xyz.xaple.notekeeper

import com.stfalcon.chatkit.commons.models.IUser

/**
 * Created by waseemakram on 13/05/18.
 */
class Author(avatar:String,name:String,id:String): IUser {

     var a_avatar:String? = null
     var a_name:String? = null
     var a_id:String? = null

    init {
        a_avatar = avatar
        a_name = name
        a_id = id
    }


    override fun getAvatar(): String {
        return a_avatar!!
    }

    override fun getName(): String {
        return a_name!!
    }

    override fun getId(): String {
        return a_id!!
    }
}
