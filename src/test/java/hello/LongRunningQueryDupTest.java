package hello;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LongRunningQueryDupTest {
    private final Logger logger = LoggerFactory.getLogger(LongRunningQueryDupTest.class);

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
    public void findsByExample() throws Exception {
        long startTime = System.currentTimeMillis();

        List<Customer> result = repository.findByFirstName(this.getClass().getCanonicalName());

        // Simulating long tests to demonstrate Circle CI test parallelization feature
        for (Customer customer : result) {
            Thread.sleep(TestConstant.MAX_WAIT_BETWEEN_ACTIONS.value());
            System.out.println(customer.toString());
        }

        assertThat(result.size()).isGreaterThanOrEqualTo(TestConstant.MAX_CUSTOMERS.value());

        long endTime = System.currentTimeMillis();
        System.out.println("***** For " + TestConstant.MAX_CUSTOMERS.value() + " customers & wait time of "
                + (float) (TestConstant.MAX_WAIT_BETWEEN_ACTIONS.value())/1000 + " seconds *****");
        System.out.println("*****     Elapsed time is " +(endTime - startTime)/1000 + " seconds");
    }

}