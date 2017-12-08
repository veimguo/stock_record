package gs.stock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式处理工具
 * 
 * @author Gsir
 * 
 */
public class DateUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static Calendar c = Calendar.getInstance();
	
	public static String today() {
		Date today = new Date(System.currentTimeMillis());
		return sdf.format(today);
	}
	/**
	 * 在 thisDay的基础上加或者减去一天，返回字符串形式 yyyyMMdd
	 *  <p>add 
	 *  <p>&nbsp;&nbsp;&nbsp; true &nbsp;增加
	 *  <p>&nbsp;&nbsp;&nbsp; false 减少
	 * @param thisDay
	 * @param add
	 * @return
	 */
	public static String addOneDay(String thisDay, boolean add) {
		try {
			int d = add ? 1 : -1;

			c.setTime(sdf.parse(thisDay));
			c.add(Calendar.DAY_OF_MONTH, d);
			return sdf.format(c.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param date
	 * @return 0：Year, 1:Month, 2:Day
	 */
	public static String[] splitDate(String date) {
		String[] result = new String[3];
		try {
			c.setTime(sdf.parse(date));
			result[0] = c.get(c.YEAR) + "";
			int month = c.get(c.MONTH) + 1;
			String append = "";
			if (month < 10) {
				append = "0";
			}
			result[1] = append + month;
			int day = c.get(c.DAY_OF_MONTH);
			if (day < 10) {
				append = "0";
			} else {
				append = "";
			}
			result[2] = append + day;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
}
