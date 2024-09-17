package com.finconsgroup.performplus.service.business.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.AllegatoIndicatorePiano;
import com.finconsgroup.performplus.repository.AllegatoIndicatorePianoRepository;
import com.finconsgroup.performplus.service.business.IAllegatoIndicatorePianoBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.ImageHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.AllegatoIndicatorePianoDTO;
import com.finconsgroup.performplus.service.dto.IndicatorePianoDTO;

@Service
@Transactional
public class AllegatoIndicatorePianoBusiness implements IAllegatoIndicatorePianoBusiness {

	@Autowired
	private AllegatoIndicatorePianoRepository allegatoManager;

	private ImageHelper imageHelper = new ImageHelper();

	@Override
	
	public AllegatoIndicatorePianoDTO crea(AllegatoIndicatorePianoDTO dto )
			throws BusinessException {
		dto.setId(null);
		return Mapping.mapping(allegatoManager.save(Mapping.mapping(dto,AllegatoIndicatorePiano.class)),AllegatoIndicatorePianoDTO.class);
	}

	@Override
	
	public AllegatoIndicatorePianoDTO aggiorna(AllegatoIndicatorePianoDTO dto)
			throws BusinessException {
		return Mapping.mapping(allegatoManager.save(Mapping.mapping(dto,AllegatoIndicatorePiano.class)),AllegatoIndicatorePianoDTO.class);
	}

	@Override
	
	public void elimina(AllegatoIndicatorePianoDTO dto)
			throws BusinessException {
		 allegatoManager.deleteById(dto.getId());
	}

	@Override
	
	public void elimina(Long id)
			throws BusinessException {
		allegatoManager.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public AllegatoIndicatorePianoDTO leggi(Long id)
			throws BusinessException {
		return Mapping.mapping(allegatoManager.findById(id),AllegatoIndicatorePianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllegatoIndicatorePianoDTO> cerca(AllegatoIndicatorePianoDTO parametri
			) throws BusinessException {
		return Mapping.mapping(allegatoManager.findAll(Example.of(Mapping.mapping(parametri,AllegatoIndicatorePiano.class))),AllegatoIndicatorePianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllegatoIndicatorePianoDTO> list(Long idEnte)
			throws BusinessException {
		return Mapping.mapping(allegatoManager.findByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoDateDeleteIsNull(idEnte),AllegatoIndicatorePianoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte)
			throws BusinessException {
		return allegatoManager.countByIndicatorePianoNodoPianoIdEnteAndIndicatorePianoNodoPianoDateDeleteIsNull(idEnte);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllegatoIndicatorePianoDTO> leggiPerIndicatorePiano(IndicatorePianoDTO indicatorePiano)
			throws BusinessException {
		return Mapping.mapping(allegatoManager.findByIndicatorePianoId(indicatorePiano.getId()),AllegatoIndicatorePianoDTO.class);
	}

	@Override
	public AllegatoIndicatorePianoDTO save(AllegatoIndicatorePianoDTO allegato, byte[] imageData
			) throws BusinessException {
		try {
			String fileName = allegato.getFileName();
			File newFile = imageHelper.toFile(fileName,ImageHelper.ATTACH);
			OutputStream outStream = new FileOutputStream(newFile);
			outStream.write(imageData);
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
		AllegatoIndicatorePiano a = allegatoManager.findByIndicatorePianoIdAndNome(allegato.getIndicatorePiano().getId(),
				allegato.getNome());

		if (a == null) {
			allegato.setId(null);
			a = allegatoManager.save(Mapping.mapping(allegato,AllegatoIndicatorePiano.class));
		}
		else {
			a.setFileName(allegato.getFileName());
			a.setContentType(allegato.getContentType());
			a = allegatoManager.save(a);
		}
		return Mapping.mapping(a,AllegatoIndicatorePianoDTO.class);
	}

	private String createImageFileName(AllegatoIndicatorePianoDTO dto, String suffix)
			throws Exception {
		return "allegato_" + dto.getIndicatorePiano().getId()+"_"+new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
	}

	
}
