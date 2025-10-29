package imoveis.aluguel.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.repositories.TenantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    @Transactional
    @CacheEvict(value = "tenants", allEntries = true)
    public Tenant create(Tenant tenant) {

        if (tenant.getContacts() != null) {

            tenant.getContacts().forEach(contact -> {
                contact.setId(null);
                contact.setLandlord(null);
                contact.setTenant(tenant);
            });

        }

        return tenantRepository.save(tenant);

    }

    @Cacheable(value = "tenants", key = "'cpfCnpj-' + #cpfCnpj")
    public Tenant findByCpfCnpj(String cpfCnpj) {

        return tenantRepository.findByCpfCnpj(cpfCnpj)
                .orElseThrow(() -> new NotFoundException(String.format("CPF/CNPJ %s não encontrado", cpfCnpj)));

    }

    @Cacheable(value = "tenants", key = "#id")
    public Tenant findById(Long id) {

        return tenantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Inquilino de id %d não encontrada", id)));

    }

    @Transactional
    @CacheEvict(value = { "tenants", "properties" }, allEntries = true)
    public Tenant update(Long id, Tenant updatedTenant) {

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

        return tenantRepository.save(tenant);

    }

    @Cacheable(value = "tenants", key = "'list-' + #sort.toString()")
    @Transactional
    public List<Tenant> list(Sort sort) {

        List<Tenant> tenants = tenantRepository.findAllWithProperties();

        tenants.forEach(tenant -> tenant.getContacts().size());

        return tenants;

    }

    @Transactional
    @CacheEvict(value = { "tenants", "properties" }, allEntries = true)
    public void deleteById(Long id) {

        var tenant = tenantRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Inquilino de id %d não encontrada.", id)));

        tenantRepository.delete(tenant);

    }

}
