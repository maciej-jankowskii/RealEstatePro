package com.realestate.controller;

import com.realestate.dto.ClientDto;
import com.realestate.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientController {

    private final ClientService clientService;


    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        ClientDto dto = clientService.getClientById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("")
    ResponseEntity<List<ClientDto>> getAllClients(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size) {
        List<ClientDto> allClients = clientService.getAllClients(page,size);
        if (allClients.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allClients);
    }

    @PostMapping
    ResponseEntity<ClientDto> saveClient(@RequestBody @Valid ClientDto clientDto) {
        ClientDto saved = clientService.saveClient(clientDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PutMapping("/update-client/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody @Valid ClientDto dto) {
        clientService.updateClient(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete-client/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

}
