package gs.stock.excel;

import java.io.File;
import java.io.IOException;

import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * 对excel的操作类
 * 
 * @author Gsir
 * 
 */
public class ExcelOperation {

	private Workbook workbook = null;

	private ExcelOperation(String excelFile) {
		try {
			workbook = Workbook.getWorkbook(new File(excelFile));
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 返回输入路径的一个ExcelOperation对象，若该文件不存在，返回 null
	 * 
	 * @param inputPath
	 * @return
	 */
	public static ExcelOperation getExcelOperationInstance(String inputPath) {
		File file = new File(inputPath);
		if (file.exists()) {
			return new ExcelOperation(inputPath);
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @return 该excel所有sheet的名称
	 */
	public String[] getAllSheetNames() {
		return workbook.getSheetNames();
	}
}
