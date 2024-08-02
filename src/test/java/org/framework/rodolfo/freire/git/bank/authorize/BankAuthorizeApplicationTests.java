package org.framework.rodolfo.freire.git.bank.authorize;

import org.framework.rodolfo.freire.git.bank.authorize.integrate.*;
import org.framework.rodolfo.freire.git.bank.authorize.unit.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class represents a test suite for the Bank Authorize application.
 * It uses the {@link Suite}, {@link SuiteDisplayName} and {@link SelectClasses} annotations
 * to define and organize the unit tests that will be executed together.
 */

@Suite
@SuiteDisplayName("Bank Authorize Test Suite")
@SelectClasses({
        AccountServiceTest.class,
        CardServiceTest.class,
        CustomerServiceTest.class,
        MerchantServiceTest.class,
        TransactionServiceTest.class,
        AccountControllerIntegrationTest.class,
        CardControllerIntegrationTest.class,
        CustomerControllerIntegrationTest.class,
        MerchantControllerIntegrationTest.class,
        TransactionControllerIntegrationTest.class
})
@SpringBootTest
class BankAuthorizeApplicationTests {
}
