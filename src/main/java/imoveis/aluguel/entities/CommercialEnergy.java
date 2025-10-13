package imoveis.aluguel.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@Table(name = "tb_commercial_energy")
public class CommercialEnergy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount_1", nullable = true)
    private Double amount1;

    @Column(name = "amount_2", nullable = true)
    private Double amount2;

    @Column(name = "internal_counter", nullable = true)
    private Double internalCounter;

    @Column(name = "account_value", nullable = true)
    private Double accountValue;

    @Column(name = "account_consumption", nullable = true)
    private Double accountConsumption;

    @Column(name = "calculated_consumption_1", nullable = true)
    private Double calculatedConsumption1;

    @Column(name = "calculated_consumption_2", nullable = true)
    private Double calculatedConsumption2;

    @Column(name = "date", nullable = true)
    private LocalDate date;

}