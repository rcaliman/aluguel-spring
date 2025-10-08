package imoveis.aluguel.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import imoveis.aluguel.dtos.TenantDtoRequest;
import imoveis.aluguel.dtos.TenantDtoResponse;
import imoveis.aluguel.entities.Tenant;

@Mapper(componentModel = "spring", uses = {ContactMapper.class})
public interface TenantMapper {
    
    TenantDtoResponse toDtoResponse(Tenant tenant);
    
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "properties", ignore = true)
    Tenant toTenant(TenantDtoRequest tenantDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    void updateEntity(Tenant source, @MappingTarget Tenant target);

}
