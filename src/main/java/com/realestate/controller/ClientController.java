package com.realestate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.realestate.dto.ClientDto;
import com.realestate.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final ObjectMapper objectMapper;

    public ClientController(ClientService clientService, ObjectMapper objectMapper) {
        this.clientService = clientService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{id}")
    ResponseEntity<ClientDto> getClientById(@PathVariable Long id){
        return clientService.getClientById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/getAll")
    ResponseEntity<List<ClientDto>> getAllClients(){
        List<ClientDto> allClients = clientService.getAllClients();
        if (allClients.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allClients);
    }

    @PostMapping
    ResponseEntity<ClientDto> saveClient(@RequestBody ClientDto clientDto){
        ClientDto saved = clientService.saveClient(clientDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody JsonMergePatch patch){
        try {
            ClientDto clientDto = clientService.getClientById(id).orElseThrow();
            ClientDto clientPatched = applyPatch(clientDto, patch);
            clientService.updateClient(clientPatched);
        }catch (JsonPatchException | JsonProcessingException ex){
            return ResponseEntity.internalServerError().build();
        }catch (NoSuchElementException ex){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    private ClientDto applyPatch(ClientDto clientDto, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode clientNode = objectMapper.valueToTree(clientDto);
        JsonNode clientPatched = patch.apply(clientNode);
        return objectMapper.treeToValue(clientPatched, ClientDto.class);
    }


}
