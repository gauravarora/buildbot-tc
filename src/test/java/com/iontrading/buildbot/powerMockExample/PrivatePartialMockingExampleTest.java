package com.iontrading.buildbot.powerMockExample;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.iontrading.buildbot.powerMockExample.PrivatePartialMockingExample;

import static org.powermock.api.mockito.PowerMockito.*;

/**
 * @author A.Jassal
 * @version $Id: $
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PrivatePartialMockingExample.class)
public class PrivatePartialMockingExampleTest {
    @Test
    public void demoPrivateMethodMocking() throws Exception {
        final String expected = "TEST VALUE";
        final String nameOfMethodToMock = "methodToMock";
        final String input = "input";

        PrivatePartialMockingExample underTest = spy(new PrivatePartialMockingExample());

        /*
         * Setup the expectation to the private method using the method name
         */
        when(underTest, nameOfMethodToMock, input);

        // Assert.assertEquals(expected, underTest.methodToTest());

        // Optionally verify that the private method was actually called
        verifyPrivate(underTest).invoke(nameOfMethodToMock, input);
    }
}
