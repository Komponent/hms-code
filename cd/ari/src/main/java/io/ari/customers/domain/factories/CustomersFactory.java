package io.ari.customers.domain.factories;

import io.ari.bucks.domain.repositories.BucksRepository;
import io.ari.customers.domain.Customer;
import io.ari.customers.domain.exceptions.CustomerExists;
import io.ari.customers.domain.exceptions.CustomerIdCardExists;
import io.ari.customers.domain.exceptions.CustomerIdExists;
import io.ari.customers.domain.exceptions.CustomerMobilePhoneExists;
import io.ari.customers.domain.repositories.CustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomersFactory {

    public Customer createCustomer(String id,
                                   String idCard,
                                   String name,
                                   String lastName,
                                   String mobilePhone) throws CustomerExists {

        verifyNonExistingCustomer(id, idCard, mobilePhone);

        Customer newCustomer = createNewCustomer(id, idCard, mobilePhone, name, lastName);
        createNewBucks(id);

        return newCustomer;
    }

    private Customer createNewCustomer(String id, String idCard, String mobilePhone, String name, String lastName) {
        Customer customer = new Customer(id, idCard);
        customer.setName(name);
        customer.setLastName(lastName);
        customer.setMobilePhone(mobilePhone);

        customersRepository.save(customer);

        return customer;
    }

    private void createNewBucks(String customerId) {
        bucksRepository.createBucks(customerId);
    }

    private void verifyNonExistingCustomer(String customerId, String idCard, String mobilePhone) throws CustomerExists {
        verifyNonExistingCustomerId(customerId);
        verifyNonExistingMobilePhone(mobilePhone);
        verifyNonExistingIdCard(idCard);
    }

    private void verifyNonExistingCustomerId(String customerId) throws CustomerIdExists {
        if (customersRepository.exists(customerId)) {
            throw new CustomerIdExists(customerId);
        }
    }

    private void verifyNonExistingMobilePhone(String mobilePhone) throws CustomerMobilePhoneExists {
        if (customersRepository.findByMobilePhone(mobilePhone).isPresent()) {
            throw new CustomerMobilePhoneExists(mobilePhone);
        }
    }

    private void verifyNonExistingIdCard(String idCard) throws CustomerIdCardExists {
        if (customersRepository.findByIdCard(idCard).isPresent()) {
            throw new CustomerIdCardExists(idCard);
        }
    }

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private BucksRepository bucksRepository;

}