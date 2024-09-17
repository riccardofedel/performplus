package com.finconsgroup.performplus.rest.api.v2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;


import com.finconsgroup.performplus.enumeration.Ruolo;
import com.finconsgroup.performplus.manager.security.BaseSecurityHelper;
import com.finconsgroup.performplus.manager.security.CustomUsernamePasswordAuthenticationToken;
import com.finconsgroup.performplus.manager.security.jwt.JwtTokenUtil;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.OrganizzazioneSmartVM;
import com.finconsgroup.performplus.rest.api.vm.UtenteVM;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.CambioPasswordRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.JwtAuthenticationRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.JwtAuthenticationResponse;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.ModificaPasswordRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.authentication.WhoIsResponse;
import com.finconsgroup.performplus.service.business.IEnteBusiness;
import com.finconsgroup.performplus.service.business.INodoPianoBusiness;
import com.finconsgroup.performplus.service.business.IOrganizzazioneBusiness;
import com.finconsgroup.performplus.service.business.IPianoBusiness;
import com.finconsgroup.performplus.service.business.IUtenteBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.pi.IRegistrazioneBusiness;
import com.finconsgroup.performplus.service.business.utils.SecurityHelper;
import com.finconsgroup.performplus.service.dto.EnteDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")

public class AuthenticationController {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private IEnteBusiness enteBusiness;

	@Autowired
	private IUtenteBusiness utenteBusiness;

	@Autowired
	private IPianoBusiness pianoBusiness;
	@Autowired
	private INodoPianoBusiness nodoPianoBusiness;
	@Autowired
	private IOrganizzazioneBusiness organizzazioneBusiness;
	@Autowired
	private IRegistrazioneBusiness registrazioneBusiness;
	
	

	@Value("${spring.profiles.active}")
	String profilesActive;

	@Value("${sso.redirect.login}")
	String ssoRedirectLogin;
	@Value("${sso.redirect.error}")
	String ssoRedirectError;
	
	@GetMapping(value = "/logout-sso")
	public RedirectView logoutSSO(RedirectAttributes attributes){
		RedirectView redirectView = new RedirectView(ssoRedirectLogin);
		redirectView.setPropagateQueryParams(true);
		return redirectView;
	}



	@PostMapping(value = "/login")
	public ResponseEntity<JwtAuthenticationResponse> createAuthenticationToken(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response)
			throws AuthenticationException, BusinessException {
		final Authentication authentication = authenticationManager
				.authenticate(new CustomUsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));
		Long idEnte = 0l;
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		if (userDetails instanceof UtenteVM ele) {
			idEnte = ele.getIdEnte();
		}
		String token = null;
		try {
			token = jwtTokenUtil.generateToken(userDetails);
		} catch (Exception e) {
			throw new BusinessException("jwt error:" + e.getMessage());
		}
		response.setHeader(tokenHeader, token);
		return ResponseEntity
				.ok(new JwtAuthenticationResponse(userDetails.getUsername(), ((UtenteVM) userDetails).getAuthorities())
						.token(token).idEnte(idEnte));
	}

	@PostMapping(value = "/logintkn")
	public ResponseEntity<JwtAuthenticationResponse> logintkn(
			@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response)
			throws AuthenticationException, BusinessException {
		
		String token=authenticationRequest.getPassword();
		UtenteVM user=jwtTokenUtil.getUserDetails(token);

		if(jwtTokenUtil.isTokenExpired(token)) {
			throw new BusinessException("token expired");
		}

		final Authentication authentication = new CustomUsernamePasswordAuthenticationToken(user.getUsername(),
				user.getPassword(),user.getAuthorities());
		
		Long idEnte = 0l;
		final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		if (userDetails instanceof UtenteVM ele) {
			idEnte = ele.getIdEnte();
		}

		return ResponseEntity
				.ok(new JwtAuthenticationResponse(userDetails.getUsername(), ((UtenteVM) userDetails).getAuthorities())
						.token(token).idEnte(idEnte));
	}

	@GetMapping(value = "/enti")
	public ResponseEntity<List<DecodificaVM>> enti() {
		final List<DecodificaVM> enti = new ArrayList<>();
		List<EnteDTO> items = enteBusiness.list();
		items.stream().forEach(e -> enti.add(new DecodificaVM(e.getId(), e.getNome(), e.getDescrizione())));
		return ResponseEntity.ok(enti);

	}

	@GetMapping(value = "/whoami")
	public ResponseEntity<WhoIsResponse> whoami(
			@RequestParam(name = "idEnte", required = false, defaultValue = "0") Long idEnte,
			@RequestParam(name = "anno", required = false) Integer year) {
		WhoIsResponse out = new WhoIsResponse();
		Integer anno = SecurityHelper.anno(idEnte, year, pianoBusiness);
		out.setAnno(anno);
		UtenteVM utente = SecurityHelper.utente(utenteBusiness);
		if (utente != null) {
			SecurityHelper.ruolo(utente, utente.getUsername(), out.getAnno(), out);
			BaseSecurityHelper.enablings(out);
			out.setIdRisorsa(SecurityHelper.risorsa(utente, anno));
			if(out.getIdRisorsa()!=null) {
				out.setValutatore(registrazioneBusiness.verificaValutatore(out.getIdRisorsa()));
				out.setValutato(!out.isValutatore());
				out.setReferente(Ruolo.REFERENTE.equals(out.getRuolo()));
			}
			return ResponseEntity.ok(out);
		}
		logger.info("whoami UNAUTHORIZED {}, {}", anno, utente);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

	}


	@GetMapping(value = "/refresh-token")
	public ResponseEntity<JwtAuthenticationResponse> refreshAndGetAuthenticationToken(HttpServletRequest request,
			HttpServletResponse response) {
		String token = request.getHeader(tokenHeader);
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (Boolean.TRUE.equals(jwtTokenUtil.canTokenBeRefreshed(token))) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			response.setHeader(tokenHeader, refreshedToken);
			return ResponseEntity
					.ok(new JwtAuthenticationResponse(userDetails.getUsername(), userDetails.getAuthorities()));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@PutMapping(value = "/cambiaPassword")
	public ResponseEntity<Void> cambiaPassword(@Valid @RequestBody(required = true) CambioPasswordRequest request) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		if (username.equals(request.getUserid())) {
			utenteBusiness.cambiaPassword(request);
			return ResponseEntity.ok(null);
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@Secured({ "AMMINISTRATORE" })
	@PutMapping(value = "/modificaPassword")
	public ResponseEntity<Void> modificaPassword(@Valid @RequestBody(required = true) ModificaPasswordRequest request) {
		utenteBusiness.modificaPassword(request);
		return ResponseEntity.ok(null);
	}

}