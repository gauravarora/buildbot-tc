package com.iontrading.buildbot.powerMockExample;

/**
 * @author A.Jassal
 * @version $Id: $
 */
public class PrivatePartialMockingExample {

    public void methodToTest() {
        methodToMock("input");
    }

    private void methodToMock(String input) {
        // return "REAL VALUE = " + input;
    }

}
