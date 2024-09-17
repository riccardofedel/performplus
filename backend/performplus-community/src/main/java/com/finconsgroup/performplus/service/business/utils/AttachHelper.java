package com.finconsgroup.performplus.service.business.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.finconsgroup.performplus.service.business.errors.BusinessException;
import com.finconsgroup.performplus.service.dto.ImageEntryDTO;

@Component
@Qualifier("AttachHelper")
@Primary
public class AttachHelper {
	public static final String TIFF = "tiff";
	public static final String IMAGE_TIFF = "image/tiff";
	public static final String PNG = "png";
	public static final String GIF = "gif";
	public static final String IMAGE_GIF = "image/gif";
	public static final String PPTX = "pptx";
	public static final String APPLICATION_VND_OPENXMLFORMATS_PRESENTATION = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	public static final String XLSX = "xlsx";
	public static final String APPLICATION_VND_OPENXMLFORMATS_SHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String DOCX = "docx";
	public static final String APPLICATION_VN_OPENXMLFORMATS_DOCUMENT = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	public static final String PPT = "ppt";
	public static final String APPLICATION_VND_MS_POWERPOINT = "application/vnd.ms-powerpoint";
	public static final String XLS = "xls";
	public static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
	public static final String DOC = "doc";
	public static final String APPLICATION_MSWORD = "application/msword";
	public static final String APPLICATION_PDF = "application/pdf";
	public static final String PDF = "pdf";
	public static final String JPEG = "jpeg";
	public static final String IMAGE_PNG = "image/png";
	public static final String IMAGE_JPEG = "image/jpeg";
	public static final String ATTACH = "/attach";
	public static final String WEB_INF = "/WEB-INF";
	public static final String PHOTOS = "/photos";
	public static Logger logger = LoggerFactory.getLogger(AttachHelper.class);

	@Value("${path.files}")
	private String pathFiles;

	public byte[] getAttach(final ImageEntryDTO imageEntry, final String dir) throws BusinessException {
		return getAttach(imageEntry.getFileName(), imageEntry.getContentType(), dir);
	}

	public byte[] getAttachFile(final String filename, final String contentType) throws BusinessException {
		return getAttach(filename, contentType, ATTACH);
	}

	public byte[] getAttach(final String filename, final String contentType, final String dir)
			throws BusinessException {
		try {
			final File f = toFile(filename, dir);
			if (f != null && f.exists()) {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				InputStream inStream = new FileInputStream(f);
				copy(inStream, outStream);
				inStream.close();
				outStream.close();
				return outStream.toByteArray();
			} else {
				return createNotAvailableAttach(contentType);
			}
		} catch (Exception e) {
			throw new BusinessException(e);
		}
	}

	public String toFileName(final String fileName, final String dir) throws BusinessException {
		String out = fileName;
		if (fileName == null)
			throw new BusinessException(HttpStatus.BAD_REQUEST, "fileName vuoto");
		String path = pathFiles;
		if (path == null) {
			path = WEB_INF + dir;
		} else {
			path = path + dir;
		}
		int k = fileName.indexOf(path);
		if (k >= 0) {
			k = k + path.length() + 1;
			if (fileName.length() < k) {
				throw new BusinessException(HttpStatus.BAD_REQUEST, "fileName errato:" + fileName);
			}
			out = fileName.substring(k);
		}
		if (out.startsWith("/"))
			out = out.substring(1);
		return out;
	}

	public File toFile(final ImageEntryDTO imageEntry, final String dir) throws BusinessException {
		if (imageEntry == null || imageEntry.getFileName() == null)
			return null;
		return toFile(imageEntry.getFileName(), dir);
	}

	public File toFile(final String fileName, final String dir) throws BusinessException {
		if (fileName == null)
			return null;
		String name = toFileName(fileName, dir);
		String path = pathFiles;
		path = path + dir;

		return new File(path, name);
	}

