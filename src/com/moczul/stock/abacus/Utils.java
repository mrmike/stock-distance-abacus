package com.moczul.stock.abacus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
			double closePrice = Double.valueOf(data[AppConst.CSV_CLOSE_PRICE]);
			stock.addPrice(closePrice);
		}

		// generate normalize price
		stock.calcNormalizePrices();
		return stock;
	}

	public static double calcDistance(Stock a, Stock b) {
		if (a.getSize() != b.getSize()) {
			throw new RuntimeException("Different number of observations.");
		}

		double distance = 0;
		List<Double> normalizeAPrices = a.getNormalizePrices();
		List<Double> normalizeBPrices = b.getNormalizePrices();

		for (int i = 0; i < normalizeAPrices.size(); i++) {
			double priceA = normalizeAPrices.get(i);
			double priceB = normalizeBPrices.get(i);

			distance += Math.pow(priceA - priceB, 2);
		}

		return distance;
	}

	public static void printSummary(String stockA, String stockB,
			double distance) {
		String format = "Distance for stocks: %s and %s is equal %,.2f";
		System.out.println(String.format(format, stockA, stockB, distance));
	}

	public static void printSummary(Stock stockA, Stock stockB, double distance) {
		printSummary(stockA.getName(), stockB.getName(), distance);
	}
}
