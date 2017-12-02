package com.ymrs.spirit.ffx;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiritApplicationTests {
	@Autowired
	@Qualifier("spiritEncryptorBean")
    private StringEncryptor stringEncryptor;
	
	@Test
	public void xx() {
		System.out.println(stringEncryptor.encrypt("iamdante"));
	}
}
