package com.example.testproject;

public class PrimeNumber {
    public boolean isPrime(int num) {
        if (num <= 1) {
            return false; // 0 and 1 are not prime numbers
        }
        for (int i = 2; i <= Math.sqrt(num); i++) { // Loop till sqrt(num)
            if (num % i == 0) {
                return false; // If divisible by any number other than 1 and itself, not prime
            }
        }
        return true; // If no divisors found, it's prime
    }
}
