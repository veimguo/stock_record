package gs.stock.excel;

import gs.stock.hkdata.StockEntity;
import gs.stock.utils.DateUtil;
import gs.stock.utils.StockColumn;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * 将获取的结果，输出为一个excel
 * 
 * @author Gsir
 * 
 */
public class GenerateExcelOfResult {
	private String outputPath;
	private WritableWorkbook writableWorkBook = null;
	private List<StockEntity> stocks = null;
	private boolean append = false;

	public GenerateExcelOfResult(String outputPath) {
		this.outputPath = outputPath;
	}

	/**
	 * 将 stock 列表中的所有股票数据写入excel中
	 * 
	 * @param stocks
	 */
	public void build(List<StockEntity> stocks) {
		File xlsFile = new File(outputPath);
		if (xlsFile.exists() && xlsFile.length() < 88) {
			System.out.println("原文件: " + outputPath + "无效，删除");
			xlsFile.delete();
		}
		this.stocks = stocks;
		try {
			append = xlsFile.exists();
			if (append) {
				writableWorkBook = Workbook.createWorkbook(xlsFile,
						Workbook.getWorkbook(xlsFile));
			} else {
				writableWorkBook = Workbook.createWorkbook(xlsFile);
			}
			create();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 创建excel的执行方法
	 * <p>
	 * 功能：
	 * <li>创建excel文件头</li>
	 * <li>对已存在的excel直接进行内容追加</li>
	 * 
	 */
	private void create() {
		if (!append) {
			generateHeader();
		}
		for (StockEntity stock : stocks) {
			System.out.println("generate excel for " + stock.getStockName()
					+ " from: " + stock.getStartDate() + " to: "
					+ stock.getEndDate());
			WritableSheet sheet = writableWorkBook.getSheet(stock
					.getStockName());
			if (sheet == null) {
				int sheetNum = writableWorkBook.getSheetNames().length;
				generateHeader(stock.getStockName(), sheetNum + 1);
				sheet = writableWorkBook.getSheet(stock.getStockName());
			}
			int row = sheet.getRows();
			int idx = 0;
			String date = stock.getStartDate();
			String sheetLastDate = row > 1 ? sheet.getCell(
					StockColumn.DATE.ordinal(), row - 1).getContents() : "";
			while (date.compareTo(stock.getEndDate()) <= 0) {
				try {
					if (sheetLastDate.compareTo(date) < 0) { // 当前日期大于文件中最后一行的日期
						sheet.addCell(new Label(StockColumn.DATE.ordinal(),
								row, date));
						long share = Long.parseLong(stock.getStockShareNum()
								.get(idx).replaceAll(",", ""));
						sheet.addCell(new Number(StockColumn.SHARE.ordinal(),
								row, share));
						if (row > 1) {
							long lastShare = Long.parseLong(sheet
									.getCell(StockColumn.SHARE.ordinal(),
											row - 1).getContents()
									.replaceAll(",", ""));
							long difference = share - lastShare;
							sheet.addCell(new Number(StockColumn.INCREACEMENT
									.ordinal(), row, difference));
							double differenceRate = 0;
							if (lastShare != 0) {
								differenceRate = difference * 100.00
										/ lastShare;
							} else {
								differenceRate = difference > 0 ? 100 : 0;
							}
							DecimalFormat df = new DecimalFormat("#.00");
							sheet.addCell(new Label(
									StockColumn.INCREACEMENTRATE.ordinal(),
									row, df.format(differenceRate) + "%"));
						}
						++row;
					}
					++idx;
				} catch (RowsExceededException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (WriteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				date = DateUtil.addOneDay(date, true);
			}
		}
		try {
			writableWorkBook.write();
			writableWorkBook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateHeader() {
		int idx = 0;
		for (StockEntity stock : stocks) {
			String stockName = stock.getStockName();
			generateHeader(stockName, idx);
			++idx;
		}
	}

	private void generateHeader(String stock, int idx) {
		String[] headers = { "日期", "持仓", "增减", "增减比例" };
		WritableSheet sheet = writableWorkBook.createSheet(stock, idx);
		int c = 0;
		for (String cont : headers) {
			try {
				sheet.addCell(new Label(c++, 0, cont));
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
