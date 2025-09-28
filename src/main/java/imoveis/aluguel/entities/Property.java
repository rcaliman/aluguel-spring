package imoveis.aluguel.entities;

import java.time.Instant;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import imoveis.aluguel.enums.PropertyTypeEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Getter
@Setter
@Table(name = "tb_properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "property_type", nullable = false)
    private PropertyTypeEnum type;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String number;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable =  true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private Tenant tenant;

    @Column(nullable = true)
    private Double value;

    @Column(nullable = true)
    private String complement;

    @Column(nullable = true)
    private String observation;

    @Column(nullable = true)
    private String paymentDay;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(nullable = true, name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
