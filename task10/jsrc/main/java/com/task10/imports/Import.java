package com.task10.imports;


public interface Import {
    String PREFIX = "cmtr-e28aa15d-";
    String POSTFIX = "";
    String Reservations_TABLE_NAME = "Reservations";
    String Tables_TABLE_NAME = "Tables";
    String COGNITO_USER_POOL_NAME = "simple-booking-userpool";
    String Reservations_TABLE_FULL_NAME = PREFIX+Reservations_TABLE_NAME+POSTFIX;
    String Tables_TABLE_FULL_NAME = PREFIX+Tables_TABLE_NAME+POSTFIX;
    String COGNITO_USER_POOL_FULL_NAME = PREFIX+COGNITO_USER_POOL_NAME+POSTFIX;
}
