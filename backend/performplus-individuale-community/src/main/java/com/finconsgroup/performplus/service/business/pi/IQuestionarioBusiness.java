package com.finconsgroup.performplus.service.business.pi;

import java.util.List;

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
import com.finconsgroup.performplus.service.business.errors.BusinessException;

public interface IQuestionarioBusiness {

	QuestionarioVM leggi(Long idQuestionario)throws BusinessException;

	QuestionarioVM crea(CreaQuestionarioRequest request)throws BusinessException;

	void aggiorna(Long idQuestionario, AggiornaQuestionarioRequest request)throws BusinessException;

	void elimina(Long idQuestionario)throws BusinessException;

	
	List<NodoQuestionarioVM> search( ParametriRiceraQuestionario parametri)throws BusinessException;

	List<NodoQuestionarioVM> ramo(TipoNodoQuestionario tipoNodo, Long idNodo)throws BusinessException;

	DetailNodoQuestionarioVM leggiNodo(Long idEnte, TipoNodoQuestionario tipoNodo, Long idNodo)throws BusinessException;

	PreparaNodoQuestionarioRequest preparaNodo(Long idEnte, TipoNodoQuestionario tipoNodoPadre, Long idNodoPadre)throws BusinessException;

	void eliminaNodo(TipoNodoQuestionario tipoNodo, Long idNodo)throws BusinessException;

	void aggiornaNodo( TipoNodoQuestionario tipoNodo,  Long idNodo,
			AggiornaNodoQuestionarioRequest request)throws BusinessException;

	NodoQuestionarioVM creaNodo( CreaNodoQuestionarioRequest request)throws BusinessException;

	void updatePeso( TipoNodoQuestionario tipoNodo,  Long idNodo,  Float peso)throws BusinessException;

}
