package com.sms.entities;
 //Author:Reshma K


/*
 * This class is an inherited class from payment and the payment through an offline payment
 * OOPS:Encapsulation,Polymorphism,Inheritance
 * Offline / Cash Payment — collects receipt reference and cash amount tendered.
 */

public class OfflinePayment extends Payment {
	
	private String receiptReference;
	private double cashTendered;
	
	//All arguments constructor
	public OfflinePayment(double amountPaid, String receiptReference, double cashTendered) {
		super(amountPaid, "OFFLINE");
		this.receiptReference = receiptReference.trim();
		this.cashTendered = cashTendered;
	}
	
	public String getReceiptReference() { return receiptReference; }
	public double getCashTendered() { return cashTendered; }
	
	//Runtime polymorphism
	@Override
	public String validate() {
		if (receiptReference == null || receiptReference.length() < 3) {
			return "Receipt reference must be at least 3 characters.";
		}
		if (cashTendered < getAmountPaid()) {
			return String.format("Cash tendered (%.2f) is less than the amount due (%.2f).", cashTendered, getAmountPaid());
		}
		return null;
	}
	
	//Runtime polymorphism
	@Override
	public boolean process() {
		double change = cashTendered - getAmountPaid();
		System.out.printf("  [OFFLINE] Cash accepted. Change to return: %.2f%n", change);
		System.out.println("  [OFFLINE] Receipt reference: " + receiptReference);
		setStatus("SUCCESS");
		setRemarks("Offline cash payment — ref: " + receiptReference);
		return true;
	}
}





