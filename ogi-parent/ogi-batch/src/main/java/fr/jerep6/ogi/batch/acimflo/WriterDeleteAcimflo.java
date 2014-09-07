package fr.jerep6.ogi.batch.acimflo;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import fr.jerep6.ogi.service.ServiceRealProperty;
import fr.jerep6.ogi.service.external.impl.ServiceAcimfloImpl;

public class WriterDeleteAcimflo implements ItemWriter<RealPropertyAcimflo>, InitializingBean {

	private ServiceAcimfloImpl	serviceAcimflo;
	private ServiceRealProperty	serviceRealProperty;

	@Override
	public void afterPropertiesSet() throws Exception {}

	public void setServiceAcimflo(ServiceAcimfloImpl serviceAcimflo) {
		this.serviceAcimflo = serviceAcimflo;
	}

	public void setServiceRealProperty(ServiceRealProperty serviceRealProperty) {
		this.serviceRealProperty = serviceRealProperty;
	}

	@Override
	public void write(List<? extends RealPropertyAcimflo> items) throws Exception {

		for (RealPropertyAcimflo a : items) {
			serviceAcimflo.delete(a.getTechid(), a.getReference());
		}
	}

}
