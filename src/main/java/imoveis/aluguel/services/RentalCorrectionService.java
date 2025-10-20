package imoveis.aluguel.services;

import imoveis.aluguel.dtos.RentalCorrectionDTO;

public interface RentalCorrectionService {

    RentalCorrectionDTO calculateCorrectedRentalValue(Long propertyId);

}
