package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ReplaceWarehouseUseCaseTest {

  @Test
  void replacesExistingWarehouseWithMatchingStockAndCapacity() {
    InMemoryWarehouseStore store = new InMemoryWarehouseStore();
    Warehouse current = warehouse("MWH.100", 50, 20);
    Warehouse replacement = warehouse("MWH.100", 50, 20);
    store.warehouses.add(current);

    new ReplaceWarehouseUseCase(store).replace(replacement);

    assertNotNull(current.archivedAt);
    assertNull(replacement.archivedAt);
    assertNotNull(replacement.createdAt);
    assertEquals(2, store.warehouses.size());
  }

  @Test
  void rejectsWhenCurrentWarehouseDoesNotExist() {
    InMemoryWarehouseStore store = new InMemoryWarehouseStore();
    Warehouse replacement = warehouse("MWH.100", 50, 20);

    assertThrows(IllegalArgumentException.class, () -> new ReplaceWarehouseUseCase(store).replace(replacement));
  }

  @Test
  void rejectsReplacementWithMismatchedStock() {
    InMemoryWarehouseStore store = new InMemoryWarehouseStore();
    Warehouse current = warehouse("MWH.100", 50, 20);
    Warehouse replacement = warehouse("MWH.100", 60, 25);
    store.warehouses.add(current);

    assertThrows(IllegalArgumentException.class, () -> new ReplaceWarehouseUseCase(store).replace(replacement));
  }

  private static Warehouse warehouse(String businessUnitCode, int capacity, int stock) {
    Warehouse warehouse = new Warehouse();
    warehouse.businessUnitCode = businessUnitCode;
    warehouse.capacity = capacity;
    warehouse.stock = stock;
    warehouse.location = "ZWOLLE-001";
    warehouse.createdAt = LocalDateTime.now();
    return warehouse;
  }

  private static class InMemoryWarehouseStore implements WarehouseStore {
    private final List<Warehouse> warehouses = new ArrayList<>();

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
      // no-op for the in-memory model; the mutated object is already referenced
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
