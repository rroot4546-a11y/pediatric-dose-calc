package com.rroot.pediatricdose

import com.rroot.pediatricdose.domain.DoseCalculator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

private const val EPS = 1e-9

class DoseCalculatorTest {

    @Test
    fun singleDose_basicMgCalculation() {
        val r = DoseCalculator.singleDose(weightKg = 20.0, mgPerKg = 15.0)
        assertEquals(300.0, r.doseMg, EPS)
        assertEquals(300.0, r.rawDoseMg, EPS)
        assertFalse(r.capped)
        assertNull(r.volumeMl)
    }

    @Test
    fun singleDose_withConcentrationGivesVolume() {
        // 300 mg @ 24 mg/mL -> 12.5 mL
        val r = DoseCalculator.singleDose(
            weightKg = 20.0,
            mgPerKg = 15.0,
            concentrationMgPerMl = 24.0,
        )
        assertNotNull(r.volumeMl)
        assertEquals(12.5, r.volumeMl!!, EPS)
    }

    @Test
    fun singleDose_appliesCapWhenExceeded() {
        // 80 kg child, 15 mg/kg = 1200 mg; cap at 1000 mg.
        val r = DoseCalculator.singleDose(
            weightKg = 80.0,
            mgPerKg = 15.0,
            maxSingleMg = 1000.0,
        )
        assertTrue(r.capped)
        assertEquals(1000.0, r.doseMg, EPS)
        assertEquals(1200.0, r.rawDoseMg, EPS)
    }

    @Test
    fun singleDose_volumeIsBasedOnCappedMg() {
        // Capped to 1000 mg, 50 mg/mL -> 20 mL (not 24 mL).
        val r = DoseCalculator.singleDose(
            weightKg = 80.0,
            mgPerKg = 15.0,
            concentrationMgPerMl = 50.0,
            maxSingleMg = 1000.0,
        )
        assertEquals(20.0, r.volumeMl!!, EPS)
    }

    @Test
    fun singleDose_doesNotCapWhenUnderThreshold() {
        val r = DoseCalculator.singleDose(
            weightKg = 10.0,
            mgPerKg = 15.0,
            maxSingleMg = 1000.0,
        )
        assertFalse(r.capped)
        assertEquals(150.0, r.doseMg, EPS)
    }

    @Test(expected = IllegalArgumentException::class)
    fun singleDose_rejectsZeroWeight() {
        DoseCalculator.singleDose(weightKg = 0.0, mgPerKg = 10.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun singleDose_rejectsNegativeMgPerKg() {
        DoseCalculator.singleDose(weightKg = 10.0, mgPerKg = -1.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun singleDose_rejectsNonPositiveConcentration() {
        DoseCalculator.singleDose(
            weightKg = 10.0,
            mgPerKg = 10.0,
            concentrationMgPerMl = 0.0,
        )
    }

    @Test
    fun dailyDose_dividedDoses() {
        // 20 kg * 90 mg/kg/day = 1800 mg/day; /3 = 600 mg/dose.
        val r = DoseCalculator.dailyDose(
            weightKg = 20.0,
            mgPerKgPerDay = 90.0,
            timesPerDay = 3,
        )
        assertEquals(1800.0, r.totalDailyMg, EPS)
        assertEquals(600.0, r.perDoseMg, EPS)
        assertFalse(r.capped)
    }

    @Test
    fun dailyDose_dailyCapOverridesTotal() {
        // 80 kg * 90 mg/kg/day = 7200 mg/day, capped at 3000 mg/day => 1000 mg/dose TID.
        val r = DoseCalculator.dailyDose(
            weightKg = 80.0,
            mgPerKgPerDay = 90.0,
            timesPerDay = 3,
            maxDailyMg = 3000.0,
        )
        assertTrue(r.capped)
        assertEquals(3000.0, r.totalDailyMg, EPS)
        assertEquals(1000.0, r.perDoseMg, EPS)
    }

    @Test
    fun dailyDose_singleCapStillApplied() {
        // Total daily 1500 mg / 2 = 750 mg/dose, but max single = 500 mg.
        val r = DoseCalculator.dailyDose(
            weightKg = 50.0,
            mgPerKgPerDay = 30.0,
            timesPerDay = 2,
            maxDailyMg = 2000.0,
            maxSingleMg = 500.0,
        )
        assertTrue(r.capped)
        assertEquals(500.0, r.perDoseMg, EPS)
    }

    @Test
    fun round_roundsToGivenDecimals() {
        assertEquals(12.35, DoseCalculator.round(12.34567, 2), EPS)
        assertEquals(12.3, DoseCalculator.round(12.34, 1), EPS)
        assertEquals(12.0, DoseCalculator.round(12.4, 0), EPS)
    }

    @Test
    fun round_handlesZeroDecimals() {
        assertEquals(13.0, DoseCalculator.round(12.6, 0), EPS)
    }

    @Test(expected = IllegalArgumentException::class)
    fun round_rejectsNegativeDecimals() {
        DoseCalculator.round(1.23, -1)
    }

    @Test
    fun clamp_insideAndOutsideRange() {
        assertEquals(5.0, DoseCalculator.clamp(5.0, 1.0..10.0), EPS)
        assertEquals(1.0, DoseCalculator.clamp(-2.0, 1.0..10.0), EPS)
        assertEquals(10.0, DoseCalculator.clamp(99.0, 1.0..10.0), EPS)
    }

    @Test
    fun weightRange_isSane() {
        assertTrue(3.0 in DoseCalculator.WEIGHT_RANGE_KG)
        assertTrue(50.0 in DoseCalculator.WEIGHT_RANGE_KG)
        assertFalse(0.1 in DoseCalculator.WEIGHT_RANGE_KG)
        assertFalse(200.0 in DoseCalculator.WEIGHT_RANGE_KG)
    }
}
