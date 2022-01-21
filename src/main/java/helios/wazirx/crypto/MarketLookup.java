package helios.wazirx.crypto;

public class MarketLookup {
    private MarketLookup() {
    }

    public enum Market {
        USDTINR("usdtinr"), BTCINR("btcinr"), GRTUSDT("grtusdt");

        private final String market;

        private Market(String market) {
            this.market = market;
        }

        public String getMarket() {
            return market;
        }
    }

    public static Market getUSDTINR() {
        return Market.USDTINR;
    }

    public static Market getBTCINR() {
        return Market.BTCINR;
    }

    public static Market getGRTUSDT() {
        return Market.GRTUSDT;
    }
}