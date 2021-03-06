package fr.jerep6.ogi.persistance.bo;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

import fr.jerep6.ogi.persistance.bo.id.DiagnosisRealPropertyId;

// Hibernate use the name of then entity to determine the table name. But the purpose of this name is to use in JPQL
@Entity
@Table(name = "TJ_PRP_DIA")
// Lombok
@Getter
@Setter
@EqualsAndHashCode(of = { "pk" })
public class RealPropertyDiagnosis {
	@EmbeddedId
	private DiagnosisRealPropertyId	pk	= new DiagnosisRealPropertyId();

	@Column(name = "DRP_DATE")
	@Temporal(TemporalType.DATE)
	private Calendar				date;

	public Diagnosis getDiagnosis() {
		return pk.getDiagnosis();
	}

	public RealProperty getRealProperty() {
		return pk.getProperty();
	}

	// needed by orika for mapping
	public void setDiagnosis(Diagnosis d) {
		pk.setDiagnosis(d);
	}

	@Override
	public String toString() {
		ToStringHelper ts = Objects.toStringHelper(this);
		ts.add("property", getRealProperty().getReference()).add("diagnosis", getDiagnosis());
		ts.add("date", date == null ? date : date.getTime());
		return ts.toString();
	}
}
