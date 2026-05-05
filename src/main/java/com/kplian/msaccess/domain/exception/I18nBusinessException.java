package com.kplian.msaccess.domain.exception;

import com.kplian.msaccess.api.service.I18nService;

public class I18nBusinessException extends BusinessException {

    public I18nBusinessException(I18nService i18nService, String messageKey, String code, Object... params) {
        super(messageKey, code, params);
    }
}
