package pmg.android.runningcalculatortest;

import junit.framework.Assert;
import org.junit.Test;

import pmg.android.runningcalculator.data.DecimalHelper;
import pmg.android.runningcalculator.data.DistanceConverter;
import pmg.android.runningcalculator.data.DistanceUnit;

public class DistanceConverterTest {

	@Test
	public void convertMetresToKilometres() {
		Assert.assertEquals(1.0, DistanceConverter.getMetresInKilometres(1000));
		Assert.assertEquals(0.8, DistanceConverter.getMetresInKilometres(800));
		Assert.assertEquals(0.5, DistanceConverter.getMetresInKilometres(500));
	}

	@Test
	public void convertMetresToTrackLaps() {
		Assert.assertEquals(4.0, DistanceConverter.getMetresInTrackLaps(1600));
		Assert.assertEquals(2.0, DistanceConverter.getMetresInTrackLaps(800));
		Assert.assertEquals(1.0, DistanceConverter.getMetresInTrackLaps(400));
		Assert.assertEquals(0.5, DistanceConverter.getMetresInTrackLaps(200));
		Assert.assertEquals(0.25, DistanceConverter.getMetresInTrackLaps(100));
	}

	@Test
	public void convertMetresToMiles() {
		Assert.assertEquals(1.0, DistanceConverter.getMetresInMiles(1609));
		Assert.assertEquals(DecimalHelper.roundDistance(0.621371192), DistanceConverter.getMetresInMiles(1000));
	}
	
	@Test
	public void convertKilometresToMiles() {
		Assert.assertEquals(1.0, DistanceConverter.getKilometresInMiles(1.609));
		Assert.assertEquals(DecimalHelper.roundDistance(0.621371192), DistanceConverter.getKilometresInMiles(1.000));
	}

	@Test
	public void convertMilesToKilometres() {
		Assert.assertEquals(DecimalHelper.roundDistance(1.609344), DistanceConverter.getMilesInKilometres(1.0));
		Assert.assertEquals(1.000, DistanceConverter.getMilesInKilometres(0.6215));
	}

	@Test
	public void convertTrackLapsToKilometres() {
		Assert.assertEquals(0.1, DistanceConverter.getTrackLapsInKilometres(0.25));
		Assert.assertEquals(0.2, DistanceConverter.getTrackLapsInKilometres(0.5));
		Assert.assertEquals(0.4, DistanceConverter.getTrackLapsInKilometres(1.0));
		Assert.assertEquals(0.8, DistanceConverter.getTrackLapsInKilometres(2));
		Assert.assertEquals(1.6, DistanceConverter.getTrackLapsInKilometres(4));
		Assert.assertEquals(3.2, DistanceConverter.getTrackLapsInKilometres(8));
	}

	@Test
	public void convertTrackLapsToMiles() {
		Assert.assertEquals(0.062, DistanceConverter.getTrackLapsInMiles(0.25));
		Assert.assertEquals(0.124, DistanceConverter.getTrackLapsInMiles(0.5));
		Assert.assertEquals(0.249, DistanceConverter.getTrackLapsInMiles(1));
		Assert.assertEquals(0.497, DistanceConverter.getTrackLapsInMiles(2));
		Assert.assertEquals(0.994, DistanceConverter.getTrackLapsInMiles(4));
	}
	@Test
	public void convertMetresToDistanceUnit() {
		Assert.assertEquals(1.0, DistanceConverter.getMetresInUnit(1000,DistanceUnit.KILOMETRES));
		Assert.assertEquals(0.8, DistanceConverter.getMetresInUnit(800,DistanceUnit.KILOMETRES));
		Assert.assertEquals(0.5, DistanceConverter.getMetresInUnit(500,DistanceUnit.KILOMETRES));

		Assert.assertEquals(4.0, DistanceConverter.getMetresInUnit(1600,DistanceUnit.TRACKLAPS));
		Assert.assertEquals(2.0, DistanceConverter.getMetresInUnit(800,DistanceUnit.TRACKLAPS));
		Assert.assertEquals(1.0, DistanceConverter.getMetresInUnit(400,DistanceUnit.TRACKLAPS));
		Assert.assertEquals(0.5, DistanceConverter.getMetresInUnit(200,DistanceUnit.TRACKLAPS));
		Assert.assertEquals(0.25, DistanceConverter.getMetresInUnit(100,DistanceUnit.TRACKLAPS));

		Assert.assertEquals(1.0, DistanceConverter.getMetresInUnit(1609,DistanceUnit.MILES));
		Assert.assertEquals(DecimalHelper.roundDistance(0.621371192), DistanceConverter.getMetresInUnit(1000,DistanceUnit.MILES));

		Assert.assertEquals(1000.0, DistanceConverter.getMetresInUnit(1000,DistanceUnit.UNSPECIFIED));
		Assert.assertEquals(800.0, DistanceConverter.getMetresInUnit(800,DistanceUnit.UNSPECIFIED));
		Assert.assertEquals(500.0, DistanceConverter.getMetresInUnit(500,DistanceUnit.UNSPECIFIED));
	}
	
	@Test
	public void convertKilometresToTrackLaps() {
		Assert.assertEquals(2.5, DistanceConverter.getKilometresInTrackLaps(1));
		Assert.assertEquals(5.0, DistanceConverter.getKilometresInTrackLaps(2));
		
	}

	@Test
	public void convertMilesToTrackLaps() {
		Assert.assertEquals(5.0, DistanceConverter.getMilesInTrackLaps(1.24274238));
		Assert.assertEquals(10.0, DistanceConverter.getMilesInTrackLaps(2.48548477));
		
	}
}
