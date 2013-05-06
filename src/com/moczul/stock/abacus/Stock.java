package com.moczul.stock.abacus;

import java.util.ArrayList;
import java.util.List;

public class Stock {

	private String mName;
	private List<Double> mPrices;
	private List<Double> mNormalizePrices;

	public Stock(String name) {
		mName = name;
		mPrices = new ArrayList<Double>();
	}

	public String getName() {
		return mName;
	}

	public void addPrice(double price) {
		mPrices.add(price);
	}

	public int getSize() {
		return mPrices.size();
	}

	public double getAverage() {
		double sum = 0;
		for (Double price : mPrices) {
			sum += price;
		}

		return sum / mPrices.size();
	}

	public double getStdDev() {
		double sum = 0;
		double average = getAverage();
		for (Double price : mPrices) {
			sum += Math.pow(price - average, 2);
		}

		return Math.pow(sum / mPrices.size(), 0.5);
	}

	public void calcNormalizePrices() {
		mNormalizePrices = new ArrayList<Double>();
		double avg = getAverage();
		double stdDev = getStdDev();

		for (Double price : mPrices) {
			double normalizePrice = (price - avg) / stdDev;
			mNormalizePrices.add(normalizePrice);
		}
	}

	public List<Double> getNormalizePrices() {
		if (mNormalizePrices == null) {
			calcNormalizePrices();
		}

		return mNormalizePrices;
	}

}
