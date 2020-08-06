package component

import com.kalvad.test.Application
import com.kalvad.test.model.entity.Address
import com.kalvad.test.model.entity.Customer
import com.kalvad.test.model.enums.AddressType
import com.kalvad.test.repository.AddressRepository
import com.kalvad.test.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.Matchers.hasSize
import static org.springframework.http.MediaType.APPLICATION_JSON
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(classes = [Application.class])
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerSearchControllerTest extends Specification {

    @Autowired
    private MockMvc mvc

    @Autowired
    private CustomerRepository customerRepository

    @Autowired
    private AddressRepository addressRepository

    def 'when calling customer list endpoint then should retrieve all customers in DB'() {
        given:
        customerRepository.saveAll([
                new Customer(1,
                        'TestFName',
                        'TestLName',
                        'TestPhoneNumber',
                        'TestEmail',
                        []),
                new Customer(2,
                        'TestFName1',
                        'TestLName1',
                        'TestPhoneNumber1',
                        'TestEmail1',
                        []),
                new Customer(3,
                        'TestFName2',
                        'TestLName2',
                        'TestPhoneNumber2',
                        'TestEmail2',
                        [])
        ])
        expect:
        mvc.perform(get('/customer')
                .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath('$', hasSize(3)))
                .andExpect(jsonPath('$[0].id').value(1))
                .andExpect(jsonPath('$[0].firstName').value('TestFName'))
                .andExpect(jsonPath('$[0].lastName').value('TestLName'))
                .andExpect(jsonPath('$[0].phoneNumber').value('TestPhoneNumber'))
                .andExpect(jsonPath('$[0].email').value('TestEmail'))
                .andExpect(jsonPath('$[1].id').value(2))
                .andExpect(jsonPath('$[1].firstName').value('TestFName1'))
                .andExpect(jsonPath('$[1].lastName').value('TestLName1'))
                .andExpect(jsonPath('$[1].phoneNumber').value('TestPhoneNumber1'))
                .andExpect(jsonPath('$[1].email').value('TestEmail1'))
                .andExpect(jsonPath('$[2].id').value(3))
                .andExpect(jsonPath('$[2].firstName').value('TestFName2'))
                .andExpect(jsonPath('$[2].lastName').value('TestLName2'))
                .andExpect(jsonPath('$[2].phoneNumber').value('TestPhoneNumber2'))
                .andExpect(jsonPath('$[2].email').value('TestEmail2'))
    }

    def 'when calling city search endpoint then should retrieve all customers with address for given city'() {
        given:
        customerRepository.saveAll([
                new Customer(1,
                        'TestFName',
                        'TestLName',
                        'TestPhoneNumber',
                        'TestEmail',
                        []),
                new Customer(2,
                        'TestFName1',
                        'TestLName1',
                        'TestPhoneNumber1',
                        'TestEmail1',
                        []),
                new Customer(3,
                        'TestFName2',
                        'TestLName2',
                        'TestPhoneNumber2',
                        'TestEmail2',
                        [])
        ])

        and:
        addressRepository.saveAll([
                new Address(1,
                        AddressType.COMMERCIAL,
                        'TestCity',
                        'TestCountry',
                        'TestAddress',
                        new Customer(1)
                ),
                new Address(2,
                        AddressType.RESIDENTIAL,
                        'TestCity',
                        'TestCountry',
                        'TestAddress',
                        new Customer(2)
                )
                ])
        expect:
        mvc.perform(get('/city/TestCity')
                .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath('$', hasSize(2)))
                .andExpect(jsonPath('$[0].id').value(1))
                .andExpect(jsonPath('$[0].firstName').value('TestFName'))
                .andExpect(jsonPath('$[0].lastName').value('TestLName'))
                .andExpect(jsonPath('$[0].phoneNumber').value('TestPhoneNumber'))
                .andExpect(jsonPath('$[0].email').value('TestEmail'))
                .andExpect(jsonPath('$[1].id').value(2))
                .andExpect(jsonPath('$[1].firstName').value('TestFName1'))
                .andExpect(jsonPath('$[1].lastName').value('TestLName1'))
                .andExpect(jsonPath('$[1].phoneNumber').value('TestPhoneNumber1'))
                .andExpect(jsonPath('$[1].email').value('TestEmail1'))
    }

    def 'when calling phone prefix search endpoint then should retrieve all customers with given phone prefix for given city'() {
        given:
        customerRepository.saveAll([
                new Customer(1,
                        'TestFName',
                        'TestLName',
                        '123456',
                        'TestEmail',
                        []),
                new Customer(2,
                        'TestFName1',
                        'TestLName1',
                        '1111',
                        'TestEmail1',
                        []),
                new Customer(3,
                        'TestFName2',
                        'TestLName2',
                        '123',
                        'TestEmail2',
                        [])
        ])

        expect:
        mvc.perform(get('/phone/123')
                .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath('$', hasSize(2)))
                .andExpect(jsonPath('$[0].id').value(1))
                .andExpect(jsonPath('$[0].firstName').value('TestFName'))
                .andExpect(jsonPath('$[0].lastName').value('TestLName'))
                .andExpect(jsonPath('$[0].phoneNumber').value('123456'))
                .andExpect(jsonPath('$[0].email').value('TestEmail'))
                .andExpect(jsonPath('$[1].id').value(3))
                .andExpect(jsonPath('$[1].firstName').value('TestFName2'))
                .andExpect(jsonPath('$[1].lastName').value('TestLName2'))
                .andExpect(jsonPath('$[1].phoneNumber').value('123'))
                .andExpect(jsonPath('$[1].email').value('TestEmail2'))
    }

    def 'when calling customer get by id endpoint and customer with given id is present then should retrieve detailed customer info'() {
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
                        AddressType.COMMERCIAL,
                        'TestCity',
                        'TestCountry',
                        'TestAddress',
                        new Customer(1)
        ))
        expect:
        mvc.perform(get('/customer/1')
                .contentType(APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath('$.id').value(1))
                .andExpect(jsonPath('$.firstName').value('TestFName'))
                .andExpect(jsonPath('$.lastName').value('TestLName'))
                .andExpect(jsonPath('$.phoneNumber').value('TestPhoneNumber'))
                .andExpect(jsonPath('$.email').value('TestEmail'))
                .andExpect(jsonPath('$.addresses', hasSize(1)))
                .andExpect(jsonPath('$.addresses[0].id').value(1))
                .andExpect(jsonPath('$.addresses[0].addressType').value(AddressType.COMMERCIAL.name()))
                .andExpect(jsonPath('$.addresses[0].city').value('TestCity'))
                .andExpect(jsonPath('$.addresses[0].country').value('TestCountry'))
                .andExpect(jsonPath('$.addresses[0].addressLine').value('TestAddress'))

    }


    def 'when calling customer get by id endpoint and customer is not present then should throw not found error'() {
        given:
        customerRepository.save(
                new Customer(1,
                        'TestFName',
                        'TestLName',
                        'TestPhoneNumber',
                        'TestEmail',
                        [])
        )

        expect:
        mvc.perform(get('/customer/2')
                .contentType(APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath('$.errorCode').value('not.found'))
                .andExpect(jsonPath('$.description').value('Entity not found'))

    }
}
