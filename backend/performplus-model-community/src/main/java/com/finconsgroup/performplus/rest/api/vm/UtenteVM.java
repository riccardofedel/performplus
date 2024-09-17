package com.finconsgroup.performplus.rest.api.vm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class UtenteVM extends EntityVM implements UserDetails {

	private String username;
	private String nome;
	private boolean admin;
//	private Boolean superAdmin;
	private List<ProfiloVM> profili;
	private RisorsaUmanaSmartVM risorsaUmana;
	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;
	private boolean enabled = true;
	private Integer anno;
	private Long idEnte;
	private String codiceFiscale;

	@JsonIgnore
	@Override
	public Collection<GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		List<ProfiloVM> items = this.getProfili();

		if (profili != null) {
			for (ProfiloVM profilo : items) {

				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(profilo.getRuolo().name());

				authorities.add(grantedAuthority);

			}
		}

		return authorities;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return null;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return username;
	}

	public UtenteVM codiceFiscale(String codiceFiscale) {
		this.codiceFiscale=codiceFiscale;
		return this;
	}
	public UtenteVM username(String username) {
		this.username=username;
		return this;
	}
}