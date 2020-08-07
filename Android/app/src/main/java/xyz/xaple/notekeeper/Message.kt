package xyz.xaple.notekeeper

import com.stfalcon.chatkit.commons.models.IMessage
import com.stfalcon.chatkit.commons.models.IUser
import java.util.*

/**
 * Created by waseemakram on 12/05/18.
 */
 class Message(id:String,createdAt:Date,user:IUser,text:String):IMessage{

    var m_id:String? = null
    var m_createdAt:Date? = null
    var m_user:IUser? = null
    var m_text:String? = null

    init{
        m_id = id
        m_createdAt = createdAt
        m_user = user
        m_text = text
    }


    override fun getId(): String {
        return m_id!!
    }

    override fun getCreatedAt(): Date {
        return m_createdAt!!
    }

    override fun getUser(): IUser {
        return m_user!!
    }

    override fun getText(): String {
       return m_text!!
    }
}
