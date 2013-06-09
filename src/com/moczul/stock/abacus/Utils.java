package com.moczul.stock.abacus;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

	public static Stock getStockFromFile(String name, String path)
			throws IOException {
		Stock stock = new Stock(name);
		File file = new File(path);
		if (!file.exists()) {
			throw new FileNotFoundException("Could not find " + path + " file.");
		}

		BufferedReader reader = new BufferedReader(new FileReader(file));
		// we should readline first to omit headings
		String line = reader.readLine();
		while ((line = reader.readLine()) != null) {
			String[] data = line.split(",");
			double closePrice = Double
					.valueOf(data[AppConst.STOOQ_CLOSE_PRICE]);
			stock.addPrice(closePrice);
		}

		// Yahoo has data from latest ones to the oldest one, we should reverse
		// it
		// i believe we should add Date field and sort by date TODO
		stock.reversePrices();

		// generate normalize price
		stock.getNormalizePrices();
		return stock;
	}

	public static double calcDistance(Stock a, Stock b, int period, int offset) {
		int size = a.getSize();
		if (period > size) {
			throw new IllegalArgumentException("Given period is longer than data-size");
		}
		if (Math.abs(size - b.getSize()) > 10) {
			throw new IllegalArgumentException("Both data set should be the same");
		}
		
		if (size > b.getSize()) {
			size = b.getSize();
		}

		double distance = 0;
		List<Double> normalizeAPrices = a.getNormalizePrices(period, offset);
		List<Double> normalizeBPrices = b.getNormalizePrices(period, offset);

		for (int i = 0; i < period; i++) {
			double priceA = normalizeAPrices.get(i);
			double priceB = normalizeBPrices.get(i);

			distance += Math.pow(priceA - priceB, 2);
		}

		return distance;
	}

	public static List<String> getSP500Symbols() {
		List<String> symbols = new ArrayList<String>();
		File f = new File("sp500/sp500.csv");
		if (!f.exists()) {
			throw new RuntimeException("File sp500.csv does not exist!");
		}

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(f));
			String line;
			while ((line = reader.readLine()) != null) {
				line = line.replace("\"", "");
				symbols.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return symbols;
	}

	public static Stock getStockFromStooq(String stockName, Date startDate,
			Date endDate) throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		int startDay = calendar.get(Calendar.DAY_OF_MONTH);
		int startMonth = calendar.get(Calendar.MONTH) + 1;
		int startYear = calendar.get(Calendar.YEAR);
		calendar.setTime(endDate);
		int endDay = calendar.get(Calendar.DAY_OF_MONTH);
		int endMonth = calendar.get(Calendar.MONTH) + 1;
		int endYear = calendar.get(Calendar.YEAR);

		File directorty = new File("stq_stock_data");
		if (!directorty.exists()) {
			directorty.mkdir();
		}

		String url = getStooqUrl(stockName, startDay, startMonth, startYear,
				endDay, endMonth, endYear);
		String fileName = getFileName(stockName, startDay, startMonth,
				startYear, endDay, endMonth, endYear);
		File f = new File(fileName);
		if (f.exists()) {
//			System.out.println("File " + fileName + " already exists");
			return getStockFromFile(stockName, f.getPath());
		}

		downloadFile(f, stockName, url);

		return getStockFromFile(stockName, f.getPath());
	}

	// perfect case for multi-threading TODO
	public static Stock getStockFromYahoo(String stockName, Date startDate,
			Date endDate) throws IOException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		int startDay = calendar.get(Calendar.DAY_OF_MONTH);
		int startMonth = calendar.get(Calendar.MONTH);
		int startYear = calendar.get(Calendar.YEAR);
		calendar.setTime(endDate);
		int endDay = calendar.get(Calendar.DAY_OF_MONTH);
		int endMonth = calendar.get(Calendar.MONTH);
		int endYear = calendar.get(Calendar.YEAR);

		File directorty = new File("stock_data");
		if (!directorty.exists()) {
			directorty.mkdir();
		}

		String url = getYahooStockUrl(stockName, startDay, startMonth,
				startYear, endDay, endMonth, endYear);
		String fileName = getFileName(stockName, startDay, startMonth,
				startYear, endDay, endMonth, endYear);
		File f = new File(fileName);
		if (f.exists()) {
			System.out.println("File " + fileName + " already exists");
			return getStockFromFile(stockName, f.getPath());
		}

		downloadFile(f, stockName, url);

		return getStockFromFile(stockName, f.getPath());
	}

	private static void downloadFile(File f, String stockName, String url) {
		BufferedInputStream inputStream = null;
		FileOutputStream outputStream = null;

		try {
			inputStream = new BufferedInputStream(new URL(url).openStream());
			outputStream = new FileOutputStream(f);
			byte data[] = new byte[1024];
			int count;
			while ((count = inputStream.read(data)) != -1) {
				outputStream.write(data, 0, count);
			}
		} catch (MalformedURLException e) {
			// simply ignore
			return;
		} catch (IOException e) {
			// ignore
			System.out.println("For this period stock data for " + stockName
					+ " could not be find");
			return;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

//		System.out.println("Downloaded data for " + stockName);
	}

	private static String getFileName(String stockName, int startDay,
			int startMonth, int startYear, int endDay, int endMonth, int endYear) {
		String format = "stock_data/%s_%d_%d_%d_to_%d_%d_%d.csv";
		return String.format(format, stockName, startDay, startMonth,
				startYear, endDay, endMonth, endYear);
	}

	private static String getYahooStockUrl(String stockName, int startDay,
			int startMonth, int startYear, int endDay, int endMonth, int endYear) {
		return String.format(AppConst.YAHOO_FORMAT, stockName, startMonth,
				startDay, startYear, endMonth, endDay, endYear);
	}

	public static String getStooqUrl(String stockName, int startDay,
			int startMonth, int startYear, int endDay, int endMonth, int endYear) {
		String sDay = String.format("%02d", startDay);
		String sMonth = String.format("%02d", startMonth);
		String eDay = String.format("%02d", endDay);
		String eMonth = String.format("%02d", endMonth);

		String sDate = new StringBuilder().append(startYear).append(sMonth)
				.append(sDay).toString();
		String eDate = new StringBuilder().append(endYear).append(eMonth)
				.append(eDay).toString();

		return String.format(AppConst.STOOQ_FORMAT, stockName, sDate, eDate);
	}
}