	public File toFileAttach(final String fileName) throws BusinessException {
		return toFile(fileName, ATTACH);
	}

	public boolean isAttachAvailable(final ImageEntryDTO imageEntry, final String dir) {
		File f = null;
		try {
			f = toFile(imageEntry, dir);
		} catch (Exception e) {
			logger.error("image", e);
		}
		return f != null && f.exists();
	}

	public static String toStringContentType(final String contentType) {
		if (StringUtils.isBlank(contentType))
			return JPEG;
		switch (contentType.toLowerCase()) {
		case APPLICATION_PDF:
			return PDF;
		case APPLICATION_MSWORD:
			return DOC;
		case APPLICATION_VND_MS_EXCEL:
			return XLS;
		case APPLICATION_VND_MS_POWERPOINT:
			return PPT;
		case APPLICATION_VN_OPENXMLFORMATS_DOCUMENT:
			return DOCX;
		case APPLICATION_VND_OPENXMLFORMATS_SHEET:
			return XLSX;
		case APPLICATION_VND_OPENXMLFORMATS_PRESENTATION:
			return PPTX;
		case IMAGE_GIF:
			return GIF;
		case IMAGE_JPEG:
			return JPEG;
		case IMAGE_PNG:
			return PNG;
		case IMAGE_TIFF:
			return TIFF;
		default:
			int k = contentType.lastIndexOf('/');
			if (k >= 0)
				return contentType.substring(k + 1).trim();
			return contentType;
		}
	}
	public static String toContentType(final String suffix) {
		if (StringUtils.isBlank(suffix))
			return APPLICATION_PDF;
		switch (suffix.toLowerCase()) {
		case PDF:
			return APPLICATION_PDF;
		case DOC:
			return APPLICATION_MSWORD;
		case XLS: 
			return APPLICATION_VND_MS_EXCEL;
		case PPT: 
			return APPLICATION_VND_MS_POWERPOINT;
		case DOCX:
			return APPLICATION_VN_OPENXMLFORMATS_DOCUMENT;
		case XLSX:
			return APPLICATION_VND_OPENXMLFORMATS_SHEET;
		case PPTX: 
			return APPLICATION_VND_OPENXMLFORMATS_PRESENTATION;
		case GIF:
			return IMAGE_GIF;
		case JPEG:
			return IMAGE_JPEG;
		case PNG:
			return IMAGE_PNG;
		case TIFF:
			return IMAGE_TIFF;
		default:
			return APPLICATION_PDF;
		}
	}
	public void copy(final InputStream source, final OutputStream destination) throws IOException {
		// Transfer bytes from source to destination
		byte[] buf = new byte[1024];
		int len;
		while ((len = source.read(buf)) > 0) {
			destination.write(buf, 0, len);
		}
		source.close();
		destination.close();
	}

	public Date getLastModifyTime(final ImageEntryDTO imageEntry, final String dir) {
		File f = null;
		try {
			f = toFile(imageEntry, dir);
		} catch (Exception e) {
			logger.error("lastTime", e);
		}
		return f == null ? null : new Date(f.lastModified());
	}

	public byte[] createNotAvailableAttach(final String contentType) throws IOException {
		// Load the "Attach Not Available"
		// image from jar, then write it out.
		StringBuilder name = new StringBuilder("com/mysticcoders/resources/ImageNotAvailable.");
		if (IMAGE_JPEG.equalsIgnoreCase(contentType)) {
			name.append("jpg");
		} else if (IMAGE_PNG.equalsIgnoreCase(contentType)) {
			name.append(PNG);
		} else {
			name.append(GIF);
		}
		URL url = getClass().getClassLoader().getResource(name.toString());
		InputStream in = url.openStream();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(in, out);
		in.close();
		out.close();
		return out.toByteArray();
	}

	public static String suffix(final String contentType) {
		if (StringUtils.isBlank(contentType)) {
			return "";
		}
		return toStringContentType(contentType);
	}
}