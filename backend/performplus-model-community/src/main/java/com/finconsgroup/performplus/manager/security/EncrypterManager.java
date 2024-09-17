package com.finconsgroup.performplus.manager.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class EncrypterManager implements IEncrypterManager {

	private PasswordEncoder encoder;

	public EncrypterManager() {
		encoder = new BCryptPasswordEncoder();
	}

	public String encode(String raw) {
		return  this.encoder.encode(raw);
	}

	public boolean matches(String raw, String encoded) {
		return this.encoder.matches(raw, encoded);
	}

//	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("Leon2203!"));
//	}
}
