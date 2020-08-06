package component

import com.kalvad.test.Application
import com.kalvad.test.model.entity.Address
import com.kalvad.test.model.entity.Customer
import com.kalvad.test.model.enums.AddressType
import com.kalvad.test.repository.AddressRepository
import com.kalvad.test.repository.CustomerRepository
import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static com.kalvad.test.model.enums.AddressType.*
import static groovy.json.JsonOutput.*
import static org.hamcrest.Matchers.hasSize
import static org.springframework.http.MediaType.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [Application.class])
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerActionsControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    @Autowired
    private CustomerRepository customerRepository

    @Autowired
    private AddressRepository addressRepository

    def 'when calling customer create endpoint with valid data then should return no content response and customer is saved in DB'() {
        when:
        def call = mvc.perform(post('/customer')
                .content(toJson(
                        [
                                firstName  : 'TestFirstName',
                                lastName   : 'TestLastName',
                                phoneNumber: 'TestPhoneNumber',
                                email      : 'TestEmail'
                        ]
                ))
                .contentType(APPLICATION_JSON)
        )
        then:
        call.andExpect(status().isNoContent())

        and:
        def customers = customerRepository.findAll()
        customers.size() == 1
        customers.get(0).firstName == 'TestFirstName'
        customers.get(0).lastName == 'TestLastName'
        customers.get(0).phoneNumber == 'TestPhoneNumber'
        customers.get(0).email == 'TestEmail'
    }

    def 'when calling customer create endpoint with invalid request data then should return 400 http status and corresponding error description'() {
        when:
        def call = mvc.perform(post('/customer')
                .content(toJson(
                        [
                                firstName  : firstName,
                                lastName   : lastName,
                                phoneNumber: phoneNumber,
                                email      : email
                        ]
                ))
                .contentType(APPLICATION_JSON)
        )
        then:
        call.andExpect(status().is4xxClientError())
                .andExpect(jsonPath('$.errorCode').value('invalid.request'))
                .andExpect(jsonPath('$.description').value(errorDescription))
        where:
        firstName       | lastName       | phoneNumber       | email       | errorDescription
        ''              | 'TestLastName' | 'TestPhoneNumber' | 'TestEmail' | 'firstName: must not be blank'
        'TestFirstName' | ''             | 'TestPhoneNumber' | 'TestEmail' | 'lastName: must not be blank'
        'TestFirstName' | 'TestLastName' | ''                | 'TestEmail' | 'phoneNumber: must not be blank'
        'TestFirstName' | 'TestLastName' | 'TestPhoneNumber' | ''          | 'email: must not be blank'
        ''              | 'TestLastName' | 'TestPhoneNumber' | ''          | 'firstName: must not be blank, email: must not be blank'
    }

    def 'when calling create address endpoint with valid customer id and valid address request data then should return no content response status'() {
        given:
        customerRepository.save(
                new Customer(1,
                        'TestFName',
                        'TestLName',
                        'TestPhoneNumber',
                        'TestEmail',
                        [])
        )
        when:
        def call = mvc.perform(post('/customer/1/address')
                .content(toJson(
                        [
                                addressType: RESIDENTIAL.name(),
                                city       : 'TestCity',
                                country    : 'TestCountry',
                                addressLine: 'TestAddress'
                        ]
                ))
                .contentType(APPLICATION_JSON)
        )
        then:
        call.andExpect(status().isNoContent())

        and:
        def addresses = addressRepository.findAll()
        addresses.size() == 1
        addresses.get(0).addressLine == 'TestAddress'
        addresses.get(0).city == 'TestCity'
        addresses.get(0).country == 'TestCountry'
        addresses.get(0).addressType == RESIDENTIAL

    }

    def 'when calling create address endpoint with with invalid request data then should return corresponding http status and error description'() {
        given:
        customerRepository.save(
                new Customer(1,
                        'TestFName',
                        'TestLName',
                        'TestPhoneNumber',
                        'TestEmail',
                        [])
        )
        when:
        def call = mvc.perform(post("/customer/${customerId}/address")
                .content(toJson(
                        [
                                addressType: addressType,
                                city       : city,
                                country    : country,
                                addressLine: addressLine
                        ]
                ))
                .contentType(APPLICATION_JSON)
        )
        then:
        call.andExpect(status().is4xxClientError())
                .andExpect(jsonPath('$.errorCode').value(errorCode))
                .andExpect(jsonPath('$.description').value(errorDescription))
        where:
        customerId | addressType | city       | country       | addressLine   | errorCode         | errorDescription
        2          | RESIDENTIAL | 'TestCity' | 'TestCountry' | 'TestAddress' | 'not.found'       | 'Customer not found with given id : 2'
        1          | null        | 'TestCity' | 'TestCountry' | 'TestAddress' | 'invalid.request' | 'addressType: must not be null'
        1          | RESIDENTIAL | ''         | 'TestCountry' | 'TestAddress' | 'invalid.request' | 'city: must not be blank'
        1          | RESIDENTIAL | 'TestCity' | ''            | 'TestAddress' | 'invalid.request' | 'country: must not be blank'
        1          | RESIDENTIAL | 'TestCity' | 'TestCountry' | ''            | 'invalid.request' | 'addressLine: must not be blank'
    }

    def 'when calling delete address endpoint with valid customer id and valid address id then should return no content response status'() {
        given:
        customerRepository.save(
                new Customer(1,
                        'TestFName',
                        'TestLName',
                        'TestPhoneNumber',
                        'TestEmail',
                        [])
        )
        and:
        addressRepository.save(
                new Address(1,
                        COMMERCIAL,
                        'TestCity',
                        'TestCountry',
                        'TestAddress',
                        new Customer(1)
                ))
        when:
        def call = mvc.perform(delete('/customer/1/address/1'))


        then:
        call.andExpect(status().isNoContent())

        and:
        def addresses = addressRepository.findAll()
        addresses.size() == 0

    }

    def 'when calling delete address endpoint with with invalid data then should return corresponding http status and error description'() {
        given:
        customerRepository.save(
                new Customer(1,
                        'TestFName',
                        'TestLName',
                        'TestPhoneNumber',
                        'TestEmail',
                        [])
        )
        when:
        def call = mvc.perform(delete("/customer/${customerId}/address/${addressId}"))
        then:
        call.andExpect(status().is4xxClientError())
                .andExpect(jsonPath('$.errorCode').value('not.found'))
                .andExpect(jsonPath('$.description').value(errorDescription))
        where:
        customerId | addressId | errorDescription
        2          | 1         | 'Customer not found with given id : 2'
        1          | 2         | 'Address not found with given id : 2 for given customer : 1'
    }
}
