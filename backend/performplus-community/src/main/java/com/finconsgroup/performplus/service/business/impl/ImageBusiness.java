package com.finconsgroup.performplus.service.business.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finconsgroup.performplus.domain.ImageEntry;
import com.finconsgroup.performplus.repository.ImageEntryRepository;
import com.finconsgroup.performplus.service.business.IImageBusiness;
import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.business.utils.ImageHelper;
import com.finconsgroup.performplus.service.business.utils.Mapping;
import com.finconsgroup.performplus.service.dto.ImageEntryDTO;

@Service
@Transactional
public class ImageBusiness implements IImageBusiness {
    
    @Autowired
    private ImageEntryRepository imageEntryManager;
    
    private ImageHelper imageHelper=new ImageHelper();

    @Override

    public ImageEntryDTO crea(ImageEntryDTO dto)
	    throws BusinessException {
    	dto.setId(null);
	return Mapping.mapping(imageEntryManager.save(Mapping.mapping(dto,ImageEntry.class)),ImageEntryDTO.class);
    }

    @Override
    
    public ImageEntryDTO aggiorna(ImageEntryDTO dto)
	    throws BusinessException {
	return Mapping.mapping(imageEntryManager.save(Mapping.mapping(dto,ImageEntry.class)),ImageEntryDTO.class);
    }

    @Override
    
    public void elimina(ImageEntryDTO dto)
	    throws BusinessException {
	 imageEntryManager.deleteById(dto.getId());
    }

    @Override
    
    public void elimina(Long id)
	    throws BusinessException {
	 imageEntryManager.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ImageEntryDTO leggi(Long id)
	    throws BusinessException {
	return Mapping.mapping(imageEntryManager.findById(id),ImageEntryDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageEntryDTO> cerca(ImageEntryDTO parametri
	    ) throws BusinessException {
	return Mapping.mapping(imageEntryManager.findAll(Example.of(Mapping.mapping(parametri,ImageEntry.class))),ImageEntryDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ImageEntryDTO> list(Long idEnte)
	    throws BusinessException {
	return Mapping.mapping(imageEntryManager.findByIdEnte(idEnte),ImageEntryDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public int count(Long idEnte)
	    throws BusinessException {
	return imageEntryManager.countByIdEnte(idEnte);
    }

    @Override
    @Transactional(readOnly = true)
    public ImageEntryDTO leggiPerRisorsa(Long idRisorsa)
	    throws BusinessException {
	return Mapping.mapping(imageEntryManager.findByRisorsaId(idRisorsa),ImageEntryDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public ImageEntryDTO leggiPerNome(Long idEnte, String nome)
	    throws BusinessException {
	return Mapping.mapping(imageEntryManager.findByIdEnteAndNome(idEnte, nome),ImageEntryDTO.class);
    }


    @Override
    
    public ImageEntryDTO save(ImageEntryDTO imageEntry,
	    byte[] imageData)
	    throws BusinessException {
	try {
	    Long idEnte=imageEntry.getIdEnte();

	    // Get the image's suffix
	    String suffix = null;
	    if ("image/gif".equalsIgnoreCase(imageEntry.getContentType())) {
		suffix = ".gif";
	    } else if ("image/jpeg".equalsIgnoreCase(imageEntry
		    .getContentType())) {
		suffix = ".jpeg";
	    } else if ("image/png"
		    .equalsIgnoreCase(imageEntry.getContentType())) {
		suffix = ".png";
	    }

	    // Create a unique name for the file in the image directory and
	    // write the image data into it.
	    String fileName = imageHelper.createImageFileName(imageEntry, suffix);
	    File newFile=imageHelper.toFile(fileName);
	    imageEntry.setFileName(fileName);
	    // System.out.println(">>>newFile:" + newFile.getAbsolutePath());
	    OutputStream outStream = new FileOutputStream(newFile);
	    outStream.write(imageData);
	    outStream.close();

	} catch (Exception e) {
	    e.printStackTrace();
	    throw new BusinessException(e);
	}
	ImageEntry ie = null;
	if (imageEntry.getRisorsa() != null) {
	    ie = imageEntryManager.findByRisorsaId(imageEntry.getRisorsa().getId());
	} else {
	    ie = imageEntryManager.findByIdEnteAndNome(imageEntry.getIdEnte(),
		    imageEntry.getNome());
	}

	if (ie == null || ie.getId() == null)
	    ie = imageEntryManager.save(Mapping.mapping(imageEntry,ImageEntry.class));
	else {
	    ie.setFileName(imageEntry.getFileName());
	    ie.setContentType(imageEntry.getContentType());
	    ie = imageEntryManager.save(ie);
	}
	return Mapping.mapping(ie,ImageEntryDTO.class);
    }

}
