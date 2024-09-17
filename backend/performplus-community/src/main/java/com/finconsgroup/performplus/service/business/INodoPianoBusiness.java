package com.finconsgroup.performplus.service.business;

import java.math.BigDecimal;
import java.util.List;

import com.finconsgroup.performplus.rest.api.vm.DecodificaVM;
import com.finconsgroup.performplus.rest.api.vm.SpostaNodoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.FiltroAlberoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.ModificaDisponibilitaRisorsaRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.RisorsaUmanaListVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.AggiornaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.CreaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoFiglioVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoDetailVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NodoPianoSmartVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.NoteAssessoriRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.CalcoloPesaturaObiettivoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.CalcoloPesaturaObiettivoResponse;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.PesaturaNodoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.pesatura.PesaturaRamoVM;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa.AssociaRisorsaNodoPianoRequest;
import com.finconsgroup.performplus.rest.api.vm.v2.programmazione.risorsa.RisorsaUmanaNodoPianoListVM;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.NodoPianoDTO;
import com.finconsgroup.performplus.service.dto.NodoPianoEasyDTO;
import com.finconsgroup.performplus.service.dto.QuadraturaIndicatoriProg;
import com.finconsgroup.performplus.service.dto.QuadraturaRisorseProg;

import jakarta.validation.Valid;

public interface INodoPianoBusiness {

	public NodoPianoDTO getRamoStampa(Long idNodo) throws BusinessException;
	public void associaRisorse(Long idNodo, List<Long> selezionate) throws BusinessException;
	public String maxCodice(Long idPadre) throws BusinessException;
	public void rimuoviRisorsaUmana(Long idRisorsaUmanaNodoPiano) throws BusinessException;

	public void eliminaNodo(Long idNodo) throws BusinessException;
	public NodoPianoSmartVM getRootOnly(Long idEnte, Integer anno) throws BusinessException;

	public List<NodoPianoSmartVM> ramo(Long id) throws BusinessException;

	List<NodoPianoSmartVM> search(Long idEnte, Integer anno, String testo) throws BusinessException;

	List<NodoPianoSmartVM> children(Long idNodoPiano) throws BusinessException;

	NodoPianoDetailVM read(Long id) throws BusinessException;

	public void elimina(Long id) throws BusinessException;

	public void crea(CreaNodoPianoRequest request) throws BusinessException;

	public void aggiorna(Long idNodo, AggiornaNodoPianoRequest request) throws BusinessException;

	public Boolean isCodiceDuplicato(Long idPadre, String codice) throws BusinessException;

	public CreaNodoPianoRequest prepareDescendant(Long idPadre) throws BusinessException;
	public CreaNodoPianoRequest prepareModifica(Long idNodo) throws BusinessException;

	public List<QuadraturaIndicatoriProg> detailIndicatori(Long id) throws BusinessException;

	public PesaturaRamoVM pesaturaRamo(Long idNodoPiano) throws BusinessException;

	public PesaturaNodoVM pesatura(Long idNodoPiano) throws BusinessException;

	public void salvaPesatura(Long idNodoPiano, CalcoloPesaturaObiettivoRequest request) throws BusinessException;

	public CalcoloPesaturaObiettivoResponse calcolo(Long idNodoPiano, CalcoloPesaturaObiettivoRequest request)
			throws BusinessException;

	public void modificaDisponibilitaRisorsa( ModificaDisponibilitaRisorsaRequest request)throws BusinessException;

	public void associaRisorsa(AssociaRisorsaNodoPianoRequest request)throws BusinessException;

	public List<RisorsaUmanaNodoPianoListVM> getRisorseAssociate( Long idNodoPiano)throws BusinessException;

	public List<RisorsaUmanaListVM> getRisorseAssociabili( Long idNodoPiano, String testo, Boolean esterna, Boolean soloStruttura)throws BusinessException;
	public List<NodoPianoSmartVM> search(FiltroAlberoVM filter)throws BusinessException;
	public List<DecodificaVM> searchProgrammi( Long idEnte,  Integer anno,  String cognome)throws BusinessException;
	public void modificaNoteAssessori(NoteAssessoriRequest request)throws BusinessException;
	List<QuadraturaRisorseProg> detailRisorse(Long idNodoPiano) throws BusinessException;
	public BigDecimal pesoTotaleFigli(Long idNodoPiano)throws BusinessException;
	public List<DecodificaVM> searchResponsabili(Long idEnte,  Integer anno,  String cognome)throws BusinessException;
	public List<DecodificaVM> searchRisultatiAttesi( Long idEnte,  Integer anno,  String testo,
			 Long idProgramma)throws BusinessException;
	public NodoPianoSmartVM leggSmart(Long id)throws BusinessException;
	public NodoPianoEasyDTO padreEasy(Long id)throws BusinessException;
	public NodoPianoSmartVM leggiAreaPerOrdine(Long idEnte, Integer anno, int ordine) throws BusinessException;
	List<NodoFiglioVM> figli(Long idNodoPiano) throws BusinessException;
	public void spostaNodo(SpostaNodoRequest request)throws BusinessException;
	public List<NodoPianoSmartVM> listSposta(@Valid Long id)throws BusinessException;

}
