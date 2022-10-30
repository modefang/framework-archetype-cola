package ${package}.domain.customer.gateway;

import ${package}.domain.customer.Customer;

public interface CustomerGateway {
    Customer getByById(String customerId);
}
