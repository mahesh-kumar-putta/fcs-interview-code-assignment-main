package com.fulfilment.application.monolith.fulfilment;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class FulfilmentAssignmentServiceTest {

  @Test
  void allowsValidAssignmentWithinLimits() {
    FulfilmentAssignmentService service = new FulfilmentAssignmentService();

    assertDoesNotThrow(() -> service.assign(new FulfilmentAssignment(1L, 10L, 100L)));
  }

  @Test
  void rejectsMoreThanTwoWarehousesPerProductPerStore() {
    FulfilmentAssignmentService service = new FulfilmentAssignmentService();
    service.assign(new FulfilmentAssignment(1L, 10L, 100L));
    service.assign(new FulfilmentAssignment(1L, 10L, 101L));

    assertThrows(IllegalArgumentException.class,
        () -> service.assign(new FulfilmentAssignment(1L, 10L, 102L)));
  }

  @Test
  void rejectsMoreThanThreeWarehousesPerStore() {
    FulfilmentAssignmentService service = new FulfilmentAssignmentService();
    service.assign(new FulfilmentAssignment(1L, 10L, 100L));
    service.assign(new FulfilmentAssignment(2L, 10L, 101L));
    service.assign(new FulfilmentAssignment(3L, 10L, 102L));

    assertThrows(IllegalArgumentException.class,
        () -> service.assign(new FulfilmentAssignment(4L, 10L, 103L)));
  }

  @Test
  void rejectsMoreThanFiveProductsPerWarehouse() {
    FulfilmentAssignmentService service = new FulfilmentAssignmentService();
    service.assign(new FulfilmentAssignment(1L, 10L, 100L));
    service.assign(new FulfilmentAssignment(2L, 11L, 100L));
    service.assign(new FulfilmentAssignment(3L, 12L, 100L));
    service.assign(new FulfilmentAssignment(4L, 13L, 100L));
    service.assign(new FulfilmentAssignment(5L, 14L, 100L));

    assertThrows(IllegalArgumentException.class,
        () -> service.assign(new FulfilmentAssignment(6L, 15L, 100L)));
  }
}
