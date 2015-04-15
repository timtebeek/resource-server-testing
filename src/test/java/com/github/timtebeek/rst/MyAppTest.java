package com.github.timtebeek.rst;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
public class MyAppTest {
	@Autowired
	private ConfigurableApplicationContext	context;

	@Test
	public void test() {
		assertTrue(context.isActive());
	}
}
