package com.example.tech_store.services;

import com.example.tech_store.repository.UserRepository;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Service
@SuppressWarnings("UnstableApiUsage")
public class BloomFilterService {

    private final UserRepository userRepository;
    private BloomFilter<String> emailBloomFilter;

    @Value("${bloomfilter.expected-users}")
    private int expectedUsers;

    @Value("${bloomfilter.false-positive-probability}")
    private double falsePositiveProbability;

    private static final Funnel<CharSequence> STRING_FUNNEL = (from, into) ->
            into.putString(from, StandardCharsets.UTF_8);

    public BloomFilterService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @PostConstruct
    public void init() {
        this.emailBloomFilter = BloomFilter.create(STRING_FUNNEL, expectedUsers, falsePositiveProbability);
        List<String> allEmails = userRepository.findAllEmails();
        allEmails.forEach(emailBloomFilter::put);
    }

    public boolean mightContain(String email) {
        return emailBloomFilter.mightContain(email);
    }

    public void add(String email) {
        emailBloomFilter.put(email);
    }
}
