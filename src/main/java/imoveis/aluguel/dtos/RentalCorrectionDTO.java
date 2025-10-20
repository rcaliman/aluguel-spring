package imoveis.aluguel.dtos;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalCorrectionDTO {

    private Double originalValue;
    private Instant originalDate;
    private Double correctedValue;
    private Instant correctionDate;
    private Double accumulatedIpcaPercentage;
    private String detailedCalculation;

}
