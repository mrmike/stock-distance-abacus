package com.moczul.stock.abacus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Runner {

	public static void main(String[] args) throws IOException {

		// List<Stock> stocks = getYahooStocks();
		// List<Stock> stocks = calcDistanceFromFiles();
		List<Stock> stocks = getStooqStocks();
		List<Result> results = new ArrayList<Result>();

		// calculate distance
		int order = 1;
		for (int i = 0; i < stocks.size(); i++) {
			Stock stock = stocks.get(i);
			for (int k = i + 1; k < stocks.size(); k++) {
				Stock pairStock = stocks.get(k);
				double distance = Utils.calcDistance(stock, pairStock, 252, 0);
				Result result = new Result(stock, pairStock, distance);
				result.printSummary(order);
				results.add(result);
				order++;
			}
		}

		Collections.sort(results, new ResultComparator());
		System.out.println("=======================");
		System.out.println("Best results: ");
		for (Result r : results) {
			r.printSummary();
		}
		for (int i = 0; i < 30; i++) {
			results.get(i).printSummary(i + 1);
		}
	}

	private static List<Stock> getYahooStocks() throws IOException {
		List<String> symbols = Utils.getSP500Symbols();
		Calendar calendar = Calendar.getInstance();
		calendar.set(2007, 0, 1);
		Date startDate = calendar.getTime();
		calendar.set(2007, 11, 31);
		Date endDate = calendar.getTime();

		List<Stock> stocks = new ArrayList<Stock>();
		for (String symbol : symbols) {
			Stock stock = Utils.getStockFromYahoo(symbol, startDate, endDate);
			if (stock != null) {
				stocks.add(stock);
			}
		}

		return stocks;
	}

	private static List<Stock> getStooqStocks() throws IOException {
		String[] symbols = new String[] { "acp", "brs", "bre", "eur", "lts",
				"gtc", "bhw", "cez", "ker", "kgh", "peo", "ago", "pgn", "pkn",
				"pko", "sns", "bio", "tps", "alc", "apt", "ast", "bdx", "cdr",
				"cie", "eat", "ech", "emp", "gnb", "gtn", "hwe", "idm", "ing",
				"itg", "kgn", "kpx", "kty", "lpp", "mds", "mil", "net", "oil",
				"orb", "pxm", "tvn" };
		String pathFormat = "stq_stock_data/2008_2012/%s_d.csv";

		List<Stock> stocks = new ArrayList<Stock>();
		for (String symbol : symbols) {
			String path = String.format(pathFormat, symbol);
			Stock stock = Utils.getStockFromFile(symbol, path);
			if (stock != null) {
				stocks.add(stock);
			}
		}

		return stocks;
	}

	private static final void printUrls() {
		String[] wig20 = new String[] { "acp", "cps", "brs", "bre", "eur",
				"lts", "gtc", "bhw", "cez", "ker", "kgh", "peo", "ago", "pgn",
				"pkn", "pko", "sns", "bio", "tps" };
		String[] mwig40 = new String[] { "alc", "apt", "ast", "att", "bdx",
				"bio", "car", "ccc", "cci", "cdr", "cie", "crm", "eat", "ech",
				"emp", "ena", "gnb", "gpw", "gtn", "hwe", "idm", "ing", "itg",
				"kgn", "kov", "kpx", "kty", "lpp", "mds", "mil", "net", "nwr",
				"oil", "orb", "pxm", "tvn", "zep" };
		for (String wig : wig20) {
			String url = Utils.getStooqUrl(wig, 1, 1, 2008, 31, 12, 2012);
			System.out.println(url);
			System.out.println("\n");
		}
		for (String mwig : mwig40) {
			String url = Utils.getStooqUrl(mwig, 1, 1, 2008, 31, 12, 2012);
			System.out.println(url);
			System.out.println("\n");
		}
	}

	private static List<Stock> calcDistanceFromFiles() throws IOException {
		List<Stock> stocks = new ArrayList<Stock>();

		// add stocks to list
		stocks.add(Utils.getStockFromFile("Asseco", "stocks/2006_asseco.csv"));
		stocks.add(Utils.getStockFromFile("Boryszew",
				"stocks/2006_boryszew.csv"));
		stocks.add(Utils.getStockFromFile("BRE", "stocks/2006_bre.csv"));
		stocks.add(Utils.getStockFromFile("EuroCash",
				"stocks/2006_eurocash.csv"));
		stocks.add(Utils.getStockFromFile("GTC", "stocks/2006_gtc.csv"));
		stocks.add(Utils.getStockFromFile("Bank Handlowy",
				"stocks/2006_handlowy.csv"));
		stocks.add(Utils.getStockFromFile("Lotos", "stocks/2006_lotos.csv"));
		stocks.add(Utils.getStockFromFile("PEO", "stocks/2006_peo.csv"));
		stocks.add(Utils.getStockFromFile("PGNIG", "stocks/2006_pgnig.csv"));
		stocks.add(Utils.getStockFromFile("PKN Orlen",
				"stocks/2006_pkn_orlen.csv"));
		stocks.add(Utils.getStockFromFile("PKO", "stocks/2006_pko.csv"));
		stocks.add(Utils.getStockFromFile("TPSA", "stocks/2006_tpsa.csv"));

		return stocks;

	}
}
