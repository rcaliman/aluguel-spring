package imoveis.aluguel.services;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.exceptions.NotFoundException;
import imoveis.aluguel.mappers.LandlordMapper;
import imoveis.aluguel.repositories.LandlordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LandlordService {

    private final LandlordRepository landlordRepository;
    private final LandlordMapper landlordMapper;

    @Transactional
    @CacheEvict(value = "landlords", allEntries = true)
    public Landlord create(Landlord landlord) {

        if (landlord.getMain()) {
            landlordRepository.setAllMainToFalse();
        }

        landlord.getContacts().forEach(contact -> {
            contact.setId(null);
            contact.setTenant(null);
            contact.setLandlord(landlord);
        });

        return landlordRepository.save(landlord);

    }

    @Cacheable(value = "landlords", key = "#cpfCnpj")
    public Landlord findByCpfCnpj(String cpfCnpj) {

        return landlordRepository.findByCpfCnpj(cpfCnpj).orElseThrow(
                () -> new NotFoundException(String.format("Locador de cpf %s não encontrado", cpfCnpj)));

    }

    @Cacheable(value = "landlords", key = "#id")
    public Landlord findById(Long id) {

        return landlordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Locador de id %d não encontrado", id)));

    }

    @Transactional
    @CacheEvict(value = "landlords", allEntries = true)
    public Landlord update(Long id, Landlord updatedLandlord) {

        var landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Locador de id %d não encontrado", id)));

        if (updatedLandlord.getMain()) {
            landlordRepository.setAllMainToFalse();
        }

        landlord.getContacts().clear();
        landlordRepository.flush();

        if (updatedLandlord.getContacts() != null) {

            updatedLandlord.getContacts().forEach(contact -> {
                contact.setId(null);
                contact.setTenant(null);

                contact.setLandlord(landlord);
                landlord.getContacts().add(contact);
                
            });
        }

        landlordMapper.updateEntity(updatedLandlord, landlord);

        return landlordRepository.saveAndFlush(landlord);

    }

    @Cacheable(value = "landlords", key = "'list-' + #sort.toString()")
    public List<Landlord> list(Sort sort) {

        return landlordRepository.findAll(sort);

    }

    @Transactional
    @CacheEvict(value = "landlords", allEntries = true)
    public void deleteById(Long id) {

        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Locador de id %d não encontrado", id)));

        landlordRepository.delete(landlord);

    }

}
