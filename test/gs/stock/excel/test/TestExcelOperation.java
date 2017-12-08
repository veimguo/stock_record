package gs.stock.excel.test;

import gs.stock.excel.ExcelOperation;

public class TestExcelOperation {
	public static void main(String[] args) {
		ExcelOperation op = ExcelOperation
				.getExcelOperationInstance("record.xls");
		for (String t : op.getAllSheetNames()) {
			System.out.println(t);
		}
	}
}
