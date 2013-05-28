package com.moczul.stock.abacus;

import java.util.Comparator;

public class ResultComparator implements Comparator<Result> {

	@Override
	public int compare(Result o1, Result o2) {
		return Double.compare(o1.getDistance(), o2.getDistance());
	}
	
}
