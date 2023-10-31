/* SmartICT Bilisim A.S. (C) 2020 */
package com.smartict.elk.constant.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum EnumExceptionMessages {
    NO_SUCH_USER("ExceptionMessages.NO_SUCH_USER", "Kullanici Bulunamdi!"),
    HAS_NO_AUTH_FOR_USER_OPERATION("ExceptionMessages.HAS_NO_AUTH_FOR_USER_OPERATION", "Bu kullanici icin islem yapma yetkiniz yoktur!"),
    USER_NOT_ACTIVE("ExceptionMessages.USER_NOT_ACTIVE", "Aktif kullanici bulunamadi!"),
    USER_ID_NOT_BE_EMPTY("ExceptionMessages.USER_ID_NOT_BE_EMPTY", "Kullanici ID bos olamaz!"),
    USER_NAME_PASSWORD_NOT_MATCH("ExceptionMessages.USER_NAME_PASSWORD_NOT_MATCH", "Hatali kullanici adi ya da parola!"),
    USER_NOT_FOUND("ExceptionMessages.USER_NOT_FOUND", "Kullanici bulunamadi!"),
    USER_ROLE_INFO_NOT_READ("ExceptionMessages.USER_ROLE_INFO_NOT_READ", "Rol bilgisi okunamadi!"),
    USERNAME_NOT_INCLUDE_SPACE("ExceptionMessages.USERNAME_NOT_INCLUDE_SPACE", "Kullanici adi bosluk iceremez!"),

    OLD_PASSWORDS_NOT_MATCH("ExceptionMessages.OLD_PASSWORDS_NOT_MATCH", "Guncel parola hatali!"),
    OLD_AND_NEW_PASSWORDS_MATCH("ExceptionMessages.OLD_AND_NEW_PASSWORDS_MATCH", "Guncel ve yeni sifre ayni olamaz!"),

    EXCEL_IMPORT_FILE_NOT_CREATE("ExceptionMessages.EXCEL_IMPORT_FILE_NOT_CREATE", "Sonuc excel dosyasi yaratilamadi!"),
    EXCEL_IMPORT_FILE_WRITE_ERROR("ExceptionMessages.EXCEL_IMPORT_FILE_WRITE_ERROR", "Sonuclar excel dosyasina yazilirken hata olustu!"),
    EXCEL_IMPORT_FILE_UNSUPPORTED_FORMAT("ExceptionMessages.EXCEL_IMPORT_FILE_UNSUPPORTED_FORMAT", "Desteklenmeyen dosya uzantisi!"),

    EXCEL_EXPORT_FILE_NOT_CREATE("ExceptionMessages.EXCEL_EXPORT_FILE_NOT_CREATE", "Sonuc excel dosyasi yaratilamadi!"),
    EXCEL_EXPORT_FILE_WRITE_ERROR("ExceptionMessages.EXCEL_EXPORT_FILE_WRITE_ERROR", "Sonuclar excel dosyasina yazilirken hata olustu!"),
    EXCEL_EXPORT_FILE_UNSUPPORTED_FORMAT("ExceptionMessages.EXCEL_EXPORT_FILE_UNSUPPORTED_FORMAT", "Desteklenmeyen dosya uzantisi!"),
    EXCEL_EXPORT_TYPE_NOT_VALID("ExceptionMessages.EXCEL_EXPORT_TYPE_NOT_VALID", "Gecersiz bir excel export tipi secildi"),

    LANGUAGE_NOT_SUPPORTED("ExceptionMessages.LANGUAGE_NOT_SUPPORTED", "Belirtilen dil desteklenmiyor!"),

    ACTIVE_DIRECTORY_USERNAME_CANNOT_BE_NULL("ExceptionMessages.ACTIVE_DIRECTORY_USERNAME_CANNOT_BE_NUL", "Active Directory kullanici adi null gelemez"),

    DEFAULT_ROLE_CANNOT_DELETABLE("ExceptionMessages.DEFAULT_ROLE_CANNOT_DELETABLE", "Varsayilan rol silinemez"),
    DEFAULT_ROLE_CANNOT_CREATABLE("ExceptionMessages.DEFAULT_ROLE_CANNOT_CREATABLE", "Varsayilan rol yaratilmaz"),
    DEFAULT_ROLE_NAME_CANNOT_CHANGEABLE("ExceptionMessages.DEFAULT_ROLE_NAME_CANNOT_CHANGEABLE", "Varsayilan rol ismi degistirilemez"),

    ROLE_CANNOT_BE_DEFAULT("ExceptionMessages.ROLE_CANNOT_BE_DEFAULT", "Normal roller, vasayilan rol olarak degistirilemez"),
    ROLE_NAME_CANNOT_BE_EMPTY("ExceptionMessages.ROLE_NAME_CANNOT_BE_EMPTY", "Role isimi bos olarak gonderilemez"),
    ROLE_NAME_IS_ALREADY_EXISTS("ExceptionMessages.ROLE_NAME_IS_ALREADY_EXISTS", "Rol ismi zaten mevcut"),

    SYSTEM_ADMIN_CANNOT_UPDATEABLE("ExceptionMessages.SYSTEM_ADMIN_CANNOT_UPDATEABLE", "Sistem Yoneticisi guncellenemez"),
    SYSTEM_ADMIN_CANNOT_DELETABLE("ExceptionMessages.SYSTEM_ADMIN_CANNOT_DELETABLE", "Sistem Yoneticisi rolu silinemez"),

    INVALID_AUTH_INDEX_LIST("ExceptionMessages.INVAILD_AUTH_INDEX_LIST", "Gecersiz bir Yetki Listesi gonderildi!"),
    HAS_NO_AUTH("ExceptionMessages.INVAILD_AUTH_INDEX_LIST", "Bu islem icin yetkiniz yoktur!"),

    THERE_ARE_DUPLICATE_NAMES_IN_ROLE_LIST("ExceptionMessages.THERE_ARE_DUPLICATE_NAMES_IN_ROLE_LIST", "Gonderilen rol listesinde ayni isimde roller mevcut"),

    NOTIFICATION_TYPE_INVALID("ExceptionMessages.NOTIFICATION_TYPE_INVALID", "Gecersiz bir bildirim tipi gonderildi"),
    NOTIFICATION_WEB_SOCKET_SEND_ERROR(
            "EnumExceptionMessages.NOTIFICATION_WEB_SOCKET_SEND_ERROR", "Bildirim Web Socket'e gonderilirken bir hata ile karsilasildi!"
    ),

    UNEXPECTED_ERROR_OCCURRED("ExceptionMessages.UNEXPECTED_ERROR_OCCURRED", "Beklenmeyen hata olustu!"),

    SEARCH_CONTEXT_MISSING_EXCEPTION("ExceptionMessages.SEARCH_CONTEXT_MISSING_EXCEPTION", "Gönderilen scroll_id için süre dolmuştur!");

    private final String languageValue;
    private final String languageKey;

    EnumExceptionMessages(String languageKey, String languageValue) {
        this.languageKey = languageKey;
        this.languageValue = languageValue;
    }

    @JsonCreator
    public static EnumExceptionMessages valueOfLanguageValue(String languageValue) {
        for (EnumExceptionMessages deger : values()) {
            if (deger.languageValue.equalsIgnoreCase(languageValue)) {
                return deger;
            }
        }
        return null;
    }

    @JsonCreator
    public static EnumExceptionMessages valueOfLanguageKey(String languageKey) {
        for (EnumExceptionMessages deger : values()) {
            if (deger.languageKey.equalsIgnoreCase(languageKey)) {
                return deger;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "EnumExceptionMessages {languageValue=" + languageValue + ", languageKey=" + languageKey + "}";
    }

}
