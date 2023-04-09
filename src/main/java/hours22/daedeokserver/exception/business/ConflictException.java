package hours22.daedeokserver.exception.business;

import hours22.daedeokserver.exception.ErrorCode;

public class ConflictException extends BusinessException{
    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
