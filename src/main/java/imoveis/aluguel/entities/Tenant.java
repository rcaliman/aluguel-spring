package imoveis.aluguel.entities;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import imoveis.aluguel.enums.MaritalStatusEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "tb_tenants")
@ToString
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "tenant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contact> contacts = new ArrayList<>();

    @Column(nullable = true)
    private String document;

    @Column(nullable = true, unique = true, name = "cpf_cnpj")
    private String cpfCnpj;

    @Column(nullable = true, name = "date_of_birth")
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate dateOfBirth;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String city;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "marital_status")
    private MaritalStatusEnum maritalStatus;

    @Column(nullable = true, name = "nationality")
    private String nationality;

    @Column(nullable = true)
    @OneToMany(mappedBy = "tenant", cascade = CascadeType.DETACH, orphanRemoval = false)
    private List<Property> properties = new ArrayList<>();

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = true, name = "updated_at")
    private Instant updatedAt;

    @PreRemove
    private void preRemove() {
        for (Property property : properties) {
            property.setTenant(null);
        }
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

}
