package xo.market.charting;

import fr.alexpado.xodb4j.XoDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class XoMarketChartingApplication {

    public static void main(String[] args) {

        SpringApplication.run(XoMarketChartingApplication.class, args);
    }

    @Bean
    public XoDB xoDB() {

        return new XoDB();
    }

}
