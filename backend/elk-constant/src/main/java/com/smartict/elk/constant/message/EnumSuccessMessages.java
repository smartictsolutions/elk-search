/* SmartICT Bilisim A.S. (C) 2020 */
package com.smartict.elk.constant.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public enum EnumSuccessMessages {
    PASSWORD_HISTORY_READ_TITLE("EnumSuccessMessages.PASSWORD_HISTORY_READ_TITLE", "Kullanıcı Şifre Geçmişi Sorgulama"),
    PASSWORD_HISTORY_READ_MESSAGE("EnumSuccessMessages.PASSWORD_HISTORY_READ_MESSAGE", "Kullanıcı şifre geçmişi başarıyla sorgulandı"),

    FORGOT_PASSWORD_RESET_LINK_TITLE("EnumSuccessMessages.FORGOT_PASSWORD_RESET_LINK_TITLE", "Şifre Sıfırlama İsteği"),
    FORGOT_PASSWORD_RESET_LINK_MESSAGE("EnumSuccessMessages.FORGOT_PASSWORD_RESET_LINK_MESSAGE", "Şifre sıfırlama maili gönderilmiştir.");

    private final String languageValue;
    private final String languageKey;

    EnumSuccessMessages(String languageValue, String languageKey) {
        this.languageValue = languageValue;
        this.languageKey = languageKey;
    }

    @JsonCreator
    public static EnumSuccessMessages valueOfLanguageValue(String languageValue) {
        for (EnumSuccessMessages deger : values()) {
            if (deger.languageValue.equalsIgnoreCase(languageValue)) {
                return deger;
            }
        }
        return null;
    }

    @JsonCreator
    public static EnumSuccessMessages valueOfLanguageKey(String languageKey) {
        for (EnumSuccessMessages deger : values()) {
            if (deger.languageKey.equalsIgnoreCase(languageKey)) {
                return deger;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "EnumSuccessMessages {languageValue=" + languageValue + ", languageKey=" + languageKey + "}";
    }

}
