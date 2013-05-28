package com.moczul.stock.abacus;

public class Result {
	
	private String mStockA;
	private String mStockB;
	private double mDistance;
	
	public Result(Stock stockA, Stock stockB, double distance) {
		this(stockA.getName(), stockB.getName(), distance);
	}
	
	public Result(String stockA, String stockB, double distance) {
		mStockA = stockA;
		mStockB = stockB;
		mDistance = distance;
	}
	
	public void printSummary(int i) {
		String format =  "[%d] Distance for stocks: %s and %s is equal %,.2f";
		System.out.println(String.format(format, i, mStockA, mStockB, mDistance));
	}
	
	public void printSummary() {
		String format =  "Distance for stocks: %s and %s is equal %,.2f";
		System.out.println(String.format(format, mStockA, mStockB, mDistance));
	}
	
	public double getDistance() {
		return mDistance;
	}

}
