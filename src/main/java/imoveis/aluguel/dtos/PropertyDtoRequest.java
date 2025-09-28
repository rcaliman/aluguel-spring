package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.PropertyTypeEnum;

public record PropertyDtoRequest(Long id, PropertyTypeEnum type, String address, String number, Long tenantId, Double value, String complement, String observation, String paymentDay) {
    
}
