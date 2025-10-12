package imoveis.aluguel.repositories;

import imoveis.aluguel.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}