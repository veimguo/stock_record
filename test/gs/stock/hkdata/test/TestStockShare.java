package gs.stock.hkdata.test;

import java.util.List;

import gs.stock.hkdata.StockEntity;
import gs.stock.hkdata.StockShare;

public class TestStockShare {
	public static void main(String[] args) {

//		StockShare ss = new StockShare(new String[] { "深天马" });
//		List<StockEntity> r = ss.getOneDay("20171022");
//		for (StockEntity se : r) {
//			System.out.println("stock: " + se.getStockName());
//			System.out.println("start: " + se.getStartDate() + "\r\nend: "
//					+ se.getEndDate());
//			System.out.println("share: " + se.getStockShareNum());
//		}

		StockShare ss1 = new StockShare(new String[] { "深天马" });
		List<StockEntity> r1 = ss1.getRangeDays("20171020", "20171027");
		for (StockEntity se : r1) {
			System.out.println("stock: " + se.getStockName());
			System.out.println("start: " + se.getStartDate() + "\r\nend: "
					+ se.getEndDate());
			System.out.println("share: " + se.getStockShareNum());
		}

	}
}
