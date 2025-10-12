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
@Table(name = "tb_energy")
public class Energy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "counter_1", nullable = false)
    private Long counter1;

    @Column(name = "counter_2", nullable = false)
    private Long counter2;

    @Column(name = "counter_3", nullable = false)
    private Long counter3;

    @Column(name = "amount_1", nullable = true)
    private Double amount1;

    @Column(name = "amount_2", nullable = true)
    private Double amount2;

    @Column(name = "amount_3", nullable = true)
    private Double amount3;

    @Column(name = "kwh_value", nullable = true)
    private Double kwhValue;

    @Column(name = "bill_amount", nullable = true)
    private Double billAmount;

    @Column(name = "date", nullable = true)
    private LocalDate date;

}
