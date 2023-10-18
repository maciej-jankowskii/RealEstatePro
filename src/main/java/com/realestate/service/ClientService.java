package com.realestate.service;

import com.realestate.dto.ClientDto;
import com.realestate.dto.OfferDto;
import com.realestate.mapper.ClientMapper;
import com.realestate.mapper.OfferMapper;
import com.realestate.model.client.Client;
import com.realestate.model.offer.Offer;
import com.realestate.repository.ClientRepository;
import com.realestate.repository.OffersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.plaf.PanelUI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public Optional<ClientDto> getClientById(Long id) {
        return clientRepository.findById(id).map(clientMapper::map);
    }

    public List<ClientDto> getAllClients() {
        List<Client> clients = (List<Client>) clientRepository.findAll();
        List<ClientDto> dtos = clients.stream().map(clientMapper::map).collect(Collectors.toList());
        return dtos;
    }

    @Transactional
    public ClientDto saveClient(ClientDto clientDto) {
        Client client = clientMapper.map(clientDto);
        Client saved = clientRepository.save(client);
        return clientMapper.map(saved);
    }

    @Transactional
    public void updateClient(ClientDto clientDto) {
        Client client = clientMapper.map(clientDto);
        clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

}
