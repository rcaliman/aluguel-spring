package imoveis.aluguel.dtos;

import imoveis.aluguel.enums.PropertyTypeEnum;

public record PropertyDtoResponse(Long id, PropertyTypeEnum type, String address, String number, TenantDtoResponse tenant, Double value, String complement, String observation, String paymentDay) {
    
}
