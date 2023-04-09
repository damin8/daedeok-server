package hours22.daedeokserver.exception.token;

import hours22.daedeokserver.exception.ErrorCode;

public class TokenInvalidException extends TokenException{
    public TokenInvalidException(ErrorCode errorCode){
        super(errorCode);
    }
}
