package imoveis.aluguel.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.dtos.TenantDtoResponse;
import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.repositories.TenantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    @Override
    @Transactional
    @CacheEvict(value = "tenants", allEntries = true)
    public TenantDtoResponse create(Tenant tenant) {

        if (tenant.getContacts() != null) {

            tenant.getContacts().forEach(contact -> {
                contact.setId(null);
                contact.setLandlord(null);
                contact.setTenant(tenant);
            });
            
        }

        var newTenant = tenantRepository.save(tenant);

        return tenantMapper.toDtoResponse(newTenant);

    }

    @Override
    @Cacheable("tenants")
    public TenantDtoResponse findByCpfCnpj(String cpfCnpj) {

        var tenant = tenantRepository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new NotFoundException(String.format("CPF/CNPJ %d não encontrado", cpfCnpj)));

        return tenantMapper.toDtoResponse(tenant);

    }

    @Override
    @Cacheable("tenants")
    public TenantDtoResponse findById(Long id) {

        var tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Inquilino de id %d não encontrada", id)));

        return tenantMapper.toDtoResponse(tenant);

    }

    @Override
    @Transactional
    @CacheEvict(value = "tenants", allEntries = true)
    public TenantDtoResponse update(Long id, Tenant updatedTenant) {

        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Inquilino de id %d não encontrada", id)));

        tenant.getContacts().clear();
        tenantRepository.flush();

        if (updatedTenant.getContacts() != null) {

            updatedTenant.getContacts().forEach(contact -> {

                contact.setId(null);
                contact.setLandlord(null);

                contact.setTenant(tenant);
                tenant.getContacts().add(contact);

            });

        }

        tenantMapper.updateEntity(updatedTenant, tenant);

        var newTenant = tenantRepository.save(tenant);

        return tenantMapper.toDtoResponse(newTenant);

    }

    @Override
    @Cacheable("tenants")
    public List<TenantDtoResponse> list(Sort sort) {

        return tenantRepository.findAll(sort).stream()
                    .map(tenantMapper::toDtoResponse)
                    .toList();

    }

    @Override
    @Transactional
    @CacheEvict(value = "tenants", allEntries = true)
    public void deleteById(Long id) {

        var tenant = tenantRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Inquilino de id %d não encontrada.", id)));
                
        tenantRepository.delete(tenant);

    }

}
