package com.realestate.service;


import com.realestate.dto.ClientDto;
import com.realestate.exceptions.CannotDeleteResourceException;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.ClientMapper;
import com.realestate.model.client.Client;
import com.realestate.repository.ClientRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ValidationService validationService;


    public ClientDto getClientById(Long id) {
        return clientRepository.findById(id).map(clientMapper::map)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }


    public List<ClientDto> getAllClients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Client> clientPage = clientRepository.findAll(pageable);
        return clientPage.getContent().stream()
                .map(clientMapper::map)
                .collect(Collectors.toList());

    }

    @Transactional
    public ClientDto saveClient(@Valid ClientDto clientDto) {
        Client client = clientMapper.map(clientDto);
        validationService.validateData(client);
        Client saved = clientRepository.save(client);
        return clientMapper.map(saved);
    }

    @Transactional
    public void updateClient(Long id,@Valid ClientDto clientDto) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        validationService.validateData(client);
        updateClient(clientDto, client);

        clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        if (!client.getOffers().isEmpty()){
            throw new CannotDeleteResourceException("Cannot delete client with offers");
        }
        clientRepository.deleteById(id);
    }


    /**

     HELPER METHODS FOR REGISTER

     **/


    private static void updateClient(ClientDto clientDto, Client client) {
        client.setTelephone(clientDto.getTelephone());
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
    }

}
