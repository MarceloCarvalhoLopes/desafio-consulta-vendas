package com.devsuperior.dsmeta.services;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	private LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> searchReport(String minDate, String maxDate, String name, Pageable pageable) {

		minDate = checkDates(minDate,today.minusYears(1L).toString());
		maxDate = checkDates(maxDate,today.toString());

		Page<Sale> entity = repository.searchReport(LocalDate.parse(minDate),LocalDate.parse(maxDate),name,pageable);
		return entity.map(x -> new SaleReportDTO(x));
	}

	public List<SaleSummaryDTO> searchSummary(String minDate, String maxDate) {
		minDate = checkDates(minDate,today.minusYears(1L).toString());
		maxDate = checkDates(maxDate,today.toString());
		return repository.searchSummary(LocalDate.parse(minDate),LocalDate.parse(maxDate));
	}

	private String checkDates(String date, String newDate) {
		if (date.isBlank()){
			return newDate;
		}
		return date;
	}
}
