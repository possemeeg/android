package pmg.android.runningcalculatortest;

import junit.framework.Assert;

import org.junit.Test;

import pmg.android.runningcalculator.PreferenceValues;


public class PreferenceAbilityTest {
	@Test
	public void isInAbilityGetsItRight(){
		Assert.assertTrue(PreferenceValues.isInAbility(200,200,500,PreferenceValues.AbilityFilterValue.All));
		Assert.assertFalse(PreferenceValues.isInAbility(199,200,500,PreferenceValues.AbilityFilterValue.All));
		Assert.assertTrue(PreferenceValues.isInAbility(200,200,500,PreferenceValues.AbilityFilterValue.All));
		Assert.assertTrue(PreferenceValues.isInAbility(500,200,500,PreferenceValues.AbilityFilterValue.All));
		Assert.assertTrue(PreferenceValues.isInAbility(500,200,500,PreferenceValues.AbilityFilterValue.All));
		Assert.assertFalse(PreferenceValues.isInAbility(501,200,500,PreferenceValues.AbilityFilterValue.All));
		Assert.assertTrue(PreferenceValues.isInAbility(200,200,500,PreferenceValues.AbilityFilterValue.Slow));
		Assert.assertFalse(PreferenceValues.isInAbility(199,200,500,PreferenceValues.AbilityFilterValue.Slow));
		Assert.assertTrue(PreferenceValues.isInAbility(350,200,500,PreferenceValues.AbilityFilterValue.Medium));
		Assert.assertFalse(PreferenceValues.isInAbility(201,200,500,PreferenceValues.AbilityFilterValue.Medium));
		Assert.assertFalse(PreferenceValues.isInAbility(499,200,500,PreferenceValues.AbilityFilterValue.Medium));
		Assert.assertTrue(PreferenceValues.isInAbility(500,200,500,PreferenceValues.AbilityFilterValue.Fast));
		Assert.assertFalse(PreferenceValues.isInAbility(501,200,500,PreferenceValues.AbilityFilterValue.Fast));
	}
}
