package com.devsuperior.bds02.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.services.exceptions.DataBaseException;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;

@Service
public class CityService {

	@Autowired
	private CityRepository repository;

	// TRAZER TUDO NÃO PAGINADO ORDENADO POR NOME

	@Transactional(readOnly = true)
	public List<CityDTO> findAll(Sort sort) {

		List<City> list = repository.findAll(Sort.by("name")); // BUSCA O METODO DO BANCO
		return list.stream().map(x -> new // CONVERTE A LISTA PARA DTO
				CityDTO(x)).collect(Collectors.toList());
	}
	
	// INSERIR
	
	@Transactional
	public CityDTO insert(CityDTO dto) {

		City entity = new City();
		
		entity.setName(dto.getName());
		
		entity = repository.save(entity);
		return new CityDTO(entity);
	}
	
	// DELETAR
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		// CASO VC TENTE DELETAR UMA CATEGORIA COM PRODUTOS VINCULADOS
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integrity Violation");
		}
	}
}
