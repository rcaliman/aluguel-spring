package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import imoveis.aluguel.entities.Tenant;

public interface TenantService {

    Tenant create(Tenant tenant);

    Tenant findByCpfCnpj(String cpfCnpj);

    Tenant findById(Long id);

    Tenant update(Long id, Tenant tenant);

    List<Tenant> list(Sort sort);

    void deleteById(Long id);

}
