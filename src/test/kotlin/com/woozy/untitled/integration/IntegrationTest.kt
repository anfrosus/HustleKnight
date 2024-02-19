package com.woozy.untitled.integration

import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Sql("classpath:/ddl.sql")
@Sql("classpath:/dml.sql")
abstract class IntegrationTest