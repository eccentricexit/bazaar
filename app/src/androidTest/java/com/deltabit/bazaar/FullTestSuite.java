package com.deltabit.bazaar;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by rigel on 16-Sep-16.
 */
public class FullTestSuite extends TestSuite {
    public FullTestSuite() {
        super();
    }

    public static Test suite() {
        return new TestSuiteBuilder(FullTestSuite.class)
                .includeAllPackagesUnderHere().build();
    }
}
