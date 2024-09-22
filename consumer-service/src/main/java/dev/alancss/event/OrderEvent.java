package dev.alancss.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {

    @NotBlank(message = "Invalid orderId. Must not be blank")
    private String orderId;

    @NotBlank(message = "Invalid customerId. Must not be blank")
    private String customerId;

    @PositiveOrZero(message = "Amount must be non-negative")
    private double amount;

    @NotBlank(message = "Invalid status. Must not be blank")
    @Pattern(regexp = "PAID|PENDING|FAILED", message = "Invalid status. Must be one of: PAID, PENDING, FAILED")
    private String status;

    @NotBlank(message = "Invalid date. Must not be blank")
    private String date;

}
