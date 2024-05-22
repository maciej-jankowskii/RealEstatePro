package com.realestate.service;

import com.realestate.dto.ClientDto;
import com.realestate.exceptions.CannotDeleteResourceException;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.ClientMapper;
import com.realestate.model.client.Client;
import com.realestate.model.offer.Offer;
import com.realestate.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;


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
    @Mock
    private ValidationService validationService;
    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getClientById_ShouldFindClient() {
        Client client = new Client();
        ClientDto dto = new ClientDto();
        Long clientId = 1L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientMapper.map(client)).thenReturn(dto);

        ClientDto resultClient = clientService.getClientById(clientId);

        assertNotNull(resultClient);
    }

    @Test
    public void getClientById_ShouldNotFound() {
        Long clientId = 1L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clientService.getClientById(clientId));
    }

    @Test
    void getAllClients_ShouldFindAllClients() {

        int page = 0;
        int size = 5;
        List<Client> clientList = new ArrayList<>();
        clientList.add(new Client());
        clientList.add(new Client());
        Page<Client> clientPage = new PageImpl<>(clientList);
        when(clientRepository.findAll(any(Pageable.class))).thenReturn(clientPage);

        List<ClientDto> clientDtoList = new ArrayList<>();
        clientDtoList.add(new ClientDto());
        clientDtoList.add(new ClientDto());
        when(clientMapper.map(any(Client.class))).thenReturn(new ClientDto());

        List<ClientDto> result = clientService.getAllClients(page, size);

        assertNotNull(result);
        assertEquals(clientDtoList.size(), result.size());
        verify(clientRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void saveClient_ValidDto_ShouldSaveClientAndReturnDto() {

        ClientDto inputDto = new ClientDto();
        Client client = new Client();
        Client savedClient = new Client();
        ClientDto expectedDto = new ClientDto();

        when(clientMapper.map(inputDto)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(savedClient);
        when(clientMapper.map(savedClient)).thenReturn(expectedDto);
        doNothing().when(validationService).validateData(any());

        ClientDto resultDto = clientService.saveClient(inputDto);

        assertNotNull(resultDto);
        assertEquals(expectedDto, resultDto);
        verify(clientMapper, times(1)).map(inputDto);
        verify(clientRepository, times(1)).save(client);
        verify(clientMapper, times(1)).map(savedClient);
        verify(validationService, times(1)).validateData(any());
    }

    @Test
    void saveClient_InvalidDto_ShouldThrowException() {
        ClientDto invalidDto = new ClientDto();
        invalidDto.setFirstName("");

        when(clientMapper.map(invalidDto)).thenReturn(new Client());
        doThrow(DataIntegrityViolationException.class).when(clientRepository).save(any());

        assertThrows(DataIntegrityViolationException.class, () -> clientService.saveClient(invalidDto));

        verify(clientMapper, times(1)).map(invalidDto);
        verify(clientRepository, times(1)).save(any());
    }

    @Test
    public void updateClient_ShouldUpdate() {
        ClientDto dto = new ClientDto();
        Client client = new Client();

        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);

        clientService.updateClient(1L, dto);

        verify(clientRepository, times(1)).save(client);

    }

    @Test
    public void deleteClient_ShouldDelete() {
        Long clientId = 1L;

        Client client = new Client();
        client.setOffers(new ArrayList<>());

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        doNothing().when(clientRepository).deleteById(clientId);

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    public void deleteClientWithOffers_ShouldNotDelete() {
        Long clientId = 1L;

        Client client = new Client();
        client.getOffers().add(new Offer());

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        assertThrows(CannotDeleteResourceException.class, () -> clientService.deleteClient(clientId));

        verify(clientRepository, never()).deleteById(clientId);
    }
}