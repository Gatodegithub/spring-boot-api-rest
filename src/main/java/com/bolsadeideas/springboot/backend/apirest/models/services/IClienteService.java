package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;
import java.util.Optional;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public Optional<Cliente> findById(Long id);
	
	public Cliente save(Cliente cliente);
	
	public void delete(Long id);

}
