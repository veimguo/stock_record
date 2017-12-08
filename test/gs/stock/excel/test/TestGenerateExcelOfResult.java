package gs.stock.excel.test;

import gs.stock.excel.GenerateExcelOfResult;
import gs.stock.hkdata.StockEntity;
import gs.stock.hkdata.StockShare;
import gs.stock.utils.DateUtil;

import java.util.List;

public class TestGenerateExcelOfResult {
	public static void main(String[] args) {
		// String share = "15,225,009";
		// share = share.replaceAll(",", "");
		// System.out.println(share);
		// long a = Long.parseLong(share);
		// System.out.println(a);
		String startDate = "20170317";
		String lastDay = DateUtil.addOneDay(DateUtil.today(), false);
		String endDate = DateUtil.addOneDay(lastDay, false);

		String[] stocks = { "索菲亚", "中国巨石", "通富微电" };
//		String[] allStocks = { "深天马", "中国平安", "索菲亚", "中国巨石", "通富微电" };
		// 初始化
//		/*
		 StockShare ss1 = new StockShare(stocks); List<StockEntity> r1 =
//		 ss1.getRangeDays(startDate, endDate);
		 ss1.getRangeDays("20171005", "20171015");
//		 */
		// 每日增量
		/*
		  StockShare ss1 = new StockShare(allStocks); List<StockEntity> r1 =
		  ss1.getRangeDays(lastDay, lastDay);
//		 */
		for (StockEntity se : r1) {
			System.out.println("stock: " + se.getStockName());
			System.out.println("start: " + se.getStartDate() + "\r\nend: "
					+ se.getEndDate());
			System.out.println("share: " + se.getStockShareNum());
		}
		String result = "record2.xls";
		GenerateExcelOfResult g = new GenerateExcelOfResult(result);
		g.build(r1);
	}
}
