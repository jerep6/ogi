package fr.jerep6.ogi.persistance.bo;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;

import fr.jerep6.ogi.enumeration.EnumDescriptionType;
import fr.jerep6.ogi.enumeration.EnumDocumentType;
import fr.jerep6.ogi.persistance.annotation.Audit;

/**
 * Classe non abstraite pour orika. Sinon, il faudrait déclarer un mapping pour chaque classe concrete
 *
 * @author jerep6
 */
// Hibernate use the name of then entity to determine the table name. But the purpose of this name is to use in JPQL
@Entity
@Table(name = "TA_PROPERTY")
@Inheritance(strategy = InheritanceType.JOINED)
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "reference" })
@Audit(excludes = { "modificationDate", "version" })
public class RealProperty {
	@Id
	@Column(name = "PRO_ID", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer						techid;

	@Column(name = "PRO_REFERENCE", nullable = false, unique = true, length = 64)
	private String						reference;

	@Column(name = "PRO_LAND_AREA")
	private Float						landArea;

	@Column(name = "PRO_DEPENDENCY_AREA")
	private Float						dependencyArea;

	/** Lotissement */
	@Column(name = "PRO_HOUSING_ESTATE")
	private Boolean						housingEstate;

	@Column(name = "PRO_INDEPENDENT_CONSULTANT")
	private Boolean						independentConsultant;					;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ADD_ID")
	private Address						address;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "property")
	private Sale						sale;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "property")
	private Rent						rent;

	// Assainissement
	@Column(name = "PRO_SANITATION")
	private String						sanitation;

	@OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
	private Set<Description>			descriptions		= new HashSet<>(0);

	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "TJ_PRP_EQP", //
	joinColumns = @JoinColumn(name = "PRO_ID"), //
	inverseJoinColumns = @JoinColumn(name = "EQP_ID")//
			)
	private Set<Equipment>				equipments			= new HashSet<>(0);

	@ManyToOne
	@JoinColumn(name = "CAT_ID", nullable = false)
	private Category					category;

	@ManyToOne
	@JoinColumn(name = "TYP_ID", nullable = true)
	private Type						type;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "TJ_PRP_DOC",//
	joinColumns = @JoinColumn(name = "PRO_ID"),//
	inverseJoinColumns = @JoinColumn(name = "DOC_ID")//
			)
	private Set<Document>				documents			= new HashSet<>(0);

	@OneToMany(mappedBy = "pk.property", cascade = CascadeType.ALL)
	private Set<RealPropertyDiagnosis>	diagnosisProperty	= new HashSet<>(0);

	@ManyToMany
	@JoinTable(name = "TJ_PRP_OWN",//
	joinColumns = @JoinColumn(name = "PRP_ID"),//
	inverseJoinColumns = @JoinColumn(name = "OWN_ID")//
			)
	private Set<Owner>					owners				= new HashSet<>(0);
	
	@OneToMany(mappedBy = "property")
	private Set<Visit>					visits				= new HashSet<>(0);

	@OneToMany(mappedBy = "property")
	private Set<PartnerRequest>			partnersRequests;

	// ##### Technical field #####
	// Il faut obligatoirement spécifier l'attribut columnDefinition sinon mysql crée un champ date time
	@Column(name = "PRO_MODIFICATION_DATE", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar					modificationDate;

	@Version
	@Column(name = "PRO_VERSION", nullable = false)
	private Integer						version;

	protected RealProperty(String reference, Category category, Type type) {
		super();
		this.reference = reference;
		this.category = category;
		this.type = type;
	}

	@PreUpdate
	@PrePersist
	private void beforePersist() {
		modificationDate = Calendar.getInstance();
		if (independentConsultant == null) {
			independentConsultant = false;
		}
	}

	public Description getDescription(EnumDescriptionType type) {
		for (Description desc : descriptions) {
			if (desc.getType().equals(type)) {
				return desc;
			}
		}
		return null;
	}

	public List<Document> getPhotos() {
		List<Document> photos = documents.stream()
				.filter(d -> EnumDocumentType.PHOTO.equalsWithDocumentType(d.getType()))//
				.sorted()//
				.collect(Collectors.<Document> toList());
		return photos;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("techid", techid).add("reference", reference).toString();
	}

}

// JPA will use all properties of the class, unless you specifically mark them with @Transient. If a field is not
// annotated it will be still mapped
// The @Column annotation is purely optional, and is there to let you override the auto-generated column name.
// Furthermore, the length attribute of @Column is only used when auto-generating table definitions, it has no effect on
// the runtime.
