package hours22.daedeokserver.exception.business;


import hours22.daedeokserver.exception.ErrorCode;

public class FileUploadException extends BusinessException {
    public FileUploadException(ErrorCode errorCode){
        super(errorCode);
    }
}
