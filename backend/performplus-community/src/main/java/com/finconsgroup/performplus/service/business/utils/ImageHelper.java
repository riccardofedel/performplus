package com.finconsgroup.performplus.service.business.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.ImageEntryDTO;

@Component
@Qualifier("ImageHelper")
public class ImageHelper extends AttachHelper{
	private static Logger logger = LoggerFactory.getLogger(ImageHelper.class);

	public byte[] getImage(final ImageEntryDTO imageEntry) throws BusinessException {
		return getAttach(imageEntry, PHOTOS);
	}

	public String toFileName(final String fileName) throws Exception {
		return toFileName(fileName, PHOTOS);
	}

	public File toFile(final ImageEntryDTO imageEntry) throws Exception {
		if (imageEntry == null || imageEntry.getFileName() == null)
			return null;
		return toFile(imageEntry.getFileName());
	}

	public File toFile(final String fileName) throws Exception {
		
		return toFile(fileName, PHOTOS);
	}


	public File createImageFile(final ImageEntryDTO dto, final String suffix) throws Exception {
		String ris = "_img_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		if (dto.getRisorsa() != null) {
			ris = dto.getRisorsa().getId() + "_" + dto.getRisorsa().getNome().trim() + "_"
					+ dto.getRisorsa().getCognome().trim();
		}
		return toFile("image_" + ris + suffix);

	}

	public String createImageFileName(final ImageEntryDTO dto, final String suffix) throws Exception {
		String ris = "_img_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		if (dto.getRisorsa() != null) {
			ris = dto.getRisorsa().getId() + "_" + dto.getRisorsa().getNome().trim() + "_"
					+ dto.getRisorsa().getCognome().trim();
		}
		return "image_" + ris + suffix;

	}

	public boolean isImageAvailable(final ImageEntryDTO imageEntry) {
		return isAttachAvailable(imageEntry, PHOTOS);
	}



	public Date getLastModifyTime(final ImageEntryDTO imageEntry) {
		return getLastModifyTime(imageEntry, PHOTOS);
	}

	public byte[] createNotAvailableImage(final String contentType) throws IOException {
		return createNotAvailableAttach(contentType);
	}



}
