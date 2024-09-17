package com.finconsgroup.performplus.manager.security;

public interface IEncrypterManager {

    public String encode(String raw);
    
    public boolean matches(String raw,String encoded);
    
}