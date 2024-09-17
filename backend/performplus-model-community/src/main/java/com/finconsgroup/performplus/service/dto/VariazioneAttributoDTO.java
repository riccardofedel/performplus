package com.finconsgroup.performplus.service.dto;

import com.finconsgroup.performplus.enumeration.TipoVariazione;

import lombok.Data;
import lombok.EqualsAndHashCode;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode

public class VariazioneAttributoDTO extends AuditEntityDTO implements
		EnteInterface {
	
	private String oggetto;
	private String attributo;
	private String identificativo;
	private String valore;
	private String valorePrecedente;
	private TipoVariazione tipoVariazione;
	private Long idEnte = 0l;
	private Integer anno;
	private Long idPiano;
	private NodoPianoDTO nodoPiano;

	public VariazioneAttributoDTO() {
		super();
	}

	public VariazioneAttributoDTO(Long idEnte, Integer anno, Long idPiano, String oggetto,
			String identificativo, String attributo) {
		this(idEnte, anno,idPiano, oggetto, identificativo, attributo, null);
	}

	public VariazioneAttributoDTO(Long idEnte, Integer anno, Long idPiano, String oggetto,
			String identificativo, String attributo,
			TipoVariazione tipoVariazione) {
		super();
		setIdEnte(idEnte);
		setAnno(anno);
		setIdPiano(idPiano);
		setOggetto(oggetto);
		setIdentificativo(identificativo);
		setTipoVariazione(tipoVariazione);
		setAttributo(attributo);
	}

//	public VariazioneAttributoDTO(Long idEnte, Long idPiano, Object dto,
//			Long id, String attributo) {
//		this(idEnte, idPiano, VariazioneHelper.oggetto(dto), id.toString(),
//				attributo);
//	}
//
//	public VariazioneAttributoDTO(Long idEnte, Long idPiano,
//			EntityDTO dto, String attributo) {
//		this(idEnte, idPiano, dto, dto.getId(), attributo);
//	}

	public VariazioneAttributoDTO anno(Integer anno) {
		setAnno(anno);
		return this;
	}
	

}
