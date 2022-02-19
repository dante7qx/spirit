package com.ymrs.spirit.ffx.pub;

import java.beans.PropertyEditorSupport;
import java.util.Date;

import org.springframework.util.StringUtils;

import com.ymrs.spirit.ffx.util.DateUtils;
import com.ymrs.spirit.ffx.util.DateUtils.TimeFormat;

public class SpecialDateEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) {
		if (!StringUtils.hasText(text)) {
			setValue(null);
			return;
		}
		Date date = null;
		if (text.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
			date = DateUtils.parseDateTime(text);
		} else if (text.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
			date = DateUtils.parseDateTime(text, TimeFormat.LONG_DATE_MINU_PATTERN_LINE);
		} else if (text.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
			date = DateUtils.parseDate(text);
		} else if (text.matches("^\\d{4}-\\d{1,2}$")) {
			date = DateUtils.parseDate(text + "-01");
		}
		setValue(date);
	}

}
