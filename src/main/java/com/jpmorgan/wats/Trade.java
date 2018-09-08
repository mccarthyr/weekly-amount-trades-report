package com.jpmorgan.wats;

import java.time.*;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonPropertyOrder( { "entity", "buyOrSell", "agreedFx", "currency", "instructionDate", "settlementDate", "units", "pricePerUnit" } )
public class Trade {

	private String entity;

	private String buyOrSell;

	private double agreedFx;

	private String currency;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate instructionDate;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	private LocalDate settlementDate;

	private int units;

	private double pricePerUnit;

	private double tradeAmount;

	private DayOfWeek settlementDay = null;	// This is set during processing


	public Trade() {}

	public Trade( String entity, String buyOrSell, double agreedFx, String currency, 
									LocalDate instructionDate, LocalDate settlementDate, int units, 
									double pricePerUnit  ) {

		this.entity = entity;
		this.buyOrSell= buyOrSell;
		this.agreedFx = agreedFx;
		this.currency = currency;
		this.instructionDate = instructionDate;
		this.settlementDate = settlementDate;
		this.units = units;
		this.pricePerUnit = pricePerUnit;

		System.out.println("IN HERE" + this.pricePerUnit);
		this.tradeAmount = pricePerUnit * units * agreedFx;

	}


	@Override
	public String toString() {
		this.tradeAmount = pricePerUnit * units * agreedFx;
		return this.entity + " - " + this. buyOrSell + " - " + this.currency + " - " + this.tradeAmount + " = " + this.getSettlementDay();
	}


	// Getters & Setters

	public String getEntity() {
		return this.entity;
	}

	public String getBuyOrSell() {
		return this.buyOrSell;
	}

	public double getAgreedFx() {
		return this.agreedFx;
	}

	public String getCurrency() {
		return this.currency;
	}

	public LocalDate getInstructionDate() {
		return this.instructionDate;
	}

	public LocalDate getSettlementDate() {
		return this.settlementDate;
	}

	public int getUnits() {
		return this.units;
	}

	public double getPricePerUnit() {
		return this.pricePerUnit;
	}

	public double getTradeAmount() {
		return this.tradeAmount;
	}

	public DayOfWeek getSettlementDay() {
		return this.settlementDay;
	}



	public void setEntity( String entity ) {
		this.entity = entity;
	}

	public void setBuyOrSell( String buyOrSell ) {
		this.buyOrSell = buyOrSell;
	}

	public void setAgreedFx( double agreedFx ) {
		this.agreedFx = agreedFx;
	}

	public void setCurrency( String currency ) {
		this.currency = currency;
	}

	public void setInstructionDate( LocalDate instructionDate ) {
		this.instructionDate = instructionDate;
	}

	public void setSettlementDate( LocalDate settlementDate ) {
		this.settlementDate = settlementDate;
	}

	public void setUnits( int units ) {
		this.units = units;
	}

	public void setPricePerUnit( double pricePerUnit ) {
		this.pricePerUnit = pricePerUnit;
	}

	public void setTradeAmount( double tradeAmount ) {
		this.tradeAmount = tradeAmount;
	}

	public void setSettlementDay( DayOfWeek settlementDay ) {
		this.settlementDay = settlementDay;
	}

}

