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

import com.finconsgroup.performplus.domain.Allegato;
import com.finconsgroup.performplus.repository.AllegatoRepository;
import com.finconsgroup.performplus.service.business.IAllegatoBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.ImageHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.AllegatoDTO;

@Service
@Transactional
public class AllegatoBusiness implements IAllegatoBusiness {

	@Autowired
	private AllegatoRepository allegatoManager;

	private ImageHelper imageHelper = new ImageHelper();

	@Override
	public AllegatoDTO crea(AllegatoDTO dto) throws BusinessException {
		dto.setId(null);
		return Mapping.mapping(allegatoManager.save(Mapping.mapping(dto, Allegato.class)), AllegatoDTO.class);
	}

	@Override
	public AllegatoDTO aggiorna(AllegatoDTO dto) throws BusinessException {
		return Mapping.mapping(allegatoManager.save(Mapping.mapping(dto, Allegato.class)), AllegatoDTO.class);
	}

	@Override
	public void elimina(AllegatoDTO dto) throws BusinessException {
		allegatoManager.deleteById(dto.getId());
	}

	@Override
	public void elimina(Long id) throws BusinessException {
		allegatoManager.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public AllegatoDTO leggi(Long id) throws BusinessException {
		return Mapping.mapping(allegatoManager.findById(id), AllegatoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllegatoDTO> cerca(AllegatoDTO parametri) throws BusinessException {
		return Mapping.mapping(allegatoManager.findAll(Example.of(Mapping.mapping(parametri, Allegato.class))),
				AllegatoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AllegatoDTO> list(Long idEnte) throws BusinessException {
		return Mapping.mapping(allegatoManager.findByIdEnte(idEnte), AllegatoDTO.class);
	}

	@Override
	@Transactional(readOnly = true)
	public int count(Long idEnte) throws BusinessException {
		return allegatoManager.countByIdEnte(idEnte);
	}

	@Override
	@Transactional(readOnly = true)
	public AllegatoDTO leggiPerNome(Long idEnte, String nome) throws BusinessException {
		return Mapping.mapping(allegatoManager.findByIdEnteAndNome(idEnte, nome), AllegatoDTO.class);
	}

	@Override
	public AllegatoDTO save(AllegatoDTO allegato, byte[] imageData) throws BusinessException {
		try {
			String suffix = ImageHelper.suffix(allegato.getContentType());
			String fileName = createImageFileName(allegato, suffix);
			File newFile = imageHelper.toFile(fileName, ImageHelper.ATTACH);
			allegato.setFileName(fileName);
			OutputStream outStream = new FileOutputStream(newFile);
			outStream.write(imageData);
			outStream.close();

		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(e);
		}
		Allegato a = allegatoManager.findByIdEnteAndNome(allegato.getIdEnte(), allegato.getNome());

		if (a == null)
			a = allegatoManager.save(Mapping.mapping(allegato, Allegato.class));
		else {
			a.setFileName(allegato.getFileName());
			a.setContentType(allegato.getContentType());
			a = allegatoManager.save(a);
		}
		return Mapping.mapping(a, AllegatoDTO.class);
	}

	private String createImageFileName(final AllegatoDTO dto, final String suffix) throws Exception {
		return "allegato_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "." + suffix;
	}

}
