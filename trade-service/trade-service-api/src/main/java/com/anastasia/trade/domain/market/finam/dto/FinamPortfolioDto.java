package com.anastasia.trade.domain.market.finam.dto;

import com.anastasia.trade.entities.Broker;
import com.anastasia.trade.entities.Currency;
import com.anastasia.trade.entities.Market;
import com.anastasia.trade.entities.user.Portfolio;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinamPortfolioDto {

    private String clientId;
    private Content content;
    private double equity;
    private double balance;
    private PositionRow[] positions;
    private CurrencyRow[] currencies;
    private MoneyRow[] money;


    public FinamPortfolioDto(String clientId, Content content, double equity, double balance, PositionRow[] positions, CurrencyRow[] currencies, MoneyRow[] money) {
        this.clientId = clientId;
        this.content = content;
        this.equity = equity;
        this.balance = balance;
        this.positions = positions;
        this.currencies = currencies;
        this.money = money;
    }

    public FinamPortfolioDto() {}


    public record Content(boolean includeCurrencies, boolean includeMoney, boolean includePositions, boolean includeMaxBuySell) {}

    public record PositionRow(String securityCode, Market market, long balance, double currentPrice, double equity,
                              double averagePrice, String currency, double accumulatedProfit, double todayProfit,
                              double unrealizedProfit, double profit, long maxBuy, long maxSell,
                              String priceCurrency, String averagePriceCurrency, long averageRate) {}

    public record CurrencyRow(String name, double balance, double crossRate, double equity, double unrealizedProfit) {}

    public record MoneyRow(Market market, String currency, double balance) {}


    public Portfolio toPortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setClientId(clientId);
        portfolio.setBroker(Broker.Finam);
        portfolio.setBalance(BigDecimal.valueOf(equity));
        for (PositionRow row : positions) {
            portfolio.getPositions().add(toPosition(row));
        }
        return portfolio;
    }

    private Portfolio.Position toPosition(PositionRow row) {
        Currency currency = row.currency.equals("RUB") ? Currency.RUR : Currency.valueOf(row.currency);
        return new Portfolio.Position(
                row.securityCode,
                row.market,
                row.currentPrice,
                row.equity,
                row.balance,
                currency,
                row.unrealizedProfit);
    }
}
