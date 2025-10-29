package imoveis.aluguel.entities;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonBackReference;

import imoveis.aluguel.enums.ContactTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_contacts")
@Getter
@Setter
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_type", nullable = false)
    private ContactTypeEnum type;

    @Column(nullable = false)
    private String contact;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    @JsonBackReference("tenant-contacts")
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(name = "landlord_id")
    @JsonBackReference("landlord-contacts")
    private Landlord landlord;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    @Override
    public String toString() {
        return this.contact;
    }
    
}
