package com.example.projectpamt.data.repository

import com.example.projectpamt.data.model.Kategori
import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.Pengeluaran

class PengeluaranRepository {
    companion object {
        private val mockKategoriMakanan = Kategori(idKategori = "4bfa0525-455b-419b-8d16-6512eb2d4ee7", name = "Makanan")
        private val mockKategoriJasa = Kategori(idKategori = "5602b255-f3d1-4339-86ff-3da58c0437be", name = "Jasa")
        
        private val mockKasUtama = Kas(idKas = "1", nama = "Kas Utama", saldo = 15234.50)
        private val mockKasKecil = Kas(idKas = "2", nama = "Kas Laci 1", saldo = 28450.00)

        private val _mockList = mutableListOf(
            Pengeluaran(
                idPengeluaran = "1",
                idKategori = "5602b255-f3d1-4339-86ff-3da58c0437be",
                idKas = "1",
                deskripsi = "Bayar Listrik Toko",
                total = 450000.0,
                createdAt = "2026-06-12T10:00:00Z",
                kategori = mockKategoriJasa,
                kas = mockKasUtama
            ),
            Pengeluaran(
                idPengeluaran = "2",
                idKategori = "4bfa0525-455b-419b-8d16-6512eb2d4ee7",
                idKas = "1",
                deskripsi = "Restock Beras Premium",
                total = 1200000.0,
                createdAt = "2026-06-10T14:30:00Z",
                kategori = mockKategoriMakanan,
                kas = mockKasUtama
            ),
            Pengeluaran(
                idPengeluaran = "3",
                idKategori = "5602b255-f3d1-4339-86ff-3da58c0437be",
                idKas = "2",
                deskripsi = "Biaya Kurir Internal",
                total = 75000.0,
                createdAt = "2026-06-08T09:15:00Z",
                kategori = mockKategoriJasa,
                kas = mockKasKecil
            ),
            Pengeluaran(
                idPengeluaran = "4",
                idKategori = "5602b255-f3d1-4339-86ff-3da58c0437be",
                idKas = "1",
                deskripsi = "Servis AC Kasir",
                total = 250000.0,
                createdAt = "2026-06-05T16:00:00Z",
                kategori = mockKategoriJasa,
                kas = mockKasUtama
            ),
            Pengeluaran(
                idPengeluaran = "5",
                idKategori = "4bfa0525-455b-419b-8d16-6512eb2d4ee7",
                idKas = "2",
                deskripsi = "Konsumsi Rapat Staff",
                total = 150000.0,
                createdAt = "2026-06-02T12:00:00Z",
                kategori = mockKategoriMakanan,
                kas = mockKasKecil
            )
        )
    }

    suspend fun getAllPengeluaran(): List<Pengeluaran> {
        return _mockList.toList()
    }

    suspend fun insertPengeluaran(pengeluaran: Pengeluaran) {
        val newId = (_mockList.mapNotNull { it.idPengeluaran?.toIntOrNull() }.maxOrNull() ?: 0) + 1
        val nowStr = java.time.Instant.now().toString()
        val toInsert = pengeluaran.copy(
            idPengeluaran = newId.toString(),
            createdAt = nowStr
        )
        _mockList.add(toInsert)
    }

    suspend fun updatePengeluaran(id: String, pengeluaran: Pengeluaran) {
        val idx = _mockList.indexOfFirst { it.idPengeluaran == id }
        if (idx != -1) {
            val old = _mockList[idx]
            _mockList[idx] = old.copy(
                idKategori = pengeluaran.idKategori,
                idKas = pengeluaran.idKas,
                deskripsi = pengeluaran.deskripsi,
                total = pengeluaran.total,
                kategori = pengeluaran.kategori ?: old.kategori,
                kas = pengeluaran.kas ?: old.kas
            )
        }
    }

    suspend fun deletePengeluaran(id: String) {
        _mockList.removeAll { it.idPengeluaran == id }
    }
}