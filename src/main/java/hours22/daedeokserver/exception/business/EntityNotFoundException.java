package hours22.daedeokserver.exception.business;

import hours22.daedeokserver.exception.ErrorCode;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
