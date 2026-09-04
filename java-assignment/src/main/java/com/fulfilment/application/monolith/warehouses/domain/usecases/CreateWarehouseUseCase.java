package com.fulfilment.application.monolith.warehouses.domain.usecases;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.ports.CreateWarehouseOperation;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;

@ApplicationScoped
public class CreateWarehouseUseCase implements CreateWarehouseOperation {

  private final WarehouseStore warehouseStore;
  private final LocationResolver locationResolver;

  public CreateWarehouseUseCase(WarehouseStore warehouseStore, LocationResolver locationResolver) {
    this.warehouseStore = warehouseStore;
    this.locationResolver = locationResolver;
  }

  @Override
  public void create(Warehouse warehouse) {
    validateWarehouse(warehouse);

    Location location = locationResolver.resolveByIdentifier(warehouse.location);
    if (location == null) {
      throw new IllegalArgumentException("Warehouse location does not exist");
    }

    if (warehouseStore.findByBusinessUnitCode(warehouse.businessUnitCode) != null) {
      throw new IllegalArgumentException("Business unit code already exists");
    }

    long warehousesAtLocation =
        warehouseStore.getAll().stream()
            .filter(existing -> warehouse.location.equals(existing.location))
            .count();
    if (warehousesAtLocation >= location.maxNumberOfWarehouses) {
      throw new IllegalArgumentException("Maximum number of warehouses reached for location");
    }

    int capacityAtLocation =
        warehouseStore.getAll().stream()
            .filter(existing -> warehouse.location.equals(existing.location))
            .mapToInt(existing -> existing.capacity)
            .sum();
    if (capacityAtLocation + warehouse.capacity > location.maxCapacity) {
      throw new IllegalArgumentException("Maximum capacity reached for location");
    }

    warehouse.createdAt = LocalDateTime.now();
    warehouse.archivedAt = null;

    warehouseStore.create(warehouse);
  }

  private void validateWarehouse(Warehouse warehouse) {
    if (warehouse == null
        || warehouse.businessUnitCode == null
        || warehouse.businessUnitCode.isBlank()
        || warehouse.location == null
        || warehouse.location.isBlank()) {
      throw new IllegalArgumentException("Business unit code and location are required");
    }
    if (warehouse.capacity == null || warehouse.capacity < 0) {
      throw new IllegalArgumentException("Warehouse capacity must be zero or greater");
    }
    if (warehouse.stock == null || warehouse.stock < 0 || warehouse.stock > warehouse.capacity) {
      throw new IllegalArgumentException("Warehouse stock must fit within its capacity");
    }
  }
}
