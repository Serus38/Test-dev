package com.testdev.test_dev.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.testdev.test_dev.model.Bodega;
import com.testdev.test_dev.model.Port;
import com.testdev.test_dev.model.ShipmentStatus;
import com.testdev.test_dev.model.TerrestrialShipment;
import com.testdev.test_dev.repository.TerrestrialShipmentRepository;

@ExtendWith(MockitoExtension.class)
class TerrestrialShipmentServiceImpTest {

    @Mock
    private TerrestrialShipmentRepository repository;

    @InjectMocks
    private TerrestrialShipmentServiceImp service;

    @Test
    void save_generatesFormattedGuideAndCalculatesDiscount() {
        TerrestrialShipment shipment = buildValidShipment();

        when(repository.save(any(TerrestrialShipment.class))).thenAnswer(invocation -> {
            TerrestrialShipment arg = invocation.getArgument(0);
            if (arg.getId() == null) {
                arg.setId(15L);
            }
            return arg;
        });

        TerrestrialShipment saved = service.save(shipment);

        assertEquals("TER0000015", saved.getGuideNumber());
        assertEquals("10.00", saved.getDiscountRate().toPlainString());
        assertEquals("90.00", saved.getTotalCost().toPlainString());
        verify(repository, times(2)).save(any(TerrestrialShipment.class));
    }

    @Test
    void save_throwsWhenOriginDestinationInvalid() {
        TerrestrialShipment shipment = buildValidShipment();
        shipment.setOriginPort(buildPort(1L));

        assertThrows(IllegalArgumentException.class, () -> service.save(shipment));
    }

    @Test
    void update_keepsExistingGuideWhenIncomingIsBlank() {
        TerrestrialShipment incoming = buildValidShipment();
        incoming.setId(8L);
        incoming.setGuideNumber(" ");

        TerrestrialShipment existing = buildValidShipment();
        existing.setId(8L);
        existing.setGuideNumber("TER0000008");

        when(repository.findById(8L)).thenReturn(Optional.of(existing));
        when(repository.save(any(TerrestrialShipment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TerrestrialShipment updated = service.update(incoming);

        assertEquals("TER0000008", updated.getGuideNumber());
        verify(repository).findById(8L);
        verify(repository).save(incoming);
    }

    @Test
    void getTerrestrialShipmentById_marksAsDelayedAndPersistsChange() {
        TerrestrialShipment shipment = buildValidShipment();
        shipment.setId(20L);
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setDeliveryDate(LocalDate.now().minusDays(2).toString());

        when(repository.findById(20L)).thenReturn(Optional.of(shipment));
        when(repository.save(any(TerrestrialShipment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TerrestrialShipment result = service.getTerrestrialShipmentById(20L);

        assertEquals(ShipmentStatus.DELAYED, result.getStatus());
        verify(repository).save(shipment);
    }

    @Test
    void getAllTerrestrialShipments_updatesStatusesAndPersistsWhenNeeded() {
        TerrestrialShipment shipment = buildValidShipment();
        shipment.setStatus(ShipmentStatus.IN_TRANSIT);
        shipment.setDeliveryDate(LocalDate.now().minusDays(5).toString());

        when(repository.findAll()).thenReturn(List.of(shipment));

        List<TerrestrialShipment> result = service.getAllTerrestrialShipments();

        assertEquals(1, result.size());
        assertEquals(ShipmentStatus.CANCELLED, result.get(0).getStatus());
        verify(repository).saveAll(result);
    }

    private TerrestrialShipment buildValidShipment() {
        TerrestrialShipment shipment = new TerrestrialShipment();
        shipment.setProductType("Electronics");
        shipment.setQuantity(20.0);
        shipment.setOriginBodega(buildBodega(1L));
        shipment.setDestinationPort(buildPort(2L));
        shipment.setRegistrationDate(LocalDate.now().toString());
        shipment.setDeliveryDate(LocalDate.now().plusDays(2).toString());
        shipment.setShippingCost(new BigDecimal("100.00"));
        shipment.setStatus(ShipmentStatus.PENDING);
        shipment.setVehiclePlate("ABC1234");
        return shipment;
    }

    private Bodega buildBodega(Long id) {
        Bodega bodega = new Bodega();
        bodega.setId(id);
        bodega.setName("Bodega " + id);
        bodega.setCountry("Colombia");
        bodega.setCity("Bogota");
        return bodega;
    }

    private Port buildPort(Long id) {
        Port port = new Port();
        port.setId(id);
        port.setName("Port " + id);
        port.setCountry("Colombia");
        port.setCity("Cartagena");
        return port;
    }
}
