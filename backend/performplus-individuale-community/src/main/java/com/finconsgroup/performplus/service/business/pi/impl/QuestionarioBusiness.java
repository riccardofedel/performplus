package com.finconsgroup.performplus.service.business.pi.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.finconsgroup.performplus.domain.AmbitoValutazione;
import com.finconsgroup.performplus.domain.CategoriaRisorsaUmana;
import com.finconsgroup.performplus.domain.Incarico;
import com.finconsgroup.performplus.domain.Questionario;
import com.finconsgroup.performplus.domain.ValoreAmbito;
import com.finconsgroup.performplus.pi.utils.MappingQuestionario;
import com.finconsgroup.performplus.repository.AmbitoValutazioneRepository;
import com.finconsgroup.performplus.repository.CategoriaRisorsaUmanaRepository;
import com.finconsgroup.performplus.repository.IncaricoRepository;
import com.finconsgroup.performplus.repository.QuestionarioRepository;
import com.finconsgroup.performplus.repository.ValoreAmbitoRepository;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaNodoQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaNodoQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.DetailNodoQuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.NodoQuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.ParametriRiceraQuestionario;
import com.finconsgroup.performplus.rest.api.pi.vm.PreparaNodoQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.QuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.TipoNodoQuestionario;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.pi.IQuestionarioBusiness;
import com.finconsgroup.performplus.service.business.utils.Mapping;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuestionarioBusiness implements IQuestionarioBusiness {
	@Autowired
	private QuestionarioRepository questionarioRepository;
	@Autowired
	private AmbitoValutazioneRepository ambitoValutazioneRepository;
	@Autowired
	private ValoreAmbitoRepository valoreAmbitoRepository;
	@Autowired
	private IncaricoRepository incaricoRepository;
	@Autowired
	private CategoriaRisorsaUmanaRepository categoriaRisorsaUmanaRepository;

	@Override
	@Transactional(readOnly = true)
	public QuestionarioVM leggi(Long idQuestionario) throws BusinessException {
		Questionario q = questionarioRepository.findById(idQuestionario).orElseThrow(
				() -> new BusinessException(HttpStatus.BAD_REQUEST, "questionario non trovato:" + idQuestionario)

		);
		return MappingQuestionario.mapping(q);
	}


	private Questionario crea(CreaNodoQuestionarioRequest request) throws BusinessException {
		Questionario q = MappingQuestionario.mapping(request);
		verificaIntestazioneQuestionario(request.getIdEnte(), request.getIntestazione(), null);
		if (request.getIncarichi() != null && !request.getIncarichi().isEmpty()) {
			final List<Incarico> incarichi = new ArrayList<>();
			for (Long id : request.getIncarichi()) {
				Optional<Incarico> op = incaricoRepository.findById(id);
				op.ifPresent(p -> incarichi.add(p));
			}
			q.setIncarichi(incarichi);
		}
		if (request.getCategorie() != null && !request.getCategorie().isEmpty()) {
			final List<CategoriaRisorsaUmana> categorie = new ArrayList<>();
			for (Long id : request.getCategorie()) {
				Optional<CategoriaRisorsaUmana> op = categoriaRisorsaUmanaRepository.findById(id);
				op.ifPresent(p -> categorie.add(p));
			}
			q.setCategorie(categorie);
		}
		return questionarioRepository.save(q);

	}

	@Override
	public QuestionarioVM crea(CreaQuestionarioRequest request) throws BusinessException {
		Questionario q = MappingQuestionario.mapping(request);
		verificaIntestazioneQuestionario(request.getIdEnte(), request.getIntestazione(), null);
		if (request.getIncarichi() != null && !request.getIncarichi().isEmpty()) {
			final List<Incarico> incarichi = new ArrayList<>();
			for (Long id : request.getIncarichi()) {
				Optional<Incarico> op = incaricoRepository.findById(id);
				op.ifPresent(p -> incarichi.add(p));
			}
			q.setIncarichi(incarichi);
		}
		if (request.getCategorie() != null && !request.getCategorie().isEmpty()) {
			final List<CategoriaRisorsaUmana> categorie = new ArrayList<>();
			for (Long id : request.getCategorie()) {
				Optional<CategoriaRisorsaUmana> op = categoriaRisorsaUmanaRepository.findById(id);
				op.ifPresent(p -> categorie.add(p));
			}
			q.setCategorie(categorie);
		}
		q = questionarioRepository.save(q);
		return MappingQuestionario.mapping(q);
	}

	@Override
	public void aggiorna(Long idQuestionario, AggiornaQuestionarioRequest request) throws BusinessException {
		Questionario q = questionarioRepository.findById(idQuestionario).orElseThrow(
				() -> new BusinessException(HttpStatus.BAD_REQUEST, "questionario non trovato:" + idQuestionario));
		aggiornaQuestionario(q, request);
	}

	private void aggiornaQuestionario(Questionario q, AggiornaQuestionarioRequest request) throws BusinessException {
		verificaIntestazioneQuestionario(q.getIdEnte(), request.getIntestazione(), q.getIntestazione());
		q.setIntestazione(request.getIntestazione());
		q.setDescrizione(request.getDescrizione());
		if (request.getIncarichi() != null && !request.getIncarichi().isEmpty()) {
			final List<Incarico> incarichi = new ArrayList<>();
			for (Long id : request.getIncarichi()) {
				Optional<Incarico> op = incaricoRepository.findById(id);
				op.ifPresent(p -> incarichi.add(p));
			}
			q.setIncarichi(incarichi);
		}
		if (request.getCategorie() != null && !request.getCategorie().isEmpty()) {
			final List<CategoriaRisorsaUmana> categorie = new ArrayList<>();
			for (Long id : request.getCategorie()) {
				Optional<CategoriaRisorsaUmana> op = categoriaRisorsaUmanaRepository.findById(id);
				op.ifPresent(p -> categorie.add(p));
			}
			q.setCategorie(categorie);
		}
		questionarioRepository.save(q);
	}

	@Override
	public void elimina(Long idQuestionario) throws BusinessException {
		Questionario q = questionarioRepository.findById(idQuestionario).orElseThrow(
				() -> new BusinessException(HttpStatus.BAD_REQUEST, "questionario non trovato:" + idQuestionario));
		valoreAmbitoRepository.deleteByAmbitoValutazioneQuestionario(q);
		ambitoValutazioneRepository.deleteByQuestionario(q);
		questionarioRepository.deleteById(q.getId());
	}

	
	@Override
	@Transactional(readOnly = true)
	public List<NodoQuestionarioVM> search(ParametriRiceraQuestionario parametri) throws BusinessException {
		List<AmbitoValutazione> ambiti = null;
		List<ValoreAmbito> valori = null;
		List<Questionario> questionari = null;
		final List<NodoQuestionarioVM> ramo = new ArrayList<>();
		ramo.add(new NodoQuestionarioVM().tipo(TipoNodoQuestionario.root).Livello(0).intestazione("ROOT").id(-1l)
				.nomeQuestinario(""));
		questionari = questionarioRepository.findByIdEnte(parametri.getIdEnte());
		if (questionari == null || questionari.isEmpty())
			return ramo;
		if (!StringUtils.isBlank(parametri.getTesto()))
			questionari.removeIf(q -> !q.getIntestazione().equalsIgnoreCase(parametri.getTesto()));
		if (questionari.isEmpty())
			return ramo;
		ramo.addAll(questionari.stream().map(m -> MappingQuestionario.toNodo(m)).collect(Collectors.toList()));

		List<Long> ids = questionari.stream().map(q -> q.getId()).collect(Collectors.toList());
		ambiti = ambitoValutazioneRepository.findByQuestionarioIdInOrderByCodiceCompleto(ids);
		if (ambiti != null) {
			ramo.addAll(ambiti.stream().map(m -> MappingQuestionario.toNodo(m)).collect(Collectors.toList()));
		}
		valori = valoreAmbitoRepository
				.findByAmbitoValutazioneQuestionarioIdInOrderByAmbitoValutazioneCodiceCompletoAscCodiceAsc(ids);
		if (valori != null) {
			ramo.addAll(valori.stream().map(v -> MappingQuestionario.toNodo(v)).collect(Collectors.toList()));
		}
		final Comparator<NodoQuestionarioVM> codeComparator = (b1, b2) -> comparator(b1, b2);
		return ramo.stream().sorted(codeComparator).collect(Collectors.toList());

	}

	private int comparator(NodoQuestionarioVM b1, NodoQuestionarioVM b2) {
		int c = b1.getNomeQuestionario().compareTo(b2.getNomeQuestionario());
		if (c == 0)
			b1.getCodiceCompleto().compareTo(b2.getCodiceCompleto());
		return c;
	}

	@Override
	@Transactional(readOnly = true)
	public List<NodoQuestionarioVM> ramo(TipoNodoQuestionario tipo, Long idNodo) throws BusinessException {
		Terna t = leggi(tipo, idNodo);
		List<AmbitoValutazione> items = null;
		List<ValoreAmbito> valori = null;
		final List<NodoQuestionarioVM> ramo = new ArrayList<>();
		switch (tipo) {
			case questionario:
				ramo.add(MappingQuestionario.toNodo(t.q));
				items = ambitoValutazioneRepository.findByQuestionarioIdOrderByCodiceCompleto(t.q.getId());
				valori = valoreAmbitoRepository
						.findByAmbitoValutazioneQuestionarioIdOrderByAmbitoValutazioneCodiceCompletoAscCodiceAsc(
								t.q.getId());
				break;
			case ambito:
				ramo.add(MappingQuestionario.toNodo(t.a));
				items = ambitoValutazioneRepository
						.findByQuestionarioIdAndCodiceCompletoStartsWithOrderByCodiceCompleto(t.q.getId(),
								t.a.getCodiceCompleto() + ".");
				valori = valoreAmbitoRepository
						.findByAmbitoValutazioneOrderByCodice(
								t.a);
				break;
			case valore:
				valori = new ArrayList<>();
				valori.add(t.v);
				break;
			default:
				break;
		}

		if (items != null) {
			ramo.addAll(items.stream().map(m -> MappingQuestionario.toNodo(m)).collect(Collectors.toList()));
		}
		if (valori != null) {
			ramo.addAll(valori.stream().map(v -> MappingQuestionario.toNodo(v)).collect(Collectors.toList()));
		}
		final Comparator<NodoQuestionarioVM> codeComparator = (b1, b2) -> comparator(b1, b2);
		return ramo.stream().sorted(codeComparator).collect(Collectors.toList());

	}

	@Override
	@Transactional(readOnly = true)
	public PreparaNodoQuestionarioRequest preparaNodo(Long idEnte,TipoNodoQuestionario tipoPadre, Long idNodoPadre)
			throws BusinessException {
		final PreparaNodoQuestionarioRequest out = new PreparaNodoQuestionarioRequest();
		if(idNodoPadre==null&& !TipoNodoQuestionario.root.equals(tipoPadre)) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Manca idNodoPadre per tipo "+tipoPadre );
		}
		Terna t = leggi(tipoPadre, idNodoPadre);
		switch (tipoPadre) {
			case root:
				out.setFoglia(false);
				out.setTipoPadre(TipoNodoQuestionario.root);
				out.setIdPadre(-1l);
				out.setTipo(TipoNodoQuestionario.questionario);
				return out;
			case questionario:
				out.setFoglia(false);
				out.setIdPadre(t.q.getId());
				out.setTipoPadre(TipoNodoQuestionario.questionario);
				out.setTipo(TipoNodoQuestionario.ambito);
				out.setPeso(BigDecimal.ZERO);
				nextCodice(out);
				return out;
			case ambito:
				out.setIdPadre(t.a.getId());
				out.setTipo(TipoNodoQuestionario.valore);
				out.setPeso(BigDecimal.ZERO);
				out.setTipoPadre(TipoNodoQuestionario.ambito);
				if (t.a.isFoglia()) {
					out.setTipo(TipoNodoQuestionario.valore);
				} else {
					out.setFoglia(false);
					out.setTipo(TipoNodoQuestionario.ambito);
				}
				nextCodice(out);
				out.setCodiceCompleto(t.a.getCodiceCompleto());
				return out;
			default:
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Tipo padre non valido " + tipoPadre);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public DetailNodoQuestionarioVM leggiNodo(final Long idEnte, final TipoNodoQuestionario tipo, final Long idNodo)
			throws BusinessException {
		DetailNodoQuestionarioVM out = null;
		switch (tipo) {
			case root:
				out = new DetailNodoQuestionarioVM();
				out.setId(-1l);
				out.setLivello(0);
				out.setIntestazione("ROOT");
				out.setTipo(TipoNodoQuestionario.root);
				List<Questionario>questionari=questionarioRepository.findByIdEnteOrderByIntestazione(idEnte);
				if (questionari != null)
					out.setFigli(questionari.stream().map(c -> MappingQuestionario.toNodo(c)).collect(Collectors.toList()));
				break;
			case questionario:
				Questionario q = questionarioRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Questionario non trovato:" + idNodo));
				out = Mapping.mapping(MappingQuestionario.toNodo(q), DetailNodoQuestionarioVM.class);
				if(q.getIncarichi()!=null&& !q.getIncarichi().isEmpty()) {
					out.setIncarichi(q.getIncarichi().stream().map(p->new DecodificaVM(p.getId(),p.getId().toString(),p.getDescrizione())).collect(Collectors.toList()));
				}
				if(q.getCategorie()!=null&& !q.getCategorie().isEmpty()) {
					out.setCategorie(q.getCategorie().stream().map(p->new DecodificaVM(p.getId(),p.getCodice().toString(),p.getDescrizione())).collect(Collectors.toList()));
				}
				out.setPeso(BigDecimal.valueOf(100));
				List<AmbitoValutazione> ambiti = ambitoValutazioneRepository.findByQuestionarioOrderByCodice(q);
				if (ambiti != null)
					out.setFigli(ambiti.stream().map(c -> MappingQuestionario.toNodo(c)).collect(Collectors.toList()));
				break;
			case ambito:
				AmbitoValutazione a = ambitoValutazioneRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Ambito non trovato:" + idNodo));
				out = Mapping.mapping(MappingQuestionario.toNodo(a), DetailNodoQuestionarioVM.class);
				List<ValoreAmbito> valori = valoreAmbitoRepository.findByAmbitoValutazioneOrderByCodice(a);
				if (valori != null)
					out.setFigli(valori.stream().map(c -> MappingQuestionario.toNodo(c)).collect(Collectors.toList()));
				break;
			case valore:
				ValoreAmbito v = valoreAmbitoRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Valore non trovato:" + idNodo));
				out = Mapping.mapping(MappingQuestionario.toNodo(v), DetailNodoQuestionarioVM.class);
				break;
		}
		return out;
	}

	@Override
	public void eliminaNodo(TipoNodoQuestionario tipo, Long idNodo) throws BusinessException {
		switch (tipo) {
			case questionario:
				Questionario q = questionarioRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Questionario non trovato:" + idNodo));
				valoreAmbitoRepository.deleteByAmbitoValutazioneQuestionario(q);
				ambitoValutazioneRepository.deleteByQuestionario(q);
				questionarioRepository.deleteById(q.getId());
				break;
			case ambito:
				AmbitoValutazione a = ambitoValutazioneRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Ambito non trovato:" + idNodo));
				valoreAmbitoRepository.deleteByAmbitoValutazioneId(a.getId());
				ambitoValutazioneRepository.deleteById(a.getId());
				break;
			case valore:
				ValoreAmbito v = valoreAmbitoRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Valore non trovato:" + idNodo));
				valoreAmbitoRepository.deleteById(v.getId());
				break;
		}

	}

	@Override
	public void aggiornaNodo(TipoNodoQuestionario tipoNodo, Long idNodo, AggiornaNodoQuestionarioRequest request)
			throws BusinessException {
		Terna t = leggi(tipoNodo, idNodo);
		switch (tipoNodo) {
			case questionario:
				verificaIntestazioneQuestionario(t.q.getIdEnte(), request.getIntestazione(), t.q.getIntestazione());
				aggiornaQuestionario(t.q, request);
				break;
			case ambito:
				verificaCodiceAmbito(t.q, t.a.getPadre(), request.getCodice(), t.a.getCodice());
				verificaPesoAmbito(t, request.getPeso());
				aggiornaAmbito(t.a, request);
				return;
			case valore:
				verificaCodiceValore(t.a, request.getCodice(), t.v.getCodice());
				verificaPesoValore(t, request.getPeso());
				aggiornaValore(t.v, request);
		}
	}

	@Override
	public NodoQuestionarioVM creaNodo(CreaNodoQuestionarioRequest request) throws BusinessException {
		Questionario q = null;
		AmbitoValutazione p = null;
		Terna t = new Terna();
		switch (request.getTipo()) {
			case questionario:
				verificaIntestazioneQuestionario(request.getIdEnte(), request.getIntestazione(), null);
				return MappingQuestionario.toNodo(crea(request));

			case ambito:
				t = leggi(request.getTipoPadre(), request.getIdPadre());
				q = t.q;
				p = t.a;
				verificaCodiceAmbito(q, p, request.getCodice(), null);
				verificaPesoAmbito(q, p, request.getPeso(),null);
				AmbitoValutazione a = MappingQuestionario.toAmbito(q, p, request);
				a = ambitoValutazioneRepository.save(a);
				return MappingQuestionario.toNodo(a);
			case valore:
				t = leggi(request.getTipoPadre(), request.getIdPadre());
				q = t.q;
				p = t.a;
				verificaCodiceValore(p, request.getCodice(), null);
				verificaPesoValore(p, request.getPeso(),null);
				ValoreAmbito v = MappingQuestionario.toValore(p, request);
				v = valoreAmbitoRepository.save(v);
				return MappingQuestionario.toNodo(v);
		}
		return null;
	}

	private void verificaCodiceValore(AmbitoValutazione p, String codice, String oldCodice) throws BusinessException {
		if (oldCodice != null && codice != null && oldCodice.equals(codice)) {
			return;
		}
		if (ambitoValutazioneRepository.existsByPadreIdAndCodice(p.getId(), codice))
			throw new BusinessException(HttpStatus.BAD_REQUEST,
					"Valore gia' esistente:" + p.getCodiceCompleto() + "." + codice);
	}

	private void verificaCodiceAmbito(Questionario q, AmbitoValutazione p, String codice, String oldCodice)
			throws BusinessException {
		if (oldCodice != null && codice != null && oldCodice.equals(codice)) {
			return;
		}
		if (p == null) {
			if (ambitoValutazioneRepository.existsByQuestionarioIdAndPadreIsNullAndCodice(q.getId(), codice))
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Ambito gia' esistente:" + codice);

		} else {
			if (ambitoValutazioneRepository.existsByPadreIdAndCodice(p.getId(), codice))
				throw new BusinessException(HttpStatus.BAD_REQUEST,
						"Ambito gia' esistente:" + p.getCodiceCompleto() + "." + codice);
		}
	}

	private void verificaIntestazioneQuestionario(Long idEnte, String intestazione, String oldIntestazione)
			throws BusinessException {
		if (oldIntestazione != null && intestazione != null && oldIntestazione.equalsIgnoreCase(intestazione)) {
			return;
		}

		if (questionarioRepository.existsByIdEnteAndIntestazioneIgnoreCase(idEnte, intestazione)>0)
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Intestazione già esistente:" + intestazione);
	}

	@Override
	public void updatePeso(final TipoNodoQuestionario tipoNodo, final Long idNodo, final Float peso)
			throws BusinessException {
		Terna t = leggi(tipoNodo, idNodo);
		final BigDecimal p = BigDecimal.valueOf(peso);
		switch (tipoNodo) {
			case ambito:
				final AmbitoValutazione a = t.a;
				verificaPesoAmbito(t, p);
				ambitoValutazioneRepository.updatePeso(a.getId(), p);
				break;
			case valore:
				ValoreAmbito v = t.v;
				verificaPesoValore(t, p);
				valoreAmbitoRepository.updatePeso(v.getId(), BigDecimal.valueOf(peso));
				break;
			default:
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Tipo nodo non valido " + tipoNodo);
		}
	}

	private void nextCodice(final CreaNodoQuestionarioRequest out) {
		String max;
		if (TipoNodoQuestionario.questionario.equals(out.getTipoPadre())) {
			AmbitoValutazione a = ambitoValutazioneRepository
					.findTopByQuestionarioIdOrderByCodiceDesc(out.getIdPadre());
			max = a == null ? "00" : a.getCodice();
		} else {
			ValoreAmbito v = valoreAmbitoRepository.findTopByAmbitoValutazioneIdOrderByCodiceDesc(out.getIdPadre());
			max = v == null ? "00" : v.getCodice();
		}
		Integer n = null;
		try {
			n = Integer.parseInt(max.trim());
		} catch (NumberFormatException e) {
		}
		if (n != null)
			out.setCodice(StringUtils.leftPad(Integer.toString(n+1), 2, '0'));
	}

	private void aggiornaValore(ValoreAmbito v, AggiornaNodoQuestionarioRequest request) {
		v.setDescrizione(request.getDescrizione());
		v.setIntestazione(request.getIntestazione());
		v.setPeso(request.getPeso());
		v.setCodice(request.getCodice());
		v = valoreAmbitoRepository.save(v);
	}

	private void verificaPesoAmbito(Terna t, BigDecimal p) throws BusinessException{
		verificaPesoAmbito(t.q, t.a.getPadre(), p, t.a.getPeso());
	}

	private void verificaPesoAmbito(final Questionario q, final AmbitoValutazione padre, BigDecimal p, BigDecimal old) throws BusinessException{

		Float peso = p == null ? 0f : p.floatValue();
		if (padre != null) {
			float totP = padre.getPeso() == null ? 0f : padre.getPeso().floatValue();
			BigDecimal sum = ambitoValutazioneRepository.sumPesoByPadreIdAndPesoIsNotNull(padre.getId());
			float tot = (sum == null ? 0f : sum.floatValue()) - (old == null ? 0f : old.floatValue()) + +peso;
			if (tot > totP) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Peso totale superiore a " + totP);
			}
		} else {
			float totP = 100f;
			BigDecimal sum = ambitoValutazioneRepository
					.sumPesoByQuestionarioIdAndPadreIsNullAndPesoIsNotNull(q.getId());
			float tot = (sum == null ? 0f : sum.floatValue()) - (old == null ? 0f : old.floatValue()) + +peso;
			if (tot > totP) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Peso totale superiore a 100");
			}
		}
	}

	private void aggiornaAmbito(AmbitoValutazione a, AggiornaNodoQuestionarioRequest request) throws BusinessException{
		a.setDescrizione(request.getDescrizione());
		a.setFoglia(request.getFoglia());
		a.setIntestazione(request.getIntestazione());
		a.setPeso(request.getPeso());
		a.setPesoMancataAssegnazione(request.getPesoMancataAssegnazione());
		a.setPesoMancatoColloquio(request.getPesoMancatoColloquio());
		a.setFlagSoloAdmin(request.getFlagSoloAdmin());
		ambitoValutazioneRepository.save(a);
	}

	private void verificaPesoValore(final Terna t, final BigDecimal p) throws BusinessException{
		verificaPesoValore(t.a, p, t.v.getPeso());
	}

	private void verificaPesoValore(final AmbitoValutazione a, final BigDecimal p, final BigDecimal old) throws BusinessException{
		float peso = p == null ? 0f : p.floatValue();
		float totP = a.getPeso() == null ? 0f : a.getPeso().floatValue();
		if (peso > totP) {
			throw new BusinessException(HttpStatus.BAD_REQUEST, "Il pseso non può essere superior a "+totP);
		}
	}

	private Terna leggi(final TipoNodoQuestionario tipoNodo, final Long idNodo) throws BusinessException {
		Terna out = new Terna();
		switch (tipoNodo) {
			case root:
				return out;
			case questionario:
				out.q = questionarioRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Questionario non trovato:" + idNodo));
				break;
			case ambito:
				out.a = ambitoValutazioneRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Ambito non trovato:" + idNodo));
				out.q = out.a.getQuestionario();
				break;
			case valore:
				out.v = valoreAmbitoRepository.findById(idNodo).orElseThrow(
						() -> new BusinessException(HttpStatus.BAD_REQUEST, "Valore ambito non trovato:" + idNodo));
				out.a = out.v.getAmbitoValutazione();
				out.q = out.a.getQuestionario();
				break;
			default:
				throw new BusinessException(HttpStatus.BAD_REQUEST, "Tipo nodo non valido " + tipoNodo);
		}
		return out;

	}

	class Terna {

		public Questionario q;
		public AmbitoValutazione a;
		public ValoreAmbito v;
	}
}
