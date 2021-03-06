package fr.jerep6.ogi.exception.business.enumeration;

import fr.jerep6.ogi.framework.exception.ErrorCode;

public enum EnumBusinessErrorProperty implements ErrorCode {
	SALE_MANDAT_DATE("SAL_01", "Wrong  mandate dates"), //

	NO_REFERENCE("NO_REFERENCE", "Property haven't reference"), //
	REFERENCE_EXISTS("REFERENCE_EXISTS", "Property reference already exist"), //
	REFERENCE_MALFORMED("REFERENCE_MALFORMED", "Property reference is malformed"), //
	NO_SALE("NO_SALE", "Property haven't sale"), //
	NO_RENT("NO_RENT", "Property haven't rent"), //
	NO_DESCRIPTION_WEBSITE_OWN("NO_DESCRIPTION_WEBSITE_OWN", "Property haven't description WEBSITE_OWN"), //
	NO_DESCRIPTION_WEBSITE_OTHER("NO_DESCRIPTION_WEBSITE_OTHER", "Property haven't description WEBSITE_OTHER"), //
	NO_ADDRESS("NO_ADDRESS", "Property haven't address"), //
	NO_ADDRESS_CITY("NO_ADDRESS_CITY", "City is mandatory"), //
	NO_ADDRESS_POSTAL_CODE("NO_ADDRESS_POSTAL_CODE", "Postal code is mandatory"), //
	NO_TYPE("NO_TYPE", "Property haven't type"), //
	NO_MANDAT_REFERENCE_SALE("NO_MANDAT_REFERENCE_SALE", "Property haven't mandat reference"), //
	NO_SALE_PRICE("NO_SALE_PRICE", "Property sale haven't price"), //
	NO_RENT_PRICE("NO_RENT_PRICE", "Property rent haven't price"), //
	NO_RENT_COMMISSION("NO_RENT_COMMISSION", "Property haven't commission"), //
	NO_CHARGES("NO_CHARGES", "Property haven't charges"), //
	NO_ROOM_NUMBER("NO_ROOM_NUMBER", "Property haven't room number"), //
	NO_ROOM_TYPE("NO_ROOM_TYPE", "Room haven't type"), //
	UNSUPPORTED_CATEGORY("UNSUPPORTED_CATEGORY", "Category is not supported"), //
	;

	private String	code;
	private String	message;

	EnumBusinessErrorProperty(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
