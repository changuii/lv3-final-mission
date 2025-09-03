package finalmission.reservation.infrastructure;

import finalmission.reservation.domain.DateGenerator;
import java.time.LocalDate;
import org.springframework.stereotype.Component;

@Component
public class BasicDateGenerator implements DateGenerator {

    @Override
    public LocalDate today() {
        return LocalDate.now();
    }
}
