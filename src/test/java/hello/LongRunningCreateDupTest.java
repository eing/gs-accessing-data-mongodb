package hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LongRunningCreateDupTest {

    private final Logger logger = LoggerFactory.getLogger(LongRunningCreateDupTest.class);

    @Autowired
    CustomerRepository repository;

    @Test
    public void createLoadTest() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int num = 1; num <= TestConstant.MAX_CUSTOMERS.value(); num++) {
            Customer customer = repository.save(new Customer(this.getClass().getCanonicalName(),
                    num + ""));
            // Simulating long tests to demonstrate Circle CI test parallelization feature
            Thread.sleep(TestConstant.MAX_WAIT_BETWEEN_ACTIONS.value());
            assertThat(customer.id).isNotNull();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("***** For " + TestConstant.MAX_CUSTOMERS.value() + " customers & wait time of "
                + (float) (TestConstant.MAX_WAIT_BETWEEN_ACTIONS.value())/1000 + " seconds *****");
        System.out.println("*****     Elapsed time is " +(endTime - startTime)/1000 + " seconds");
   }

}