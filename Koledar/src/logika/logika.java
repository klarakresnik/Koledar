package logika;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.*;


public class logika {
	
	public static Integer[] danasnjiDatum() {
		Date today = Calendar.getInstance().getTime();
		DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy G 'at' HH:mm:ss z");
		String[] date_string = dateFormat.format(today).split(" ")[0].split("\\.");
		
		int day = Integer.parseInt(date_string[0]);
		int month = Integer.parseInt(date_string[1]);
		int year = Integer.parseInt(date_string[2]);
		Integer[] date_int = new Integer[]{day, month, year};
		return date_int;
	}
	
//	metoda vrne stevilko, ki pove iz katerega tedna v mesecu je podani datum
	public static int tedenVMesecu(int year, int month, int day) {
		LocalDate localDate = LocalDate.of(year, month, day);
		TemporalField weekOfMonth = WeekFields.of(DayOfWeek.MONDAY, 1).weekOfMonth();
		return localDate.get(weekOfMonth);
	}
	
//	metoda vrne stevilko, ki pove kateri dan v tednu je podan datum
	public static int danVTednu(int year, int month, int day) {
		LocalDate localDate = LocalDate.of(year, month, day);
		return localDate.getDayOfWeek().getValue();
		
	}
	
	public static int steviloDniVMesecu(int year, int month) {
		YearMonth yearMonthObject = YearMonth.of(year, month);
		return yearMonthObject.lengthOfMonth();
	}
	
	public static int steviloTednovVMesecu(int year, int month) {
		Calendar cal = Calendar.getInstance();
	    cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.DAY_OF_MONTH, 1);
	    cal.set(Calendar.MONTH, month - 1);
	    int weekOfMonth = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
		return weekOfMonth;
	}
}