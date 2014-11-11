package fr.jerep6.ogi.search.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.jerep6.ogi.framework.service.impl.AbstractService;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.search.obj.SearchResult;
import fr.jerep6.ogi.search.persistance.DaoSearch;
import fr.jerep6.ogi.search.service.ServiceSearch;
import fr.jerep6.ogi.service.ServiceRealProperty;

@Service("serviceSearch")
public class ServiceSearchImpl extends AbstractService implements ServiceSearch {
	private final Logger		LOGGER	= LoggerFactory.getLogger(this.getClass());

	@Autowired
	private DaoSearch			daoSearch;

	@Autowired
	private ServiceRealProperty	serviceRealProperty;

	@Override
	public void delete(List<String> references) {
		daoSearch.delete(references);
	}

	@Override
	public void index(List<RealProperty> r) {
		daoSearch.index(r);
	}

	@Override
	public void index(RealProperty r) {
		daoSearch.index(Arrays.asList(r));
	}

	@Override
	public SearchResult search(String keyword) {
		Optional<RealProperty> prp = serviceRealProperty.readByReference(keyword);
		List<RealProperty> l = new ArrayList<RealProperty>(0);
		if (prp.isPresent()) {
			l.add(prp.get());
		}
		return new SearchResult(l, l.size());
	}

}