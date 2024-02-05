package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;


public interface SaleRepository extends JpaRepository<Sale, Long> {


    @Query(value = "SELECT obj FROM Sale obj JOIN FETCH obj.seller WHERE UPPER(obj.seller.name) " +
            "LIKE UPPER(CONCAT('%', :name, '%')) AND obj.date BETWEEN :minDate AND :maxDate",
            countQuery = "SELECT COUNT(obj) FROM Sale obj JOIN obj.seller")
    Page<Sale> searchReport(LocalDate minDate, LocalDate maxDate, String name, Pageable pageable);

    @Query(value = "SELECT new com.devsuperior.dsmeta.dto.SaleSummaryDTO(obj.seller.name, " +
                          "SUM(obj.amount)) " +
                     "FROM Sale obj WHERE obj.date BETWEEN :minDate AND :maxDate GROUP BY obj.seller.name")
    List<SaleSummaryDTO> searchSummary(LocalDate minDate, LocalDate maxDate);

}
