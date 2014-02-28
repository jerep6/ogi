package fr.jerep6.ogi.transfert.mapping;

import javax.annotation.PostConstruct;

import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.ConverterFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import ma.glasnost.orika.metadata.TypeFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.jerep6.ogi.framework.exception.BusinessException;
import fr.jerep6.ogi.framework.exception.MultipleBusinessException;
import fr.jerep6.ogi.framework.transfert.ErrorTo;
import fr.jerep6.ogi.framework.transfert.ExceptionTo;
import fr.jerep6.ogi.framework.utils.ContextUtils;
import fr.jerep6.ogi.framework.utils.ExceptionUtils;
import fr.jerep6.ogi.framework.utils.UrlUtils;
import fr.jerep6.ogi.persistance.bo.Address;
import fr.jerep6.ogi.persistance.bo.Category;
import fr.jerep6.ogi.persistance.bo.DPE;
import fr.jerep6.ogi.persistance.bo.Description;
import fr.jerep6.ogi.persistance.bo.Document;
import fr.jerep6.ogi.persistance.bo.Label;
import fr.jerep6.ogi.persistance.bo.Owner;
import fr.jerep6.ogi.persistance.bo.PartnerRequest;
import fr.jerep6.ogi.persistance.bo.RealProperty;
import fr.jerep6.ogi.persistance.bo.RealPropertyDiagnosis;
import fr.jerep6.ogi.persistance.bo.RealPropertyLivable;
import fr.jerep6.ogi.persistance.bo.RealPropertyPlot;
import fr.jerep6.ogi.persistance.bo.Room;
import fr.jerep6.ogi.persistance.bo.Sale;
import fr.jerep6.ogi.persistance.bo.State;
import fr.jerep6.ogi.service.ServiceUrl;
import fr.jerep6.ogi.transfert.FileUpload;
import fr.jerep6.ogi.transfert.bean.AddressTo;
import fr.jerep6.ogi.transfert.bean.CategoryTo;
import fr.jerep6.ogi.transfert.bean.DPETo;
import fr.jerep6.ogi.transfert.bean.DescriptionTo;
import fr.jerep6.ogi.transfert.bean.DocumentTo;
import fr.jerep6.ogi.transfert.bean.FileUploadTo;
import fr.jerep6.ogi.transfert.bean.LabelTo;
import fr.jerep6.ogi.transfert.bean.OwnerTo;
import fr.jerep6.ogi.transfert.bean.PartnerRequestTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyDiagnosisTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyLinkTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyLivableTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyPlotTo;
import fr.jerep6.ogi.transfert.bean.RealPropertyTo;
import fr.jerep6.ogi.transfert.bean.RoomTo;
import fr.jerep6.ogi.transfert.bean.SaleTo;
import fr.jerep6.ogi.transfert.bean.StateTo;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumCategory;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumDescriptionType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumLabelType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumMandateType;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumOrientation;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumPartner;
import fr.jerep6.ogi.transfert.mapping.converter.ConverterEnumPartnerRequestType;
import fr.jerep6.ogi.utils.DocumentUtils;

@Component("orikaMapper")
public class OrikaMapper extends ConfigurableMapper {
	@Autowired
	private ServiceUrl		serviceUrl;

	private MapperFactory	factory;

	@Override
	protected void configure(MapperFactory factory) {
		super.configure(factory);
		this.factory = factory;
	}

	/** Mapping for exception */
	private void exceptionMapping() {
		factory.classMap(BusinessException.class, ErrorTo.class)//
				.customize(new CustomMapper<BusinessException, ErrorTo>() {
					@Override
					public void mapAtoB(BusinessException a, ErrorTo b, MappingContext context) {
						String msg = a.getMessage() == null ? "" : ExceptionUtils.i18n(a);
						b.setMessage(msg);
					}

					@Override
					public void mapBtoA(ErrorTo b, BusinessException a, MappingContext context) {}
				})//
				.byDefault().register();

		factory.classMap(Exception.class, ErrorTo.class)//
				.customize(new CustomMapper<Exception, ErrorTo>() {
					@Override
					public void mapAtoB(Exception a, ErrorTo b, MappingContext context) {
						String msg = a.getMessage() == null ? "" : ExceptionUtils.i18n(a);
						b.setMessage(msg);

						// Exception other than business exception don't have code
						b.setCode("TECH");
					}

					@Override
					public void mapBtoA(ErrorTo b, Exception a, MappingContext context) {}
				})//
				.byDefault().register();

		factory.classMap(Exception.class, ExceptionTo.class)//
				.customize(new CustomMapper<Exception, ExceptionTo>() {

					@Override
					public void mapAtoB(Exception a, ExceptionTo b, MappingContext context) {
						b.add(map(a, ErrorTo.class));
					}

					@Override
					public void mapBtoA(ExceptionTo b, Exception a, MappingContext context) {}
				})//
				.register();

		factory.classMap(MultipleBusinessException.class, ExceptionTo.class)//
				.field("exceptions", "errors")//
				.register();
	}

