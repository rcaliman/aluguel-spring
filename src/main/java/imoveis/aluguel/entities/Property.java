package imoveis.aluguel.entities;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import imoveis.aluguel.enums.PropertyTypeEnum;
import imoveis.aluguel.enums.PropertyUseTypeEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyTypeEnum propertyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "use_type", nullable = true)
    private PropertyUseTypeEnum useType;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true)
    private String state;

    @Column(nullable = true)
    private String city;

    @Column(nullable = true)
    private String number;

    @Column(nullable = true)
    private String complement;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonIgnore
    private Tenant tenant;

    @Column(nullable = true, name = "rental_value")
    private Double value;

    @Column(nullable = true)
    private String observation;

    @Column(nullable = true)
    private String paymentDay;

    @JsonIgnore
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PropertyLog> propertyLogs = new ArrayList<>();

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

    public void addPropertyLog(PropertyLog log) {
        this.propertyLogs.add(log);
        log.setProperty(this);
    }

}
