package com.targeteducare;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateUtils {
	// dd/mm/yyyy hh:mm:ss
	private static final int SPLIT_CODE = -1;
	public static String DEN_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public static String DEN_FORMAT_TIME = "hh:mm a";
	public static String DATE_FORMAT_MAIN = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	public static String DATE_APT_DATE = "dd MMM yyyy hh:mm a";
	public static String DATE_APT_DATE_MONTH = "dd MMM hh:mm a";
	public static String DATE_APT_DATE_NEW = "yyyy-MM-dd HH:mm:ss";// 2015-07-20T00:00:00

	// Jul 1 2015 12:00AM

	public static String getFormat() {
		return "Sqlite Date Format = 29-01-2011 09:12:12";
	}

	public static String getCal(Calendar cal) {
		return formatDate(cal.getTime(), "dd-MM-yyyy");
	}

	public static String getCal(Calendar cal, String format){
		return formatDate(cal.getTime(), format);
	}


	public static Date parseDate(String content, String format) {
		// yyyy-MM-dd HH:mm:ss
		// Jan 28 2015 12:00AM
		SimpleDateFormat dateFormat = new SimpleDateFormat(format,
				Locale.getDefault());
		try {
			Date d = dateFormat.parse(content);
			return d;
		} catch (Exception e) {
			Log.i("DATE UTILS", e.toString());
			return null;
		}
	}
	public static String parseDate(String content, String existingformat, String expectedformat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(existingformat,
				Locale.getDefault());
		try {
			Date d = dateFormat.parse(content);
			return formatDate(d, expectedformat);
		} catch (Exception e) {
			return "";
		}
	}
	public static Date parseDate(String content) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_MAIN,
				Locale.getDefault());
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			Date d = dateFormat.parse(content);
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	public static String parseAndGet(String content) {
		Date d = parseDate(content);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		if (Calendar.getInstance().get(Calendar.YEAR) != cal.get(Calendar.YEAR)) {
			return formatDate(parseDate(content), DATE_APT_DATE);
		} else {
			if (isToday(cal)) {
				return "Today "
						+ formatDate(parseDate(content), DEN_FORMAT_TIME);
			} else {
				return formatDate(parseDate(content), DATE_APT_DATE_MONTH);
			}
		}

	}

	public static String formatDate(Date content, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format,
				Locale.getDefault());
		try {
			return dateFormat.format(content);
		} catch (Exception e) {
			return null;
		}
	}

	public static String parseAptDate(String content) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_APT_DATE,
				Locale.getDefault());
		try {
			Date d = dateFormat.parse(content);
			return formatDate(d, "dd MMM");
		} catch (Exception e) {
			return "";
		}
	}

	public static String parseAptDateNew(String content) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm",
				Locale.getDefault());
		try {
			Date d = dateFormat.parse(content);
			
			String append = "AM";
			if (content.toLowerCase(Locale.getDefault()).contains("pm")) {
				append = "PM";
			}

			return formatDate(d, "dd MMM yyyy hh:mm a") ;
		} catch (Exception e) {
			return "";
		}
	}

	public static ArrayList<Calendar> getStartEnd(String aptDate, String timeContent) {
		// Jun 4 2015 12:00AM
		// 3:30 PM To 3:45 PM
		String format = "MMM dd yyyy hh:mm a";
		String[] strarr = timeContent.split("To");
		ArrayList<Calendar> list = new ArrayList<Calendar>();
		if (strarr.length == 2) {
			String[] datearr = aptDate.split("12:00A");
			String startdate = datearr[0] + strarr[0].trim();
			String enddate = datearr[0] + strarr[1].trim();
			Calendar start = Calendar.getInstance();
			start.setTime(parseDate(startdate, format));
			Calendar end = Calendar.getInstance();
			end.setTime(parseDate(enddate, format));
			list.add(start);
			list.add(end);
		}
		return list;
	}

	public static long getHours(Calendar from, Calendar to) {
		return TimeUnit.MILLISECONDS.toHours(to.getTimeInMillis()
				- from.getTimeInMillis());
	}

	public static Calendar addHours(Calendar caldata, int duration) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(caldata.getTime());
		int minute = cal.get(Calendar.MINUTE);
		Log.i("MINUTE ORI", "" + minute);
		cal.add(Calendar.MINUTE, duration + minute);
		return cal;
	}

	public static Calendar addHours(Date oldDate, int duration) {
		Date newDate = new Date(oldDate.getTime()
				+ TimeUnit.MINUTES.toMillis(duration));
		Calendar cal = Calendar.getInstance();
		cal.setTime(newDate);
		return cal;
	}

	public static int getYears(Calendar a, Calendar b) {
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
				|| (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a
						.get(Calendar.DATE) > b.get(Calendar.DATE))) {
			diff--;
		}
		return diff;
	}

	public static Date parse(String dateContent, String timeContent) {
		String dateFormat = "MMM dd yyyy";
		String timeFormat = "hh:mm a";
		Date d = parseDate(dateContent, dateFormat + " hh:mma");
		dateContent = formatDate(d, dateFormat);
		String format = dateFormat + " " + timeFormat;
		String content = dateContent + " " + timeContent;
		return parseDate(content, format);
	}

	public static boolean isMatch(String dateContent, Calendar selected) {
		String dateFormat = "MMM dd yyyy hh:mma";
		Date d = parseDate(dateContent, dateFormat);
		Calendar found = Calendar.getInstance();
		found.setTime(d);
		int temp = selected.get(Calendar.YEAR);
		if (getYear(found) != temp) {
			return false;
		}
		temp = selected.get(Calendar.MONTH);
		if (getMonth(found) != temp) {
			return false;
		}
		temp = selected.get(Calendar.DAY_OF_MONTH);
        return getDay(found) == temp;
    }

	public static boolean isToday(Calendar cal) {
		Calendar c = Calendar.getInstance();
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int year = cal.get(Calendar.YEAR);
        return (c.get(Calendar.MONTH) == month) && (c.get(Calendar.YEAR) == year)
                && (c.get(Calendar.DATE) == day);
	}

	public static boolean isMatch(Calendar cal, Calendar c) {
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DATE);
		int year = cal.get(Calendar.YEAR);
        return (c.get(Calendar.MONTH) == month) && (c.get(Calendar.YEAR) == year)
                && (c.get(Calendar.DATE) == day);
	}

	public static boolean isToday(String content) {
		// Jul 1 2015 12:00AM
		Date d = parseDate(content, DATE_APT_DATE);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return isToday(cal);
	}

	public static boolean isMaterialToday(String content) {
		// Jul 1 2015 12:00AM
		Date d = parseDate(content.replace("T", " "), DATE_APT_DATE_NEW);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		return isToday(cal);
	}

	public static int getYear(Calendar cal) {
		return cal.get(Calendar.YEAR);
	}

	public static int getMonth(Calendar cal) {
		return cal.get(Calendar.MONTH);
	}

	public static int getDay(Calendar cal) {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	public static String getSqliteTime() {
		return getSqliteTime(Calendar.getInstance());
	}

	public static String getSqliteTime(Calendar dt) {
		// Calendar dt = Calendar.getInstance();
		// Sqlite Date Format = 29-01-2011 09:12:12
		
		dt.set(Calendar.MONTH, dt.get(Calendar.MONTH)+1);
		StringBuilder sb = new StringBuilder();
		sb.append(dt.get(Calendar.YEAR) + "-");
		if (dt.get(Calendar.MONTH) < 10) {
			sb.append("0" + dt.get(Calendar.MONTH));
		} else
			sb.append(dt.get(Calendar.MONTH) + "");
		sb.append("-");
		if (dt.get(Calendar.DATE) < 10) {
			sb.append("0" + dt.get(Calendar.DATE));
		} else
			sb.append(dt.get(Calendar.DATE) + "");

		sb.append(" ");

		if (dt.get(Calendar.HOUR_OF_DAY) < 10)
			sb.append("0" + dt.get(Calendar.HOUR_OF_DAY));
		else
			sb.append(dt.get(Calendar.HOUR_OF_DAY) + "");
		sb.append(":");
		if (dt.get(Calendar.MINUTE) < 10)
			sb.append("0" + dt.get(Calendar.MINUTE));
		else
			sb.append(dt.get(Calendar.MINUTE) + "");
		sb.append(":");
		if (dt.get(Calendar.SECOND) < 10)
			sb.append("0" + dt.get(Calendar.SECOND));
		else
			sb.append(dt.get(Calendar.SECOND) + "");

		return sb.toString();
	}

	public static Calendar getCalendar(String dt) {
		String[] parseCal = dt.split(" ", SPLIT_CODE);
		String[] parseDate = parseCal[0].split("-", SPLIT_CODE);
		String[] parseTime = parseCal[1].split(":", SPLIT_CODE);
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parseDate[2]));
		cal.set(Calendar.MONTH, Integer.parseInt(parseDate[1]));
		cal.set(Calendar.YEAR, Integer.parseInt(parseDate[0]));

		cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parseTime[0]));
		cal.set(Calendar.MINUTE, Integer.parseInt(parseTime[1]));
		cal.set(Calendar.SECOND, Integer.parseInt(parseTime[2]));
		return cal;
	}

	public static String format(Calendar dt, boolean addTime) {
		String addyear = "";
		String addtime = "";
		if (dt.get(Calendar.YEAR) != Calendar.getInstance().get(Calendar.YEAR)) {
			addyear = dt.get(Calendar.YEAR) + "";
		}
		if (addTime) {
			addtime = dt.get(Calendar.HOUR_OF_DAY) + ":"
					+ dt.get(Calendar.MINUTE);
		}
		return dt.get(Calendar.DAY_OF_MONTH) + " "
				+ getMonthAbr(dt.get(Calendar.MONTH)) + " " + addyear + " "
				+ addtime;
	}

	public static String letterOfDayOfTheWeek(Date date) {
		Locale locale = Locale.getDefault();
		SimpleDateFormat sdf = new SimpleDateFormat("EEE", locale);
		Date d = new Date();
		return sdf.format(d);
	}

	public static String getMonthAbr(int month) {
		String dat = "";
		switch (month) {
		case Calendar.JANUARY:
			dat = "Jan";
			break;
		case Calendar.FEBRUARY:
			dat = "Feb";
			break;
		case Calendar.MARCH:
			dat = "Mar";
			break;
		case Calendar.APRIL:
			dat = "Apr";
			break;
		case Calendar.MAY:
			dat = "May";
			break;
		case Calendar.JUNE:
			dat = "Jun";
			break;
		case Calendar.JULY:
			dat = "Jul";
			break;
		case Calendar.AUGUST:
			dat = "Aug";
			break;
		case Calendar.SEPTEMBER:
			dat = "Sep";
			break;
		case Calendar.OCTOBER:
			dat = "Oct";
			break;
		case Calendar.NOVEMBER:
			dat = "Nov";
			break;
		case Calendar.DECEMBER:
			dat = "Dec";
			break;
		}
		return dat;
	}

	public static String parseDateNew(String content, String existingformat, String expectedformat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(existingformat,
				Locale.getDefault());
		try {
			Date d = dateFormat.parse(content);
			return formatDate(d, expectedformat);
		} catch (Exception e) {
			Log.e("error ", "error " + e.toString());
			return "";
		}
	}
}
