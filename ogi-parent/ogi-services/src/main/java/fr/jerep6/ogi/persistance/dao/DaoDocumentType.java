package fr.jerep6.ogi.persistance.dao;

import java.util.List;
import java.util.Optional;

import fr.jerep6.ogi.enumeration.EnumGestionMode;
import fr.jerep6.ogi.framework.persistance.dao.DaoCRUD;
import fr.jerep6.ogi.persistance.bo.DocumentType;

public interface DaoDocumentType extends DaoCRUD<DocumentType, Integer> {

	List<DocumentType> listDocumentType(EnumGestionMode zone);

	Optional<DocumentType> readByCode(String code);

}
