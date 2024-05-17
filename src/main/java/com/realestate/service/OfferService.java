package com.realestate.service;

import com.realestate.dto.OfferDto;
import com.realestate.exceptions.ResourceNotFoundException;
import com.realestate.mapper.OfferMapper;
import com.realestate.model.Property.Property;
import com.realestate.model.client.Client;
import com.realestate.model.offer.Offer;
import com.realestate.model.user.UserEmployee;
import com.realestate.repository.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OfferService {

    private final OffersRepository offersRepository;
    private final UserRepository userRepository;
    private  final PropertyRepository propertyRepository;
    private final ClientRepository clientRepository;
    private final ReservationRepository reservationRepository;
    private final OfferMapper offerMapper;
    private final ValidationService validationService;

    public OfferService(OffersRepository offersRepository, UserRepository userRepository, PropertyRepository propertyRepository, ClientRepository clientRepository, ReservationRepository reservationRepository, OfferMapper offerMapper, ValidationService validationService) {
        this.offersRepository = offersRepository;
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
        this.clientRepository = clientRepository;
        this.reservationRepository = reservationRepository;
        this.offerMapper = offerMapper;
        this.validationService = validationService;
    }

    @Transactional
    public OfferDto saveOffer(@Valid OfferDto offerDto){
        Offer offer = offerMapper.map(offerDto);
        validationService.validateData(offer);
        Offer saved = offersRepository.save(offer);
        return offerMapper.map(saved);
    }

    public OfferDto getOfferById(Long id){
        return offersRepository.findById(id).map(offerMapper::map).orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
    }

    public List<OfferDto> getOffersByClient(Long clientId){
        List<Offer> offers = offersRepository.findAllByClient_Id(clientId);
        List<OfferDto> dtos = offers.stream().map(offerMapper::map).collect(Collectors.toList());
        return dtos;
    }

    public List<OfferDto> getAllOffers(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Offer> offerPage = offersRepository.findAll(pageable);
        List<OfferDto> dtos = offerPage.getContent().stream().map(offerMapper::map).collect(Collectors.toList());
        return dtos;
    }

    public void markOfferAsSold(Long offerId){
        Offer offer = offersRepository.findById(offerId).orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        offer.setIsAvailable(false);
        offersRepository.save(offer);
    }

    public List<OfferDto> findAvailableOffers(){
        List<Offer> offers = (List<Offer>) offersRepository.findAll();
        List<OfferDto> availableOffers = offers.stream().filter(Offer::getIsAvailable).map(offerMapper::map).collect(Collectors.toList());
        return availableOffers;
    }

    public List<OfferDto> findSoldOffers(){
        List<Offer> offers = (List<Offer>) offersRepository.findAll();
        List<OfferDto> soldOffers = offers.stream().filter(offer -> !offer.getIsAvailable()).map(offerMapper::map).collect(Collectors.toList());
        return soldOffers;
    }



    @Transactional
    public void updateOffer(Long id, @Valid OfferDto updateDto){
        Offer offer = offersRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Offer not found"));
        validationService.validateData(offer);
        updateOffer(updateDto, offer);
        offersRepository.save(offer);
    }

    public void deleteOffer(Long id){
        offersRepository.deleteById(id);
    }

    private void updateOffer(OfferDto updateDto, Offer offer) {
        UserEmployee userEmployee = userRepository.findById(updateDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Property property = propertyRepository.findById(updateDto.getPropertyId()).orElseThrow(() -> new ResourceNotFoundException("Property not found"));
        Client client = clientRepository.findById(updateDto.getClientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found"));

        offer.setUser(userEmployee);
        offer.setProperty(property);
        offer.setClient(client);
        offer.setIsBooked(updateDto.getIsBooked());
        offer.setIsAvailable(updateDto.getIsAvailable());
    }

}
