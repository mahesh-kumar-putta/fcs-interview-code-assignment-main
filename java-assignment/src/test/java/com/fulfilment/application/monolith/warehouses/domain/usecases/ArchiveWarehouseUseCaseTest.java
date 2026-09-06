package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ArchiveWarehouseUseCaseTest {

  @Test
  void archivesExistingWarehouse() {
    InMemoryWarehouseStore store = new InMemoryWarehouseStore();
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = "MWH.100";
    warehouse.capacity = 100;
    warehouse.stock = 20;
    warehouse.createdAt = LocalDateTime.now();
    store.warehouses.add(warehouse);

    new ArchiveWarehouseUseCase(store).archive(warehouse);

    assertNotNull(warehouse.archivedAt);
    assertEquals(warehouse, store.updatedWarehouse);
  }

  @Test
  void rejectsNullWarehouse() {
    InMemoryWarehouseStore store = new InMemoryWarehouseStore();

    assertThrows(IllegalArgumentException.class, () -> new ArchiveWarehouseUseCase(store).archive(null));
  }

  private static class InMemoryWarehouseStore implements WarehouseStore {
    private final List<Warehouse> warehouses = new ArrayList<>();
    private Warehouse updatedWarehouse;

    @Override
    public List<Warehouse> getAll() {
      return warehouses;
    }

    @Override
    public void create(Warehouse warehouse) {
      warehouses.add(warehouse);
    }

    @Override
    public void update(Warehouse warehouse) {
      updatedWarehouse = warehouse;
    }

    @Override
    public void remove(Warehouse warehouse) {
      warehouses.remove(warehouse);
    }

    @Override
    public Warehouse findByBusinessUnitCode(String buCode) {
      return warehouses.stream()
          .filter(warehouse -> buCode.equals(warehouse.businessUnitCode))
          .findFirst()
          .orElse(null);
    }
  }
}
