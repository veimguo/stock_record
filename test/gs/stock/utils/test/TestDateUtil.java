package gs.stock.utils.test;

import gs.stock.utils.DateUtil;

public class TestDateUtil {
	public static void main(String[] args) {
		System.out.println(DateUtil.today());
		System.out.println(DateUtil.addOneDay(DateUtil.today(), true));
		System.out.println(DateUtil.addOneDay(DateUtil.today(), false));
		for (String t : DateUtil.splitDate("20191803"))
			System.out.println(t);
	}
}
