package com.fulfilment.application.monolith.warehouses.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fulfilment.application.monolith.warehouses.domain.models.Location;
import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.LocationResolver;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CreateWarehouseUseCaseTest {

	@Test
	void createsWarehouseWithCreationTimestamp() {
		InMemoryWarehouseStore store = new InMemoryWarehouseStore();
		CreateWarehouseUseCase useCase =
				new CreateWarehouseUseCase(store, new TestLocationResolver());
		Warehouse warehouse = warehouse("MWH.100", 20, 10);

		useCase.create(warehouse);

		assertEquals(1, store.warehouses.size());
		assertEquals(warehouse, store.warehouses.get(0));
		org.junit.jupiter.api.Assertions.assertNotNull(warehouse.createdAt);
	}

	@Test
	void rejectsStockThatExceedsCapacity() {
		InMemoryWarehouseStore store = new InMemoryWarehouseStore();
		CreateWarehouseUseCase useCase =
				new CreateWarehouseUseCase(store, new TestLocationResolver());

		assertThrows(IllegalArgumentException.class, () -> useCase.create(warehouse("MWH.100", 10, 11)));
		assertEquals(0, store.warehouses.size());
	}

	@Test
	void rejectsDuplicateBusinessUnitCode() {
		InMemoryWarehouseStore store = new InMemoryWarehouseStore();
		store.warehouses.add(warehouse("MWH.100", 20, 10));
		CreateWarehouseUseCase useCase =
				new CreateWarehouseUseCase(store, new TestLocationResolver());

		assertThrows(IllegalArgumentException.class, () -> useCase.create(warehouse("MWH.100", 20, 10)));
		assertEquals(1, store.warehouses.size());
	}

	private Warehouse warehouse(String businessUnitCode, int capacity, int stock) {
		var warehouse = new Warehouse();
		warehouse.businessUnitCode = businessUnitCode;
		warehouse.location = "ZWOLLE-001";
		warehouse.capacity = capacity;
		warehouse.stock = stock;
		return warehouse;
	}

	private static class TestLocationResolver implements LocationResolver {
		@Override
		public Location resolveByIdentifier(String identifier) {
			return "ZWOLLE-001".equals(identifier) ? new Location(identifier, 2, 40) : null;
		}
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
		public void update(Warehouse warehouse) {}

		@Override
		public void remove(Warehouse warehouse) {}

		@Override
		public Warehouse findByBusinessUnitCode(String businessUnitCode) {
			return warehouses.stream()
					.filter(warehouse -> businessUnitCode.equals(warehouse.businessUnitCode))
					.findFirst()
					.orElse(null);
		}
	}
}
