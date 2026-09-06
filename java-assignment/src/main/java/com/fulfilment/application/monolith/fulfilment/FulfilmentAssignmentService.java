package com.fulfilment.application.monolith.fulfilment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FulfilmentAssignmentService {

  private final List<FulfilmentAssignment> assignments = new ArrayList<>();

  public void assign(FulfilmentAssignment assignment) {
    if (assignment == null) {
      throw new IllegalArgumentException("Assignment is required");
    }

    validateProductLimitPerStore(assignment);
    validateStoreLimit(assignment);
    validateProductLimitPerWarehouse(assignment);

    assignments.add(assignment);
  }

  private void validateProductLimitPerStore(FulfilmentAssignment assignment) {
    long count = assignments.stream()
        .filter(existing -> existing.productId.equals(assignment.productId))
        .filter(existing -> existing.storeId.equals(assignment.storeId))
        .count();

    if (count >= 2) {
      throw new IllegalArgumentException("Each product can be fulfilled by a maximum of 2 warehouses per store");
    }
  }

  private void validateStoreLimit(FulfilmentAssignment assignment) {
    long count = assignments.stream()
        .filter(existing -> existing.storeId.equals(assignment.storeId))
        .count();

    if (count >= 3) {
      throw new IllegalArgumentException("Each store can be fulfilled by a maximum of 3 warehouses");
    }
  }

  private void validateProductLimitPerWarehouse(FulfilmentAssignment assignment) {
    long count = assignments.stream()
        .filter(existing -> existing.warehouseId.equals(assignment.warehouseId))
        .count();

    if (count >= 5) {
      throw new IllegalArgumentException("Each warehouse can store maximally 5 types of products");
    }
  }
}