	/**
	 * La méthode configure est appelée lors de la construction de l'objet et les injections spring ne sont pas encore
	 * réalisées
	 */
	@PostConstruct
	private void postConstruct() {
		ConverterFactory converterFactory = factory.getConverterFactory();

		// Specifics converter (instantiate and copy properties)
		converterFactory.registerConverter(new ConverterEnumCategory());
		converterFactory.registerConverter(new ConverterEnumDescriptionType());
		converterFactory.registerConverter(new ConverterEnumOrientation());
		converterFactory.registerConverter(new ConverterEnumMandateType());
		converterFactory.registerConverter(new ConverterEnumLabelType());
		converterFactory.registerConverter(new ConverterEnumPartner());
		converterFactory.registerConverter(new ConverterEnumPartnerRequestType());

		exceptionMapping();

		// Specifics factory (create object)
		factory.registerObjectFactory(new FactoryRealPropertyTo(), TypeFactory.valueOf(RealPropertyTo.class));
		factory.registerObjectFactory(new FactoryRealProperty(), TypeFactory.valueOf(RealProperty.class));

		// Mapping definition
		factory.classMap(Category.class, CategoryTo.class).byDefault().register();
		factory.classMap(Address.class, AddressTo.class).byDefault().register();
		factory.classMap(Description.class, DescriptionTo.class).byDefault().register();
		factory.classMap(RealPropertyDiagnosis.class, RealPropertyDiagnosisTo.class)
				.field("diagnosis.label", "diagnosis").byDefault().register();
		factory.classMap(Room.class, RoomTo.class).byDefault().register();
		factory.classMap(Sale.class, SaleTo.class).byDefault().register();
		factory.classMap(Label.class, LabelTo.class).byDefault().register();
		factory.classMap(State.class, StateTo.class).byDefault().register();
		factory.classMap(Owner.class, OwnerTo.class).byDefault().register();
		factory.classMap(DPE.class, DPETo.class).byDefault().register();
		factory.classMap(RealProperty.class, RealPropertyLinkTo.class)//
				.customize(new CustomMapper<RealProperty, RealPropertyLinkTo>() {
					@Override
					public void mapAtoB(RealProperty a, RealPropertyLinkTo b, MappingContext context) {
						b.setUrl(serviceUrl.urlProperty(a.getReference()));
					}

					@Override
					public void mapBtoA(RealPropertyLinkTo b, RealProperty a, MappingContext context) {}
				})//
				.fieldAToB("reference", "reference").register();

		factory.classMap(Document.class, DocumentTo.class)//
				.customize(new CustomMapper<Document, DocumentTo>() {
					@Override
					public void mapAtoB(Document a, DocumentTo b, MappingContext context) {
						b.setUrl(serviceUrl.urlDocument(a.getPath()));
					}

					@Override
					public void mapBtoA(DocumentTo b, Document a, MappingContext context) {
						String urlWithoutParam = UrlUtils.deleteQueryParams(b.getUrl());
						a.setPath(DocumentUtils.relativePathFromUrl(urlWithoutParam).toString());
					}
				})//
				.byDefault().register();
		factory.classMap(FileUpload.class, FileUploadTo.class).byDefault().register();

		factory.classMap(RealProperty.class, RealPropertyTo.class)//
				.field("type.label", "type") //
				.field("equipments{label}", "equipments{}")//
				.field("diagnosisProperty", "diagnosis")//
				// must exclude the field else classcast exception. It doesn't detected that description is mapped as
				// map bellow
				.exclude("descriptions")//
				.field("descriptions{type}", "descriptions{key}")//
				.field("descriptions{}", "descriptions{value}")//
				.fieldAToB("photos", "photos")//
				.fieldBToA("photos", "documents")//
				.byDefault()//
				.register();

		factory.classMap(RealPropertyLivable.class, RealPropertyLivableTo.class) //
				.use(RealProperty.class, RealPropertyTo.class) //
				.exclude("descriptions")//
				.exclude("type")// Need to exclude type because "byDefault" override parent behaviour
				.byDefault()//
				.register();

		factory.classMap(RealPropertyPlot.class, RealPropertyPlotTo.class) //
				.use(RealProperty.class, RealPropertyTo.class) //
				.exclude("descriptions")//
				.exclude("type").byDefault()//
				.register();

		factory.classMap(PartnerRequest.class, PartnerRequestTo.class)//
				.customize(new CustomMapper<PartnerRequest, PartnerRequestTo>() {
					@Override
					public void mapAtoB(PartnerRequest a, PartnerRequestTo b, MappingContext context) {
						b.setLabel(ContextUtils.getMessage("partner.request." + a.getRequestType().getCode()));
					}

					@Override
					public void mapBtoA(PartnerRequestTo b, PartnerRequest a, MappingContext context) {}
				})//
				.byDefault().register();
	}
}
