package com.example.testproject;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PrimeNumberTest {

    private PrimeNumber primeNumber;

    @Before
    public void init(){
        primeNumber = new PrimeNumber();
    }

    @Test
    public void testPrime29(){
        boolean result = primeNumber.isPrime(29);
        assertTrue(result);
    }
}