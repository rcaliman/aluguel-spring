package imoveis.aluguel.entities;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import imoveis.aluguel.enums.PropertyTypeEnum;
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
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_properties_logs")
@Getter
@Setter
@NoArgsConstructor
public class PropertyLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, name = "property_type")
    private PropertyTypeEnum propertyType;

    @Column(nullable = true)
    private String number;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true, name = "rental_value")
    private Double value;

    @Column(nullable = true)
    private String complement;

    @Column(nullable = true)
    private String observation;

    @Column(nullable = true)
    private String paymentDay;

    @Column(nullable = true)
    private String tenantName;

    @ManyToOne
    @JoinColumn(name = "property_id")
    @JsonIgnore
    private Property property;

    @Column(nullable = true)
    private String tenantCpfCnpj;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }

}
