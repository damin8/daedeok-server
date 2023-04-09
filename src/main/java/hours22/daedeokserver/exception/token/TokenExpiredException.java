package hours22.daedeokserver.exception.token;

import hours22.daedeokserver.exception.ErrorCode;

public class TokenExpiredException extends TokenException{
    public TokenExpiredException(ErrorCode errorCode){
        super(errorCode);
    }
}
