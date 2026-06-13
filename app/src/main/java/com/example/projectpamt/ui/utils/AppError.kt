package com.example.projectpamt.ui.utils

sealed class AppError {
    object InvalidCredentials : AppError()
    object EmailAlreadyUsed : AppError()
    object WeakPassword : AppError()
    object SessionExpired : AppError()
    object EmailNotConfirmed : AppError()
    object UserNotFound : AppError()
    object TooManyRequests : AppError()
    object SignUpDisabled : AppError()
    object NoInternet : AppError()
    object ServerError : AppError()
    object Timeout : AppError()
    object DataNotFound : AppError()
    object DuplicateEntry : AppError()
    object ForeignKeyViolation : AppError()
    object NotNullViolation : AppError()
    object CheckViolation : AppError()
    object PermissionDenied : AppError()
    object TableNotFound : AppError()
    object FileTooLarge : AppError()
    object InvalidFileType : AppError()
    object UploadFailed : AppError()
    object StorageBucketNotFound : AppError()
    object StorageObjectNotFound : AppError()
    object StoragePermissionDenied : AppError()
    data class Unknown(val rawMessage: String? = null) : AppError()
}

fun AppError.toUserMessage(): String = when (this) {
    AppError.InvalidCredentials      -> "Email atau password salah. Silakan coba lagi."
    AppError.EmailAlreadyUsed        -> "Email ini sudah terdaftar. Silakan login."
    AppError.WeakPassword            -> "Password terlalu lemah. Gunakan minimal 8 karakter."
    AppError.SessionExpired          -> "Sesi Anda telah berakhir. Silakan login kembali."
    AppError.EmailNotConfirmed       -> "Email belum dikonfirmasi. Cek inbox Anda."
    AppError.UserNotFound            -> "Akun tidak ditemukan."
    AppError.TooManyRequests         -> "Terlalu banyak percobaan. Tunggu beberapa saat."
    AppError.SignUpDisabled          -> "Pendaftaran akun sedang dinonaktifkan."
    AppError.NoInternet              -> "Tidak ada koneksi internet. Periksa jaringan Anda."
    AppError.ServerError             -> "Server sedang bermasalah. Coba beberapa saat lagi."
    AppError.Timeout                 -> "Koneksi timeout. Silakan coba lagi."
    AppError.DataNotFound            -> "Data tidak ditemukan."
    AppError.DuplicateEntry          -> "Data sudah ada. Gunakan data yang berbeda."
    AppError.ForeignKeyViolation     -> "Data terkait tidak ditemukan atau tidak valid."
    AppError.NotNullViolation        -> "Ada data wajib yang belum diisi."
    AppError.CheckViolation          -> "Data tidak memenuhi syarat yang ditentukan."
    AppError.PermissionDenied        -> "Anda tidak memiliki akses untuk tindakan ini."
    AppError.TableNotFound           -> "Terjadi kesalahan konfigurasi. Hubungi administrator."
    AppError.FileTooLarge            -> "Ukuran file terlalu besar."
    AppError.InvalidFileType         -> "Format file tidak didukung."
    AppError.UploadFailed            -> "Gagal mengunggah file. Silakan coba lagi."
    AppError.StorageBucketNotFound   -> "Terjadi kesalahan penyimpanan. Hubungi administrator."
    AppError.StorageObjectNotFound   -> "File tidak ditemukan."
    AppError.StoragePermissionDenied -> "Anda tidak memiliki akses ke file ini."
    is AppError.Unknown              -> "Terjadi kesalahan. Silakan coba lagi."
}
