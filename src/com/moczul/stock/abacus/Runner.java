package com.moczul.stock.abacus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Runner {

	public static void main(String[] args) throws IOException {
		List<Stock> stocks = new ArrayList<>();

		// add stocks to list
		stocks.add(Utils.getStockFromFile("Asseco", "stocks/2006_asseco.csv"));
		stocks.add(Utils.getStockFromFile("Boryszew", "stocks/2006_boryszew.csv"));
		stocks.add(Utils.getStockFromFile("BRE", "stocks/2006_bre.csv"));
		stocks.add(Utils.getStockFromFile("EuroCash", "stocks/2006_eurocash.csv"));
		stocks.add(Utils.getStockFromFile("GTC", "stocks/2006_gtc.csv"));
		stocks.add(Utils.getStockFromFile("Bank Handlowy", "stocks/2006_handlowy.csv"));
		stocks.add(Utils.getStockFromFile("Lotos", "stocks/2006_lotos.csv"));
		stocks.add(Utils.getStockFromFile("PEO", "stocks/2006_peo.csv"));
		stocks.add(Utils.getStockFromFile("PGNIG", "stocks/2006_pgnig.csv"));
		stocks.add(Utils.getStockFromFile("PKN Orlen", "stocks/2006_pkn_orlen.csv"));
		stocks.add(Utils.getStockFromFile("PKO", "stocks/2006_pko.csv"));
		stocks.add(Utils.getStockFromFile("TPSA", "stocks/2006_tpsa.csv"));
		
		String minStockA = null;
		String minStockB = null;
		double minDistance = 0;
		
		// calculate distance
		for (int i = 0; i < stocks.size(); i++) {
			Stock stock = stocks.get(i);
			for (int k = i + 1; k < stocks.size(); k++) {
				Stock pairStock = stocks.get(k);
				double distance = Utils.calcDistance(stock, pairStock);
				if (k == 1 && i == 0) {
					minDistance = distance;
				}
				if (distance < minDistance) {
					minDistance = distance;
					minStockA = stock.getName();
					minStockB = pairStock.getName();
				}
				Utils.printSummary(stock, pairStock, distance);
			}
		}
		
		// print min distance
		System.out.println("================================");
		Utils.printSummary(minStockA, minStockB, minDistance);
		System.out.println("================================");
	}
	
}
