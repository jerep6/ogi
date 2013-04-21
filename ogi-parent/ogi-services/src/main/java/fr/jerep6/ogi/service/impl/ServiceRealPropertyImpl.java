package fr.jerep6.ogi.service.impl;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

import fr.jerep6.ogi.framework.service.impl.AbstractTransactionalService;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.Equipment;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.Type;
import fr.jerep6.ogi.persistance.dao.DaoProperty;
import fr.jerep6.ogi.service.ServiceCategory;
import fr.jerep6.ogi.service.ServiceEquipment;
import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.ServiceType;

@Service("serviceRealProperty")
@Transactional(propagation = Propagation.REQUIRED)
public class ServiceRealPropertyImpl extends AbstractTransactionalService<RealProperty, Integer> implements
		ServiceRealProperty {

	@Autowired
	private DaoProperty			daoProperty;

	@Autowired
	private ServiceCategory		serviceCategory;

	@Autowired
	private ServiceType			serviceType;

	@Autowired
	private ServiceEquipment	serviceEquipment;

	@Value("${search.result.max}")
	private Integer				searchMaxResult;

	@Override
	public RealProperty createFromBusinessFields(RealProperty property) {
		Preconditions.checkNotNull(property);

		// It's impossible to create new category
		Category cat = serviceCategory.readByCode(property.getCategory().getCode());
		property.setCategory(cat);

		Type t = serviceType.readOrInsert(property.getType().getLabel(), cat);
		property.setType(t);

		Set<Equipment> eqpts = new HashSet<>(property.getEquipments().size());
		for (Equipment oneEqpt : property.getEquipments()) {
			// Read equipment from DB
			Equipment eqptFull = serviceEquipment.readByLabel(oneEqpt.getLabel(), cat.getCode());

			// if not exist create it
			if (eqptFull == null) {
				eqptFull = new Equipment(oneEqpt.getLabel(), cat);
			}

			// not add property do equipment because property is relation owner
			eqpts.add(eqptFull);
		}
		property.setEquipments(eqpts);

		// Description, address

		// Save real property into database
		save(property);

		return null;
	}

	@Override
	@PostConstruct
	protected void init() {
		super.setDao(daoProperty);
	}

	@Override
	public RealProperty readByReference(String reference) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(reference));

		return daoProperty.readByReference(reference);
	}

}
