package hours22.daedeokserver.exception.token;

import hours22.daedeokserver.exception.ErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends RuntimeException{
    private final ErrorCode errorCode;

    public TokenException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
