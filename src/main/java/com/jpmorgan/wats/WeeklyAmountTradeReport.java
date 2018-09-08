package com.jpmorgan.wats;

import java.util.*;
import java.io.File;
import java.time.*;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;

import java.util.stream.*;

import java.io.*;


public class WeeklyAmountTradeReport {


	public WeeklyAmountTradeReport() {}


	public void run()  {

		try {

			List<Trade> trades = this.loadCsvDataIntoTrades();

			this.generateWeeklyAmountTradeReport( trades );

		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}


	private List<Trade> loadCsvDataIntoTrades() throws Exception {

		InputStream csvFile = this.getClass().getResourceAsStream("/weeklyTradeData.csv");

		CsvMapper mapper = new CsvMapper();

		CsvSchema schema = CsvSchema.emptySchema().withHeader();

		MappingIterator<Trade> mappedTrades = mapper.readerFor( Trade.class ).with( schema ).readValues( csvFile );
			
		List<Trade> trades = mappedTrades.readAll();
		return trades;
	}



	public void generateWeeklyAmountTradeReport( List<Trade> trades  ) {

			Comparator<Trade> tradeComparator = Comparator.comparing( Trade::getEntity ).thenComparing( Trade::getTradeAmount );


			System.out.println( "-------------------------------------------" );
			System.out.println("Ranking of entities based on OUTGOING amount ");
			System.out.println( "-------------------------------------------\n" );


			List<Trade> buyTrades = trades.stream()
						.filter( t -> "B".equals( t.getBuyOrSell() ) && this.validateSettlementDate( t.getSettlementDate(), t.getCurrency() ) )
						.map( t -> {
							t.setSettlementDay( t.getSettlementDate().getDayOfWeek() );
							t.setTradeAmount( t.getPricePerUnit() * t.getUnits() * t.getAgreedFx() );
							return t;
						} )
						.collect( Collectors.toList());

			Collections.sort( buyTrades, tradeComparator );

			buyTrades.forEach( System.out::println );


            Map<DayOfWeek, Double> sumDailyBuyTrades = buyTrades.stream()
             					.collect( 
             						Collectors.groupingBy( Trade::getSettlementDay, Collectors.summingDouble( Trade::getTradeAmount ) )
             					 );

			System.out.println( "---------------------------------------" );
           	System.out.println( "Amount in USD settled OUTGOING everyday " );
           	System.out.println( "---------------------------------------\n" );
            
            System.out.println( sumDailyBuyTrades + "\n");


			System.out.println( "------------------------------------------" );
			System.out.println("Ranking of entities based on INCOMING amount");
			System.out.println( "------------------------------------------\n" );


			List<Trade> sellTrades = trades.stream()
						.filter( t -> "S".equals( t.getBuyOrSell() ) && this.validateSettlementDate( t.getSettlementDate(), t.getCurrency() )  )
						.map( t -> {
							t.setSettlementDay( t.getSettlementDate().getDayOfWeek() );
							t.setTradeAmount( t.getPricePerUnit() * t.getUnits() * t.getAgreedFx() );
							return t;
						} )
						.collect( Collectors.toList() );


			Collections.sort( sellTrades, tradeComparator );

			sellTrades.forEach( System.out::println );


			// SUMMING UP THE TRADE AMOUNT PER DAY...            
             Map<DayOfWeek, Double> sumDailySellTrades = sellTrades.stream()
             					.collect( 
             						Collectors.groupingBy( Trade::getSettlementDay, Collectors.summingDouble( Trade::getTradeAmount ) )
             					 );

  			 System.out.println( "---------------------------------------" );
           	 System.out.println( "Amount in USD settled INCOMING everyday " );
           	 System.out.println( "---------------------------------------\n" );

             System.out.println( sumDailySellTrades + "\n");

	}




	private boolean validateSettlementDate( LocalDate settlementDate, String currency ) {
		
		LocalDate weekStartDate;
		LocalDate weekEndDate;	

		if ( "AED".equals( currency ) || "SAR".equals( currency ) ) {

			weekStartDate = this.getAlternativeCurrentWeekStartDate();
			weekEndDate = this.getAlternativeCurrentWeekEndDate();
		} else {

			weekStartDate = this.getCurrentWeekStartDate();
			weekEndDate = this.getCurrentWeekEndDate();			
		}


		if ( ( settlementDate.isAfter( weekStartDate ) || settlementDate.isEqual( weekStartDate ) ) 
				&& ( settlementDate.isBefore( weekEndDate ) || settlementDate.isEqual( weekEndDate ) )  ) {
			return true;
		}
		return false;
	}



	private LocalDate getCurrentWeekStartDate() {
        
        LocalDate today = LocalDate.now();

        LocalDate monday = today;
        while ( monday.getDayOfWeek() != DayOfWeek.MONDAY ) {
        	monday = monday.minusDays(1);
        }
        return monday;
	}

	private LocalDate getCurrentWeekEndDate() {

        LocalDate today = LocalDate.now();

        LocalDate friday = today;
        while ( friday.getDayOfWeek() != DayOfWeek.FRIDAY ) {
        	friday = friday.plusDays(1);
        }
        return friday;
	}

	private LocalDate getAlternativeCurrentWeekStartDate() {
        
        LocalDate today = LocalDate.now();

        LocalDate sunday = today;
        while ( sunday.getDayOfWeek() != DayOfWeek.SUNDAY ) {
        	sunday = sunday.minusDays(1);
        }
        return sunday;
	}

	private LocalDate getAlternativeCurrentWeekEndDate() {

        LocalDate today = LocalDate.now();

        LocalDate thursday = today;
        
        DayOfWeek currentDay = today.getDayOfWeek();
        if ( DayOfWeek.FRIDAY.equals( currentDay ) ) {
			return today.minusDays(1);
        }
        if ( DayOfWeek.SATURDAY.equals( currentDay ) ) {
			return today.minusDays(2);
        }

        while ( thursday.getDayOfWeek() != DayOfWeek.THURSDAY ) {
        	thursday = thursday.plusDays(1);
        }
        return thursday;
	}


}



