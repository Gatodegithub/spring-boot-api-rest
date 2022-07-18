package com.bolsadeideas.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Cliente;
import com.bolsadeideas.springboot.backend.apirest.models.services.IClienteService;

@CrossOrigin(origins = { "http://localhost:8080" })
@RestController
@RequestMapping("/api")
public class ClienteRestController {

	@Autowired
	private IClienteService clienteService;

	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}

	@GetMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.OK) // Es rebundante ya que por defecto si no se pone esta anotacion y todo sale
									// bien se response con un OK
	public ResponseEntity<?> show(@PathVariable Long id) {

		Optional<Cliente> cliente = clienteService.findById(id);

		if (cliente.isPresent()) {
			return new ResponseEntity<Cliente>(cliente.get(), HttpStatus.ACCEPTED);
		}

		Map<String, Object> response = new HashMap<>();

		response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

	}

	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED) // Cambiamos el codigo de la respuesta a 201 de created, por defecto cuando sale
										// todo bien el HttpStatus queda como OK
	public Cliente create(@RequestBody Cliente cliente) {
		return clienteService.save(cliente);
	}

	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<?> update(@RequestBody Cliente cliente, @PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		try {
			Optional<Cliente> clienteActual = clienteService.findById(id);

			if (clienteActual.isPresent()) {

				Cliente c = clienteActual.get();

				c.setApellido(cliente.getApellido());
				c.setNombre(cliente.getNombre());
				c.setEmail(cliente.getEmail());
				
				clienteService.save(c);

				return new ResponseEntity<Cliente>(c, HttpStatus.CREATED);
			}
		} catch (DataAccessException e) { // Esta excepsion se lanza cuando hay un error en la bd
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ".concat(e.getMostSpecificCause().getMessage())));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(name = "id") Long identificador) {
		clienteService.delete(identificador);
	}
}
