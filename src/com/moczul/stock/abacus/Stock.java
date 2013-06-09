package com.moczul.stock.abacus;

import java.util.ArrayList;
import java.util.Collections;
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

	private void calcNormalizePrices(int period, int offset) {
		if (period + offset > mPrices.size()) {
			throw new IllegalArgumentException(
					"Given period and offset is longer than data-size");
		}
		mNormalizePrices = new ArrayList<Double>();
		double avg = getAverage();
		double stdDev = getStdDev();

		for (int i = offset; i < period + offset; i++) {
			double price = mPrices.get(i);
			double normalizePrice = (price - avg) / stdDev;
			mNormalizePrices.add(normalizePrice);
		}
	}

	public void reversePrices() {
		Collections.reverse(mPrices);
	}

	public List<Double> getNormalizePrices() {
		return getNormalizePrices(mPrices.size(), 0);
	}

	public List<Double> getNormalizePrices(int period, int offset) {
		// TODO add offset feature
		if (mNormalizePrices == null || mNormalizePrices.size() != period) {
			calcNormalizePrices(period, offset);
		}

		return mNormalizePrices;
	}

}
