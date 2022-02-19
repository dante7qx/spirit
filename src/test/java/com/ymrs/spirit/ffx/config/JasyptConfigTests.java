package com.ymrs.spirit.ffx.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ymrs.spirit.ffx.SpiritApplicationTests;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JasyptConfigTests extends SpiritApplicationTests {
	
	@Autowired
	private JasyptConfig jasypt;

	@Test
	public void testEncrypt() {
		String password = "12345678";
		String encrypt = jasypt.stringEncryptor().encrypt(password);
		log.info("encrypt ======================> {}", encrypt);
		String decrypt = jasypt.stringEncryptor().decrypt(encrypt);
		log.info("decrypt ======================> {}", decrypt);
		
		
	}
	
}
