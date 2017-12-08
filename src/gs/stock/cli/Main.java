package gs.stock.cli;

import gs.stock.utils.DateUtil;

public class Main {
	private static String[] dailyUpdateStocks = { "record.xls" };
	private static String[] newStokcNames = { "宜华生活" };

	public static void main(String[] args) {
		dailyUpdate();
//		createNewStocks("record1.xls");
	}

	public static void dailyUpdate() {
		for (String stockFile : dailyUpdateStocks) {
			StockDataObtain sdo = new StockDataObtain(stockFile);
			sdo.dailyUpdate();
		}
	}

	public static void createNewStocks(String outputPath, String startDate,
			String endDate) {
		StockDataObtain sdo = new StockDataObtain(outputPath);
		sdo.addStock(newStokcNames);
		sdo.setStartDate(startDate);
		sdo.setEndDate(endDate);
		sdo.initailStockShare();
	}

	public static void createNewStocks(String outputPath) {
		String s = "20170317";
		String e = DateUtil.addOneDay(DateUtil.today(), false);
		createNewStocks(outputPath, s, e);
	}
}
