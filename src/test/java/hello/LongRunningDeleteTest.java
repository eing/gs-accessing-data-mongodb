package hello;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LongRunningDeleteTest {
    private final Logger logger = LoggerFactory.getLogger(LongRunningDeleteTest.class);

    @Autowired
    CustomerRepository repository;

    @Before
    public void setUp() {
        for (int num = 1; num <= TestConstant.MAX_CUSTOMERS.value(); num++) {
            Customer customer = repository.save(new Customer(this.getClass().getCanonicalName(),
                    num + ""));
            assertThat(customer.id).isNotNull();
        }
   }

    @Test
    public void deleteLoadTest() throws Exception {
        long startTime = System.currentTimeMillis();

        List<Customer> result = repository.findByFirstName(this.getClass().getCanonicalName());
        assertThat(result.size()).isGreaterThanOrEqualTo(TestConstant.MAX_CUSTOMERS.value());

        for (Customer customer : result) {
            // Simulating long tests to demonstrate Circle CI test parallelization feature
            Thread.sleep(TestConstant.MAX_WAIT_BETWEEN_ACTIONS.value());
            repository.delete(customer);
            Customer afterDelete = repository.findOne(Example.of(customer));
            assertThat(afterDelete).isNull();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("***** For " + TestConstant.MAX_CUSTOMERS.value() + " customers & wait time of "
                + (float) (TestConstant.MAX_WAIT_BETWEEN_ACTIONS.value())/1000 + " seconds *****");
        System.out.println("*****     Elapsed time is " +(endTime - startTime)/1000 + " seconds");
    }


}