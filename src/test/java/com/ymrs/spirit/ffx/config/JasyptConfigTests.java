package com.ymrs.spirit.ffx.config;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymrs.spirit.ffx.SpiritApplicationTests;

public class JasyptConfigTests extends SpiritApplicationTests {
	
	@Autowired
	private JasyptConfig jasypt;

	@Test
	public void testEncrypt() {
		String decrypt = jasypt.stringEncryptor().decrypt("ENC@[rDGU93pTlUIbTcYYQJq9cdF4yCP74ykj]");
		System.out.println("======================> " + decrypt);
	}
	
}
