package com.realestate.service;

import com.realestate.dto.ClientDto;
import com.realestate.mapper.ClientMapper;
import com.realestate.model.client.Client;
import com.realestate.repository.ClientRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetClientById() {
        Client client = new Client();
        ClientDto dto = new ClientDto();
        Long clientId = 1L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientMapper.map(client)).thenReturn(dto);

        Optional<ClientDto> resultClient = clientService.getClientById(client.getId());

        assertNotNull(resultClient);
    }

    @Test
    public void testClientNotFound() {
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        Optional<ClientDto> result = clientService.getClientById(clientId);
        assertEquals(Optional.empty(), result);

    }

    @Test
    public void testGetAllClients() {
        List<Client> clients = new ArrayList<>();
        clients.add(new Client());
        clients.add(new Client());

        List<ClientDto> clientDtos = new ArrayList<>();
        clientDtos.add(new ClientDto());
        clientDtos.add(new ClientDto());

        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.map(any(Client.class))).thenReturn(clientDtos.get(0), clientDtos.get(1));

        List<ClientDto> allClients = clientService.getAllClients();

        assertEquals(clients.size(), allClients.size());
        assertNotNull(allClients);
    }

    @Test
    public void testSaveClient() {
        ClientDto dto = new ClientDto();
        Client client = new Client();

        when(clientMapper.map(dto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.map(client)).thenReturn(dto);

        ClientDto result = clientService.saveClient(dto);

        assertEquals(client.getId(), result.getId());
        assertNotNull(result);

    }

    @Test
    public void testUpdateClient() {
        ClientDto dto = new ClientDto();
        Client client = new Client();

        when(clientMapper.map(dto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);

        clientService.updateClient(dto);

        verify(clientRepository, times(1)).save(client);

    }

    @Test
    public void testDeleteClient() {
        Long clientId = 1L;

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);

    }

}