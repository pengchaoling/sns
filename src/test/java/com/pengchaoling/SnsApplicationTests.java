package com.pengchaoling;

import com.pengchaoling.model.User;
import com.pengchaoling.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SnsApplication.class)
@WebAppConfiguration
public class SnsApplicationTests {
	@Autowired
	UserService UserService;
	@Test
	public void contextLoads() {

		List<User> users= UserService.getUsers(0,10);

	}

}
