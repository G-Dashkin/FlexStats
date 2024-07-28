package com.perfomax.flexstats.data.repository

import android.content.Context
import android.util.Log
import com.perfomax.data.R
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

import com.perfomax.flexstats.models.User
import com.perfomax.flexstats.data_api.repository.AuthRepository
import com.perfomax.flexstats.data_api.storage.AuthStorage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val EMAIL_SENDER = "support@flexstats.ru"
private const val EMAIL_PASSWORD = "745189aB!"
private const val EMAIL_HOST = "smtp.timeweb.ru"
private const val EMAIL_PORT = "465"

class AuthRepositoryImpl @Inject constructor(
    private val authStorage: AuthStorage,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): AuthRepository {
    override suspend fun create(newUser: User) = authStorage.add(newUser)
    override suspend fun getAll(): List<User> = authStorage.getAllUsers()
    override suspend fun setAuth() = authStorage.setAuth()
    override suspend fun getAuth(): Boolean = authStorage.getAuth()
    override suspend fun logout() = authStorage.logout()
    override suspend fun setAuthUser(authUser: User) = authStorage.setAuthUser(authUser)
    override suspend fun getAuthUser(): User = authStorage.getAuthUser()
    override suspend fun reset(email: String): Boolean {
        val user = authStorage.getAllUsers().find { it.email == email }
        if (user != null) { resetPassword(user) }
        return user != null
    }

    private suspend fun resetPassword(user: User): Unit = withContext(dispatcher) {
        val subject = context.resources.getString(com.perfomax.ui.R.string.reset_password_mail_title)
        val body = context.resources.getString(com.perfomax.ui.R.string.reset_password_mail_body_1) +
                   context.resources.getString(com.perfomax.ui.R.string.reset_password_mail_body_2) +
                   context.resources.getString(com.perfomax.ui.R.string.reset_password_mail_body_3) +
                   user.user +
                   context.resources.getString(com.perfomax.ui.R.string.reset_password_mail_body_4) +
                   user.password

        val properties = System.getProperties()

        properties.put("mail.smtp.host", EMAIL_HOST)
        properties.put("mail.smtp.port", EMAIL_PORT)
        properties.put("mail.smtp.auth", "true")
        properties.put("mail.smtp.ssl.enable", "true")

        val session: Session = Session.getDefaultInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(EMAIL_SENDER, EMAIL_PASSWORD)
            }
        })

        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(EMAIL_SENDER))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(user.email))
            message.subject = subject
            message.setText(body)
            val messageBodyPart = MimeBodyPart()
            messageBodyPart.setText(body)
            val multipart = MimeMultipart()
            multipart.addBodyPart(messageBodyPart)
            message.setContent(multipart)
            Transport.send(message)

        } catch (mex: MessagingException) {
            mex.printStackTrace()
        }
    }
}