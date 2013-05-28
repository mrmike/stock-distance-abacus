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
			double closePrice = Double.valueOf(data[AppConst.YAHOO_ADJ_CLOSE]);
			stock.addPrice(closePrice);
		}

		// generate normalize price
		stock.calcNormalizePrices();
		return stock;
	}

	public static double calcDistance(Stock a, Stock b) {
		int size = a.getSize();
		if (size > b.getSize()) {
			size = b.getSize();
		}

		double distance = 0;
		List<Double> normalizeAPrices = a.getNormalizePrices();
		List<Double> normalizeBPrices = b.getNormalizePrices();

		for (int i = 0; i < size; i++) {
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
		String fileName = getFileName(stockName, startDay, startMonth, startYear, endDay, endMonth, endYear);
		File f = new File(fileName);
		if (f.exists()) {
			System.out.println("File " + fileName + " already exists");
			return getStockFromFile(stockName, f.getPath());
		}
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
			return null;
		} catch (IOException e) {
			// ignore
			System.out.println("For this period stock data for " + stockName + " could not be find");
			return null;
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

		System.out.println("Downloaded data for " + stockName);

		return getStockFromFile(stockName, f.getPath());
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
}
