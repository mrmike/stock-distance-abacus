package com.moczul.stock.abacus;

public class AppConst {
	
	public static final String YAHOO_FORMAT = "http://ichart.finance.yahoo.com/table.csv?s=%1$s&a=%2$d&b=%3$d&c=%4$d&g=d&d=%5$d&e=%6$d&f=%7$d&ignore=.csv";
	
	// Constants for csv files from finance.yahoo.com
	public static final int YAHOO_DATE = 0;
	public static final int YAHOO_OPEN = 1;
	public static final int YAHOO_HIGH = 2;
	public static final int YAHOO_LOW = 3;
	public static final int YAHOO_CLOSE = 4;
	public static final int YAHOO_VOLUME = 5;
	public static final int YAHOO_ADJ_CLOSE = 6;

	// Constants for csv files from stooq.pl
	public static final int STOOQ_DATE = 0;
	public static final int STOOQ_OPEN_PRICE = 1;
	public static final int STOOQ_HIGHEST_PRICE = 2;
	public static final int STOOQ_LOWEST_PRICE = 3;
	public static final int STOOQ_CLOSE_PRICE = 4;
	public static final int STOOQ_VOLUME = 5;
}
