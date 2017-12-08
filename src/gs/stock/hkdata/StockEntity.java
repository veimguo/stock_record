package gs.stock.hkdata;

import java.util.LinkedList;
import java.util.List;

/**
 * 股票相关数据的封装
 * 
 * @author Gsir
 * 
 */
public class StockEntity {
	private String stockName;
	private List<String> stockShareNum;
	private String startDate;
	private String endDate;

	public StockEntity() {
		stockShareNum = new LinkedList<>();
		stockName = "";
		startDate = "";
		endDate = "";
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public List<String> getStockShareNum() {
		return stockShareNum;
	}

	public void addStockShareNum(String stockShareNum) {
		this.stockShareNum.add(stockShareNum);
	}

	public void addStockShareNum(List<String> stockShareNum) {
		this.stockShareNum.addAll(stockShareNum);
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * 将se中的数据添加到该对象中
	 * @param se
	 * @return
	 */
	public boolean append(StockEntity se) {
		if (se.getStockName().equals(stockName)) {
			endDate = se.endDate;
			stockShareNum.addAll(se.getStockShareNum());
			return true;
		} else {
			return false;
		}

	}

}
