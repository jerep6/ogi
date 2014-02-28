package fr.jerep6.ogi.persistance.dao.impl;

import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Iterables;

import fr.jerep6.ogi.enumeration.EnumPartner;
import fr.jerep6.ogi.enumeration.EnumPartnerRequestType;
import fr.jerep6.ogi.framework.persistance.dao.impl.AbstractDao;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;
import fr.jerep6.ogi.persistance.dao.DaoPartnerRequest;

@Repository("daoPartnerRequest")
@Transactional(propagation = Propagation.MANDATORY)
public class DaoPartnerRequestImpl extends AbstractDao<PartnerRequest, Integer> implements DaoPartnerRequest {
	private static Logger		LOGGER				= LoggerFactory.getLogger(DaoPartnerRequestImpl.class);
	private static final String	PARAM_PARTNER		= "PARTNER";
	private static final String	PARAM_REQUEST_TYPE	= "REQUEST_TYPE";
	private static final String	PARAM_PROPERTY		= "PROPERTY";

	@Override
	public PartnerRequest lastRequest(EnumPartner partner, Integer prpTechid) {
		StringBuilder q = new StringBuilder();
		q.append("SELECT r FROM " + PartnerRequest.class.getName() + " r ");
		q.append(" WHERE r.partner = :" + PARAM_PARTNER);
		q.append(" AND r.property = :" + PARAM_PROPERTY);
		q.append(" ORDER BY r.modificationDate DESC");

		TypedQuery<PartnerRequest> query = entityManager.createQuery(q.toString(), PartnerRequest.class);
		query.setParameter(PARAM_PARTNER, partner);
		query.setParameter(PARAM_PROPERTY, prpTechid);

		List<PartnerRequest> resultList = query.getResultList();
		return Iterables.getFirst(resultList, null);

		// Another version in SQL
		// SELECT req.* FROM ogi.ta_partner_request req
		// JOIN (SELECT max(req.REQ_MODIFICATION_DATE), REQ_ID FROM ogi.ta_partner_request req
		// WHERE REQ_PARTNER='acimflo') reqMax ON req.REQ_ID=reqMax.REQ_ID
		// WHERE req.req_TYPE IN ('push');
	}

	@Override
	public boolean lastRequestIs(EnumPartner partner, Integer prpTechid, List<EnumPartnerRequestType> l) {

		/*
		 * Pour un partenaire, retourne toutes les lignes du type spécifié qui n'ont pas d'enregistrement postérieur
		 * dans le temps. Si la requête renvoie des résultats, cela signifie qu'il existe un enregistrement plus récent
		 */
		StringBuilder q = new StringBuilder();
		q.append("SELECT r FROM " + PartnerRequest.class.getName() + " r ");
		q.append(" WHERE r.partner = :" + PARAM_PARTNER);
		q.append(" AND r.property = :" + PARAM_PROPERTY);
		q.append(" AND r.requestType IN ( :" + PARAM_REQUEST_TYPE + ")");
		q.append(" AND NOT EXISTS (");
		q.append(" 		SELECT r2.techid FROM " + PartnerRequest.class.getName() + " r2 ");
		q.append(" 		WHERE r2.partner = r.partner");
		q.append(" 		AND r2.modificationDate > r.modificationDate");
		q.append(")");

		TypedQuery<PartnerRequest> query = entityManager.createQuery(q.toString(), PartnerRequest.class);
		query.setParameter(PARAM_PARTNER, partner);
		query.setParameter(PARAM_REQUEST_TYPE, l);
		query.setParameter(PARAM_PROPERTY, prpTechid);

		List<PartnerRequest> resultList = query.getResultList();

		LOGGER.debug("JPQL result = {}", l);

		return !resultList.isEmpty();
	}

}
