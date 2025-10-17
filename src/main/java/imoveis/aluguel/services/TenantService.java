package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;

import imoveis.aluguel.dtos.TenantDtoResponse;
import imoveis.aluguel.entities.Tenant;

public interface TenantService {

    TenantDtoResponse create(Tenant tenant);

    TenantDtoResponse findByCpfCnpj(String cpfCnpj);

    TenantDtoResponse findById(Long id);

    TenantDtoResponse update(Long id, Tenant tenant);

    List<TenantDtoResponse> list(Sort sort);

    void deleteById(Long id);

}
