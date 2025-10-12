package imoveis.aluguel.services;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import imoveis.aluguel.entities.Landlord;
import imoveis.aluguel.mappers.LandlordMapper;
import imoveis.aluguel.repositories.LandLordRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LandlordServiceImpl implements LandlordService {

    private final LandLordRepository landlordRepository;
    private final LandlordMapper landlordMapper;

    @Override
    @Transactional
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

    @Override
    public Landlord findByCpfCnpj(String cpfCnpj) {

        var landlord = landlordRepository.findByCpfCnpj(cpfCnpj).orElseThrow(
                () -> new EntityNotFoundException(String.format("Locador de cpf %s não encontrado", cpfCnpj)));

        return landlord;

    }

    @Override
    public Landlord findById(Long id) {

        var landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Locador de id %d não encontrado", id)));

        return landlord;

    }

    @Override
    @Transactional
    public Landlord update(Long id, Landlord updatedLandlord) {

        var landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Locador de id %d não encontrado", id)));

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

    @Override
    public List<Landlord> list(Sort sort) {

        return landlordRepository.findAll(sort);

    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        Landlord landlord = landlordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Locador de id %d não encontrado", id)));

        landlordRepository.delete(landlord);

    }

}
