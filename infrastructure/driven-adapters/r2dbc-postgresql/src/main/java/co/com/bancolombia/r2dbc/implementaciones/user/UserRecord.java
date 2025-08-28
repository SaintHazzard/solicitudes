package co.com.bancolombia.r2dbc.implementaciones.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRecord(
    String id,String names,
    String lastname,LocalDate birthDate,
    String address,
    String phone,
    String email,
    BigDecimal salaryBase) {

}
