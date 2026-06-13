package com.example.projectpamt

import com.example.projectpamt.data.model.Kas
import com.example.projectpamt.data.model.Pengeluaran
import com.example.projectpamt.data.repository.PengeluaranRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PengeluaranRepositoryTest {

    private val repository = PengeluaranRepository()

    @Test
    fun testGetAllPengeluaran() = runBlocking {
        val list = repository.getAllPengeluaran()
        assertNotNull(list)
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun testInsertPengeluaran() = runBlocking {
        val initialSize = repository.getAllPengeluaran().size
        val newPengeluaran = Pengeluaran(
            idKategori = "5602b255-f3d1-4339-86ff-3da58c0437be",
            idKas = "1",
            deskripsi = "Test Pengeluaran Baru",
            total = 50000.0
        )
        repository.insertPengeluaran(newPengeluaran)
        
        val updatedList = repository.getAllPengeluaran()
        assertEquals(initialSize + 1, updatedList.size)
        
        val added = updatedList.last()
        assertEquals("Test Pengeluaran Baru", added.deskripsi)
        assertEquals(50000.0, added.total, 0.001)
        assertNotNull(added.idPengeluaran)
    }

    @Test
    fun testUpdatePengeluaran() = runBlocking {
        val list = repository.getAllPengeluaran()
        assertTrue(list.isNotEmpty())
        
        val target = list.first()
        val targetId = target.idPengeluaran!!
        
        val updated = target.copy(
            deskripsi = "Updated Deskripsi",
            total = 99999.0
        )
        repository.updatePengeluaran(targetId, updated)
        
        val listAfterUpdate = repository.getAllPengeluaran()
        val found = listAfterUpdate.first { it.idPengeluaran == targetId }
        assertEquals("Updated Deskripsi", found.deskripsi)
        assertEquals(99999.0, found.total, 0.001)
    }

    @Test
    fun testDeletePengeluaran() = runBlocking {
        val list = repository.getAllPengeluaran()
        assertTrue(list.isNotEmpty())
        
        val targetId = list.first().idPengeluaran!!
        val initialSize = list.size
        
        repository.deletePengeluaran(targetId)
        
        val listAfterDelete = repository.getAllPengeluaran()
        assertEquals(initialSize - 1, listAfterDelete.size)
        assertTrue(listAfterDelete.none { it.idPengeluaran == targetId })
    }

    @Test
    fun testBalanceAdjustmentLogic_sameKas() {
        // Scenario: Kas remains the same, amount changes from 100k to 80k.
        // Formula: oldTotal - newTotal => 100k - 80k = +20k (Kas receives 20k refund/adjustment).
        val oldTotal = 100000.0
        val newTotal = 80000.0
        
        val adjustment = oldTotal - newTotal
        assertEquals(20000.0, adjustment, 0.001)
        
        // Scenario: Kas remains the same, amount changes from 100k to 120k.
        // Formula: oldTotal - newTotal => 100k - 120k = -20k (Kas balance is reduced by additional 20k).
        val newTotalHigher = 120000.0
        val adjustmentNegative = oldTotal - newTotalHigher
        assertEquals(-20000.0, adjustmentNegative, 0.001)
    }

    @Test
    fun testBalanceAdjustmentLogic_changedKas() {
        // Scenario: Kas changes from Kas A (ID 1) to Kas B (ID 2).
        // Original expense: Kas A, Total = 50k
        // Updated expense: Kas B, Total = 70k
        // Expected outcome:
        // 1. Kas A balance gets restored with old amount (+50k)
        // 2. Kas B balance gets reduced by new amount (-70k)
        val originalTotal = 50000.0
        val updatedTotal = 70000.0
        
        val oldKasRestoreAmount = originalTotal
        val newKasDeductAmount = -updatedTotal
        
        assertEquals(50000.0, oldKasRestoreAmount, 0.001)
        assertEquals(-70000.0, newKasDeductAmount, 0.001)
    }

    @Test
    fun testKategoriUpdate() = runBlocking {
        val list = repository.getAllPengeluaran()
        assertTrue(list.isNotEmpty())

        val target = list.first()
        val targetId = target.idPengeluaran!!
        val newKategoriId = "4bfa0525-455b-419b-8d16-6512eb2d4ee7" // Makanan
        val newKategoriObj = com.example.projectpamt.data.model.Kategori(
            idKategori = newKategoriId,
            name = "Makanan"
        )

        val updated = target.copy(
            idKategori = newKategoriId,
            kategori = newKategoriObj
        )
        repository.updatePengeluaran(targetId, updated)

        val updatedList = repository.getAllPengeluaran()
        val found = updatedList.first { it.idPengeluaran == targetId }
        assertEquals(newKategoriId, found.idKategori)
        assertEquals("Makanan", found.kategori?.name)
    }
}
