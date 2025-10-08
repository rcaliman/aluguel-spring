package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.Tenant;
import imoveis.aluguel.mappers.TenantMapper;
import imoveis.aluguel.repositories.TenantRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    @Override
    @Transactional
    public Tenant create(Tenant tenant) {


        if(tenant.getContacts() != null) {
            tenant.getContacts().forEach(
                contact -> {
                    contact.setId(null);
                    contact.setLandlord(null);
                    contact.setTenant(tenant);
                }
            );
        }
        
        return tenantRepository.save(tenant);

    }

    @Override
    public Tenant findByCpfCnpj(String cpfCnpj) {

        var tenant = tenantRepository.findByCpfCnpj(cpfCnpj).orElseThrow(
            () -> new EntityNotFoundException(String.format("CPF/CNPJ %d não encontrado", cpfCnpj))
        );

        return tenant;

    }

    @Override
    public Tenant findById(Long id) {

        var tenant = tenantRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Inquilino de id %d não encontrada", id))
        );

        return tenant;
        
    }
    
    @Override
    @Transactional
    public Tenant update(Long id, Tenant updatedTenant) {

        Tenant tenant = tenantRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Inquilino de id %d não encontrada", id))
        );

        tenant.getContacts().clear();
        tenantRepository.flush();

        if(updatedTenant.getContacts() != null) {
            updatedTenant.getContacts().forEach(
                contact -> {
                    contact.setId(null);
                    contact.setLandlord(null);
                    
                    contact.setTenant(tenant);
                    tenant.getContacts().add(contact);
                }
            );
        }

        tenantMapper.updateEntity(updatedTenant, tenant);


        return tenantRepository.save(tenant);

    }

    @Override
    public List<Tenant> list(Sort sort) {
        return tenantRepository.findAll(sort);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        var tenant = tenantRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Inquilino de id %d não encontrada.", id))
        );
        tenantRepository.delete(tenant);
    }

}
