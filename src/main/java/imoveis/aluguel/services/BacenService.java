package imoveis.aluguel.services;

import java.time.LocalDate;
import java.util.List;

import imoveis.aluguel.dtos.BacenIpcaResponseDTO;

public interface BacenService {

    List<BacenIpcaResponseDTO> getIpcaData(LocalDate startDate, LocalDate endDate);

    Double calculateAccumulatedIpca(LocalDate startDate, LocalDate endDate);

}
