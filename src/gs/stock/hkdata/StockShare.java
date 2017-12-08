package gs.stock.hkdata;

import gs.stock.utils.DateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 * 根据名称获取该股票的港资持仓数据
 * 
 * @author Gsir
 * 
 */
public class StockShare {

	private String[] stocks = null;
	private String url_sh = "http://sc.hkexnews.hk/TuniS/www.hkexnews.hk/sdw/search/mutualmarket_c.aspx?t=sh";
	private String url_sz = "http://sc.hkexnews.hk/TuniS/www.hkexnews.hk/sdw/search/mutualmarket_c.aspx?t=sz";
	private InputStream in_sh = null;
	private InputStream in_sz = null;
	private String sh_base_path = "";
	private String sz_base_path = "";
	private String day = "";

	public StockShare(String[] stockNames) {
		// TODO Auto-generated constructor stub
		stocks = stockNames;
	}

	public StockShare(List<String> stockNames) {
		// String[] allStock = new String[stockNames.size()];
		// for (int i = 0; i < allStock.length; i++) {
		// allStock[i] = stockNames.get(i);
		// }
		stocks = (String[]) stockNames.toArray(new String[stockNames.size()]);
		// stocks = allStock;
	}

	public List<StockEntity> getOneDay(String date) {
		String[] dates = DateUtil.splitDate(date);
		String path = dates[0] + File.separator + dates[1];
		day = dates[2];
		sh_base_path = "stockshare" + File.separator + "sh" + File.separator
				+ path;
		sz_base_path = "stockshare" + File.separator + "sz" + File.separator
				+ path;
		initial(date);
		List<StockEntity> result = new ArrayList<>(); // 保存该天要获取的所有股票的数据
		try {
			for (String stock : stocks) {
				in_sh = new FileInputStream(sh_base_path + File.separator + day);
				in_sz = new FileInputStream(sz_base_path + File.separator + day);
				System.out.println("get data for " + stock + " on " + date);
				StockEntity se = new StockEntity();
				se.setStockName(stock);
				se.setStartDate(date);
				se.setEndDate(date);
				String stockShareNum = findStock(in_sh, stock);
				if (stockShareNum == null) {
					stockShareNum = findStock(in_sz, stock);
				}
				if (stockShareNum == null) {
					stockShareNum = "0";
				}
				se.addStockShareNum(stockShareNum);
				result.add(se);
				in_sh.close();
				in_sz.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public List<StockEntity> getRangeDays(String startDate, String endDate) {
		String today = startDate;
		List<StockEntity> result = null;
		while (today.compareTo(endDate) <= 0) {
			List<StockEntity> temp = getOneDay(today);
			if (result == null) {
				result = temp;
			} else {
				for (int i = 0; i < result.size(); i++) {
					result.get(i).append(temp.get(i));
				}
			}
			today = DateUtil.addOneDay(today, true);
		}
		return result;
	}

	private void initial(String date) {
		OutputStream os_sh = null;
		OutputStream os_sz = null;
		try {
			File f_sh = new File(sh_base_path);
			File f_sz = new File(sz_base_path);
			if (!f_sh.exists()) {
				f_sh.mkdirs();
			}
			if (!f_sz.exists()) {
				f_sz.mkdirs();
			}
			File file_sh = new File(sh_base_path + File.separator + day);
			File file_sz = new File(sz_base_path + File.separator + day);
			if (!file_sh.exists()) {
				os_sh = new FileOutputStream(sh_base_path + File.separator
						+ day);
			}
			if (!file_sz.exists()) {
				os_sz = new FileOutputStream(sz_base_path + File.separator
						+ day);
			}
			if (os_sh == null && os_sz == null) {
				return;
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List<NameValuePair> formParams = new ArrayList<>();
		String[] ymd = DateUtil.splitDate(date);
		// formParams.add(new BasicNameValuePair("today", date));
		formParams.add(new BasicNameValuePair("ddlShareholdingDay", ymd[2]));
		formParams.add(new BasicNameValuePair("ddlShareholdingMonth", ymd[1]));
		formParams.add(new BasicNameValuePair("ddlShareholdingYear", ymd[0]));
		formParams
				.add(new BasicNameValuePair(
						"__VIEWSTATE",
						"vhBaZV0V2YA9s82MN3z1a2Xy5pNPP1Br7q2JsQosnK5MPiDPLAWUxUeUm32qsOMIHTPpRPSIC0gxU7ilhtaQVJUqM9pWYKBXxvGdgMA/2w96z9/kb8tm79vMoMN2MaFBQOvXOyZYNkxB+aEWQVE3ZxIP0efA4G1zLz6h0ffX/JJhmPyY+DYr7WMW5MUHzZvBFAMYDg=="));
		formParams.add(new BasicNameValuePair("__VIEWSTATEGENERATOR",
				"EC4ACD6F"));
		formParams
				.add(new BasicNameValuePair(
						"__EVENTVALIDATION",
						"Qz4WlW9L3y4FrEIXBuQryiyyaJuJBGjJrJRFLU1GAaCE4wTII3CDJelNClgiQYyHHK8X77PzuFt190pH0OnEokWM0/b3hfnlr/1zZXOm/sYo2j08IPLaGXFCe2SHK4tHQWJitnSbH0LRLqjFXh9fSwa0ajWf98WD5SPUnxpmHsXzYPQsVclJ2VRRmtFPxGFvX5o9Ah9+fJavrj/vhfsTuNPcMDA88/LY7d3EGYN3BpFpA7wnd2A/GtyUN7SCxrErwrvvtfcKfPxscr7HKLoVmjAoQnYiWMhEpfDqO1g1Jd1nW3xnsMXsmJL3bOhxybuLrffeCnXIDJItS8Aom2xr4h9BdCIc9/b4q7oSo9axzBwLuSGtRqEBVhAHrF5qS2ZVvHdQD8O0l6JqQCqVjHrYaBZtpJKECvv8BRo2fKGmObYKEKRaUkJsX45eCtyzzqSOs8NP83LL8xPg0MARCQp6+CIC7tpusHuKwRB068Pq4gUZ6MLBKGCEXRbP1yXBBBC1lLzuPX3qncYdfuL17CxUnYwMqzJ2CN4wV4P61OTPwCqvDOxiybS7T9nmra4C3K72PCHnDsWDf5q+mgH15gru/iJMIdc/o2J2BHGqUgN9qXHgH/lFfMAIweQu2czZHQ9X0zEz3oldIquKbXBeqOK23IfxQNw/pX7ZrcngmHYXkwOaRLZWo5nJ2Pe9MzEDyDiEoQaukGM7Pf6/+u51kEpKHDeAyQlfPXewwk+woOF39fpevkfdQ6szN2woIIfrkDabiP42o1a3b/b6Svf8pOFD3ZaaXzIwDndVcjl2NbLI3jzg6VdWn3EoZS5IaZ/EcBqctsVp/lq5JCfGv0TskgD3NSLV4NHyp9+iijtw58uXGgAUk/bLmmjM+sqFtwHSgHRYEd67v+0p0yzYBk9kUhkmkvsbTgzHbhdB2ZLe8Ms3lovUnHrGZBsyb/HqW6P6GlMPj2GppAmz4DF4Umga+dgTW8RtWV86TbRdjhhwjSIVCm0hGcmgfJsBAjcCLocRvmXuHViwkbc+ZgqS8TH+cC3bVDV+WhXj+H8ayezWTW4VBiGLTeg4ZHwCM36+XK97e6GQcXorx2as4s70P7yCLDsXsSeNsOWwgMvtpoJFjBFF6y8Vcp89NxLJ5bZIofpPzecQ3Y6r/XcIGNZ2ooSkxA36mxjAu0g="));
		// 通过在浏览器查看form提交时，有这两个属性
		formParams.add(new BasicNameValuePair("btnSearch.x", "29"));
		formParams.add(new BasicNameValuePair("btnSearch.y", "13"));
		try {
			HttpEntity entity = new UrlEncodedFormEntity(formParams);
			HttpClient httpClient = HttpClients.custom()
					.setConnectionTimeToLive(30, TimeUnit.MINUTES).build();
			if (os_sh != null) {
				HttpPost post_sh = new HttpPost(url_sh);
				post_sh.setEntity(entity);
				HttpResponse reponse_sh = httpClient.execute(post_sh);
				reponse_sh.getEntity().writeTo(os_sh);
				os_sh.flush();
				os_sh.close();
			}
			if (os_sz != null) {
				HttpPost post_sz = new HttpPost(url_sz);
				post_sz.setEntity(entity);
				HttpResponse reponse_sz = httpClient.execute(post_sz);
				reponse_sz.getEntity().writeTo(os_sz);
				os_sz.flush();
				os_sz.close();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String findStock(InputStream in, String stockName) {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		try {
			line = br.readLine();
			while (line != null) {
				if (line.contains(stockName)) {
					int idx = 2;
					while (idx > 0) {
						br.readLine();
						--idx;
					}
					String t = br.readLine().trim();
					return t;
				}
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
