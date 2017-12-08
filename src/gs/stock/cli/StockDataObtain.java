package gs.stock.cli;

import gs.stock.excel.ExcelOperation;
import gs.stock.excel.GenerateExcelOfResult;
import gs.stock.hkdata.StockEntity;
import gs.stock.hkdata.StockShare;
import gs.stock.utils.DateUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * 获取股票港股持仓数据的操作类
 * 
 * @author Gsir
 * 
 */
public class StockDataObtain {

	private ExcelOperation excelOP = null;
	private String resultPath = "";
	private List<String> stocks = null;
	private String startDate = "";
	private String endDate = "";

	public StockDataObtain(String excelFile) {
		excelOP = ExcelOperation.getExcelOperationInstance(excelFile);
		resultPath = excelFile;
	}

	/**
	 * 更新 输入路径的文件的当天股票持仓数据
	 * 
	 */
	public void dailyUpdate() {
		String[] stockNames = excelOP.getAllSheetNames();
		String lastDay = DateUtil.addOneDay(DateUtil.today(), false);
		StockShare ss = new StockShare(stockNames);
		List<StockEntity> r = ss.getOneDay(lastDay);
		GenerateExcelOfResult g = new GenerateExcelOfResult(resultPath);
		g.build(r);
	}

	/**
	 * 初始化股票港资持仓数据
	 * 
	 * @return true:成功， false: 失败
	 */
	public boolean initailStockShare() {
		if (stocks.size() == 0 || endDate.compareTo(startDate) < 0) {
			return false;
		}
		StockShare ss = new StockShare(stocks);
		List<StockEntity> r = ss.getRangeDays(startDate, endDate);
		GenerateExcelOfResult g = new GenerateExcelOfResult(resultPath);
		g.build(r);
		return true;
	}

	/**
	 * 添加要获取的股票
	 */
	public void addStock(String stock) {
		if (stocks == null) {
			stocks = new LinkedList<String>();
		}
		stocks.add(stock);
	}

	/**
	 * 添加要获取的股票
	 */
	public void addStock(String[] stock) {
		if (stocks == null) {
			stocks = new LinkedList<String>();
		}
		for (String t : stock) {
			stocks.add(t);
		}
	}

	/**
	 * 添加要获取的股票
	 */
	public void addStock(List<String> stock) {
		if (stocks == null) {
			stocks = new LinkedList<String>();
		}
		stocks.addAll(stock);
	}

	/**
	 * 删除要获取的股票
	 */
	public void deleteStock(String stock) {
		if (stocks != null) {
			stocks.remove(stock);
		}
	}

	/**
	 * 设置要获取股票的开始日期
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * 设置要获取股票的结束日期
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
