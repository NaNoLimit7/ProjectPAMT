package com.example.projectpamt.ui.utils

import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toAppError(): AppError = when (this) {
    is UnknownHostException,
    is ConnectException            -> AppError.NoInternet
    is HttpRequestTimeoutException,
    is SocketTimeoutException      -> AppError.Timeout
    is RestException               -> mapRestException(this)
    is HttpRequestException        -> mapStorageException(this)
    else                           -> AppError.Unknown(this.message)
}

private fun mapRestException(e: RestException): AppError {
    val msg = e.message.orEmpty().lowercase()
    val status = e.statusCode
    return when {
        "invalid login credentials" in msg       -> AppError.InvalidCredentials
        "invalid password" in msg                -> AppError.InvalidCredentials
        "email already registered" in msg        -> AppError.EmailAlreadyUsed
        "user already registered" in msg         -> AppError.EmailAlreadyUsed
        "email not confirmed" in msg             -> AppError.EmailNotConfirmed
        "password should be at least" in msg     -> AppError.WeakPassword
        "should be at least 6 characters" in msg -> AppError.WeakPassword
        "user not found" in msg                  -> AppError.UserNotFound
        "signup is disabled" in msg              -> AppError.SignUpDisabled
        "email rate limit exceeded" in msg       -> AppError.TooManyRequests
        "jwt expired" in msg                     -> AppError.SessionExpired
        "invalid jwt" in msg                     -> AppError.SessionExpired
        "token is expired" in msg                -> AppError.SessionExpired
        "23505" in msg                           -> AppError.DuplicateEntry
        "23503" in msg                           -> AppError.ForeignKeyViolation
        "23502" in msg                           -> AppError.NotNullViolation
        "23514" in msg                           -> AppError.CheckViolation
        "42501" in msg                           -> AppError.PermissionDenied
        "42p01" in msg                           -> AppError.TableNotFound
        "pgrst116" in msg                        -> AppError.DataNotFound
        "pgrst204" in msg                        -> AppError.DataNotFound
        status == 401                            -> AppError.SessionExpired
        status == 403                            -> AppError.PermissionDenied
        status == 404                            -> AppError.DataNotFound
        status == 409                            -> AppError.DuplicateEntry
        status == 429                            -> AppError.TooManyRequests
        status in 500..599                       -> AppError.ServerError
        else                                     -> AppError.Unknown(e.message)
    }
}

private fun mapStorageException(e: HttpRequestException): AppError {
    val msg = e.message.orEmpty().lowercase()
    return when {
        "payload too large" in msg -> AppError.FileTooLarge
        "entity too large" in msg  -> AppError.FileTooLarge
        "invalid mime type" in msg -> AppError.InvalidFileType
        "mime type" in msg         -> AppError.InvalidFileType
        "bucket not found" in msg  -> AppError.StorageBucketNotFound
        "object not found" in msg  -> AppError.StorageObjectNotFound
        "not found" in msg         -> AppError.StorageObjectNotFound
        "unauthorized" in msg      -> AppError.StoragePermissionDenied
        "forbidden" in msg         -> AppError.StoragePermissionDenied
        else                       -> AppError.UploadFailed
    }
}
