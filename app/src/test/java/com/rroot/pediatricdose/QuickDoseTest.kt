package com.rroot.pediatricdose

import com.rroot.pediatricdose.data.DoseMode
import com.rroot.pediatricdose.data.PediDrug
import com.rroot.pediatricdose.data.PediDrugList
import com.rroot.pediatricdose.domain.QuickDose
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class QuickDoseTest {

    @Test
    fun hollidaySegarBreakpoints() {
        // 5 kg → 5 × 100 = 500 mL/day
        assertEquals(500.0, QuickDose.hollidaySegarPerDay(5.0), 0.001)
        // 10 kg → 1000
        assertEquals(1000.0, QuickDose.hollidaySegarPerDay(10.0), 0.001)
        // 15 kg → 1000 + 5 × 50 = 1250
        assertEquals(1250.0, QuickDose.hollidaySegarPerDay(15.0), 0.001)
        // 20 kg → 1500
        assertEquals(1500.0, QuickDose.hollidaySegarPerDay(20.0), 0.001)
        // 25 kg → 1500 + 5 × 20 = 1600
        assertEquals(1600.0, QuickDose.hollidaySegarPerDay(25.0), 0.001)
    }

    @Test
    fun paracetamolIvVolumeAt10Kg() {
        val drug = PediDrugList.all.first { it.id == "paracetamol-iv" }
        val r = QuickDose.compute(drug, 10.0)
        // 15 mg/kg × 10 = 150 mg / 10 mg/cc = 15 cc
        assertTrue(r.primary.startsWith("15"))
        assertNotNull(r.secondary)
    }

    @Test
    fun paracetamolIvCappedAt100Kg() {
        val drug = PediDrugList.all.first { it.id == "paracetamol-iv" }
        val r = QuickDose.compute(drug, 100.0)
        assertTrue(r.capped)
        // capped at 1000 mg → 100 cc
        assertTrue(r.primary.startsWith("100"))
    }

    @Test
    fun saltyBolusAt8Kg() {
        val drug = PediDrugList.all.first { it.id == "ns-bolus" }
        val r = QuickDose.compute(drug, 8.0)
        // 20 × 8 = 160 cc
        assertTrue(r.primary.contains("160"))
    }

    @Test
    fun midazolamInfusionEqualsKgPerHour() {
        val drug = PediDrugList.all.first { it.id == "midazolam-infusion" }
        // With recipe (1 cc 5 mg/cc + 49 cc N.S = 50 cc total)
        // final concentration = 5 mg / 50 cc = 0.1 mg/cc
        // For 0.1 mg/kg/h × 6 kg = 0.6 mg/h ÷ 0.1 = 6 cc/h
        val r = QuickDose.compute(drug, 6.0)
        assertTrue("expected ≈6 cc/h, got ${r.primary}", r.primary.startsWith("6"))
    }

    @Test
    fun atropineMinimumApplied() {
        val drug = PediDrugList.all.first { it.id == "atropine" }
        // At 1 kg, raw dose = 0.02 mg, but min 0.1 mg → cc = 0.1/0.6 ≈ 0.17 cc
        val r = QuickDose.compute(drug, 1.0)
        assertTrue(r.capped)
    }

    @Test
    fun zeroWeightYieldsDash() {
        val drug = PediDrugList.all.first { it.id == "ampicillin" }
        val r = QuickDose.compute(drug, 0.0)
        assertEquals("—", r.primary)
    }

    @Test
    fun everyDrugComputes() {
        for (d in PediDrugList.all) {
            val r = QuickDose.compute(d, 12.0)
            assertNotNull("drug ${d.id} returned null primary", r.primary)
            assertTrue("drug ${d.id} produced empty primary", r.primary.isNotBlank())
        }
    }
}
