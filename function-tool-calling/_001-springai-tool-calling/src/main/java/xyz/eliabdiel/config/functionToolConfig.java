package xyz.eliabdiel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import xyz.eliabdiel.service.MockBookingStatusService;

import java.util.function.Function;

@Configuration
public class functionToolConfig {

    @Bean
    @Description("Get the status of a hotel booking")
    public Function<MockBookingStatusService.BookingRequest,MockBookingStatusService.BookingResponse> bookingStatus() {
        return new MockBookingStatusService();
    }
}
