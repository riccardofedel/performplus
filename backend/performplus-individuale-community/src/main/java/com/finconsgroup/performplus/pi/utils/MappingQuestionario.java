package com.finconsgroup.performplus.pi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.finconsgroup.performplus.domain.AmbitoValutazione;
import com.finconsgroup.performplus.domain.Questionario;
import com.finconsgroup.performplus.domain.ValoreAmbito;
import com.finconsgroup.performplus.rest.api.pi.vm.AggiornaQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.AmbitoValutazioneVM;
import com.finconsgroup.performplus.rest.api.pi.vm.CreaNodoQuestionarioRequest;
import com.finconsgroup.performplus.rest.api.pi.vm.NodoQuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.QuestionarioVM;
import com.finconsgroup.performplus.rest.api.pi.vm.TipoNodoQuestionario;
import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.service.business.utils.Mapping;

public class MappingQuestionario {

	public static QuestionarioVM mapping(Questionario q) {
		QuestionarioVM out = Mapping.mapping(q, QuestionarioVM.class);
		if(q.getIncarichi()!=null&& !q.getIncarichi().isEmpty()) {
			out.setIncarichi(q.getIncarichi().stream().map(p->new DecodificaVM(p.getId(),p.getId().toString(),p.getDescrizione())).collect(Collectors.toList()));
		}
		if(q.getCategorie()!=null&& !q.getCategorie().isEmpty()) {
			out.setCategorie(q.getCategorie().stream().map(p->new DecodificaVM(p.getId(),p.getCodice().toString(),p.getDescrizione())).collect(Collectors.toList()));
		}
		return out;
	}
	public static void mapping(Questionario q,QuestionarioVM out) {
		Mapping.mapping(q, out);
		if(q.getIncarichi()!=null&& !q.getIncarichi().isEmpty()) {
			out.setIncarichi(q.getIncarichi().stream().map(p->new DecodificaVM(p.getId(),p.getId().toString(),p.getDescrizione())).collect(Collectors.toList()));
		}
		if(q.getCategorie()!=null&& !q.getCategorie().isEmpty()) {
			out.setCategorie(q.getCategorie().stream().map(p->new DecodificaVM(p.getId(),p.getCodice().toString(),p.getDescrizione())).collect(Collectors.toList()));
		}
	}
	public static List<AmbitoValutazioneVM> toListAmbito(final List<AmbitoValutazione> items) {
		final List<AmbitoValutazioneVM> out = new ArrayList<>();
		if (items == null)
			return out;
		return items.stream().map(a -> mapping(a)).collect(Collectors.toList());
	}

	public static List<AmbitoValutazioneVM> toListVAmbito(final List<ValoreAmbito> items) {
		final List<AmbitoValutazioneVM> out = new ArrayList<>();
		if (items == null)
			return out;
		return items.stream().map(a -> mapping(a)).collect(Collectors.toList());
	}

	public static AmbitoValutazioneVM mapping(AmbitoValutazione a) {
		AmbitoValutazioneVM out = new AmbitoValutazioneVM();
		out.setCodice(a.getCodice());
		out.setCodiceCompleto(a.getCodiceCompleto());
		out.setDescrizione(a.getDescrizione());
		out.setIntestazione(a.getIntestazione());
		out.setPeso(a.getPeso());
		out.setIdPadre(a.getPadre() == null ? null : a.getPadre().getId());
		out.setIdQuestionario(a.getQuestionario() == null ? null : a.getQuestionario().getId());
		out.setId(a.getId());
		out.setIdEnte(a.getIdEnte());
		out.setFoglia(a.isFoglia());
		out.setPesoMancataAssegnazione(a.getPesoMancataAssegnazione());
		out.setPesoMancatoColloquio(a.getPesoMancatoColloquio());
		out.setFlagSoloAdmin(a.getFlagSoloAdmin());
		return out;
	}

