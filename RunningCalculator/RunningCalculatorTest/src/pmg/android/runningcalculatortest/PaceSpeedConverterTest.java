package pmg.android.runningcalculatortest;

import junit.framework.Assert;

import org.junit.Test;

import pmg.android.runningcalculator.data.DecimalHelper;
import pmg.android.runningcalculator.data.PaceSpeedConverter;

public class PaceSpeedConverterTest {

	@Test
	public void convertKphToSecPerKm() {
		Assert.assertEquals(DecimalHelper.roundPace(5 * 60), PaceSpeedConverter.getKphInSecPerKm(12));
		Assert.assertEquals(DecimalHelper.roundPace(15 * 60), PaceSpeedConverter.getKphInSecPerKm(4));
	}

	@Test
	public void convertKphToSecPerMi() {
		Assert.assertEquals(DecimalHelper.roundPace(24.14016 * 60), PaceSpeedConverter.getKphInSecPerMi(4));
		Assert.assertEquals(DecimalHelper.roundPace(8.04672 * 60), PaceSpeedConverter.getKphInSecPerMi(12));
	}

	@Test
	public void convertMphToSecPerKm() {
		Assert.assertEquals(DecimalHelper.roundPace(9.32056788 * 60), PaceSpeedConverter.getMphInSecPerKm(4));
		Assert.assertEquals(DecimalHelper.roundPace(4.14247461 * 60), PaceSpeedConverter.getMphInSecPerKm(9));
	}

	@Test
	public void convertMphToSecPerMi() {
		Assert.assertEquals(DecimalHelper.roundPace(15 * 60), PaceSpeedConverter.getMphInSecPerMi(4));
		Assert.assertEquals(DecimalHelper.roundPace(6.66666667 * 60), PaceSpeedConverter.getMphInSecPerMi(9));

	}

	@Test
	public void convertSecPerKmToKph() {
		Assert.assertEquals(DecimalHelper.roundSpeed(4), PaceSpeedConverter.getSecPerKmInKph(15 * 60));
		Assert.assertEquals(DecimalHelper.roundSpeed(12), PaceSpeedConverter.getSecPerKmInKph(5 * 60));

	}

	@Test
	public void convertSecPerMiToKph() {
		Assert.assertEquals(DecimalHelper.roundSpeed(4), PaceSpeedConverter.getSecPerMiInKph(24.14016 * 60));
		Assert.assertEquals(DecimalHelper.roundSpeed(12), PaceSpeedConverter.getSecPerMiInKph(8.04672 * 60));

	}

	@Test
	public void convertSecPerKmToMph() {
		Assert.assertEquals(DecimalHelper.roundSpeed(4), PaceSpeedConverter.getSecPerKmInMph(9.32056788 * 60));
		Assert.assertEquals(DecimalHelper.roundSpeed(9), PaceSpeedConverter.getSecPerKmInMph(4.14247461 * 60));
	}

	@Test
	public void convertSecPerMiToMph() {
		Assert.assertEquals(DecimalHelper.roundSpeed(4), PaceSpeedConverter.getSecPerMiInMph(15 * 60));
		Assert.assertEquals(DecimalHelper.roundSpeed(9), PaceSpeedConverter.getSecPerMiInMph(6.66666667 * 60));
	}

}
