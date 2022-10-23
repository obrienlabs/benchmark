package org.obrienscience.collatz.model;

import java.math.BigInteger;

public class CollatzMilestone {
	private BigInteger start;
	private BigInteger path;
	private BigInteger height;
	
	public BigInteger getStart() {
		return start;
	}
	public void setStart(BigInteger start) {
		this.start = start;
	}
	public BigInteger getPath() {
		return path;
	}
	public void setPath(BigInteger path) {
		this.path = path;
	}
	public BigInteger getHeight() {
		return height;
	}
	public void setHeight(BigInteger height) {
		this.height = height;
	}	

}
