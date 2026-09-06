# Case Study Scenarios to discuss

## Scenario 1: Cost Allocation and Tracking
**Situation**: The company needs to track and allocate costs accurately across different Warehouses and Stores. The costs include labor, inventory, transportation, and overhead expenses.

**Task**: Discuss the challenges in accurately tracking and allocating costs in a fulfillment environment. Think about what are important considerations for this, what are previous experiences that you have you could related to this problem and elaborate some questions and considerations

**Questions you may have and considerations:**
Costs should be allocated using clear, consistent cost drivers. Labor may be driven by hours or handled units, transportation by distance, weight, volume, or delivery count, inventory by storage space and days held, and overhead by an agreed activity-based allocation. The model should distinguish fixed and variable costs and avoid allocating shared costs twice.

Before defining the solution, I would clarify the required reporting grain: location, warehouse, store, product, order, or business unit. I would also confirm how to handle shared warehouses, returns, transfers, damaged stock, stock adjustments, and costs that arrive later than the operational event. Each cost record should have a source, timestamp, currency, allocation rule, and audit trail so that totals can be reconciled with finance.

The first useful outcome would be a trusted cost baseline and dashboards showing cost per unit, order, product, warehouse, and store. Allocation rules should be versioned because changing a rule must not rewrite historical reporting without an explicit restatement.

## Scenario 2: Cost Optimization Strategies
**Situation**: The company wants to identify and implement cost optimization strategies for its fulfillment operations. The goal is to reduce overall costs without compromising service quality.

**Task**: Discuss potential cost optimization strategies for fulfillment operations and expected outcomes from that. How would you identify, prioritize and implement these strategies?

**Questions you may have and considerations:**
I would start by separating controllable costs from costs that are fixed in the short term. Potential levers include improving warehouse utilization, reducing excess inventory and handling, consolidating transportation, optimizing delivery routes, reducing avoidable transfers, improving labor scheduling, and reviewing supplier or carrier contracts. Service quality must be measured alongside cost through delivery time, availability, accuracy, damage, and customer satisfaction.

To prioritize initiatives, I would establish the current baseline, estimate savings and implementation effort, identify operational and customer risks, and rank opportunities by expected value and confidence. A pilot in one warehouse or store group would allow the company to compare the result with a control period before wider rollout.

Each initiative should have an owner, target metric, expected savings, deadline, and rollback plan. Savings should be measured against a normalized baseline so that seasonal volume changes are not mistaken for improvement. The result should be continuous optimization rather than one-time cost cutting that damages service.

## Scenario 3: Integration with Financial Systems
**Situation**: The Cost Control Tool needs to integrate with existing financial systems to ensure accurate and timely cost data. The integration should support real-time data synchronization and reporting.

**Task**: Discuss the importance of integrating the Cost Control Tool with financial systems. What benefits the company would have from that and how would you ensure seamless integration and data synchronization?

**Questions you may have and considerations:**
Integration with financial systems provides an authoritative view of actual costs, supports reconciliation, and reduces manual spreadsheet work. It also allows operational events such as warehouse activity, store fulfillment, transfers, and inventory changes to be connected to accounting entries and reporting periods.

Before choosing real-time or batch synchronization, I would clarify finance's latency requirements, source-of-truth ownership, chart-of-accounts mapping, tax and currency requirements, correction processes, and expected data volume. The integration should use stable identifiers for warehouses, stores, products, and business units, and should preserve the source transaction ID for traceability.

I would prefer an idempotent integration with a clear event or batch contract, schema versioning, validation, retries, dead-letter handling, monitoring, and reconciliation reports. Failed messages must be replayable without creating duplicate postings. Access should be secured with least privilege, and sensitive financial data should be protected in transit, at rest, and in logs.

## Scenario 4: Budgeting and Forecasting
**Situation**: The company needs to develop budgeting and forecasting capabilities for its fulfillment operations. The goal is to predict future costs and allocate resources effectively.

**Task**: Discuss the importance of budgeting and forecasting in fulfillment operations and what would you take into account designing a system to support accurate budgeting and forecasting?

**Questions you may have and considerations:**
Budgeting and forecasting help the company plan capacity, staffing, inventory, transport, and investment before costs are incurred. They also make deviations visible early enough for managers to act instead of discovering problems after the reporting period.

The design should support budgets by time period, location, warehouse, store, product category, and cost type. Forecasts should use historical actuals together with drivers such as volume, seasonality, promotions, delivery distance, labor rates, capacity, inflation, and planned warehouse changes. It should be possible to compare budget, latest forecast, actuals, and variance with both financial and operational explanations.

I would include scenario planning for changes in demand, capacity, carrier pricing, and warehouse replacement. Forecast versions, assumptions, approvals, ownership, and adjustment history should be retained. Accuracy should be measured over time, while alerts should focus on material variances and actionable exceptions rather than every small fluctuation.

## Scenario 5: Cost Control in Warehouse Replacement
**Situation**: The company is planning to replace an existing Warehouse with a new one. The new Warehouse will reuse the Business Unit Code of the old Warehouse. The old Warehouse will be archived, but its cost history must be preserved.

**Task**: Discuss the cost control aspects of replacing a Warehouse. Why is it important to preserve cost history and how this relates to keeping the new Warehouse operation within budget?

**Questions you may have and considerations:**
Replacement must be treated as a controlled transition, not as a deletion followed by a new record. The archived warehouse's costs, stock movements, utilization, and operational history must remain linked to its historical identity and period of operation. Reusing the Business Unit Code for the new warehouse is useful for business continuity, but the system still needs an immutable warehouse identifier or version so that old and new records cannot be confused.

Before approving the replacement, I would compare the old and new warehouse budgets, capacity, expected volume, labor, transport, lease, equipment, migration, and temporary double-running costs. The transition budget should include one-time costs as well as recurring operating costs, with an owner and approval threshold for overruns.

Reports should support both continuity at the business-unit level and separation by warehouse version. Actual costs from the old warehouse should not be reassigned to the new warehouse merely because the code is reused. I would define cutover dates, reconcile opening stock and balances, monitor service levels during the transition, and review actual-versus-plan costs after go-live.

## Information Needed Before Defining Scope

Across all scenarios, I would first confirm the business objectives, decision owners, reporting deadlines, source systems, data quality, required reporting grain, cost and accounting definitions, compliance requirements, expected volumes, and acceptable data latency. I would also identify the highest-value decisions the tool must support, the service-level metrics that cannot regress, the integration constraints, and how success will be measured. This information would define a focused first release while leaving lower-value automation and advanced forecasting for later phases.

## Instructions for Candidates
Before starting the case study, read the [BRIEFING.md](BRIEFING.md) to quickly understand the domain, entities, business rules, and other relevant details.

**Analyze the Scenarios**: Carefully analyze each scenario and consider the tasks provided. To make informed decisions about the project's scope and ensure valuable outcomes, what key information would you seek to gather before defining the boundaries of the work? Your goal is to bridge technical aspects with business value, bringing a high level discussion; no need to deep dive.
