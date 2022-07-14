package com.simpleTests;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({ComputationTest.class, WebInitializeTest.class})
public class TestConfiguration
{
}
