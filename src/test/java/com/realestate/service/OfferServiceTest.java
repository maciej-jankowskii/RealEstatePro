package com.realestate.service;

import com.realestate.dto.OfferDto;
import com.realestate.mapper.OfferMapper;
import com.realestate.model.client.Client;
import com.realestate.model.offer.Offer;
import com.realestate.repository.OffersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OfferServiceTest {

    @Mock
    private OffersRepository offersRepository;
    @Mock
    private OfferMapper offerMapper;
    @InjectMocks
    private OfferService offerService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveOffer() {
        OfferDto dto = new OfferDto();
        Offer offer = new Offer();

        when(offerMapper.map(dto)).thenReturn(offer);
        when(offersRepository.save(offer)).thenReturn(offer);
        when(offerMapper.map(offer)).thenReturn(dto);

        OfferDto result = offerService.saveOffer(dto);

        assertEquals(dto, result);
        assertNotNull(result);

    }

    @Test
    public void testGetOfferById() {
        OfferDto dto = new OfferDto();
        Offer offer = new Offer();
        Long offerId = 1L;

        when(offersRepository.findById(offerId)).thenReturn(Optional.of(offer));
        when(offerMapper.map(offer)).thenReturn(dto);

        Optional<OfferDto> resultOffer = offerService.getOfferById(offerId);

        assertTrue(resultOffer.isPresent());
        assertEquals(dto, resultOffer.get());

    }

    @Test
    public void testGetOffersByClient() {
        Client client = new Client();
        Offer offer = new Offer();
        OfferDto dto = new OfferDto();
        Long clientId = 1L;

        when(offersRepository.findAllByClient_Id(clientId)).thenReturn(Collections.singletonList(offer));
        when(offerMapper.map(offer)).thenReturn(dto);

        List<OfferDto> offersByClient = offerService.getOffersByClient(clientId);

        assertNotNull(offersByClient);
        assertEquals(1, offersByClient.size());
        assertEquals(dto, offersByClient.get(0));
    }

    @Test
    public void testGetAllOffers() {
        List<Offer> offers = new ArrayList<>();
        List<OfferDto> dtos = new ArrayList<>();
        offers.add(new Offer());
        offers.add(new Offer());
        dtos.add(new OfferDto());
        dtos.add(new OfferDto());

        when(offersRepository.findAll()).thenReturn(offers);
        when(offerMapper.map(any(Offer.class))).thenReturn(dtos.get(0), dtos.get(1));

        List<OfferDto> allOffers = offerService.getAllOffers();

        assertNotNull(allOffers);
        assertEquals(dtos, allOffers);
    }

    @Test
    public void testMarkOfferAsSold() {
        Offer offer = new Offer();
        Long offerId = 1L;
        offer.setIsAvailable(true);

        when(offersRepository.findById(offerId)).thenReturn(Optional.of(offer));

        offerService.markOfferAsSold(offerId);

        verify(offersRepository, times(1)).findById(offerId);
        assertFalse(offer.getIsAvailable());
    }

    @Test
    public void testFindAvailableOffers() {
        List<Offer> offers = createOfferList();
        List<OfferDto> dtoOffers = createDtoOfferList();

        when(offersRepository.findAll()).thenReturn(offers);
        when(offerMapper.map(any(Offer.class))).thenReturn(dtoOffers.get(0), dtoOffers.get(1));

        List<OfferDto> availableOffers = offerService.findAvailableOffers();

        assertEquals(1, availableOffers.size());
        assertEquals(dtoOffers.get(0), availableOffers.get(0));

    }

    @Test
    public void testFindSoldOffers() {
        List<Offer> offers = createOfferList();
        List<OfferDto> dtoOffers = createDtoOfferList();

        when(offersRepository.findAll()).thenReturn(offers);
        when(offerMapper.map(any(Offer.class))).thenReturn(dtoOffers.get(0), dtoOffers.get(1));

        List<OfferDto> soldOffers = offerService.findSoldOffers();

        assertEquals(1, soldOffers.size());
        assertEquals(dtoOffers.get(0), soldOffers.get(0));
    }

    @Test
    public void testUpdateOffer() {
        OfferDto dto = new OfferDto();
        Offer offer = new Offer();

        when(offerMapper.map(dto)).thenReturn(offer);
        when(offersRepository.save(offer)).thenReturn(offer);

        offerService.updateOffer(dto);

        verify(offersRepository, times(1)).save(offer);
    }

    @Test
    public void testDeleteOffer() {
        Long offerId = 1L;

        offerService.deleteOffer(offerId);

        verify(offersRepository, times(1)).deleteById(offerId);
    }

    private List<OfferDto> createDtoOfferList() {
        List<OfferDto> dtos = new ArrayList<>();
        OfferDto dto1 = new OfferDto();
        OfferDto dto2 = new OfferDto();
        dto1.setIsAvailable(true);
        dto1.setIsAvailable(false);
        dtos.add(dto1);
        dtos.add(dto2);
        return dtos;
    }

    private List<Offer> createOfferList() {
        List<Offer> offers = new ArrayList<>();
        Offer offer1 = new Offer();
        Offer offer2 = new Offer();
        offer1.setIsAvailable(true);
        offer2.setIsAvailable(false);
        offers.add(offer1);
        offers.add(offer2);
        return offers;
    }

}