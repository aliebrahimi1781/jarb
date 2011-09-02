/*
 * (C) 2011 Nidera (www.nidera.com). All rights reserved.
 */
package org.jarb.violation.factory;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RegexViolationMatchingStrategyTest {

    private RegexViolationMatchingStrategy strategy;

    @Before
    public void setUp() {
        strategy = new RegexViolationMatchingStrategy();
    }

    @Test
    public void testMatch() {
        assertTrue(strategy.nameMatches("uk_persons_name", "uk_.+_name"));
    }

    @Test
    public void testMismatch() {
        assertFalse(strategy.nameMatches("uk_persons_name", "fk_.+"));
    }

    @Test
    public void testMixedCasing() {
        assertTrue(strategy.nameMatches("UK_PERSONS_NAME", "uk_.+_name"));
        strategy.setCaseSensitive(true);
        assertFalse(strategy.nameMatches("UK_PERSONS_NAME", "uk_.+_name"));
    }

}
