package imoveis.aluguel.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import imoveis.aluguel.entities.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>{
    
}
