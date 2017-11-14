package com.greenhat.mvc.fault;

public class ServerException extends CodedBaseRuntimeException{

    public static int ENTITIY_NOT_FOUND = 404;
    public static int SESSION_ACQUIRE_FALIED = 501;
    public static int SCHEMA_NOT_FOUND = 504;
    public static int VALUE_NEEDED = 505;
    public static int EVAL_FALIED = 506;
    public static int VALIDATE_FALIED = 507;
    public static int DATABASE_ACCESS_FAILED = 510;
    public static int ACCESS_DENIED = 511;
    public static int DAO_NOT_FOUND = 512;


    public ServerException(String msg){
        super(msg);
    }

    public ServerException(Throwable e){
        super(e);
    }

    public ServerException(int code,Throwable e){
        super(code,e);
    }

    public ServerException(Throwable e,int code,String msg){
        super(code,msg,e);
    }

    public ServerException(int code,String msg) {
        super(code,msg);
    }
}
