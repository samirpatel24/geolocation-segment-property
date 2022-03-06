package com.liferay.geolocation.segment.field.customizer;

import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.segments.field.customizer.SegmentsFieldCustomizer;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;

@Component(
		immediate = true,
		property = {
			"segments.field.customizer.entity.name=Context",
			"segments.field.customizer.key=" + GeoLocationSegmentFieldCustomizer.KEY,
			"segments.field.customizer.priority:Integer=50"
		},
		service = SegmentsFieldCustomizer.class
	)

public class GeoLocationSegmentFieldCustomizer implements SegmentsFieldCustomizer  {

	public static final String KEY = "country";

	
	
	@Override
	public List<String> getFieldNames() {
		// TODO Auto-generated method stub
		return _fieldNames;
	}

	@Override
	public String getLabel(String fieldName, Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());

		return LanguageUtil.get(resourceBundle, "country-field-label");
	}
	
	
	@Override
	public String getKey() {
		// TODO Auto-generated method stub
		return KEY;
	}

	
	private static final List<String> _fieldNames = ListUtil.fromArray(
			new String[] {"country"});
	
}
