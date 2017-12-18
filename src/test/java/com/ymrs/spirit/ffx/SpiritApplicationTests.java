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
		System.out.println(stringEncryptor.encrypt("root"));
		System.out.println(stringEncryptor.encrypt("jfOnGm0NpvMdkTrfas"));
		System.out.println(stringEncryptor.encrypt("redis"));
		System.out.println(stringEncryptor.encrypt("HiApp@Redis!Uat"));
		System.out.println(stringEncryptor.encrypt("druid"));
		System.out.println(stringEncryptor.encrypt("HiApp@Mon!Druid"));
		System.out.println(stringEncryptor.encrypt("/AppServer/filedata/"));
		
//		li2L66wpNqWp7dCD//cctg==
//		qhI2/5wB6kIgvUHJYgFsr44lxgh1hWc3To2r7Ue3Gsg=
//		olIugiITNUDB3DFIAVzZGg==
//		KOQtXqlUp5IaKgz/YRvWo++0XULlYo9Y
//		w1OI7ZQ/Bca2qN5oML2E+w==
//		kD98BN9qoD1jz6bJUl8iMhedVQWQj+9M
//		4g9oiWwMDfwJzvEm2H+xOWI7vHxqbDjlDXVmAbdyyGg=
	}
}
