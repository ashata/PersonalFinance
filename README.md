# PersonalFinance
some ideas to start, using financial APIs like Barchart-OnDemand

Spring boot sample app that uses with Barchart on demand client to fetch stock market data

Auto scheduled tasks to fetch stock history and quote data for profile symbols. The scheduled tasks runs at different intervals and EoD

Integrated Spring data elastic search to save the market data fetched

To use free version of data from Barchart-OnDemand, please update your version of BarchartOnDemandClient class with the one
in resources/hacks folder. URL and apikey property are updated everything else is same as the maven jar 
        <dependency>
            <groupId>com.barchart.base</groupId>
            <artifactId>barchart-ondemand-client</artifactId>
            <version>2.0.3-Local</version>
        </dependency>
