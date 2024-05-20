package com.realestate.mapper;

import com.realestate.dto.ClientDto;
import com.realestate.model.client.Client;
import com.realestate.model.offer.Offer;
import com.realestate.repository.OffersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientMapper {
    private final OffersRepository offersRepository;



    public ClientDto map(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setFirstName(client.getFirstName());
        dto.setLastName(client.getLastName());
        dto.setEmail(client.getEmail());
        dto.setTelephone(client.getTelephone());
        return dto;
    }

    public Client map(ClientDto clientDto) {
        Client client = new Client();
        client.setId(clientDto.getId());
        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setEmail(clientDto.getEmail());
        client.setTelephone(clientDto.getTelephone());
        List<Offer> offers = offersRepository.findAllByClient_Id(clientDto.getId());
        client.setOffers(offers);
        return client;

    }
}
