package com.finconsgroup.performplus.service.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finconsgroup.performplus.rest.api.vm.UtenteFlatVM;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode

public class UtenteDTO extends UtenteEasyDTO implements UserDetails{

	private static final long serialVersionUID = 1L;

	private List<ProfiloDTO> profili;

	@Override
	public Collection<GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		List<ProfiloDTO> items = this.getProfili();

		if (profili != null) {
			for (ProfiloDTO profilo : items) {

				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(profilo.getRuolo().name());

				authorities.add(grantedAuthority);

			}
		}

		return authorities;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return true;
	}

	@Override
	@JsonIgnore
	public String getPassword() {
		return getPasswd();
	}

	@Override
	@JsonIgnore
	public String getUsername() {
		return super.getUsername();
	}

	public UtenteDTO username(String username) {
		setUsername(username);
		return this;
	}

}