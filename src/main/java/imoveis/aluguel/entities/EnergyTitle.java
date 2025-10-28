package imoveis.aluguel.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_energy_title")
@Getter
@Setter
public class EnergyTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title_amount_1")
    private String titleAmount1;

    @Column(name = "title_amount_2")
    private String titleAmount2;

    @Column(name = "title_amount_3")
    private String titleAmount3;

    @Column(name = "title_amount_4")
    private String titleAmount4;

    @Column(name = "title_amount_5")
    private String titleAmount5;

}