	public static AmbitoValutazioneVM mapping(ValoreAmbito a) {
		AmbitoValutazioneVM out = new AmbitoValutazioneVM();
		out.setCodice(a.getCodice());
		out.setCodiceCompleto(a.getAmbitoValutazione().getCodiceCompleto() + "." + a.getCodice());
		out.setDescrizione(a.getDescrizione());
		out.setIntestazione(a.getIntestazione());
		out.setPeso(a.getPeso());
		out.setIdPadre(a.getAmbitoValutazione().getId());
		out.setIdQuestionario(a.getAmbitoValutazione().getQuestionario() == null ? null
				: a.getAmbitoValutazione().getQuestionario().getId());
		out.setId(a.getId());
		out.setIdEnte(a.getAmbitoValutazione().getIdEnte());
		out.setFoglia(true);
		return out;
	}

	public static Questionario mapping(AggiornaQuestionarioRequest request) {
		final Questionario q=new Questionario();
		q.setIdEnte(request.getIdEnte());
		q.setDescrizione(request.getDescrizione());
		q.setIntestazione(request.getIntestazione());
		return q;
	}

	public static NodoQuestionarioVM toNodo(AmbitoValutazione a) {
		return new NodoQuestionarioVM(a.getId(), a.getQuestionario().getId(), a.getCodice(), a.getIntestazione(), a.getDescrizione()
				, a.isFoglia(), a.getPadre()==null?a.getQuestionario().getId(): a.getPadre().getId(), a.getCodiceCompleto(), a.getPeso()
				,a.getPadre()==null?TipoNodoQuestionario.questionario:TipoNodoQuestionario.ambito,
						a.getQuestionario().getIntestazione()).pesoMancataAssegnazione(a.getPesoMancataAssegnazione())
		.pesoMancatoColloquio(a.getPesoMancatoColloquio())
		.flagSoloAdmin(a.getFlagSoloAdmin());
	}

	public static NodoQuestionarioVM toNodo(ValoreAmbito v) {
		return new NodoQuestionarioVM(v.getId(), v.getAmbitoValutazione().getQuestionario().getId(), v.getCodice(), v.getIntestazione(), v.getDescrizione()
				, v.getAmbitoValutazione().getId(), v.getAmbitoValutazione().getCodiceCompleto(), v.getPeso(),
				v.getAmbitoValutazione().getQuestionario().getIntestazione());
	}

	public static NodoQuestionarioVM toNodo(Questionario q) {
		return new NodoQuestionarioVM(q.getId(), q.getIntestazione(), q.getDescrizione());
	}
	public static NodoQuestionarioVM toNodo(QuestionarioVM q) {
		return new NodoQuestionarioVM(q.getId(), q.getIntestazione(), q.getDescrizione());
	}

	public static AmbitoValutazione toAmbito(final Questionario q, final AmbitoValutazione p, final CreaNodoQuestionarioRequest request) {
		AmbitoValutazione a = new AmbitoValutazione();
		a.setCodice(request.getCodice());
		a.setDescrizione(request.getDescrizione());
		a.setFoglia(Boolean.TRUE.equals(request.getFoglia()));
		a.setIdEnte(q.getIdEnte());
		a.setIntestazione(request.getIntestazione());
		a.setPadre(p);
		a.setQuestionario(q);
		a.setPeso(request.getPeso());	
		a.setCodiceCompleto(p == null ? a.getCodice() : p.getCodiceCompleto() + "." + a.getCodice());	
		a.setPesoMancataAssegnazione(request.getPesoMancataAssegnazione());
		a.setPesoMancatoColloquio(request.getPesoMancatoColloquio());
		a.setFlagSoloAdmin(request.getFlagSoloAdmin());
		return a;
	}

	public static ValoreAmbito toValore(AmbitoValutazione p, CreaNodoQuestionarioRequest request) {
		ValoreAmbito v = new ValoreAmbito();
		v.setCodice(request.getCodice());
		v.setDescrizione(request.getDescrizione());
		v.setIntestazione(request.getIntestazione());
		v.setAmbitoValutazione(p);
		v.setPeso(request.getPeso());
		return v;
	}
}
